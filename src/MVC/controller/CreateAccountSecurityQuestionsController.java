/*
Name:           CreateAccountSecurityQuestionsController.java

Authors:        Chris, Conor, Harry, Milo, Yacine

Description:    Class controls the CreateAccountHealth screen, forming a part
                of the create account section of the application. This screen
                allows the user to their security information, which is to be
                used in case of password resetting.
*/
package MVC.controller;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class CreateAccountSecurityQuestionsController extends Controller implements Initializable
{
    @FXML
    private ComboBox comboBoxSecurityQuestion1;
    @FXML
    private ComboBox comboBoxSecurityQuestion2;
    @FXML
    private TextField textFieldSecurityQuestion1;
    @FXML
    private TextField textFieldSecurityQuestion2;
    @FXML
    private Button buttonNext;
    @FXML
    private Hyperlink hyperlinkReturn;

    private ArrayList<String> questions;

    /**
     * finds a string in an array of strings and returns the index of the occurance
     * @param list list of strings to be searched
     * @param toBeFound what is being looked for
     * @return index where string is found in arraylist
     */
    public static int getIndex(ArrayList<String> list, String toBeFound)
    {
        int index = 0;

        for (int i = 0; i < list.size(); i++)
        {
            if (list.get(i).equals(toBeFound))
            {
                index = i;
            }
        }

        return index;
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
        boolean validQuestionOne = questions.contains(comboBoxSecurityQuestion1.getValue());
        boolean validQuestionTwo = questions.contains(comboBoxSecurityQuestion2.getValue());
        boolean validAnswerOne = InputValidation.checkOnlyLetters(textFieldSecurityQuestion1.getText());
        boolean shortEnoughQuestionOne = InputValidation.checkLength(textFieldSecurityQuestion1.getText(), 50);
        boolean validAnswerTwo = InputValidation.checkOnlyLetters(textFieldSecurityQuestion2.getText());
        boolean shortEnoughQuestionTwo = InputValidation.checkLength(textFieldSecurityQuestion2.getText(), 50);

        CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR);
        StringBuilder alertMessage = new StringBuilder("Please enter a valid:\n");

        if (!validQuestionOne)
        {
            alertMessage.append("Question 1 selection, ");
        }
        if (!validQuestionTwo)
        {
            alertMessage.append("Question 2 selection, ");
        }
        if (!validAnswerOne || !shortEnoughQuestionOne)
        {
            alertMessage.append("Answer to Question 1, ");
        }
        if (!validAnswerTwo || !shortEnoughQuestionTwo)
        {
            alertMessage.append("Answer to Question 2, ");
        }

        // if one of the validators returns false, display an error message
        // showing all input errors to fix by the user
        if (!(validAnswerOne && validAnswerTwo && validQuestionOne && validQuestionTwo && shortEnoughQuestionOne && shortEnoughQuestionTwo))
        {
            alertMessage.setLength(alertMessage.length() - 2); // remove tailing ", "

            alert.setHeaderText("Invalid Input");
            alert.setContentText(alertMessage.toString());
            alert.showAndWait();
        }
        else
        {
            StringBuilder path = new StringBuilder("../view/");

            String securityQuestionOne = comboBoxSecurityQuestion1.getValue().toString();
            int securityQuestionOneIndex = getIndex(questions, securityQuestionOne);
            String securityAnswerOne = textFieldSecurityQuestion1.getText();

            String securityQuestionTwo = comboBoxSecurityQuestion2.getValue().toString();
            int securityQuestionTwoIndex = getIndex(questions, securityQuestionTwo);
            String securityAnswerTwo = textFieldSecurityQuestion2.getText();

            user.setSecurityQuestionOneID(securityQuestionOneIndex + 1);
            user.setSecurityQuestionTwoID(securityQuestionTwoIndex + 1);

            user.setSecurityAnswerOne(securityAnswerOne);
            user.setSecurityAnswerTwo(securityAnswerTwo);

            path.append("createAccountHealth.fxml");

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
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        comboBoxSecurityQuestion1.getItems().removeAll();
        comboBoxSecurityQuestion2.getItems().removeAll();

        DatabaseManagement db = new DatabaseManagement();
        String[] params = {};
        questions = new ArrayList<>();
        ArrayList<HashMap<String, String>> results = db.select("SELECT * FROM security_questions", params);

        for (int i = 0; i < results.size(); i++)
        {
            questions.add(results.get(i).get("question"));
        }

        comboBoxSecurityQuestion1.getItems().addAll(questions.subList(0, 3));
        comboBoxSecurityQuestion2.getItems().addAll(questions.subList(3, 6));
    }

    @Override
    public void initUser(User user)
    {
        this.user = user;
    }
}
