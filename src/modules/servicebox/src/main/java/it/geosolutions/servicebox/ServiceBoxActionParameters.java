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

import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;

/**
 * Envelope for action callbacks and action servlets parameters sharing
 * 
 * @author adiaz
 * 
 */
public class ServiceBoxActionParameters {

	private boolean success = false;
	private List<FileItem> items;
	private Map<String, Object> extensions;

	/**
	 * @return the extensions
	 */
	public Map<String, Object> getExtensions() {
		return extensions;
	}

	/**
	 * @param extensions
	 *            the extensions to set
	 */
	public void setExtensions(Map<String, Object> extensions) {
		this.extensions = extensions;
	}

	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @param success
	 *            the success to set
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * @return the items
	 */
	public List<FileItem> getItems() {
		return items;
	}

	/**
	 * @param items
	 *            the items to set
	 */
	public void setItems(List<FileItem> items) {
		this.items = items;
	}

}
