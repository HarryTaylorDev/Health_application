/*
Name:           Controller.java

Authors:        Chris, Conor, Harry, Milo, Yacine

Description:    Controller abstract class for all controllers to inherit from.
                This abstract class allows user objects to be passed between
                controllers, this allowing for data persistence within the
                application
*/
package MVC.controller;

import MVC.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public abstract class Controller
{
    public User user;

    public abstract void initUser(User user) throws IOException, SQLException;

    public void loadScene(String path, ActionEvent event) throws IOException, SQLException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(path));
        Parent parent = loader.load();

        if (!path.contains("login.fxml"))
        {
            Controller controller = loader.getController();
            controller.initUser(user);
        }

        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}