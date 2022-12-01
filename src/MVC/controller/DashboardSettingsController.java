/*
Name:           DashboardSettingsController.java

Authors:        Chris, Conor, Harry, Milo, Yacine

Description:    Class controls the Dashboard settings page. Here, users can
                change their settings and security information.
*/

package MVC.controller;

import MVC.model.User;
import MVC.utils.CustomAlert;
import MVC.utils.DatabaseManagement;
import MVC.utils.InputValidation;
import MVC.utils.LoginUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class DashboardSettingsController extends Controller implements Initializable
{
    @FXML
    private Button buttonDiet;
    @FXML
    private Button buttonHome;
    @FXML
    private Button buttonFitness;
    @FXML
    private Button buttonWeight;
    @FXML
    private Button buttonGroups;
    @FXML
    private Button buttonSignOut;
    @FXML
    private Button buttonHelp;

    @FXML
    private Label labelUsername;
    @FXML
    private Label labelPoints;


    @FXML
    private ComboBox<Integer> comboBoxWeightFrequency;
    @FXML
    private TextField textFieldChangeHeight;
    @FXML
    private ComboBox<Integer> comboBoxLifeStyle;

    @FXML
    private PasswordField textFieldCurrentPassword;

    @FXML
    private ComboBox comboBoxSQQuestionOne;
    @FXML
    private TextField textFieldSQQuestionOneAnswer;
    @FXML
    private ComboBox comboBoxSQQuestionTwo;
    @FXML
    private TextField textFieldSQQuestionTwoAnswer;

    @FXML
    private TextField textFieldChangePWNewPassword;
    @FXML
    private TextField textFieldChangePWRepeatPassword;

    @FXML
    private Tooltip tooltipPassword;

    public void buttonMenuClicked(ActionEvent event) throws IOException, SQLException
    {
        StringBuilder path = new StringBuilder("../view/");

        if (event.getSource() == buttonHome)
            path.append("dashboardHome.fxml");
        else if (event.getSource() == buttonDiet)
            path.append("dashboardDiet.fxml");
        else if (event.getSource() == buttonFitness)
            path.append("dashboardFitness.fxml");
        else if (event.getSource() == buttonWeight)
            path.append("dashboardWeight.fxml");
        else if (event.getSource() == buttonGroups)
            path.append("dashboardGroups.fxml");
        else if (event.getSource() == buttonSignOut)
            path.append("login.fxml");
        else if (event.getSource() == buttonHelp)
            path.append("dashboardHelp.fxml");

        loadScene(path.toString(), event);
    }

    @Override
    public void initUser(User user)
    {
        this.user = user;

        labelUsername.setText(user.getUsername());
        labelPoints.setText(String.valueOf(user.getProfile().getPoints().getTotal()));

        for (int i = 1; i < 8; i++)
        {
            comboBoxWeightFrequency.getItems().add(new Integer(i));
        }

        Callback<ListView<Integer>, ListCell<Integer>> cellFactory = new Callback<ListView<Integer>, ListCell<Integer>>()
        {
            @Override
            public ListCell<Integer> call(ListView<Integer> l)
            {
                return new ListCell<Integer>()
                {
                    @Override
                    protected void updateItem(Integer item, boolean empty)
                    {
                        super.updateItem(item, empty);
                        if (item == null || empty)
                        {
                            setGraphic(null);
                        }
                        else
                        {
                            if (item.intValue() == 1)
                            {
                                setText("Every day");
                            }
                            else
                            {
                                setText("Every " + item.intValue() + " days");
                            }
                        }
                    }
                };
            }
        };

        comboBoxWeightFrequency.setCellFactory(cellFactory);
        comboBoxWeightFrequency.setButtonCell(cellFactory.call(null));

        ArrayList<String> lifestyle = new ArrayList<>();
        lifestyle.add("Sedentary (Little to no exercise)");
        lifestyle.add("Lightly Active (Exercise 1-3 times a week)");
        lifestyle.add("Moderately active (Exercise 4-5 times a week)");
        lifestyle.add("Highly active (Exercise 6-7 times a week)");
        lifestyle.add("Extremely active (Intense exercise daily)");

        Callback<ListView<Integer>, ListCell<Integer>> cellFactoryLifeStyle = new Callback<ListView<Integer>, ListCell<Integer>>()
        {
            @Override
            public ListCell<Integer> call(ListView<Integer> l)
            {
                return new ListCell<Integer>()
                {
                    @Override
                    protected void updateItem(Integer item, boolean empty)
                    {
                        super.updateItem(item, empty);
                        if (item == null || empty)
                        {
                            setGraphic(null);
                        }
                        else
                        {
                            setText(lifestyle.get(item.intValue()));
                        }
                    }
                };
            }
        };

        comboBoxLifeStyle.setCellFactory(cellFactoryLifeStyle);
        comboBoxLifeStyle.setButtonCell(cellFactoryLifeStyle.call(null));

        for (int i = 0; i < 5; i++)
        {
            comboBoxLifeStyle.getItems().add(i);
        }

        ArrayList<String> questions = new ArrayList<>();

        DatabaseManagement db = new DatabaseManagement();
        String securityStatement = "SELECT * FROM security_questions";
        String[] params = {};
        ArrayList<HashMap<String, String>> results = db.select(securityStatement, params);
        for (int i = 0; i < results.size(); i++)
        {
            String question = results.get(i).get("question");
            questions.add(question);
        }


        comboBoxSQQuestionOne.getItems().addAll(questions.subList(0, 3));
        comboBoxSQQuestionTwo.getItems().addAll(questions.subList(3, 6));

        comboBoxWeightFrequency.setValue(user.getProfile().getWeightEntryFrequency());
        textFieldChangeHeight.setText(user.getProfile().getHeight().toString());
        comboBoxLifeStyle.setValue(user.getProfile().getLifeStyleValue());
    }

    public void setWeightFrequency(ActionEvent event)
    {
        if (!comboBoxWeightFrequency.getSelectionModel().isEmpty())
        {
            user.getProfile().setWeightEntryFrequency(comboBoxWeightFrequency.getSelectionModel().getSelectedItem());
        }
        else
        {
            CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Input");
            alert.setContentText("You must enter a time period");
            alert.showAndWait();
        }
    }

    public void setNewUserHeight(ActionEvent event)
    {
        if (InputValidation.checkOnlyNumbers(textFieldChangeHeight.getText()))
        {
            user.getProfile().setHeight(new Float(textFieldChangeHeight.getText()));
        }
        else
        {
            CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Input");
            alert.setContentText("You must enter a valid height");
            alert.showAndWait();
        }
    }

    public void setNewLifeStyle(ActionEvent event)
    {
        if (!comboBoxLifeStyle.getSelectionModel().isEmpty())
        {
            user.getProfile().setLifeStyleValue(comboBoxLifeStyle.getValue());
        }
        else
        {
            CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Input");
            alert.setContentText("");
            alert.showAndWait();
        }
    }

    public void setNewSecurityQuestions(ActionEvent event)
    {
        String errorMessage = "";
        if (textFieldCurrentPassword.getText() != null)
        {
            if (LoginUtils.hashDataWithSalt(textFieldCurrentPassword.getText(), user.getSalt()).equals(user.getPassword()))
            {
                if (!comboBoxSQQuestionOne.getSelectionModel().isEmpty() && !comboBoxSQQuestionTwo.getSelectionModel().isEmpty())
                {
                    if (!textFieldSQQuestionOneAnswer.getText().isEmpty() && !textFieldSQQuestionTwoAnswer.getText().isEmpty())
                    {
                        // Get combobox data
                        // Create database management object
                        // Write to file
                        int questionOneOrdinal = (comboBoxSQQuestionOne.getSelectionModel().getSelectedIndex()) + 1;
                        int questionTwoOrdinal = (comboBoxSQQuestionTwo.getSelectionModel().getSelectedIndex()) + 4;

                        String question1Answer = textFieldSQQuestionOneAnswer.getText();
                        String question2Answer = textFieldSQQuestionTwoAnswer.getText();

                        changeSecurityQuestions(user.getEmail(), questionOneOrdinal, question1Answer, questionTwoOrdinal, question2Answer);

                        //Alert user that security questions were properly changed
                        CustomAlert successfulChange = new CustomAlert(Alert.AlertType.INFORMATION);
                        successfulChange.setHeaderText("Security Questions Changed");
                        successfulChange.setContentText("Security Questions Changed Successfully.");
                        successfulChange.showAndWait();

                    }
                    else
                    {
                        errorMessage = "You must answer both questions";
                    }
                }
                else
                {
                    errorMessage = "You choose two questions";
                }
            }
            else
            {
                errorMessage = "Incorrect password, please enter your password again";
            }
        }
        else
        {
            errorMessage = "You must enter your current password first";
        }
        if (!errorMessage.equals(""))
        {
            CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Input");
            alert.setContentText(errorMessage);
            alert.showAndWait();
        }
    }

    /**
     * Updates the security questions the user has entered in the database
     * @param email users email
     * @param questionOne security question 1
     * @param answerOne answer 1
     * @param questionTwo security question 2
     * @param answerTwo answer 2
     */
    public void changeSecurityQuestions(String email, int questionOne, String answerOne, int questionTwo, String answerTwo)
    {
        user.setSecurityQuestionOneID(questionOne);
        user.setSecurityQuestionTwoID(questionTwo);
        user.setSecurityAnswerOne(answerOne);
        user.setSecurityAnswerTwo(answerTwo);
        DatabaseManagement db = new DatabaseManagement();

        String sqlStatement = "UPDATE users SET question1_id = ?, question1_answer = ?, question2_id = ?, question2_answer = ? WHERE email= ?";
        String questionOneStr = "" + questionOne;
        String questionTwoStr = "" + questionTwo;
        String[] params = {questionOneStr, answerOne, questionTwoStr, answerTwo, email};
        db.insert(sqlStatement, params);
    }

    public void setNewPassword(ActionEvent event)
    {
        String errorMessage = "";
        if (textFieldCurrentPassword.getText() != null)
        {
            if (LoginUtils.hashDataWithSalt(textFieldCurrentPassword.getText(), user.getSalt()).equals(user.getPassword()))
            {
                if (textFieldChangePWNewPassword.getText() != null && textFieldChangePWRepeatPassword.getText() != null)
                {
                    if (textFieldChangePWNewPassword.getText().equals(textFieldChangePWRepeatPassword.getText()))
                    {
                        if (InputValidation.checkPasswordSecurity(textFieldChangePWNewPassword.getText()))
                        {
                            //Code for changing password in here please
                            resetPassword(user.getEmail(), textFieldChangePWNewPassword.getText());

                            //Alert user that password was successfully changed
                            CustomAlert successfulChange = new CustomAlert(Alert.AlertType.INFORMATION);
                            successfulChange.setHeaderText("Password Changed");
                            successfulChange.setContentText("Password Changed Successfully.");
                            successfulChange.showAndWait();
                        }
                        else
                        {
                            errorMessage = "Your password must be in the right form";
                        }
                    }
                    else
                    {
                        errorMessage = "The second password must match";
                    }
                }
                else
                {
                    errorMessage = "You must enter a new password and repeat it";
                }
            }
            else
            {
                errorMessage = "Incorrect password, please enter your password again";
            }
        }
        else
        {
            errorMessage = "You must enter your current password first";
        }
        if (!errorMessage.equals(""))
        {
            CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Input");
            alert.setContentText(errorMessage);
            alert.showAndWait();
        }
    }

    /**
     * Resets the users password
     * @param email users email
     * @param newPassword new password
     */
    public void resetPassword(String email, String newPassword)
    {
        DatabaseManagement db = new DatabaseManagement();
        String salt = User.generateSalt();
        String hashPassword = LoginUtils.hashDataWithSalt(newPassword, salt);

        // Set users new password
        user.setPassword(newPassword);

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
