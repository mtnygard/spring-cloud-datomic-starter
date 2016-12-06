package com.datomic;

import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @author <a href="mailto:josh@joshlong.com">Josh Long</a>
 */
public class Connections {

    private final Set<Connection> connections =
            new ConcurrentSkipListSet<>(Comparator.comparing(Connection::getUri));

    public Connections(Connection[] connections) {
        if (null != connections) {
            for (Connection c : connections) {
                this.addConnection(c);
            }
        }
    }

    public void addConnection(Connection connection) {
        Objects.requireNonNull(connection);
        this.connections.add(connection);
    }
}