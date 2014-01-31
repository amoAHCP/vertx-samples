package org.jacpfx.gui.perspective;

import javafx.event.Event;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.lifecycle.OnShow;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.api.annotations.lifecycle.PreDestroy;
import org.jacpfx.api.annotations.perspective.Perspective;
import org.jacpfx.api.message.Message;
import org.jacpfx.gui.configuration.BaseConfig;
import org.jacpfx.gui.fragment.ConnectFragment;
import org.jacpfx.rcp.componentLayout.FXComponentLayout;
import org.jacpfx.rcp.componentLayout.PerspectiveLayout;
import org.jacpfx.rcp.components.managedFragment.ManagedFragmentHandler;
import org.jacpfx.rcp.context.Context;
import org.jacpfx.rcp.perspective.FXPerspective;
import org.jacpfx.rcp.util.FXUtil;

import java.util.ResourceBundle;

/**
 * Created by Andy Moncsek on 13.12.13.
 * A JacpFX perspective.
 *
 * @author <a href="mailto:amo.ahcp@gmail.com"> Andy Moncsek</a>
 */
@Perspective(id = BaseConfig.DRAWING_PERSPECTIVE, name = "drawingPerspective",
        components = {"id001", "id002"},
        viewLocation = "/fxml/DrawingPerspective.fxml",
        resourceBundleLocation = "bundles.languageBundle",
        localeID = "en_US")
public class DrawingPerspective implements FXPerspective {

    @Resource
    private Context context;

    @Override
    public void handlePerspective(Message<Event, Object> message, PerspectiveLayout perspectiveLayout) {
        if (message.messageBodyEquals(FXUtil.MessageUtil.INIT)) {
          //...
        }


    }

    @OnShow
    /**
     * @OnShow will be executed when perspective is switched to foreground
     * @param layout, The layout object gives you access to menu bar and tool bar
     * @param resourceBundle, The resource bundle defined in Perspective annotation
     */
    public void onShow(final FXComponentLayout layout,
                       final ResourceBundle resourceBundle) {

    }

    @PostConstruct
    /**
     * @PostConstruct annotated method will be executed when component is activated.
     * @param perspectiveLayout, allows you to access the JavaFX root node of the current perspective and to register target areas
     * @param layout, The layout object gives you access to menu bar and tool bar
     * @param resourceBundle, The resource bundle defined in Perspective annotation
     */
    public void onStartPerspective(final PerspectiveLayout perspectiveLayout,
                                   final FXComponentLayout layout,
                                   final ResourceBundle resourceBundle) {
        GridPane.setVgrow(perspectiveLayout.getRootComponent(),
                Priority.ALWAYS);
        GridPane.setHgrow(perspectiveLayout.getRootComponent(),
                Priority.ALWAYS);
        perspectiveLayout.registerTargetLayoutComponent("vMain", perspectiveLayout.getRootComponent());
        startConnectDialog();

    }

    @PreDestroy
    /**
     * @PreDestroy annotated method will be executed when component is deactivated.
     * @param layout, The layout object gives you access to menu bar and tool bar
     */
    public void onTearDownPerspective(final FXComponentLayout layout) {
        // remove toolbars and menu entries when close perspective

    }

    private void startConnectDialog() {
        ManagedFragmentHandler<ConnectFragment> handler = context.getManagedFragmentHandler(ConnectFragment.class);
        context.showModalDialog(handler.getFragmentNode());
    }



}
