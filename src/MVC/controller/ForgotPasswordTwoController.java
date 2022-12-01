/*
Name:           ForgotPasswordTwoController.java

Authors:        Chris, Conor, Harry, Milo, Yacine

Description:    Class forms a part of the Forgot Password chain. This allows
                users to reset their password if needed
*/
package MVC.controller;

import MVC.model.User;
import MVC.utils.CustomAlert;
import MVC.utils.DatabaseManagement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class ForgotPasswordTwoController extends Controller
{
    //labelWeightOne.setText("Set Weight Now!")
    @FXML
    private Button buttonNext2;
    @FXML
    private Label labelQuestionOne;
    @FXML
    private Label labelQuestionTwo;
    @FXML
    private TextField textFieldAnswer1;
    @FXML
    private TextField textFieldAnswer2;
    @FXML
    private Hyperlink hyperlinkReturn;

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
        StringBuilder path = new StringBuilder("../view/");

        if (event.getSource() == buttonNext2)
        {
            path.append("forgotPasswordThree.fxml");   // change this
        }

        //ForgotPasswordController forgotControllerTwo = new ForgotPasswordController(user.getEmail(), textFieldAnswer1.getText(), textFieldAnswer2.getText());
        Boolean securityQuestionsCorrect = checkSecurityQuestions(textFieldAnswer1.getText(), textFieldAnswer2.getText());

        if (!securityQuestionsCorrect)
        {
            CustomAlert recoverError = new CustomAlert(Alert.AlertType.ERROR);
            recoverError.setHeaderText("Invalid Input");
            recoverError.setContentText("Security Questions incorrect.");
            recoverError.showAndWait();

            return;
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

    @Override
    public void initUser(User user)
    {
        this.user = user;
        setSecurityQuestions(this.user.getEmail());
    }

    /**
     * function to check if user enters the right answers
     * @param answerOne first security question answer
     * @param answerTwo second security question answer
     * @return returns true if both of the answers match
     */
    private boolean checkSecurityQuestions(String answerOne, String answerTwo)
    {
        DatabaseManagement db = new DatabaseManagement();
        String securityQuestion = "SELECT question1_answer, question2_answer FROM users WHERE email=?";
        String[] parameters = new String[]{user.getEmail()};

        ArrayList<HashMap<String, String>> answers = db.select(securityQuestion, parameters);

        String databaseA1 = answers.get(0).get("question1_answer");
        String databaseA2 = answers.get(0).get("question2_answer");

        return databaseA1.equals(answerOne) && databaseA2.equals(answerTwo);
    }

    /**
     * function to set the security questions that the user has chosen
     * @param email users email address
     */
    private void setSecurityQuestions(String email)
    {
        DatabaseManagement db = new DatabaseManagement();

        String[] params = {email};

        String securityQuestionOne = "SELECT question FROM security_questions WHERE question_id IN (SELECT question1_id FROM users WHERE email=?)";
        String securityQuestionTwo = "SELECT question FROM security_questions WHERE question_id IN (SELECT question2_id FROM users WHERE email=?)";

        ArrayList<HashMap<String, String>> results1 = db.select(securityQuestionOne, params);
        ArrayList<HashMap<String, String>> results2 = db.select(securityQuestionTwo, params);

        labelQuestionOne.setText(results1.get(0).get("question"));
        labelQuestionTwo.setText(results2.get(0).get("question"));
    }
}