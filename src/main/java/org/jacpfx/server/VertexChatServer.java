package org.jacpfx.server;

import org.jacpfx.ws.WebSocketRepository;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.ServerWebSocket;
import org.vertx.java.platform.Verticle;

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
        vertx.eventBus().registerHandler("org.jacpfx.message",(message) -> handleWSMessagesFromBus(message));
    }

    /**
     * Handle redirected messages from WebSocket.
     * @param message
     */
    private void handleWSMessagesFromBus(final Message<byte[]> message) {
        Buffer buffer = new Buffer();
        buffer.appendBytes(message.body());
        repository.getWebSockets().parallelStream().forEach(ws->ws.writeBinaryFrame(buffer));
    }

    /**
     * Registers onMessage and onClose message handler for WebSockets
     * @param httpServer
     */
    private void registerWebsocketHandler(final HttpServer httpServer) {
            httpServer.websocketHandler((serverSocket)->{
                repository.addWebSocket(serverSocket);
                serverSocket.dataHandler((data)->redirectWSMessageToBus(data)) ;
                serverSocket.closeHandler((close)->handleConnectionClose(close,serverSocket));
            })  ;
    }

    /**
     * handles connection close
     * @param event
     */
    private void handleConnectionClose(final Void event, ServerWebSocket socket) {
        repository.removeWebSocket(socket);
    }

    /**
     * handles websocket messages and redirect to message bus
     * @param data
     */
    private void redirectWSMessageToBus(final Buffer data) {
        vertx.eventBus().send("org.jacpfx.message", data.getBytes());
    }
}
