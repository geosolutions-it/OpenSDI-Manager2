package it.geosolutions.opensdi2.mvc;

import org.apache.commons.exec.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Controller
@RequestMapping("/vibi")
public final class DataBaseDumpControler extends BaseFileManager {

    private static File temporaryFolder = new File(System.getProperty("java.io.tmpdir"));

    @RequestMapping(value = "sqlDump", method = {RequestMethod.POST, RequestMethod.GET})
    public void sqlDump(HttpServletResponse response) {
        File folder = new File(temporaryFolder, UUID.randomUUID().toString());
        super.newFolder("", folder.getPath());
        try {
            super.downloadFile("", folder.getAbsolutePath(), createSqlDump(folder), response);
        } finally {
            super.deleteFolder("", folder.getAbsolutePath(), "");
        }
    }

    private static String createSqlDump(File folder) {
        String fileName = String.format("%s-vibi.dump", System.currentTimeMillis());
        File file = new File(folder, fileName);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("dbUrl", DbUtils.URL);
        map.put("file", file);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            CommandLine commandLine = new CommandLine("pg_dump");
            commandLine.addArgument("--dbname=${dbUrl}", false);
            commandLine.addArgument("-F", false);
            commandLine.addArgument("c", false);
            commandLine.addArgument("-f", false);
            commandLine.addArgument("${file}", false);
            commandLine.setSubstitutionMap(map);
            DefaultExecutor executor = new DefaultExecutor();
            ExecuteWatchdog watchdog = new ExecuteWatchdog(28800000);
            executor.setWatchdog(watchdog);
            executor.setStreamHandler(new PumpStreamHandler(output));
            int exitValue = executor.execute(commandLine);
            if (exitValue != 0) {
                throw new RuntimeException("Error creating database dump file '" + file + "': " + output.toString());
            }
        } catch (ExecuteException exception) {
            throw new RuntimeException("Error creating database dump file '" + file + "'.", exception);
        } catch (IOException exception) {
            throw new RuntimeException("Error creating database dump file '" + file + "'.", exception);
        } finally {
            try {
                output.close();
            } catch (IOException exception) {
                LOGGER.error("Error closing output.", exception);
            }
        }
        return fileName;
    }
}
