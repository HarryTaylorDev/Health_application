/*
Name:           ForgotPasswordThreeController.java

Authors:        Chris, Conor, Harry, Milo, Yacine

Description:    Class forms a part of the Forgot Password chain. This allows
                users to reset their password if needed
*/
package MVC.controller;

import MVC.model.User;
import MVC.utils.CustomAlert;
import MVC.utils.DatabaseManagement;
import MVC.utils.InputValidation;
import MVC.utils.LoginUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ForgotPasswordThreeController extends Controller implements Initializable
{
    @FXML
    private Button buttonReset;
    @FXML
    private TextField passwordFieldNewPassword;
    @FXML
    private TextField passwordFieldRepeat;
    @FXML
    private Hyperlink hyperlinkReturn;
    @FXML
    private Tooltip tooltipPassword;

    public void hyperlinkReturnClicked(ActionEvent event) throws IOException
    {
        StringBuilder path = new StringBuilder("../view/");
        path.append("login.fxml");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(path.toString()));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void buttonMenuClicked(ActionEvent event) throws IOException, SQLException
    {
        boolean validPassword;
        boolean passwordEqual;
        int validityCounter = 0;

        if (passwordFieldNewPassword.getText() != null && passwordFieldRepeat != null)
        {
            String newPassword = passwordFieldNewPassword.getText();

            // check date is valid and check date is before age restriction
            // of 13 years old
            validPassword = InputValidation.checkPasswordSecurity(newPassword);

            if (!validPassword)
            {
                validityCounter += 1;
            }

            passwordEqual = newPassword.equals(passwordFieldRepeat.getText());

            if (!passwordEqual)
            {
                validityCounter += 1;
            }
        }
        else
        {
            validPassword = false;
            passwordEqual = true;
            validityCounter = 1;
        }

        CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR);
        StringBuilder alertMessage = new StringBuilder("Input invalid:\n");

        if (!validPassword)
        {
            alertMessage.append("Password not secure enough");
            if (validityCounter == 1)
            {
                alertMessage.append(".");
            }
            else
            {
                alertMessage.append(", ");
            }
        }

        if (!passwordEqual)
        {
            alertMessage.append("Passwords don't match.");
        }

        if (!(validPassword && passwordEqual))
        {
            alert.setHeaderText("Invalid Input");
            alert.setContentText(alertMessage.toString());
            alert.showAndWait();
            return;
        }
        else
        {
            resetPassword(user.getEmail(), passwordFieldNewPassword.getText());

            StringBuilder path = new StringBuilder("../view/");

            if (event.getSource() == buttonReset)
            {
                path.append("login.fxml");
            }

            Parent parent = FXMLLoader.load(getClass().getResource(path.toString()));
            Scene scene = new Scene(parent);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();

        }
    }

    @Override
    public void initUser(User user)
    {
        this.user = user;
    }

    /**
     * function to reset the users password, searches for the user from their
     * email, hashes the newpassword for security
     * @param email users email address
     * @param newPassword the new password to be used
     */
    public void resetPassword(String email, String newPassword)
    {
        DatabaseManagement db = new DatabaseManagement();
        String salt = User.generateSalt();
        String hashPassword = LoginUtils.hashDataWithSalt(newPassword, salt);

        String resetStatement = "UPDATE users SET password = ?, salt = ? WHERE email= ?";
        String[] parameters = {hashPassword, salt, email};
        db.insert(resetStatement, parameters);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        tooltipPassword.setText("Password requires:\n" +
                "one upper case,\n" +
                "one lower case,\n" +
                "one number,\n" +
                "one special character.\n" +
                "Length: 8-32 characters.");
    }
}
