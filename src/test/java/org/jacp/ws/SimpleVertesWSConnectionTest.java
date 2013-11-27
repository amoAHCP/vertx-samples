package org.jacp.ws;

import org.jacp.dto.ChatMessage;
import org.jacp.server.MyVertxServer;
import org.jacp.util.MessageUtil;
import org.jacp.util.Serializer;
import org.junit.Before;
import org.junit.Test;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.VertxFactory;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpClient;
import org.vertx.java.core.http.WebSocket;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import static junit.framework.Assert.assertNotNull;

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
    public void simpleConnect() throws InterruptedException, IOException {
        CountDownLatch latch = new CountDownLatch(1);
        CountDownLatch latch2 = new CountDownLatch(1);
        HttpClient client = vertx.createHttpClient().setHost("localhost").setPort(8080);
        final WebSocket[] wsTemp=new WebSocket[1];
        HttpClient websocket = client.connectWebsocket("/", new Handler<WebSocket>() {
            public void handle(WebSocket ws) {
                // Connected!^                                         ^
                System.out.println("connected");
                latch.countDown();
                wsTemp[0]= ws;
                ws.dataHandler((data)->{

                    ChatMessage messageObject= null;
                    try {
                        messageObject = MessageUtil.getMessage(data.getBytes(), ChatMessage.class);
                    } catch (IOException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                    System.out.println("client data handler:" + messageObject.getMessage());
                    latch2.countDown();
                });

            }
        });
        latch.await();

        assertNotNull(wsTemp[0]);

        ChatMessage message = new ChatMessage("me","you","hello world");

        Buffer buffer = new Buffer();
        buffer.appendBytes(Serializer.serialize(message));
        wsTemp[0].writeBinaryFrame(buffer);
        latch2.await();

    }
}
