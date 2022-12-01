/*
Name:           ForgotPasswordController.java

Authors:        Chris, Conor, Harry, Milo, Yacine

Description:    Class forms a part of the Forgot Password chain. This allows
                users to reset their password if needed
*/
package MVC.controller;

import MVC.model.Profile;
import MVC.model.User;
import MVC.utils.CustomAlert;
import MVC.utils.DatabaseManagement;
import MVC.utils.InputValidation;
import MVC.utils.SerialisationHelper;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ForgotPasswordController extends Controller implements Initializable
{
    @FXML
    private Button buttonNext;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private DatePicker datePickerDateOfBirth;

    /**
     * function to check if the user has entered the right date of birth
     * @param email users email
     * @param inputDoB entered date of birth
     * @return true if the date of birth is correct
     */
    public static boolean checkDetails(String email, LocalDate inputDoB)
    {
        DatabaseManagement db = new DatabaseManagement();
        String emailStatement = "SELECT COUNT(*) AS count FROM USERS WHERE email=?";
        String[] parameters = new String[]{email};

        int count = 0;
        ArrayList<HashMap<String, String>> userCount = db.select(emailStatement, parameters);
        count = Integer.parseInt(userCount.get(0).get("count"));

        if (count < 1)
        {
            return false;
        }

        String getDOBStatement = "SELECT profile FROM USERS WHERE email=?";

        ArrayList<HashMap<String, String>> profileList = db.select(getDOBStatement, parameters);
        String profileString = profileList.get(0).get("profile");

        Profile profile = SerialisationHelper.deserializeProfile(profileString);

        LocalDate databaseDOB = profile.getDoB();


        return databaseDOB.isEqual(inputDoB);
    }

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
        boolean validDateOfBirth;

        // Initialise a new user object
        this.user = new User();

        // Validating Date of Birth input
        if (datePickerDateOfBirth.getValue() != null)
        {
            LocalDate DoB = datePickerDateOfBirth.getValue();
            validDateOfBirth = InputValidation.checkDateBeforeToday(DoB);
        }
        else
        {
            validDateOfBirth = false;
        }

        // Validate user email
        boolean validEmail = InputValidation.checkOnlyEmail(textFieldEmail.getText());

        if (!validEmail || !validDateOfBirth)
        {
            CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Input");
            alert.setContentText("Please enter a valid email address or date of birth");
            alert.showAndWait();
        }
        else
        {
            StringBuilder path = new StringBuilder("../view/");

            if (event.getSource() == buttonNext)
            {
                path.append("forgotPasswordTwo.fxml");
            }


            Boolean exists = checkDetails(textFieldEmail.getText(), datePickerDateOfBirth.getValue());

            if (!exists)
            {
                CustomAlert recoverError = new CustomAlert(Alert.AlertType.ERROR);
                recoverError.setHeaderText("Invalid Input");
                recoverError.setContentText("Please enter a valid email address or date of birth");
                recoverError.showAndWait();
                return;
            }

            user.setEmail(textFieldEmail.getText());

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(path.toString()));
            Parent parent = loader.load();

            Controller controller = loader.getController();
            controller.initUser(user);

            Scene scene = new Scene(parent);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();

        }
    }

    @Override
    public void initUser(User user)
    {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

    }
}
