package org.jacp.server;

import org.jacp.ws.WebSocketRepository;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.ServerWebSocket;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;
import sun.jvm.hotspot.interpreter.Bytecodes;

/**
 * Created with IntelliJ IDEA.
 * User: amo
 * Date: 12.11.13
 * Time: 13:42
 * To change this template use File | Settings | File Templates.
 */
public class VertexChatServer extends Verticle {
    private WebSocketRepository repository = new WebSocketRepository();

    @Override
    public void start() {
        final HttpServer httpServer = startServer();
        registerEventBusMessageHandler();
        registerWebsocketHandler(httpServer);
        httpServer.listen(8080);
    }

    private HttpServer startServer() {
        HttpServer httpServer = vertx.createHttpServer();

        return httpServer;
    }

    private void registerEventBusMessageHandler() {
        vertx.eventBus().registerHandler("org.jacp.message",(message) -> handleWSMessagesFromBus(message));
    }

    /**
     * Handle redirected messages from WebSocket.
     * @param message
     */
    private void handleWSMessagesFromBus(final Message<JsonObject> message) {
        final String name = message.body().getString("name");
        final String body = message.body().getString("message");
        final String wsMessage = name.concat(":").concat(body);
        repository.getWebSockets().parallelStream().forEach(ws->ws.writeTextFrame(wsMessage));
    }

    /**
     * Registers onMessage and onClose message handler for WebSockets
     * @param httpServer
     */
    private void registerWebsocketHandler(final HttpServer httpServer) {
            httpServer.websocketHandler((serverSocket)->{
                repository.addWebSocket(serverSocket);
                serverSocket.dataHandler((data)->redirectWSMessageToBus(data)) ;
                serverSocket.closeHandler((close)->handleConnectionClose(close));
            })  ;
    }

    /**
     * handles connection close
     * @param event
     */
    private void handleConnectionClose(final Void event) {

    }

    /**
     * handles websocket messages and redirect to message bus
     * @param data
     */
    private void redirectWSMessageToBus(final Buffer data) {
        System.out.println(data.toString());
        String name = data.toString().split(":")[0];
        String message = data.toString().split(":")[1];
        JsonObject messageObject = new JsonObject();
        messageObject.putString("name", name);
        messageObject.putString("message", message);
        vertx.eventBus().send("org.jacp.message", messageObject);
    }
}
