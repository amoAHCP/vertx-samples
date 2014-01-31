package org.jacpfx.gui.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Andy Moncsek on 31.01.14.
 * The spring java configuration and component id config
 */
@Configuration
@ComponentScan
public class BaseConfig {

    public static  final String DRAWING_PERSPECTIVE="id01";
    public static  final String CANVAS_COMPONENT="id001";
    public static  final String WEBSOCKET_COMPONENT="id002";

    public static String getGlobalId(final String perspectiveId, final String componentId) {
         return perspectiveId.concat(".").concat(componentId);
    }
}
