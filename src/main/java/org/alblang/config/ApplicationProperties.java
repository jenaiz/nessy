package org.alblang.config;

import org.alblang.exceptions.ServerException;

import java.io.InputStream;
import java.util.Properties;

/**
 * @author jesus.navarrete  (29/08/14)
 */
public class ApplicationProperties {

    public static final String ENV_PROPERTY = "PROFILE";

    public static String APP_PROPERTIES = "application.properties";

    final private Properties properties = new Properties();

    private static ApplicationProperties instance;

    public static ApplicationProperties getInstance() throws ServerException {
        if (instance == null) {
            instance = new ApplicationProperties();
        }
        return instance;
    }

    private ApplicationProperties() throws ServerException {

        try {
            final Properties envProperties = new Properties();

            String env = System.getenv(ENV_PROPERTY);
            if (env == null || env.isEmpty()) {
                env = "live";
            }

            final InputStream app = getClass().getClassLoader().getResourceAsStream(APP_PROPERTIES);
            final InputStream prod = getClass().getClassLoader().getResourceAsStream(env + "-" + APP_PROPERTIES);

            properties.load(app);
            envProperties.load(prod);

            app.close();
            prod.close();

            for (Object key : envProperties.keySet()) {
                properties.setProperty((String) key, envProperties.getProperty((String) key));
            }

        } catch (Exception e) {
            throw new ServerException("Error reading the properties for the application", e);
        }
    }

    public String getValue(final String property) {
        return properties.getProperty(property);
    }
}
