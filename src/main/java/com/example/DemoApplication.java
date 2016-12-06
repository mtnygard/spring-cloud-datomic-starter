package com.example;

import datomic.Peer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author <a href="mailto:mtnygard@gmail.com">Michael T. Nygard</a>
 * @author <a href="mailto:josh@joshlong.com">Josh Long</a>
 */
@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(DemoApplication.class, args);
        Thread.sleep(100);
        Peer.shutdown(true);
    }
}
