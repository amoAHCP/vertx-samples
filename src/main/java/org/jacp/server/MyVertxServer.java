package org.jacp.server;

import org.jacp.ws.WebSocketRepository;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.file.impl.PathAdjuster;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.ServerWebSocket;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

import java.util.concurrent.CountDownLatch;

/**
 * Created with IntelliJ IDEA.
 * User: ady
 * Date: 30.10.13
 * Time: 21:01
 * To change this template use File | Settings | File Templates.
 */
public class MyVertxServer extends Verticle {
    private WebSocketRepository repository = new WebSocketRepository();
    public static CountDownLatch latch = new CountDownLatch(1) ;
    @Override
    public void start() {
        System.out.println("This vertex: "+this+"  Thread: "+Thread.currentThread());
        HttpServer httpServer = vertx.createHttpServer();
        httpServer.requestHandler(new Handler<HttpServerRequest>() {

            @Override
            public void handle(HttpServerRequest req) {

                req.response().sendFile("web/index.html");
                //req.response().headers().add("Content-Type", "text/html;charset-UTF-8");
                //req.response().end("<html><body><h1>Hello from  vert.x!</h1></body></html>");
            }
        });

        vertx.eventBus().registerHandler("de.mz.chat", new Handler<Message<JsonObject>>() {

            @Override
            public void handle(Message<JsonObject> message) {
                String out = message.body().getString("name") + ": " + message.body().getString("message");
                for (ServerWebSocket webSocket : repository.getWebSockets()) {
                    webSocket.writeTextFrame(out);
                }
            }
        });

        httpServer.websocketHandler(new Handler<ServerWebSocket>() {
            @Override
            public void handle(final ServerWebSocket ws) {
                repository.addWebSocket(ws);
                System.out.println("connected on server");
                ws.dataHandler(new Handler<Buffer>() {

                    @Override
                    public void handle(Buffer data) {
                        System.out.println("data handler:" + data);
                        String name = data.toString().split(":")[0];
                        String message = data.toString().split(":")[1];
                        JsonObject messageObject = new JsonObject();
                        messageObject.putString("name", name);
                        messageObject.putString("message", message);
                        vertx.eventBus().send("de.mz.chat", messageObject);
                        ws.writeTextFrame("OK");
                    }
                });
                ws.closeHandler(new Handler<Void>() {

                    @Override
                    public void handle(Void event) {
                        repository.removeWebSocket(ws);
                    }
                });
            }
        });
        httpServer.listen(8080);
    }
}