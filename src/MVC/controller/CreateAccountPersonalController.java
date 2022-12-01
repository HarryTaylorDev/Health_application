/*
Name:           CreateAccountPersonalController.java

Authors:        Chris, Conor, Harry, Milo, Yacine

Description:    Class controls the CreateAccountPersonal screen, forming a part
                of the create account section of the application. This screen
                allows the user to input personal data, such as their name,
                age and chosen login data.
*/
package MVC.controller;

import MVC.model.Profile;
import MVC.model.User;
import MVC.utils.CustomAlert;
import MVC.utils.DatabaseManagement;
import MVC.utils.InputValidation;
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

public class CreateAccountPersonalController extends Controller implements Initializable
{
    @FXML
    private TextField textFieldFirstName;
    @FXML
    private TextField textFieldLastName;
    @FXML
    private DatePicker datePickerDateOfBirth;
    @FXML
    private ComboBox comboBoxGender;
    @FXML
    private TextField textFieldUsername;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private TextField textFieldPassword;
    @FXML
    private TextField textFieldPasswordRepeat;
    @FXML
    private Button buttonNext;
    @FXML
    private Tooltip tooltipPassword;
    @FXML
    private Hyperlink hyperlinkReturn;

    private ArrayList<String> genders;

    /**
     * function to check if the username passed already exists in the database
     * @param userName username to check
     * @return true if username already exists, false if not
     */
    public static boolean userNameIsTaken(String userName) throws SQLException
    {
        boolean taken = false;
        DatabaseManagement db = new DatabaseManagement();
        String statement = "SELECT COUNT(*) AS count FROM users WHERE username=?";
        String[] params = {userName};
        ArrayList<HashMap<String, String>> results = db.select(statement, params);
        int count = Integer.parseInt(results.get(0).get("count"));

        if (count > 0)
        {
            taken = true;
        }
        return taken;
    }

    /**
     * function to check if the email passed already exists in the database
     * @param email email to check
     * @return true if email already exists, false if not
     */
    public static boolean emailIsTaken(String email) throws SQLException
    {
        boolean taken = false;
        DatabaseManagement db = new DatabaseManagement();
        String statement = "SELECT COUNT(*) AS count FROM users WHERE email=?";
        String[] params = {email};
        ArrayList<HashMap<String, String>> results = db.select(statement, params);
        int count = Integer.parseInt(results.get(0).get("count"));

        if (count > 0)
        {
            taken = true;
        }
        return taken;
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

    public void buttonNextClicked(ActionEvent event) throws IOException, SQLException
    {
        boolean validFirstName = InputValidation.checkName(textFieldFirstName.getText());
        boolean shortEnoughFirstName = InputValidation.checkLength(textFieldFirstName.getText(), 30);
        boolean validLastName = InputValidation.checkName(textFieldLastName.getText());
        boolean shortEnoughLastName = InputValidation.checkLength(textFieldLastName.getText(), 50);
        boolean validGender = genders.contains(comboBoxGender.getValue());
        boolean validDateOfBirth;
        boolean validAge;

        // check for not null otherwise could cause errors
        if (datePickerDateOfBirth.getValue() != null)
        {
            LocalDate DoB = datePickerDateOfBirth.getValue();

            // check date is valid and check date is before age restriction
            // of 13 years old
            validDateOfBirth = InputValidation.checkDateBeforeToday(DoB);
            validAge = InputValidation.checkAgeRestriction(DoB, 13);
        }
        else
        {
            validDateOfBirth = false;
            validAge = false;
        }

        boolean validUsername = InputValidation.checkOnlyLettersAndNumbers(textFieldUsername.getText());
        boolean shortEnoughUsername = InputValidation.checkLength(textFieldUsername.getText(), 16);
        boolean validEmail = InputValidation.checkOnlyEmail(textFieldEmail.getText());
        boolean shortEnoughEmail = InputValidation.checkLength(textFieldEmail.getText(), 45);
        boolean validPassword = InputValidation.checkPasswordSecurity(textFieldPassword.getText());
        boolean validRepeatPassword = textFieldPassword.getText().equals(textFieldPasswordRepeat.getText())
                && !textFieldPasswordRepeat.getText().isEmpty();

        boolean usernameTaken;
        boolean emailTaken;

        CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR);
        StringBuilder alertMessage = new StringBuilder("Please enter a valid:\n");

        if (!validFirstName || !shortEnoughFirstName)
        {
            alertMessage.append("First Name, ");
        }
        if (!validLastName || !shortEnoughLastName)
        {
            alertMessage.append("Last Name, ");
        }
        if (!validDateOfBirth)
        {
            alertMessage.append("Date of Birth, ");
        }
        if (!validGender)
        {
            alertMessage.append("Gender, ");
        }
        if (!validUsername || !shortEnoughUsername)
        {
            alertMessage.append("Username, ");
        }
        if (!validEmail || shortEnoughEmail)
        {
            alertMessage.append("Email, ");
        }
        if (!validPassword)
        {
            alertMessage.append("Password (upper case, lower case, number and special character; also length between 8-32), ");
        }
        if (!validRepeatPassword)
        {
            alertMessage.append("Repeat password, ");
        }

        // if one of the validators returns false, display an error message
        // showing all input errors to fix by the user
        if (!(validDateOfBirth && validEmail && validFirstName && validLastName && validPassword && validRepeatPassword && validUsername && validGender && shortEnoughEmail && shortEnoughFirstName && shortEnoughLastName && shortEnoughUsername))
        {
            alertMessage.setLength(alertMessage.length() - 2); // remove tailing ", "

            alert.setHeaderText("Invalid Input");
            alert.setContentText(alertMessage.toString());
            alert.showAndWait();
        }
        // if the user is under 13, they cannot create an account
        else if (!validAge)
        {
            alert.setHeaderText("Invalid Input");
            alert.setContentText("You have to be at least 13 years old to create an account.");
            alert.showAndWait();
        }
        else
        {
            usernameTaken = userNameIsTaken(textFieldUsername.getText());
            emailTaken = emailIsTaken(textFieldEmail.getText());

            if (usernameTaken)
            {
                alert.setHeaderText("Username Taken");
                alert.setContentText("Please enter a different username.");
                alert.showAndWait();
            }
            else if (emailTaken)
            {
                alert.setHeaderText("Email Address Taken");
                alert.setContentText("Did you mistype the email address? If not, and you have forgotten your password, use the forgot password option.");
                alert.showAndWait();
            }
            else
            {
                StringBuilder path = new StringBuilder("../view/");

                if (event.getSource() == buttonNext)
                {
                    Profile profile = new Profile();

                    // Get the text fields
                    String firstName = textFieldFirstName.getText();
                    user.setFirstName(firstName);

                    String lastName = textFieldLastName.getText();
                    user.setLastName(lastName);

                    LocalDate dateOfBirth = datePickerDateOfBirth.getValue();
                    profile.setDoB(dateOfBirth);
                    profile.setAgeFromDoB();
                    user.setProfile(profile);

                    String gender = comboBoxGender.getValue().toString();
                    if (gender.equals("Male"))
                        user.getProfile().setSex("MALE");
                    else
                        user.getProfile().setSex("FEMALE");

                    String userName = textFieldUsername.getText();
                    user.setUsername(userName);

                    String email = textFieldEmail.getText();
                    user.setEmail(email);

                    String salt = User.generateSalt();
                    user.setSalt(salt);

                    String password = textFieldPassword.getText();
                    password = User.hashDataWithSalt(password, salt);
                    user.setPassword(password);


                    path.append("createAccountSecurityQuestions.fxml");
                }

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
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        comboBoxGender.getItems().removeAll();

        genders = new ArrayList<>();
        genders.add("Male");
        genders.add("Female");

        comboBoxGender.getItems().addAll(genders);

        tooltipPassword.setText("Password requires:\n" +
                "one upper case,\n" +
                "one lower case,\n" +
                "one number,\n" +
                "one special character.\n" +
                "Length: 8-32 characters.");
    }

    @Override
    public void initUser(User user)
    {
        this.user = user;
    }
}
