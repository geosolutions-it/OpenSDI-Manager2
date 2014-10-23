/*
 *  Copyright (C) 2013 GeoSolutions S.A.S.
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
package it.geosolutions.opensdi.model;

import java.util.EnumMap;
import java.util.Map;

/**
 *
 * @author ETj (etj at geo-solutions.it)
 */
public enum Season {
    NONE("", new String[]{}),
    RABI(Names.RABI, new String[]{Names.RABI}),
    KHARIF(Names.KHARIF, new String[]{Names.KHARIF}),
    RABI_KHARIF("RABI,KHARIF", new String[]{Names.RABI,Names.KHARIF});

    public static class Names {
        public final static String RABI = "RABI";
        public final static String KHARIF = "KHARIF";
    }

    private String representation;
    private String[] arrayRepresentation;


    private Season(String representation, String []arr) {
        this.representation = representation;
        this.arrayRepresentation = arr;
    }    

    private static final Map<Season, Map<Season, Season>> addTable = new EnumMap<Season, Map<Season, Season>>(Season.class);
    static{
        Map<Season, Season> base;
        // NONE
        base = new EnumMap<Season, Season>(Season.class);
        addTable.put(NONE, base);
        base.put(NONE, NONE);
        base.put(RABI, RABI);
        base.put(KHARIF, KHARIF);
        base.put(RABI_KHARIF, RABI_KHARIF);

        // RABI
        base = new EnumMap<Season, Season>(Season.class);
        addTable.put(RABI, base);
        base.put(NONE, RABI);
        base.put(RABI, RABI);
        base.put(KHARIF, RABI_KHARIF);
        base.put(RABI_KHARIF, RABI_KHARIF);

        // KHARIF
        base = new EnumMap<Season, Season>(Season.class);
        addTable.put(KHARIF, base);
        base.put(NONE,KHARIF);
        base.put(RABI,RABI_KHARIF);
        base.put(KHARIF, KHARIF);
        base.put(RABI_KHARIF,RABI_KHARIF);

        // RABI_KHARIF
        base = new EnumMap<Season, Season>(Season.class);
        addTable.put(RABI_KHARIF, base);
        base.put(NONE,RABI_KHARIF);
        base.put(RABI,RABI_KHARIF);
        base.put(KHARIF,RABI_KHARIF);
        base.put(RABI_KHARIF,RABI_KHARIF);
    }

    public static Season parse(String s) {
        Season season = NONE;
        if(s != null) {
            if(s.toLowerCase().contains("rabi")) {
                season = season.add(RABI);
            }
            if(s.toLowerCase().contains("kharif")) {
                season = season.add(KHARIF);
            }
        }
        return season;
    }

    public Season add(Season s) {
        return addTable.get(this).get(s);
    }

    @Override
    public String toString() {
        return representation;
    }

    public String[] toArray() {
        return arrayRepresentation;
    }

}
