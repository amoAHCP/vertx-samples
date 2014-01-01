package org.jacpfx.gui.dialog;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.dialog.Dialog;
import org.jacpfx.api.dialog.Scope;
import org.jacpfx.dto.ConnectionProperties;
import org.jacpfx.rcp.context.JACPContext;

/**
 * Created by Andy Moncsek on 27.12.13.
 */
@Dialog(id = "id1001", viewLocation = "/fxml/ConnectDialog.fxml", resourceBundleLocation = "bundles.languageBundle", localeID = "en_US", scope = Scope.SINGLETON)
public class ConnectDialog {
    @Resource
    private JACPContext context;

    @FXML
    private TextField connectAddress;



    @FXML
    public void connectToServer() {
        final String connectValue=connectAddress.getText();
        if(connectValue==null || connectValue.isEmpty()) return;
        final String[] val = connectValue.split(":");
        if(val.length<2) return;
        context.send("id01.id002",new ConnectionProperties(val[0],val[1]));
        context.hideModalDialog();
    }
}
