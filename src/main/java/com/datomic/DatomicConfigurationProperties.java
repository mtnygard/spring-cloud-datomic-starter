package com.datomic;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author <a href="mailto:mtnygard@gmail.com">Michael T. Nygard</a>
 * @author <a href="mailto:josh@joshlong.com">Josh Long</a>
 */
@ConfigurationProperties(prefix = "datomic")
public class DatomicConfigurationProperties {
    private String uri;
    private String storageType;
    private String dbName;

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String determineDatomicUri() {
        if (uri != null) {
            return uri;
        } else {

            return "datomic:" + storageType + "://" + dbName;
        }
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
}
