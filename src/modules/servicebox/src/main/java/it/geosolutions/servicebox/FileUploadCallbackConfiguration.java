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

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Default configuration for a File upload callback
 * 
 * @author adiaz
 * 
 */
public class FileUploadCallbackConfiguration implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3221221019219179718L;

	private long maxSize;
	private int maxItems;
	private String tempFolder;
	int buffSize;
	private String fileTypes;
	private List<Pattern> fileTypePatterns;

	/**
	 * @return the maxSize
	 */
	public long getMaxSize() {
		return maxSize;
	}

	/**
	 * @param maxSize
	 *            the maxSize to set
	 */
	public void setMaxSize(long maxSize) {
		this.maxSize = maxSize;
	}

	/**
	 * @return the maxItems
	 */
	public int getMaxItems() {
		return maxItems;
	}

	/**
	 * @param maxItems
	 *            the maxItems to set
	 */
	public void setMaxItems(int maxItems) {
		this.maxItems = maxItems;
	}

	/**
	 * @return the tempFolder
	 */
	public String getTempFolder() {
		return tempFolder;
	}

	/**
	 * @param tempFolder
	 *            the tempFolder to set
	 */
	public void setTempFolder(String tempFolder) {
		this.tempFolder = tempFolder;
	}

	/**
	 * @return the buffSize
	 */
	public int getBuffSize() {
		return buffSize;
	}

	/**
	 * @param buffSize
	 *            the buffSize to set
	 */
	public void setBuffSize(int buffSize) {
		this.buffSize = buffSize;
	}

	/**
	 * @return the fileTypes
	 */
	public String getFileTypes() {
		return fileTypes;
	}

	/**
	 * @param fileTypes the fileTypes to set
	 */
	public void setFileTypes(String fileTypes) {
		this.fileTypes = fileTypes;
		// compile patterns
		if(this.fileTypes != null){
			List<Pattern> patterns = new LinkedList<Pattern>();
			for(String fileType: this.fileTypes.split(",")){
				patterns.add(Pattern.compile(fileType));
			}
			this.fileTypePatterns = patterns;
		}
	}

	/**
	 * @return the fileTypePatterns
	 */
	public List<Pattern> getFileTypePatterns() {
		return fileTypePatterns;
	}

	/**
	 * @param fileTypePatterns the fileTypePatterns to set
	 */
	public void setFileTypePatterns(List<Pattern> fileTypePatterns) {
		this.fileTypePatterns = fileTypePatterns;
	}

}
