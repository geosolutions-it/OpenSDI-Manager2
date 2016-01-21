package it.geosolutions.opensdi2.service;

import com.googlecode.genericdao.search.Search;
import com.googlecode.genericdao.search.SearchResult;
import com.googlecode.genericdao.search.Sort;
import it.geosolutions.opensdi2.persistence.GenericVibiDao;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.EntityMode;
import org.hibernate.metadata.ClassMetadata;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public abstract class BaseService<T, K extends Serializable> {

    private final static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(BaseService.class);

    protected abstract GenericVibiDao<T, K> getDao();

    public SearchResult getAll(String keywordString, String filtersString,
                               String sortingString, int maxResults, int firstResult, int page) {
        Search search = new Search(getDao().getEntityType());
        if (keywordString != null && !keywordString.isEmpty()) {
            search.addFilter(getDao().getKeyWordSearchFilter(keywordString));
        }
        if (filtersString != null && !filtersString.isEmpty()) {
            search.addFilter(getDao().getPropertiesFilter(filtersString));
        }
        if (sortingString != null && !sortingString.isEmpty()) {
            for (Sort sort : getDao().getPropertiesSorting(sortingString)) {
                search.addSort(sort);
            }
        }
        search.setMaxResults(maxResults);
        search.setFirstResult(firstResult);
        search.setPage(page);
        return getDao().searchAndCount(search);
    }

    public String getEntityName() {
        return getDao().getEntityType().getSimpleName();
    }

    public void writeEntitiesToExcel(File file, List<Object> entities, String propertiesMappingString) {
        List<PropertyMapping> propertiesMappings = getAllPropertiesMappings(propertiesMappingString);
        Workbook workBook = new HSSFWorkbook();
        Sheet sheet = workBook.createSheet(getEntityName());
        Row row = sheet.createRow(0);
        for (int i = 0; i < propertiesMappings.size(); i++) {
            row.createCell(i).setCellValue(propertiesMappings.get(i).header);
        }
        ClassMetadata classMetadata = getDao().getClassMetadata();
        for (int i = 0; i < entities.size(); i++) {
            writeEntityToExcel(sheet.createRow(i + 1), entities.get(i), classMetadata, propertiesMappings);
        }
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(file);
            workBook.write(output);

        } catch (Exception exception) {
            throw new RuntimeException("Error writing excel file.", exception);
        } finally {
            try {
                if (output != null) {
                    output.flush();
                    output.close();
                }
            } catch (Exception exception) {
                LOGGER.error("Error closing excel output file.", exception);
            }
        }
    }

    private void writeEntityToExcel(Row row, Object entity,
                                    ClassMetadata classMetadata, List<PropertyMapping> propertiesMappings) {
        String idProperty = classMetadata.getIdentifierPropertyName();
        for (int i = 0; i < propertiesMappings.size(); i++) {
            PropertyMapping mapping = propertiesMappings.get(i);
            if (!mapping.name.equalsIgnoreCase(idProperty)) {
                setRowValue(row, classMetadata.getPropertyValue(entity, mapping.name, EntityMode.POJO), i);
            } else {
                setRowValue(row, classMetadata.getIdentifier(entity, EntityMode.POJO), i);
            }
        }
    }

    private void setRowValue(Row row, Object value, int columnIndex) {
        if (value == null) {
            return;
        }
        if (value instanceof Double) {
            row.createCell(columnIndex).setCellValue((Double) value);
        } else if (value instanceof Date) {
            row.createCell(columnIndex).setCellValue((Date) value);
        } else if (value instanceof Calendar) {
            row.createCell(columnIndex).setCellValue((Calendar) value);
        } else if (value instanceof Boolean) {
            row.createCell(columnIndex).setCellValue((Boolean) value);
        } else {
            row.createCell(columnIndex).setCellValue(value.toString());
        }
    }

    public void writeEntitiesToCsv(File file, List<Object> entities, String propertiesMappingString) {
        List<PropertyMapping> propertiesMappings = getAllPropertiesMappings(propertiesMappingString);
        String[] header = new String[propertiesMappings.size()];
        for (int i = 0; i < propertiesMappings.size(); i++) {
            header[i] = propertiesMappings.get(i).header;
        }
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader(header);
        FileWriter fileWriter = null;
        CSVPrinter csvPrinter = null;
        try {
            fileWriter = new FileWriter(file);
            csvPrinter = new CSVPrinter(fileWriter, csvFileFormat);
            ClassMetadata classMetadata = getDao().getClassMetadata();
            for (Object entity : entities) {
                writeEntityToCsv(csvPrinter, entity, classMetadata, propertiesMappings);
            }
        } catch (IOException exception) {
            throw new RuntimeException("Error writing csv properties.", exception);
        } finally {
            try {
                if (fileWriter != null) {
                    fileWriter.flush();
                    fileWriter.close();
                    if (csvPrinter != null) {
                        csvPrinter.flush();
                        csvPrinter.close();
                    }
                }
            } catch (Exception exception) {
                LOGGER.error("Error closing file writer or csv printer.", exception);
            }
        }
    }

    private void writeEntityToCsv(CSVPrinter csvPrinter, Object entity,
                                  ClassMetadata classMetadata, List<PropertyMapping> propertiesMappings) throws IOException {
        List<Object> record = new ArrayList<Object>();
        String idProperty = classMetadata.getIdentifierPropertyName();
        for (PropertyMapping propertyMapping : propertiesMappings) {
            if (!propertyMapping.name.equalsIgnoreCase(idProperty)) {
                record.add(classMetadata.getPropertyValue(entity, propertyMapping.name, EntityMode.POJO));
            } else {
                record.add(classMetadata.getIdentifier(entity, EntityMode.POJO));
            }
        }
        csvPrinter.printRecord(record);
    }

    private List<PropertyMapping> getAllPropertiesMappings(String propertiesMappingString) {
        if (propertiesMappingString == null || propertiesMappingString.isEmpty()) {
            return getAllPropertiesMappings();
        }
        List<PropertyMapping> propertiesMapping = new ArrayList<PropertyMapping>();
        for (String propertiesMappingPart : propertiesMappingString.split(";")) {
            String[] propertyMappingParts = propertiesMappingPart.split(":");
            if (propertyMappingParts.length != 2) {
                throw new RuntimeException(String.format(
                        "Invalid mapping property '%s' in the context of mapping properties: '%s'.",
                        propertiesMappingPart, propertiesMappingString));
            }
            propertiesMapping.add(new PropertyMapping(propertyMappingParts[0], propertyMappingParts[1]));
        }
        return propertiesMapping;
    }

    private List<PropertyMapping> getAllPropertiesMappings() {
        List<PropertyMapping> propertiesMapping = new ArrayList<PropertyMapping>();
        for (String propertyName : getDao().getAllPropertiesNames()) {
            propertiesMapping.add(new PropertyMapping(propertyName, propertyName));
        }
        return propertiesMapping;
    }

    private static final class PropertyMapping {

        final String name;
        final String header;

        public PropertyMapping(String name, String header) {
            this.name = name;
            this.header = header;
        }
    }

    public void persist(T entity) {
        getDao().persist(entity);
    }

    public void refreshCalculations() {
        getDao().getEntityManager().createNativeQuery("select from refresh_calculations()");
    }

    public void merge(K id, T entity) {
        getDao().getClassMetadata().setIdentifier(entity, id, EntityMode.POJO);
        getDao().merge(entity);
    }

    public void delete(K id) {
        T entity = getDao().find(id);
        if (entity != null) {
            getDao().remove(entity);
        }
    }

    protected void persistDerivated(Serializable primaryKey, Object entity) {
        if (primaryKey != null && getDao().getEntityManager().find(entity.getClass(), primaryKey) == null) {
            ClassMetadata classMetadata = getDao().getClassMetadata(entity.getClass());
            classMetadata.setIdentifier(entity, primaryKey, EntityMode.POJO);
            getDao().getEntityManager().persist(entity);
        }
    }
}
