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
import org.jacpfx.rcp.componentLayout.FXComponentLayout;
import org.jacpfx.rcp.componentLayout.PerspectiveLayout;
import org.jacpfx.rcp.components.toolBar.JACPOptionButton;
import org.jacpfx.rcp.components.toolBar.JACPToolBar;
import org.jacpfx.rcp.context.JACPContext;
import org.jacpfx.rcp.perspective.FXPerspective;

import java.util.ResourceBundle;

import static org.jacpfx.api.util.ToolbarPosition.NORTH;
import static org.jacpfx.rcp.components.toolBar.JACPOptionButtonOrientation.BOTTOM;

/**
 * Created by Andy Moncsek on 13.12.13.
 * A JacpFX perspective.
 *
 * @author <a href="mailto:amo.ahcp@gmail.com"> Andy Moncsek</a>
 */
@Perspective(id = "id01", name = "drawingPerspective",
        components = {"id001","id002"},
        viewLocation = "/fxml/DrawingPerspective.fxml",
        resourceBundleLocation = "bundles.languageBundle",
        localeID = "en_US")
public class DrawingPerspective implements FXPerspective {

    @Resource
    private JACPContext context;

    @Override
    public void handlePerspective(Message<Event, Object> message, PerspectiveLayout perspectiveLayout) {
        GridPane.setVgrow(perspectiveLayout.getRootComponent(),
                Priority.ALWAYS);
        GridPane.setHgrow(perspectiveLayout.getRootComponent(),
                Priority.ALWAYS);
        perspectiveLayout.registerTargetLayoutComponent("vMain", perspectiveLayout.getRootComponent());


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
     * @param layout, The layout object gives you access to menu bar and tool bar
     * @param resourceBundle, The resource bundle defined in Perspective annotation
     */
    public void onStartPerspective(final FXComponentLayout layout,
                                   final ResourceBundle resourceBundle) {

        final JACPToolBar north = layout.getRegisteredToolBar(NORTH);
        JACPOptionButton optionButton = new JACPOptionButton("style", layout, BOTTOM);
        north.addOnEnd(context.getId(),optionButton);

    }

    @PreDestroy
    /**
     * @PreDestroy annotated method will be executed when component is deactivated.
     * @param layout, The layout object gives you access to menu bar and tool bar
     */
    public void onTearDownPerspective(final FXComponentLayout layout) {
        // remove toolbars and menu entries when close perspective

    }
}
