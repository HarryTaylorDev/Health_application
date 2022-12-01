/*
Name:           LoginController.java

Authors:        Chris, Conor, Harry, Milo, Yacine

Description:    Class controls the login screen for the application. This
                allows the user to login, and also gives options to create an
                account, and to reset their password if they have forgotten
                their details.
*/
package MVC.controller;

import MVC.model.User;
import MVC.model.WeightGoal;
import MVC.utils.CustomAlert;
import MVC.utils.LoginUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class LoginController implements Initializable
{
    @FXML
    private Button buttonLogin;
    @FXML
    private Button buttonCreateAccount;
    @FXML
    private Hyperlink hyperlinkForgotPassword;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private TextField textFieldPassword;

    public void buttonMenuClicked(ActionEvent event) throws IOException, SQLException
    {
        StringBuilder path = new StringBuilder("../view/");

        User user = null;

        if (event.getSource() == buttonLogin)
        {
            LoginUtils lc = new LoginUtils(textFieldEmail.getText(), textFieldPassword.getText());

            if (lc.login())
            {

                //load user here
                user = lc.getUser();
                boolean userHasntLoggedInToday = user.getProfile().checkCurrentDay();

                //daily login points
                if (userHasntLoggedInToday)
                {
                    user.getProfile().loggedInPoints();
                }

                // WEIGHT GOAL CHECKING
                WeightGoal weightGoal = user.getProfile().getWeightGoal();

                if (weightGoal.getTargetDate().isBefore(LocalDate.now()))
                {
                    CustomAlert alert = new CustomAlert(Alert.AlertType.CONFIRMATION);
                    alert.setContentText("Your weight goal has expired. Do you want to set a new one?");
                    alert.setHeaderText("Weight Goal Expired");
                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.get() == ButtonType.OK)
                    {
                        path.append("dashboardWeight.fxml");
                    }
                    else
                    {
                        path.append("dashboardHome.fxml");
                    }
                }
                else
                {
                    //FITNESS GOAL CHECKING
                    path.append("dashboardHome.fxml");
                }
            }
            else
            {
                CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR);
                alert.setHeaderText("Invalid Input");
                alert.setContentText("Wrong username or password, please try again.");
                alert.showAndWait();
                return;         // change this
            }
        }
        else if (event.getSource() == buttonCreateAccount)
        {
            path.append("createAccountPersonal.fxml");
            user = new User();
        }
        else if (event.getSource() == hyperlinkForgotPassword)
        {
            path.append("forgotPassword.fxml");
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(path.toString()));
        Parent parent = loader.load();

        if (!path.toString().contains("forgotPassword.fxml"))
        {
            Controller controller = loader.getController();
            controller.initUser(user);
        }

        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        textFieldEmail.setOnKeyPressed(
                event ->
                {
                    if (event.getCode() == KeyCode.ENTER)
                    {
                        textFieldPassword.requestFocus();
                    }
                }
        );


        textFieldPassword.setOnKeyPressed(
                event ->
                {
                    if (event.getCode() == KeyCode.ENTER)
                    {
                        buttonLogin.requestFocus();
                        buttonLogin.fireEvent(new ActionEvent());
                    }
                }
        );

        Parent root = buttonLogin.getParent();
        Platform.runLater(root::requestFocus);
    }
}
