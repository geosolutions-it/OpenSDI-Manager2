package it.geosolutions.opensdi2.persistence;

import com.googlecode.genericdao.dao.jpa.GenericDAOImpl;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;
import com.googlecode.genericdao.search.Sort;
import com.googlecode.genericdao.search.jpa.JPASearchProcessor;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.StringType;
import org.hibernate.type.TextType;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Transactional(value = "opensdiTransactionManager")
@Repository
public abstract class BaseDao<T, ID extends Serializable> extends GenericDAOImpl<T, ID> implements GenericVibiDao<T, ID> {

    private final static Logger LOGGER = LoggerFactory.getLogger(BaseDao.class);

    private final static Pattern propertyFilterPattern = Pattern.compile("^(.*?):(.*?):'(.*?)'$");
    private final static Pattern propertySortPattern = Pattern.compile("^(.*?):((?:ASC)|(?:DESC))$");

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        super.setEntityManager(entityManager);
    }

    @Override
    @Autowired
    public void setSearchProcessor(JPASearchProcessor searchProcessor) {
        super.setSearchProcessor(searchProcessor);
    }

    public boolean removeByPK(String[] names, Serializable... pkObjects) {
        T found = searchByPK(names, pkObjects);
        if (found != null) {
            // Remove
            boolean ret = remove((T) found);
            return ret;
        }
        return false;
    }

    public boolean removeByPK(Serializable... pkObjects) {
        return removeByPK(getPKNames(), pkObjects);
    }

    /**
     * Obtain a entity by its pk
     *
     * @return entity found or null if not found
     */
    public T searchByPK(Serializable... pkObjects) {
        return searchByPK(getPKNames(), pkObjects);
    }

    /**
     * Obtain a entity by its pk
     *
     * @return entity found or null if not found
     */
    public T searchByPK(String[] names, Serializable... pkObjects) {
        T found = null;
        // Check if correct pkObjects
        if (names != null && pkObjects != null && pkObjects.length == names.length) {
            Search search = new Search(persistentClass);

            int index = 0;
            for (String name : names) {
                Object value = pkObjects[index++];
                if (value != null) {
                    search.addFilterEqual(name, value);
                }
            }
            try {
                found = searchUnique(search);
            } catch (NonUniqueResultException e) {
                LOGGER.error(
                        "Non unique result searching for  "
                                + persistentClass.getSimpleName() + " - " + search,
                        e);
            } catch (Exception e) {
                LOGGER.error(
                        "Result not found for  "
                                + persistentClass.getSimpleName() + " - " + search,
                        e);
                // return null
            }
        }
        // Return found object
        return found;
    }

    /**
     * Returns the field identified as unique, that represent the composite primary key in the Relational model.
     * <p/>
     * This method should replace the hardcoded list in the old DAO objects.
     *
     * @return
     */
    protected String[] getUniqueFields() {

        Class clazz = getEntityType();

        Table tableAnnotation = (Table) getEntityType().getAnnotation(Table.class);
        if (tableAnnotation == null) {
            // No Table annotation on this class, return an 0 length array
            return new String[0];
        }
        UniqueConstraint[] uc = tableAnnotation.uniqueConstraints();
        if (uc.length == 0) {
            // No unique constraints on this entity, return an 0 length array
            return new String[0];
        } else if (uc.length != 1) {
            // TODO: remove this for VIBI
            throw new IllegalStateException("Annotation 'Table' for entity Fertilizer has more than 1 unique constraint definition... this should never happen...");
        }
        return uc[0].columnNames().clone();
    }

    @Override
    public ClassMetadata getClassMetadata() {
        return sessionFactory.getClassMetadata(getEntityType());
    }

    @Override
    public ClassMetadata getClassMetadata(Class entityType) {
        return sessionFactory.getClassMetadata(entityType);
    }

    @Override
    public String[] getAllPropertiesNames() {
        ClassMetadata classMetadata = getClassMetadata();
        String[] normalProperties = classMetadata.getPropertyNames();
        String[] properties = Arrays.copyOf(normalProperties, normalProperties.length + 1);
        properties[properties.length - 1] = classMetadata.getIdentifierPropertyName();
        return properties;
    }

    @Override
    public Filter getKeyWordSearchFilter(String keyword) {
        List<Filter> filters = new ArrayList<Filter>();
        ClassMetadata classMetadata = getClassMetadata();
        keyword = "%" + keyword + "%";
        for (String property : getAllPropertiesNames()) {
            if (classMetadata.getPropertyType(property).getName().equals("string")) {
                filters.add(Filter.ilike(property, keyword));
            } else {
                filters.add(Filter.custom(String.format("lower(cast({%s} as string)) like lower(?1)", property), keyword));
            }
        }
        return Filter.or(filters.toArray(new Filter[filters.size()]));
    }

    @Override
    public Filter getPropertiesFilter(String filtersString) {
        String[] filterParts = filtersString.split(";");
        List<Filter> filters = new ArrayList<Filter>();
        ClassMetadata classMetadata = getClassMetadata();
        for (String propertyFilter : filterParts) {
            filters.add(parsePropertyFilter(classMetadata, propertyFilter));
        }
        return Filter.and(filters.toArray(new Filter[filters.size()]));
    }

    @Override
    public List<Sort> getPropertiesSorting(String sortingString) {
        String[] sortingParts = sortingString.split(";");
        List<Sort> sorts = new ArrayList<Sort>();
        for (String propertySort : sortingParts) {
            sorts.add(getSortingForProperty(propertySort));
        }
        return sorts;
    }

    private Filter parsePropertyFilter(ClassMetadata classMetadata, String propertyFilter) {
        Matcher matcher = propertyFilterPattern.matcher(propertyFilter);
        if (!matcher.matches()) {
            throw new RuntimeException(String.format("Invalid property filter '%s'.", propertyFilter));
        }
        String propertyName = matcher.group(1);
        String operator = matcher.group(2);
        String expression = matcher.group(3);
        if(isStringType(classMetadata, propertyName)) {
            return Filter.custom(String.format("{%s} %s '%s'", propertyName, operator, expression));
        }
        return Filter.custom(String.format("{%s} %s %s", propertyName, operator, expression));
    }

    private boolean isStringType(ClassMetadata classMetadata, String propertyName) {
        Type type;
        if(propertyName.equals(classMetadata.getIdentifierPropertyName())) {
            type = classMetadata.getIdentifierType();
        } else {
            type = classMetadata.getPropertyType(propertyName);
        }
        return type instanceof StringType || type instanceof TextType;
    }

    private Sort getSortingForProperty(String propertySort) {
        Matcher matcher = propertySortPattern.matcher(propertySort);
        if (!matcher.matches()) {
            throw new RuntimeException(String.format("Invalid property sorting '%s'.", propertySort));
        }
        String propertyName = matcher.group(1);
        String order = matcher.group(2);
        if (order.equals("ASC")) {
            return Sort.asc(propertyName);
        }
        return Sort.desc(propertyName);
    }

    @Override
    public void persist(T entity) {
        em().persist(entity);
    }

    @Override
    public EntityManager getEntityManager() {
        return em();
    }
}
