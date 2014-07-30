/*
 *  OpenSDI Manager
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
package it.geosolutions.opensdi2.service;

import java.io.File;
import java.io.IOException;
import java.util.Map.Entry;

import org.springframework.web.multipart.MultipartFile;

/**
 * File upload service handling. Is used to perform file upload in parts with PLUPLOAD and with simple file upload
 * 
 * @author adiaz
 */
public interface FileUploadService {

    /**
     * Add a chunk of a file upload
     * 
     * @param name of the file
     * @param chunks total for the file
     * @param chunk number on this upload
     * @param file with the content uploaded
     * @return current entry with the key of chunk data and the list of updated chunks
     * @throws IOException if no more uploads are available
     */
    public Entry<String, ?> addChunk(String name, int chunks, int chunk, MultipartFile file)
            throws IOException;

    /**
     * Create a temporal file with a byte array
     * 
     * @param key of the file
     * @param bytes to write
     * @param i index by the file name
     * @return absolute path to the file
     * @throws IOException
     */
    public String createTemporalFile(String key, byte[] bytes, int i) throws IOException;

    /**
     * Get a chunk of a file upload
     * 
     * @param name of the file
     * @param chunks total for the file
     * @param chunk number on this upload
     * @param file with the content uploaded
     * @return current entry for the file
     * @throws IOException if no more uploads are available
     */
    public Entry<String, ?> getChunk(String name, int chunks, int chunk) throws IOException;

    /**
     * @return pending upload files size
     */
    public int size();

    /**
     * Remove a file upload
     * 
     * @param key
     */
    public void remove(String key);

    /**
     * This method cleans concurrent uploading files in two executions. It's ready to be called on a cronable method to check if there are pending
     * incomplete files without changes in the interval.
     */
    public void cleanup();

    /**
     * Obtain a temporal file item with chunked bytes
     * 
     * @param name
     * @param entry
     * @return
     */
    public File getCompletedFile(String name, Entry<String, ?> entry);

    /**
     * Obtain the final file
     * 
     * @param name
     * @param targetPath complete path for the file
     * @param entry
     * @return
     */
    public File getCompletedFile(String name, String targetPath, Entry<String, ?> entry);

    /**
     * Get a temporal file from a single multipart file
     * 
     * @param name of the file
     * @param file with the content uploaded
     * @return File
     * @throws IOException if something occur while file generation
     */
    public File getCompletedFile(String name, MultipartFile file) throws IOException;

    /**
     * Get a file from a single multipart file to a file path
     * 
     * @param file
     * @param targetPath
     * 
     * @throws IOException if something occur while file generation
     */
    public File getCompletedFile(MultipartFile file, String targetPath) throws IOException;

}
