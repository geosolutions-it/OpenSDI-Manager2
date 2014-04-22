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
package it.geosolutions.opensdi2.utils;

import java.io.File;

import org.apache.log4j.Logger;

/**
 * Controller utilities
 * 
 * @author adiaz
 * 
 */
public class ControllerUtils {

	/**
	 * Separator for composite descriptors
	 */
	public static final String SEPARATOR = System.getProperty("file.separator");

	/**
	 * DOT character "."
	 */
	public final static String DOT = ".";

	private final static Logger LOGGER = Logger
			.getLogger(ControllerUtils.class);

	/**
	 * remove /../ and /./ from a path
	 * 
	 * @param dirString
	 * @return
	 */
	public static String preventDirectoryTrasversing(String dirString) {
		// prevent directory traversing
		if (dirString == null)
			return null;
		dirString = dirString.replace("..", "");
		// clean path
		dirString = dirString.replace("/./", "/");
		dirString = dirString.replaceAll("/{2,}", "/");
		return dirString;
	}

	/**
	 * Get extension if exists in fileName
	 * 
	 * @param fileName
	 *            to obtain the extension
	 * @return file extension
	 */
	public static String getExtension(String fileName) {
		if (fileName != null && fileName.lastIndexOf(DOT) >= 0)
			return fileName.substring(fileName.lastIndexOf(DOT) + 1);
		else
			return null;
	}

	/**
	 * Remove extension if exists in fileName
	 * 
	 * @param fileName
	 *            to obtain the name
	 * @return name without extension
	 */
	public static String removeExtension(String fileName) {
		if (fileName != null && fileName.lastIndexOf(DOT) >= 0)
			fileName = fileName.substring(0, fileName.lastIndexOf(DOT));
		return fileName;
	}

	public static String normalizePath(String folderPath) {
		folderPath = normalizeSeparator(folderPath);
		if (!folderPath.endsWith(File.separator)) {
			LOGGER.debug("[WARN] path not ending with file name separator ("
					+ File.separator + "), appending one");
			folderPath = folderPath.concat(File.separator);
		}
		return folderPath;
	}

	public static String normalizeSeparator(String folderPath) {
		return folderPath.replace("/", File.separator).replace("\\",
				File.separator);
	}

	/**
	 * Clean duplicate (or more) separators for a string
	 * 
	 * @param origin
	 * @return replaced with simple separators
	 */
	public static String cleanDuplicateSeparators(String origin) {
		String result = origin;
		if (result != null) {
			while (result.contains(SEPARATOR + SEPARATOR)) {
				result = result.replace(SEPARATOR + SEPARATOR, SEPARATOR);
			}
		}
		return result;
	}

}
