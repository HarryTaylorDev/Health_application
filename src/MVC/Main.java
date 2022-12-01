/*
Name:           Main.java

Authors:        Chris, Conor, Harry, Milo, Yacine

Description:    The main java class to run the whole Health Tracker
                application from. This class must be run in for the application
                to start
*/

package MVC;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        //primaryStage.initStyle(StageStyle.UNDECORATED);

        Parent root = FXMLLoader.load(getClass().getResource("view/login.fxml"));
        Scene scene = new Scene(root, 1280, 720);

        root.setId("pane");
        primaryStage.setResizable(false);

        primaryStage.setTitle("Redefining Health");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("images/icon.png")));

        primaryStage.show();
    }
}