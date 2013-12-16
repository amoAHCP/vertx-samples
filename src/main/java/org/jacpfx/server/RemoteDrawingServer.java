package org.jacpfx.server;

import org.jacpfx.dto.PayloadContainer;
import org.jacpfx.util.MessageUtil;
import org.jacpfx.util.Serializer;
import org.jacpfx.ws.WebSocketRepository;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.ServerWebSocket;
import org.vertx.java.platform.Verticle;

import java.io.IOException;

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
        System.out.println("started");
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
    private void handleWSMessagesFromBus(final Message<byte[]> message) {

        PayloadContainer payload=null;
        try {
            payload = MessageUtil.getMessage(message.body(), PayloadContainer.class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
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

        try {
            vertx.eventBus().send("org.jacpfx.draw.message", Serializer.serialize(new PayloadContainer(id,data.getBytes())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
