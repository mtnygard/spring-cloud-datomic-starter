package com.datomic;

import datomic.Util;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author <a href="mailto:mtnygard@gmail.com">Michael T. Nygard</a>
 * @author <a href="mailto:josh@joshlong.com">Josh Long</a>
 */
@ConfigurationProperties(prefix = "datomic")
public class ConnectionsProperties {

    public static class ConnectionProperties {

        public String deriveUri() {
            return StringUtils.hasText(this.uri) ? uri : uriFor(storage);
        }

        private static String encode(Object o) {
            try {
                return URLEncoder.encode(o.toString(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

        private static String queryString(Map<String, Object> paramValues) {
            Optional<String> qs = paramValues.entrySet().stream().
                    filter(e -> Objects.nonNull(e.getValue())).
                    map(e -> encode(e.getKey()) + "=" + encode(e.getValue())).
                    reduce((a, b) -> a + "&" + b);

            return qs.isPresent() ? "?" + qs.get() : "";
        }

        private String uriFor(String storage) {
            if ("cass".equals(storage)) {
                return cassandraUri();
            } else if ("couchbase".equals(storage)) {
                return couchbaseUri();
            } else if ("dev".equals(storage)) {
                return devUri();
            } else if ("ddb".equals(storage)) {
                return dynamoDbUri();
            } else if ("ddb-local".equals(storage)) {
                return dynamoDbLocalUri();
            } else if ("free".equals(storage)) {
                return freeUri();
            } else if ("inf".equals(storage)) {
                return infinispanUri();
            } else if ("mem".equals(storage)) {
                return memUri();
            } else if ("riak".equals(storage)) {
                return riakUri();
            } else if ("sql".equals(storage)) {
                return jdbcUri();
            } else {
                throw new IllegalArgumentException("Storage '" + storage + "' isn't (yet) supported directly. You can configure it by setting datomic.connections.<cxn>.uri and using a Datomic URI as shown at http://docs.datomic.com/javadoc/datomic/Peer.html");
            }
        }

        private String jdbcUri() {
            // datomic:sql://{db-name}?{jdbc-url}
            return String.format("datomic:sql://%s?%s", db, jdbcUrl);
        }

        private String riakUri() {
            // datomic:riak://{host}[:{port}]/{bucket}/{dbname}[?interface=http|https|protobuf]
            return String.format("datomic:riak://%s%s/%s/%s%s",
                    host == null ? "localhost" : host,
                    port == null ? "" : ":" + port,
                    db,
                    queryString(Util.map("interface", riakInterface)));
        }

        private String memUri() {
            //  datomic:mem://{db-name}
            return String.format("datomic:mem://%s", db);
        }

        private String infinispanUri() {
            //  datomic:inf://{cluster-member-host}:{port}/{db-name}
            return String.format("datomic:inf://%s:%s/%s", clusterMemberHost, port, db);
        }

        private String freeUri() {
            // datomic:free://{transactor-host}:{port}/{db-name}
            return String.format("datomic:dev://%s:%s/%s",
                    host == null ? "localhost" : host,
                    port == null ? 4334 : port,
                    db);
        }

        private String dynamoDbLocalUri() {
            // datomic:ddb-local://{endpoint}:{port}/{dynamodb-table}/{db-name}?aws_access_key_id={XXX}&aws_secret_key={YYY}
            return String.format("datomic:ddb-local://%s:%s/%s/%s%s",
                    host,
                    port,
                    dynamoDbTable,
                    db,
                    queryString(Util.map("aws_access_key_id", awsAccessKeyId, "aws_secret_key", awsSecretKey)));
        }

        private String dynamoDbUri() {
            // datomic:ddb://{aws-region}/{dynamodb-table}/{db-name}?aws_access_key_id={XXX}&aws_secret_key={YYY}
            return String.format("datomic:ddb://%s/%s/%s%s",
                    awsRegion,
                    dynamoDbTable,
                    db,
                    queryString(Util.map("aws_access_key_id", awsAccessKeyId, "aws_secret_key", awsSecretKey)));
        }

        private String devUri() {
            // datomic:dev://{transactor-host}:{port}/{db-name}
            return String.format("datomic:dev://%s:%s/%s",
                    host == null ? "localhost" : host,
                    port == null ? 4334 : port,
                    db);
        }

        private String couchbaseUri() {
            // datomic:couchbase://{host}/{bucket}/{dbname}[?password={xxx}]
            return String.format("datomic:couchbase://%s/%s/%s%s",
                    host == null ? "localhost" : host,
                    bucket,
                    db,
                    queryString(Util.map("password", password)));
        }

        private String cassandraUri() {
            // datomic:cass://{cluster-member-host}[:{port}]/{keyspace}.{table}/{db-name}[?user={user}&password={pwd}][&ssl=true]
            return String.format("datomic:cass://%s%s/%s.%s%s",
                    clusterMemberHost,
                    port == null ? "" : ":" + port,
                    keyspace,
                    table,
                    queryString(Util.map("user", user, "password", password, "ssl", ssl)));
        }

        private String awsAccessKeyId;
        private String awsRegion;
        private String awsSecretKey;
        private String bucket;
        private String clusterMemberHost;
        private String db;
        private String dynamoDbTable;
        private String host;
        private String jdbcUrl;
        private String keyspace;
        private String password;
        private Integer port;
        private String riakInterface;
        private Boolean ssl;
        private String storage;
        private String table;
        private String uri;
        private String user;

        @Override
        public String toString() {
            return "ConnectionProperties{" +
                    "uri='" + uri + '\'' +
                    ", storage='" + storage + '\'' +
                    ", db='" + db + '\'' +
                    ", host='" + host + '\'' +
                    ", port=" + port +
                    '}';
        }

        public String getAwsAccessKeyId() {
            return awsAccessKeyId;
        }

        public void setAwsAccessKeyId(String awsAccessKeyId) {
            this.awsAccessKeyId = awsAccessKeyId;
        }

        public String getAwsRegion() {
            return awsRegion;
        }

        public void setAwsRegion(String awsRegion) {
            this.awsRegion = awsRegion;
        }

        public String getAwsSecretKey() {
            return awsSecretKey;
        }

        public void setAwsSecretKey(String awsSecretKey) {
            this.awsSecretKey = awsSecretKey;
        }

        public String getBucket() {
            return bucket;
        }

        public void setBucket(String bucket) {
            this.bucket = bucket;
        }

        public String getClusterMemberHost() {
            return clusterMemberHost;
        }

        public void setClusterMemberHost(String clusterMemberHost) {
            this.clusterMemberHost = clusterMemberHost;
        }

        public String getDb() {
            return db;
        }

        public void setDb(String db) {
            this.db = db;
        }

        public String getDynamoDbTable() {
            return dynamoDbTable;
        }

        public void setDynamoDbTable(String dynamoDbTable) {
            this.dynamoDbTable = dynamoDbTable;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getJdbcUrl() {
            return jdbcUrl;
        }

        public void setJdbcUrl(String jdbcUrl) {
            this.jdbcUrl = jdbcUrl;
        }

        public String getKeyspace() {
            return keyspace;
        }

        public void setKeyspace(String keyspace) {
            this.keyspace = keyspace;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public String getRiakInterface() {
            return riakInterface;
        }

        public void setRiakInterface(String riakInterface) {
            this.riakInterface = riakInterface;
        }

        public Boolean getSsl() {
            return ssl;
        }

        public void setSsl(Boolean ssl) {
            this.ssl = ssl;
        }

        public String getStorage() {
            return storage;
        }

        public void setStorage(String storage) {
            this.storage = storage;
        }

        public String getTable() {
            return table;
        }

        public void setTable(String table) {
            this.table = table;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }
    }

    private Map<String, ConnectionProperties> connections = new HashMap<>();

    public Map<String, ConnectionProperties> getConnections() {
        return connections;
    }

    public void setConnections(Map<String, ConnectionProperties> connections) {
        this.connections = connections;
    }
}
