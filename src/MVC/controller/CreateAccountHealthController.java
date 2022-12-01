/*
Name:           CreateAccountHealthController.java

Authors:        Chris, Conor, Harry, Milo, Yacine

Description:    Class controls the CreateAccountHealth screen, forming a part
                of the create account section of the application. This screen
                allows the user to input data related to their health and
                well-being.
*/
package MVC.controller;

import MVC.model.Day;
import MVC.model.User;
import MVC.model.WeightGoal;
import MVC.utils.CustomAlert;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CreateAccountHealthController extends Controller implements Initializable
{
    ArrayList<String> lifestyleList;
    @FXML
    private Button buttonCreateAccount;
    @FXML
    private TextField textFieldMetricHeight;
    @FXML
    private TextField textFieldMetricWeight;
    @FXML
    private TextField textFieldMetricTarget;
    @FXML
    private DatePicker datePickerTargetWeightDate;
    @FXML
    private ComboBox comboBoxLifestyle;
    @FXML
    private Hyperlink hyperlinkReturn;

    /**
     * Function to get the index of a string searched in the string arraylist
     * @param list to be searched
     * @param toBeFound string to find in arraylist
     * @return index where string is in arraylist
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

    public void buttonCreateAccountAndSignInClicked(ActionEvent event) throws IOException
    {
        boolean validHeight = InputValidation.checkOnlyNumbers(textFieldMetricHeight.getText());
        boolean shortEnoughHeight = InputValidation.checkLength(textFieldMetricHeight.getText(), 5);
        boolean validWeight = InputValidation.checkOnlyFloatingPoint(textFieldMetricWeight.getText());
        boolean shortEnoughWeight = InputValidation.checkLength(textFieldMetricWeight.getText(), 5);
        boolean validTargetWeight = InputValidation.checkOnlyFloatingPoint(textFieldMetricTarget.getText());
        boolean shortEnoughTarget = InputValidation.checkLength(textFieldMetricTarget.getText(), 5);
        boolean validTargetWeightDate;

        // check for not null otherwise could cause errors
        if (datePickerTargetWeightDate.getValue() != null)
        {
            LocalDate targetWeightDate = datePickerTargetWeightDate.getValue();
            validTargetWeightDate = InputValidation.checkDateAfterToday(targetWeightDate);
        }
        else
        {
            validTargetWeightDate = false;
        }

        boolean validLifestyle = lifestyleList.contains(comboBoxLifestyle.getValue());

        CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR);
        StringBuilder alertMessage = new StringBuilder("Please enter a valid:\n");

        if (!validHeight || !shortEnoughHeight)
        {
            alertMessage.append("Height, ");
        }
        if (!validWeight || !shortEnoughWeight)
        {
            alertMessage.append("Weight, ");
        }
        if (!validTargetWeight || !shortEnoughTarget)
        {
            alertMessage.append("Target Weight, ");
        }
        if (!validTargetWeightDate)
        {
            alertMessage.append("Target Weight Date, ");
        }
        if (!validLifestyle)
        {
            alertMessage.append("Lifestyle, ");
        }

        if (!(validHeight && validLifestyle && validTargetWeight && validTargetWeightDate && validWeight && shortEnoughHeight && shortEnoughWeight && shortEnoughTarget))
        {
            alertMessage.setLength(alertMessage.length() - 2); // remove tailing ", "

            alert.setHeaderText("Invalid Input");
            alert.setContentText(alertMessage.toString());
            alert.showAndWait();
        }
        else
        {
            Float height;
            Float weight;
            Float targetWeight;

            height = Float.parseFloat(textFieldMetricHeight.getText());
            weight = Float.parseFloat(textFieldMetricWeight.getText());
            targetWeight = Float.parseFloat(textFieldMetricTarget.getText());

            StringBuilder path = new StringBuilder("../view/");

            if (event.getSource() == buttonCreateAccount)
            {
                // Setting height
                user.getProfile().setHeight(height);

                // Setting current weight (to be within first weight object)
                Day day = new Day();
                day.setWeight(weight);
                user.getProfile().getDays().add(day);

                // Get weight target here
                LocalDate weightGoalDate = datePickerTargetWeightDate.getValue();

                WeightGoal weightGoal = new WeightGoal(LocalDate.now(), weightGoalDate, user.getProfile().getWeight(), targetWeight, null);
                user.getProfile().setWeightGoal(weightGoal);

                // Setting lifestyle value
                String lifestyle = comboBoxLifestyle.getValue().toString();
                int lifeStyleValue = getIndex(lifestyleList, lifestyle);
                user.getProfile().setLifeStyleValue(lifeStyleValue);

                // Load activity and meals data into user object
                user.getProfile().loadDefaultData();
                User.writeNewUserToDatabase(user);
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
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        comboBoxLifestyle.getItems().removeAll();
        ArrayList<String> lifestyle = new ArrayList<>();
        lifestyle.add("Sedentary (Little to no exercise)");
        lifestyle.add("Lightly Active (Exercise 1-3 times a week)");
        lifestyle.add("Moderately active (Exercise 4-5 times a week)");
        lifestyle.add("Highly active (Exercise 6-7 times a week)");
        lifestyle.add("Extremely active (Intense exercise daily)");
        comboBoxLifestyle.getItems().addAll(lifestyle);
        this.lifestyleList = lifestyle;
    }

    @Override
    public void initUser(User user)
    {
        this.user = user;
    }
}
