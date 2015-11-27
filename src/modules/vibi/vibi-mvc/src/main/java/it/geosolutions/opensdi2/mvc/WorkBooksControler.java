package it.geosolutions.opensdi2.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.Pattern;


@Controller
@RequestMapping("/vibi")
public final class WorkBooksControler extends BaseFileManager {

    private final static Pattern allowedFileExtensions = Pattern.compile(".+?\\.(?:(?:xls$)|(?:xlsx$))");

    @RequestMapping(value = "download", method = {RequestMethod.POST, RequestMethod.GET})
    public void download(
            @RequestParam String folder,
            @RequestParam String file,
            HttpServletRequest request, HttpServletResponse response) {
        super.downloadFile("", folder, file, response);
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public void upload(
            @RequestParam MultipartFile file,
            @RequestParam String name,
            @RequestParam(required = false, defaultValue = "-1") int chunks,
            @RequestParam(required = false, defaultValue = "-1") int chunk,
            @RequestParam(required = false) String folder,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (!allowedFileExtensions.matcher(name).matches()) {
            throw new RuntimeException("Only Excel files (xls, xlsx) are allowed: '" + name + "'.");
        }
        String uniqueName = UUID.randomUUID().toString() + "_uuid_" + name;
        super.upload("", file, name, uniqueName, chunks, chunk, folder, request, response);
    }
}
