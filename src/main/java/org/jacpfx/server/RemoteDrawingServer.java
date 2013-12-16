package org.jacpfx.server;

import org.jacpfx.dto.PayloadContainer;
import org.jacpfx.ws.WebSocketRepository;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.ServerWebSocket;
import org.vertx.java.platform.Verticle;

/**
 * Created by Andy Moncsek on 13.12.13.
 */
public class RemoteDrawingServer extends Verticle {
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
        vertx.eventBus().registerHandler("org.jacpfx.draw.message",(message) -> handleWSMessagesFromBus(message));
    }

    /**
     * Handle redirected messages from WebSocket.
     * @param message
     */
    private void handleWSMessagesFromBus(final Message<PayloadContainer> message) {
        final PayloadContainer payload = message.body();
        final String sourceId = payload.getId();
        final Buffer buffer = new Buffer();
        buffer.appendBytes(payload.getPayload());
        repository.getWebSockets()
                .parallelStream()
                .filter(socket -> !socket.binaryHandlerID().equalsIgnoreCase(sourceId))
                .forEach(ws -> ws.writeBinaryFrame(buffer));
    }

    /**
     * Registers onMessage and onClose message handler for WebSockets
     * @param httpServer
     */
    private void registerWebsocketHandler(final HttpServer httpServer) {
        httpServer.websocketHandler((serverSocket)->{
            repository.addWebSocket(serverSocket);
            serverSocket.dataHandler((data)->redirectWSMessageToBus(data,serverSocket.binaryHandlerID()));
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
    private void redirectWSMessageToBus(final Buffer data,String id) {
        vertx.eventBus().send("org.jacpfx.draw.message", new PayloadContainer(id,data.getBytes()));
    }

}
