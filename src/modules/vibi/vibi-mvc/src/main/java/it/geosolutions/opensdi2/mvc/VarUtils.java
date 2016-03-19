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
