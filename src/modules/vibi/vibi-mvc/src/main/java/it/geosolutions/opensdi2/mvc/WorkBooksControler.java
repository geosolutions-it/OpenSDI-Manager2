/*
 *  Copyright (C) 2016 GeoSolutions S.A.S.
 *  http://www.geo-solutions.it
 *
 *  GPLv3 + Classpath exception
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.geosolutions.opensdi2.mvc;

import it.geosolutions.opensdi2.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;


@Controller
@RequestMapping("/vibi")
public final class WorkBooksControler extends BaseFileManager {

    @Autowired
    SecurityService securityService;
    
    /**
     * a list of available locations
     */
    List<String> locationsList = new ArrayList<String>();
    {
        locationsList.add("EMP");
        locationsList.add("MW");
        locationsList.add("NCNE");
    }

    private final static Pattern allowedFileExtensions = Pattern.compile(".+?\\.(?:(?:xls$)|(?:xlsx$))");

    @RequestMapping(value = "download", method = {RequestMethod.POST, RequestMethod.GET})
    public void download(
            @RequestParam(required = false) String folder,
            @RequestParam String file,
            HttpServletRequest request, HttpServletResponse response) {
        securityService.validate("download", "workbook", null, null, null);
        String finalFolder = folder == null ? VarUtils.GEOBATCH_OUTPUT : folder;
        super.downloadFile("", finalFolder, file, response);
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public void upload(
            @RequestParam MultipartFile file,
            @RequestParam String name,
            @RequestParam(required = false, defaultValue = "-1") int chunks,
            @RequestParam(required = false, defaultValue = "-1") int chunk,
            @RequestParam(required = false) String folder,
            @RequestParam(required = false) String location,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        securityService.validate("upload", "workbook", null, null, null);
        String finalFolder = folder == null ? VarUtils.GEOBATCH_INPUT : folder;
        if (!allowedFileExtensions.matcher(name).matches()) {
            throw new RuntimeException("Only Excel files (xls, xlsx) are allowed: '" + name + "'.");
        }
        
        String locationTag = "_uuid_";
        if(
                location != null
                && locationsList != null
                && locationsList.size() > 0
                && locationsList.contains(location.toUpperCase())){
            locationTag = "_"+location+"_";
        }
        
        String uniqueName = UUID.randomUUID().toString() + locationTag + name;
        super.upload("", file, name, uniqueName, chunks, chunk, finalFolder, request, response);
    }
}
