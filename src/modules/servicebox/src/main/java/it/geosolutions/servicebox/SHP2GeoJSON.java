/*
 *  ServiceBox
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
package it.geosolutions.servicebox;

import it.geosolutions.servicebox.utils.GeoUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

/**
 * Servlet implementation class SHP2GeoJSON. It serve a shape file in GeoJSON format
 * 
 * @author adiaz
 */
public class SHP2GeoJSON extends FileUploader {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SHP2GeoJSON() {
		super();
	}

	/**
	 * @param response
	 * @param uuid
	 * @throws IOException
	 * @throws ServletException
	 */
	protected String readFileContents(HttpServletResponse response, String uuid)
			throws IOException, ServletException {
		StringBuilder content = new StringBuilder();
		File file = null;
		BufferedReader br = null;
		PrintWriter writer = null;
		try {
			// get file
			File originalFile = new File(tempDirectory + File.separatorChar
					+ uuid);
			File shpFile = new File(tempDirectory + File.separatorChar + uuid
					+ ".shp");
			FileUtils.moveFile(originalFile, shpFile);
			// convert it to GeoJSON
			String targetJSON = tempDirectory + File.separatorChar + uuid
					+ ".json";
			GeoUtil.toJson(shpFile, targetJSON, LOGGER);
			file = new File(targetJSON);
			br = new BufferedReader(new FileReader(file));
			if (response != null)
				writer = response.getWriter();
			String line = null;
			while ((line = br.readLine()) != null) {
				if (writer != null)
					writer.println(line);

				content.append(line.trim().replaceAll("\"", "'"));
			}
			// delete files
			file.delete();
			shpFile.delete();

			return content.toString();
		} catch (IOException ex) {
			if (LOGGER.isLoggable(Level.SEVERE)) {
				LOGGER.log(Level.SEVERE,
						"Error encountered while downloading file");
			}

			if (response != null) {
				response.setContentType("text/html");
				writeResponse(
						response,
						"{ \"success\":false, \"errorMessage\":\""
								+ ex.getLocalizedMessage() + "\"}");
			}

			return null;
		} finally {
			try {
				if (br != null) {
					br.close();
				}

				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				if (LOGGER.isLoggable(Level.SEVERE)) {
					LOGGER.log(Level.SEVERE, "Error closing streams ", e);
				}
				throw new ServletException(e.getMessage());
			}
		}
	}
}
