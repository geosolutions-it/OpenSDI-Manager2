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

// TODO: we should be using the module configurations

public final class VarUtils {

    public static String DB_URL = getVarValue("VIBI_DB_URL", "postgresql://postgres:postgres@127.0.0.1:5432/postgres");

    public static String GEOBATCH_INPUT = getVarValue("VIBI_GEOBATCH_INPUT", null);

    public static String GEOBATCH_OUTPUT = getVarValue("VIBI_GEOBATCH_OUTPUT", null);

    private VarUtils() {
    }

    private static String getVarValue(String varName, String fallBack) {
        String value = System.getenv(varName);
        if (value == null) {
            if (fallBack == null) {
                throw new RuntimeException(String.format("Property '%s' is missing.", varName));
            }
            value = System.getProperty(varName, fallBack);
        }
        return value;
    }
}
