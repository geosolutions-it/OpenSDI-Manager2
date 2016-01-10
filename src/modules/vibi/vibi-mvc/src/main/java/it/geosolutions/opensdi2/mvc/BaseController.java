package it.geosolutions.opensdi2.mvc;

import com.googlecode.genericdao.search.SearchResult;
import it.geosolutions.opensdi2.old.dto.CRUDResponseWrapper;
import it.geosolutions.opensdi2.service.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
import java.util.UUID;

public abstract class BaseController<T> extends BaseFileManager {

    private static File temporaryFolder = new File(System.getProperty("java.io.tmpdir"));

    protected abstract BaseService<T> getBaseService();

    @RequestMapping(value = "", method = RequestMethod.GET)
    public
    @ResponseBody
    CRUDResponseWrapper<T> list(@RequestParam(required = false) String keyword,
                                @RequestParam(required = false) String filters,
                                @RequestParam(required = false) String ordering,
                                @RequestParam(required = false, defaultValue = "50") Integer maxResults,
                                @RequestParam(required = false, defaultValue = "-1") Integer firstResult,
                                @RequestParam(required = false, defaultValue = "-1") Integer page) {
        SearchResult searchResult = getBaseService().getAll(keyword, filters, ordering, maxResults, firstResult, page);
        CRUDResponseWrapper<T> responseWrapper = new CRUDResponseWrapper<T>();
        responseWrapper.setCount(searchResult.getResult().size());
        responseWrapper.setTotalCount(searchResult.getTotalCount());
        responseWrapper.setData(searchResult.getResult());
        return responseWrapper;
    }


    @RequestMapping(value = "export", method = {RequestMethod.POST, RequestMethod.GET})
    public void export(@RequestParam(required = false) String keyword,
                       @RequestParam(required = false) String filters,
                       @RequestParam(required = false) String ordering,
                       @RequestParam(required = false, defaultValue = "50") Integer maxResults,
                       @RequestParam(required = false, defaultValue = "-1") Integer firstResult,
                       @RequestParam(required = false, defaultValue = "-1") Integer page,
                       @RequestParam(required = false) String format,
                       @RequestParam(required = false) String propertiesMappings,
                       HttpServletResponse response) {
        SearchResult searchResult = getBaseService().getAll(keyword, filters, ordering, maxResults, firstResult, page);
        File folder = new File(temporaryFolder, UUID.randomUUID().toString());
        super.newFolder("", folder.getPath());
        try {
            String file = handleExport(format, folder, searchResult.getResult(), propertiesMappings);
            super.downloadFile("", folder.getAbsolutePath(), file, response);
        } finally {
            super.deleteFolder("", folder.getAbsolutePath(), "");
        }
    }

    private String handleExport(String format, File folder, List<Object> entities, String propertiesMappings) {
        if (format == null || format.equalsIgnoreCase("csv")) {
            return handleCsv(folder, entities, propertiesMappings);
        }
        if (format.equalsIgnoreCase("excel")) {
            return handleCsv(folder, entities, propertiesMappings);
        }
        throw new RuntimeException(String.format(
                "Invalid export format '%s', available ones are: ['%s', '%s'].", format, "csv", "excel"));
    }

    private String handleCsv(File folder, List<Object> entities, String propertiesMappings) {
        String fileName = String.format("%s-%s.csv", System.currentTimeMillis(), getBaseService().getEntityName());
        File file = new File(folder, fileName);
        getBaseService().writeEntitiesToCsv(file, entities, propertiesMappings);
        return fileName;
    }
}