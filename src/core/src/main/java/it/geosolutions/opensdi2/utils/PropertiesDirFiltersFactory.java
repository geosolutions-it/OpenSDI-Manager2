/*
 *  Copyright (C) 2007-2012 GeoSolutions S.A.S.
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
import java.io.FileFilter;

/**
 * This Class return a proper implementation of the class {@link FileFilter} useful to get a <b>resource<b> inside the OpenSDI manager Configuration Directory.
 * The resource is a File instance that can be retrieved, using the proper {@link FileFilter} instance, using the method {@link File#listFiles(FileFilter)}
 * and represent:
 * <ul>
 * <li>The configuration directory of a module ( {@link ModulesDirFilter} )</li>
 * <li>The configuration file inside a module configuration directory ( {@link InstancesConfigFilter} )</li>
 * </ul> 
 * See the OpenSDI2 documentation for more info about the Configuration datadir structure.
 * 
 * 
 * @author DamianoG
 * 
 */
public class PropertiesDirFiltersFactory {

    public enum FILTER_TYPE {
        MODULE, INSTANCE, MODULE_LIST, INSTANCE_LIST, ALL_DIRS, ALL_FILES
    }

    final public static String MODULE_NAME_PREFIX = "mod_";

    final public static String INSTANCE_CONFIGNAME_PREFIX = "config_";
    
    final public static String INSTANCE_CONFIGNAME_EXTENSION = ".properties";

    /**
     * Helper method useful for client classes in order to load the proper FileFilter instance
     * 
     * @param filter
     * @param param
     * @return
     */
    public FileFilter getFilter(FILTER_TYPE filter, String param) {

        FileFilter ff = null;
        switch (filter) {
        case MODULE:
            ff = this.new ModulesDirFilter(param);
            break;
        case INSTANCE:
            ff = this.new InstancesConfigFilter(param);
            break;
        case MODULE_LIST:
            ff = this.new ModulesListDirFilter();
            break;
        case INSTANCE_LIST:
            ff = this.new InstancesListConfigFilter();
            break;    
        case ALL_FILES:
            ff = this.new ListFileFilter();
            break;    
        case ALL_DIRS:
            ff = this.new ListDirFilter();
            break;                
        default:
            ff = null;
            break;
        }
        return ff;
    }

    /**
     * FileFilter implementation to select the module directory inside the OpenSDI2 configuration datadir  
     * 
     * @author DamianoG
     *
     */
    public class ModulesDirFilter implements FileFilter {

        private String moduleName;

        public ModulesDirFilter(String moduleName) {
            this.moduleName = moduleName;
        }

        @Override
        public boolean accept(File arg0) {
            String[] nameSplitted = arg0.getName().split("_");
            if (nameSplitted.length != 2) {
                return false;
            }

            if (arg0 != null && arg0.isDirectory()
                    && (nameSplitted[0] + "_").equals(MODULE_NAME_PREFIX)
                    && nameSplitted[1].equals(moduleName)) {
                return true;
            }
            return false;
        }
    }

    /**
     * FileFilter implementation to select the configuration file inside an OpenSDI2 module configuration datadir
     * 
     * @author DamianoG
     *
     */
    public class InstancesConfigFilter implements FileFilter {

        private String instanceName;

        public InstancesConfigFilter(String instanceName) {
            this.instanceName = instanceName;
        }

        @Override
        public boolean accept(File arg0) {
            String[] nameSplitted = arg0.getName().split("_");
            if (nameSplitted.length != 2) {
                return false;
            }
            String[] instanceSplitted = nameSplitted[1].split("\\.");
            if (instanceSplitted.length != 2) {
                return false;
            }

            if (arg0 != null && arg0.isFile() && nameSplitted[0].equals(INSTANCE_CONFIGNAME_PREFIX.substring(0, INSTANCE_CONFIGNAME_PREFIX.length()-1))
                    && instanceSplitted[0].equals(instanceName)
                    && ("."+instanceSplitted[1]).equals(INSTANCE_CONFIGNAME_EXTENSION)) {
                return true;
            }
            return false;
        }
    }
    
    /**
     * FileFilter implementation to select all the module directories inside the OpenSDI2 configuration datadir
     * 
     * @author DamianoG
     * 
     */
    public class ModulesListDirFilter implements FileFilter {

        @Override
        public boolean accept(File arg0) {
            String[] nameSplitted = arg0.getName().split("_");
            if (nameSplitted.length != 2) {
                return false;
            }

            if (arg0 != null && arg0.isDirectory()
                    && (nameSplitted[0] + "_").equals(MODULE_NAME_PREFIX)) {
                return true;
            }
            return false;
        }
    }

    /**
     * FileFilter implementation to select All the configuration files inside an OpenSDI2 module configuration datadir
     * 
     * @author DamianoG
     * 
     */
    public class InstancesListConfigFilter implements FileFilter {

        @Override
        public boolean accept(File arg0) {
            String[] nameSplitted = arg0.getName().split("_");
            if (nameSplitted.length != 2) {
                return false;
            }
            String[] instanceSplitted = nameSplitted[1].split("\\.");
            if (instanceSplitted.length != 2) {
                return false;
            }

            if (arg0 != null
                    && arg0.isFile()
                    && nameSplitted[0].equals(INSTANCE_CONFIGNAME_PREFIX.substring(0,
                            INSTANCE_CONFIGNAME_PREFIX.length() - 1))
                    && ("." + instanceSplitted[1]).equals(INSTANCE_CONFIGNAME_EXTENSION)) {
                return true;
            }
            return false;
        }
    }
    
    /**
     * FileFilter implementation to select only and all the readable and writable directories inside another directory
     * 
     * @author DamianoG
     * 
     */
    public class ListDirFilter implements FileFilter {

        @Override
        public boolean accept(File arg0) {
            if (arg0 != null && arg0.isDirectory() && arg0.canRead() && arg0.canWrite()) {
                return true;
            }
            return false;
        }
    }

    /**
     * FileFilter implementation to select only and all the readable and writable files inside a directory
     * 
     * @author DamianoG
     * 
     */
    public class ListFileFilter implements FileFilter {

        @Override
        public boolean accept(File arg0) {
            if (arg0 != null && arg0.isFile() && arg0.canRead() && arg0.canWrite()) {
                return true;
            }
            return false;
        }
    }
}
