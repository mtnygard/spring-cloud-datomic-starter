package com.datomic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author <a href="mailto:mtnygard@gmail.com">Michael T. Nygard</a>
 * @author <a href="mailto:josh@joshlong.com">Josh Long</a>
 */
public class Connection {

        private final Log log = LogFactory.getLog(getClass());
        private String uri;

        public Log getLog() {
                return log;
        }

        public String getUri() {
                return uri;
        }

        public Connection(String uri) {
                this.uri = uri;
                this.log.info("uri: " + this.uri);
        }
}