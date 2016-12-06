package com.datomic;

import datomic.Peer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author <a href="mailto:mtnygard@gmail.com">Michael T. Nygard</a>
 * @author <a href="mailto:josh@joshlong.com">Josh Long</a>
 */
public class Connection {

    private final Log log = LogFactory.getLog(getClass());
    private final datomic.Connection connection;
    private String uri;

    public Log getLog() {
        return log;
    }

    public String getUri() {
        return uri;
    }

    public datomic.Connection getConnection() {
        return connection;
    }

    public Connection(String uri) {
        this.uri = uri;
        this.log.info("uri: " + this.uri);
        boolean created = Peer.createDatabase(uri);
        if(created) {
            this.log.info("created db: " + this.uri);
        }
        this.connection = Peer.connect(uri);
    }
}