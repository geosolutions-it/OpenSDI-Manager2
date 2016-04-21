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

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import it.geosolutions.opensdi2.old.dto.CRUDResponseWrapper;
import org.apache.log4j.Logger;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

final class MvcTestsUtils {

    private final static Logger LOGGER = Logger.getLogger(MvcTestsUtils.class);

    static String VIBI_TEST_SERVER = "VIBI_TEST_SERVER";

    static boolean MVC_TEST_ACTIVATED;

    static String BASE_URL;

    static String ADMIN_USER;
    static String ADMIN_PASS;

    static {
        String rawParameters = System.getenv(VIBI_TEST_SERVER);
        if (rawParameters == null) {
            rawParameters = System.getProperty(VIBI_TEST_SERVER);
            if (rawParameters == null) {
                MVC_TEST_ACTIVATED = false;
                LOGGER.warn("To activate VIBI tests setup a VIBI server and define test parameters" +
                        " using OS environment or Java property VIBI_TEST_SERVER. " +
                        "As an example: 'host=localhost;port=9191;user=admin;pass=admin'");
            }
        }
        if (rawParameters != null) {
            MVC_TEST_ACTIVATED = true;
            Map<String, String> parameters = new HashMap<String, String>();
            for (String rawParameter : rawParameters.split(";")) {
                String[] rawParameterParts = rawParameter.split("=");
                parameters.put(rawParameterParts[0], rawParameterParts[1]);
            }
            BASE_URL = String.format("http://%s:%s/opensdi2-manager/mvc/vibi/",
                    parameters.get("host"), parameters.get("port"));
            ADMIN_USER = parameters.get("user");
            ADMIN_PASS = parameters.get("pass");
        }
    }

    private MvcTestsUtils() {
    }

    static <T> List<T> list(GenericType<CRUDResponseWrapper<T>> genericType, String entityName, String keyword, String filters,
                            String ordering, Integer maxResults, Integer firstResult, Integer page) {
        ClientConfig configuration = new DefaultClientConfig();
        configuration.getClasses().add(JacksonJsonProvider.class);
        Client client = Client.create(configuration);
        client.addFilter(new HTTPBasicAuthFilter(ADMIN_USER, ADMIN_PASS));
        WebResource webResource = client.resource(BASE_URL + entityName);
        webResource = addQueryParameter(webResource, "keyword", keyword);
        webResource = addQueryParameter(webResource, "filters", filters);
        webResource = addQueryParameter(webResource, "ordering", ordering);
        webResource = addQueryParameter(webResource, "maxResults", maxResults);
        webResource = addQueryParameter(webResource, "firstResult", firstResult);
        webResource = addQueryParameter(webResource, "page", page);
        ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
        assertThat(response.getStatus(), is(200));
        return response.getEntity(genericType).getData();
    }

    static void create(String entityName, String definition) {
        ClientConfig configuration = new DefaultClientConfig();
        configuration.getClasses().add(JacksonJsonProvider.class);
        Client client = Client.create(configuration);
        client.addFilter(new HTTPBasicAuthFilter(ADMIN_USER, ADMIN_PASS));
        WebResource webResource = client.resource(BASE_URL + entityName);
        ClientResponse response = webResource.type("application/json").post(ClientResponse.class, definition);
        assertThat(response.getStatus(), is(200));
    }

    static void update(String entityName, String entityId, String definition) {
        ClientConfig configuration = new DefaultClientConfig();
        configuration.getClasses().add(JacksonJsonProvider.class);
        Client client = Client.create(configuration);
        client.addFilter(new HTTPBasicAuthFilter(ADMIN_USER, ADMIN_PASS));
        WebResource webResource = client.resource(BASE_URL + entityName + "/" + encode(entityId));
        ClientResponse response = webResource.type("application/json").put(ClientResponse.class, definition);
        assertThat(response.getStatus(), is(200));
    }

    static void delete(String entityName, String entityId) {
        ClientConfig configuration = new DefaultClientConfig();
        configuration.getClasses().add(JacksonJsonProvider.class);
        Client client = Client.create(configuration);
        client.addFilter(new HTTPBasicAuthFilter(ADMIN_USER, ADMIN_PASS));
        WebResource webResource = client.resource(BASE_URL + entityName + "/" + encode(entityId));
        webResource.delete();
    }

    static String encode(String string) {
        try {
            return URLEncoder.encode(string.replaceAll("%", "%25"), "UTF-8");
        } catch (Exception exception) {
            throw new RuntimeException(String.format("Error encoding string '%s'.", string), exception);
        }
    }

    static String readResourceFile(String relativeFilePath) {
        URL resource = MvcTestsUtils.class.getClassLoader().getResource(relativeFilePath);
        assertThat(resource, notNullValue());
        return readFile(new File(resource.getFile()));
    }

    static String readFile(File file) {
        try {
            FileInputStream input = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            input.read(data);
            input.close();
            return new String(data).replaceAll("\\r\\n", "\n");
        } catch (Exception exception) {
            throw new RuntimeException(String.format("Error reading file '%s' content.",
                    file.getAbsolutePath()), exception);
        }
    }

    static WebResource addQueryParameter(WebResource webResource, String key, Object value) {
        if (value != null) {
            return webResource.queryParam(key, value.toString());
        }
        return webResource;
    }

    static String safe(Object object, String fallBack) {
        return object == null ? fallBack : object.toString();
    }
}
