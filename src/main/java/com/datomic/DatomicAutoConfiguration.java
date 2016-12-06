package com.datomic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="mailto:mtnygard@gmail.com">Michael T. Nygard</a>
 * @author <a href="mailto:josh@joshlong.com">Josh Long</a>
 */
@Configuration
@EnableConfigurationProperties(ConnectionsProperties.class)
public class DatomicAutoConfiguration {
    @Autowired(required = false)
    private Connection[] connections;

    @Bean
    public Connections connections(ConnectionsProperties properties) {
        Connections connections = new Connections(this.connections);
        properties.getConnections()
                .values()
                .stream()
                .map(this::from)
                .forEach(connections::addConnection);
        return connections;
    }

    private Connection from(ConnectionsProperties.ConnectionProperties p) {
        return new Connection(p.deriveUri());
    }
}
