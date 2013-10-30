package org.jacp.ws;

import org.junit.Before;
import org.junit.Test;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.VertxFactory;
import org.vertx.java.core.http.HttpClient;
import org.vertx.java.core.http.WebSocket;

import java.util.concurrent.CountDownLatch;

/**
 * Created with IntelliJ IDEA.
 * User: ady
 * Date: 30.10.13
 * Time: 22:16
 * To change this template use File | Settings | File Templates.
 */
public class SimpleVertesWSConnectionTest {
    Vertx vertx =null;
    @Before
    public void onStart() {
        vertx = VertxFactory.newVertx();
    }

    @Test
    public void simpleConnect() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        HttpClient client = vertx.createHttpClient().setHost("localhost").setPort(8080);

        client.connectWebsocket("/", new Handler<WebSocket>() {
            public void handle(WebSocket ws) {
                // Connected!
                System.out.println("connected");
                latch.countDown();

            }
        });
        latch.await();
    }
}
