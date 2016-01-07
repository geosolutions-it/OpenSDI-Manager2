package it.geosolutions.opensdi2.mvc;

public final class DbUtils {

    public static String URL = getVibiDbUrl();

    private DbUtils() {
    }

    private static String getVibiDbUrl() {
        String dbUrl = System.getenv("VIBI_DB_URL");
        if (dbUrl == null) {
            dbUrl = System.getProperty("VIBI_DB_URL", "postgresql://postgres:postgres@127.0.0.1:5432/postgres");
        }
        return dbUrl;
    }
}
