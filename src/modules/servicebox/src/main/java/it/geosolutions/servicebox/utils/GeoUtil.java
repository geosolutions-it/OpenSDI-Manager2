package it.geosolutions.servicebox.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.FeatureStore;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.geojson.feature.FeatureJSON;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;

public class GeoUtil {

    public static Geometry geoDataParse(String type, String geoData) throws ParseException {

        if (type.equalsIgnoreCase("WKT")) {
            return new WKTReader().read(geoData);
        }

        return null;
    }

    public static String geometryWrite(String type, Geometry geometry) {

        if (type.equalsIgnoreCase("WKT")) {
            return new WKTWriter().write(geometry);
        }

        return null;
    }

    /**
     * Convert a shape file to GeoJSON format
     * 
     * @param shpFile
     * @param targetJSON
     * @throws IOException
     */
    public static void toJson(File shpFile, String targetJSON) throws IOException {
        toJson(shpFile, targetJSON, null);
    }

    /**
     * Convert a shape file to GeoJSON format
     * 
     * @param shpFile
     * @param targetJSON
     * @param logger
     * @throws IOException
     */
    public static void toJson(File shpFile, String targetJSON, Logger logger) throws IOException {

        // get the feature iterator from the shp file
        ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("url", shpFile.toURI().toURL());
        ShapefileDataStore shpDataStore = (ShapefileDataStore) dataStoreFactory
                .createNewDataStore(params);
        String typeName = shpDataStore.getTypeNames()[0];
        SimpleFeatureSource featureSource = shpDataStore.getFeatureSource(typeName);
        SimpleFeatureIterator sfi = null;

        // convert to JSON
        FeatureJSON fjson = new FeatureJSON();
        FileWriter writer = new FileWriter(targetJSON);
        try {
            if (featureSource instanceof FeatureStore) {
                SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;
                sfi = featureStore.getFeatures().features();
                while (sfi.hasNext()) {
                    SimpleFeature sf = sfi.next();
                    fjson.writeFeature(sf, writer);
                }
            } else {
                if (logger != null) {
                    logger.log(Level.SEVERE, typeName + " does not support read/write access");
                }
            }
            writer.flush();
        } catch (Exception e) {
            if (logger != null) {
                logger.log(Level.SEVERE, "Error on SHP to GeoJSON conversion", e);
            }
        } finally {
            // release iterator
            if (sfi != null)
                sfi.close();
        }
    }

}
