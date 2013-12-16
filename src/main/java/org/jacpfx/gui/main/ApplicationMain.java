package org.jacpfx.gui.main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.jacpfx.gui.workbench.DrawingWorkbench;
import org.jacpfx.rcp.workbench.FXWorkbench;
import org.jacpfx.spring.launcher.AFXSpringLauncher;

/**
 * Created by amo on 13.12.13.
 * @author <a href="mailto:amo.ahcp@gmail.com"> Andy Moncsek</a>
 */
public class ApplicationMain extends AFXSpringLauncher {
    public static final String[] STYLE_FILES = {"/styles/style_light.css", "/styles/style_dark.css"};
    public ApplicationMain() {
        super("main.xml");
    }

    /**
     * @param args  aa  ddd
     */
    public static void main(final String[] args) {
        Application.launch(args);
    }

    @Override
    protected Class<? extends FXWorkbench> getWorkbechClass() {
        return DrawingWorkbench.class;
    }

    @Override
    protected String[] getBasePackages() {
        return new String[]{"org.jacpfx.gui"};
    }

    @Override
    protected void postInit(Stage stage) {
        final Scene scene = stage.getScene();
        stage.getIcons().add(new Image("images/icons/JACP_512_512.png"));
        scene.getStylesheets().add(STYLE_FILES[0]);
    }
}
