/*
Name:           CustomAlert.java

Authors:        Chris, Conor, Harry, Milo, Yacine

Description:    Small helper class to help with custom alerts.
*/
package MVC.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class CustomAlert extends Alert
{
    public CustomAlert(AlertType alertType)
    {
        super(alertType);

        // adds icon to top left of each dialog
        Stage stage = (Stage) this.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResource("../images/icon.png").toString()));

        // adds the styling from style.css to the dialog
        DialogPane dp = this.getDialogPane();
        dp.getStylesheets().add(getClass().getResource("../style/style.css").toExternalForm());
        dp.getStyleClass().add("customDialog");
    }
}
