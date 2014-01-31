package org.jacpfx.gui.component;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.DeclarativeView;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.api.annotations.lifecycle.PreDestroy;
import org.jacpfx.api.message.Message;
import org.jacpfx.api.util.ToolbarPosition;
import org.jacpfx.dto.CanvasPoint;
import org.jacpfx.gui.configuration.BaseConfig;
import org.jacpfx.rcp.component.FXComponent;
import org.jacpfx.rcp.componentLayout.FXComponentLayout;
import org.jacpfx.rcp.components.toolBar.JACPToolBar;
import org.jacpfx.rcp.context.Context;

/**
 * Created by Andy Moncsek on 16.12.13.
 * This Component contains a view with a JavaFX Canvas element.
 */
@DeclarativeView(id = BaseConfig.CANVAS_COMPONENT, name = "CanvasComponent", active = true, resourceBundleLocation = "bundles.languageBundle", initialTargetLayoutId = "vMain", viewLocation = "/fxml/CanvasComponent.fxml")
public class CanvasComponent implements FXComponent {

    @FXML
    private Canvas canvas;
    @FXML
    private VBox root;
    @FXML
    private VBox labelContainer;
    private
    @Resource
    Context context;

    @Override
    public Node postHandle(Node node, Message<Event, Object> message) throws Exception {
        if (message.isMessageBodyTypeOf(CanvasPoint.class)) {
            CanvasPoint point = message.getTypedMessageBody(CanvasPoint.class);
            drawPoint(point);
        }
        return null;
    }

    private void drawPoint(CanvasPoint point) {
        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        switch (point.getType()) {
            case BEGIN:
                graphicsContext.beginPath();
                graphicsContext.moveTo(point.getX(), point.getY());
                break;
            case DRAW:
                graphicsContext.lineTo(point.getX(), point.getY());
                break;
            case CLEAR:
                final double height = canvas.getHeight();
                final double width = canvas.getWidth();
                graphicsContext.beginPath();
                graphicsContext.clearRect(0, 0, width, height);
                break;
            default:
        }
        graphicsContext.stroke();
    }

    @Override
    public Node handle(Message<Event, Object> message) throws Exception {
        return null;
    }

    @PostConstruct
    public void onStart(final FXComponentLayout layout) {
        initBinding(root, canvas, labelContainer);
        initDraw(canvas.getGraphicsContext2D());
        initEventHandler(canvas);
        addClearButton(layout);
    }


    @PreDestroy
    public void onDestroy() {
        labelContainer.prefHeightProperty().unbind();
        labelContainer.prefWidthProperty().unbind();
        canvas.heightProperty().unbind();
        canvas.widthProperty().unbind();
    }

    private void initBinding(final VBox root,
                             final Canvas canvas,
                             final VBox labelContainer) {
        bindLabeConteiner(labelContainer, root.heightProperty(), root.widthProperty());
        bindCanvas(canvas, root.heightProperty(), root.widthProperty());
    }

    private void bindLabeConteiner(final VBox labelContainer,
                                   final ReadOnlyDoubleProperty rootHight,
                                   final ReadOnlyDoubleProperty rootWidth) {
        labelContainer.prefHeightProperty().bind(rootHight.multiply(.05));
        labelContainer.prefWidthProperty().bind(rootWidth);
    }

    private void bindCanvas(final Canvas canvas,
                            final ReadOnlyDoubleProperty rootHight,
                            final ReadOnlyDoubleProperty rootWidth) {
        canvas.heightProperty().bind(rootHight.multiply(.95));
        canvas.widthProperty().bind(rootWidth);
    }

    private void initDraw(final GraphicsContext gc) {
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(1);

    }

    private void initEventHandler(final Canvas canvas) {
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, (event) -> {
            context.send(BaseConfig.getGlobalId(BaseConfig.DRAWING_PERSPECTIVE,BaseConfig.WEBSOCKET_COMPONENT), new CanvasPoint(event.getX(), event.getY(), CanvasPoint.Type.BEGIN));
        }
        );
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, (event) -> {
            context.send(BaseConfig.getGlobalId(BaseConfig.DRAWING_PERSPECTIVE,BaseConfig.WEBSOCKET_COMPONENT), new CanvasPoint(event.getX(), event.getY(), CanvasPoint.Type.DRAW));
        });
    }

    private void addClearButton(final FXComponentLayout layout) {
        final JACPToolBar registeredToolBar = layout.getRegisteredToolBar(ToolbarPosition.NORTH);
        Button clear = new Button("clear");
        clear.setOnMouseClicked(context.getEventHandler(BaseConfig.getGlobalId(BaseConfig.DRAWING_PERSPECTIVE,BaseConfig.WEBSOCKET_COMPONENT), new CanvasPoint(0, 0, CanvasPoint.Type.CLEAR)));
        registeredToolBar.add(BaseConfig.getGlobalId(BaseConfig.DRAWING_PERSPECTIVE,BaseConfig.CANVAS_COMPONENT), clear);
    }

}
