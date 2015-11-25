/*
 *  OpenSDI Manager 2
 *  Copyright (C) 2014 GeoSolutions S.A.S.
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

import it.geosolutions.opensdi2.configurations.exceptions.OSDIConfigurationException;
import it.geosolutions.opensdi2.configurations.model.OSDIConfigurationKVP;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Controller for the base file manager.
 * **NOTE** : This have to be hard-coded because of this https://jira.spring.io/browse/SPR-5757
 * Once the issue is resolved and Spring Updated, use xml configuration to map base
 * class.
 * @author Lorenzo Natali (lorenzo.natali at geo-solutions.it)
 *
 */
@Controller
@RequestMapping("/vibi")
public class FileUpload extends BaseFileManager {

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public void upload(
			@RequestParam MultipartFile file,
			@RequestParam(required = false, defaultValue = "uploadedFile") String name,
			@RequestParam(required = false, defaultValue = "-1") int chunks,
			@RequestParam(required = false, defaultValue = "-1") int chunk,
			@RequestParam(required = false) String folder,
			HttpServletRequest request, HttpServletResponse servletResponse)
			throws IOException {
		super.upload("/", file, name, chunks, chunk, folder, request, servletResponse);
	}
}
