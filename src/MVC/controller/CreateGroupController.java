/*
Name:           CreateGroupController.java

Authors:        Chris, Conor, Harry, Milo, Yacine

Description:    Class controls the Create Group screen, which forms a part
                of the main application.
*/
package MVC.controller;

import MVC.model.Group;
import MVC.model.User;
import MVC.utils.CustomAlert;
import MVC.utils.InputValidation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CreateGroupController extends Controller implements Initializable
{
    /*********    Pane:  Side buttons              ************/
    @FXML
    private Button buttonHome;
    @FXML
    private Button buttonDiet;
    @FXML
    private Button buttonFitness;
    @FXML
    private Button buttonWeight;
    @FXML
    private Button buttonGroups;
    @FXML
    private Button buttonHelp;
    @FXML
    private Button buttonSettings;
    @FXML
    private Button buttonSignOut;

    @FXML
    private Label labelUsername;
    @FXML
    private Label labelPoints;


    /*********    Pane: Create Group               ************/
    @FXML
    private TextField textFieldGroupName;
    @FXML
    private TextArea textAreaGroupDescription;

    // group image stuff

    @FXML
    private ToggleGroup toggleGroupImage;
    @FXML
    private ToggleButton imgButtonBaseball;
    @FXML
    private ToggleButton imgButtonBoxing;
    @FXML
    private ToggleButton imgButtonFootball;
    @FXML
    private ToggleButton imgButtonGolf;
    @FXML
    private ToggleButton imgButtonRunning;
    @FXML
    private ToggleButton imgButtonSkiing;
    @FXML
    private ToggleButton imgButtonSwimming;
    @FXML
    private ToggleButton imgButtonVolleyball;
    @FXML
    private ToggleButton imgButtonWeightLifting;

    @FXML
    private Button buttonCreateGroup;
    @FXML
    private Hyperlink hyperlinkReturn;


    public void buttonMenuClicked(ActionEvent event) throws IOException, SQLException
    {
        StringBuilder path = new StringBuilder("../view/");

        if (event.getSource() == buttonHome)
        {
            path.append("dashboardHome.fxml");
            User.writeExistingUserToDatabase(this.user);
        }
        else if (event.getSource() == buttonDiet)
        {
            path.append("dashboardDiet.fxml");
            User.writeExistingUserToDatabase(this.user);
        }
        else if (event.getSource() == buttonFitness)
        {
            path.append("dashboardFitness.fxml");
            User.writeExistingUserToDatabase(this.user);
        }
        else if (event.getSource() == buttonWeight)
        {
            path.append("dashboardWeight.fxml");
            User.writeExistingUserToDatabase(this.user);
        }
        else if (event.getSource() == buttonSettings)
        {
            path.append("dashboardSettings.fxml");
            User.writeExistingUserToDatabase(this.user);
        }
        else if (event.getSource() == buttonSignOut)
        {
            path.append("login.fxml");
            User.writeExistingUserToDatabase(this.user);
        }
        else if (event.getSource() == buttonGroups)
        {
            path.append("dashboardGroups.fxml");
            User.writeExistingUserToDatabase(this.user);
        }
        else if (event.getSource() == buttonHelp)
        {
            path.append("dashboardHelp.fxml");
            User.writeExistingUserToDatabase(this.user);
        }

        loadScene(path.toString(), event);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        imgButtonBaseball.setUserData("baseball");
        imgButtonBoxing.setUserData("boxing");
        imgButtonFootball.setUserData("football");
        imgButtonGolf.setUserData("golf");
        imgButtonRunning.setUserData("running");
        imgButtonSkiing.setUserData("skiing");
        imgButtonSwimming.setUserData("swimming");
        imgButtonVolleyball.setUserData("volleyball");
        imgButtonWeightLifting.setUserData("weight_lifting");
    }

    @Override
    public void initUser(User user)
    {
        this.user = user;

        labelUsername.setText(user.getUsername());
        labelPoints.setText(String.valueOf(user.getProfile().getPoints().getTotal()));
    }

    public void hyperlinkReturnClicked(ActionEvent event) throws IOException, SQLException
    {
        loadScene("../view/dashboardGroups.fxml", event);
    }

    public void createGroup(ActionEvent event) throws IOException, SQLException
    {
        StringBuilder errorMessage = new StringBuilder();

        if (!InputValidation.checkLength(textFieldGroupName.getText(), 50) || textFieldGroupName.getText().length() < 5)
        {
            errorMessage.append("You must enter a valid group name that is over 5  characters under 50 characters\n");
        }
        if (!InputValidation.checkLength(textAreaGroupDescription.getText(), 130) || textAreaGroupDescription.getText().length() < 5)
        {
            errorMessage.append("You must enter a valid description that is over 5 under characters 130 characters\n");
        }
        if (toggleGroupImage.getSelectedToggle() == null)
        {
            errorMessage.append("You must select a valid image\n");
        }
        if (Group.isNameTaken(textFieldGroupName.getText()))
        {
            errorMessage.append("Your group name must be unique, try a new name!\n");
        }
        if (!(errorMessage.toString().length() > 0))
        {
            String name = textFieldGroupName.getText();
            String description = textAreaGroupDescription.getText();
            String imgID = (String) toggleGroupImage.getSelectedToggle().getUserData();

            Group newGroup = new Group(user.getUsername(), name, description, imgID);
            Group.writeNewGroupToDatabase(newGroup);

            user.getProfile().addGroup(newGroup.getGroupID());
            User.writeExistingUserToDatabase(user);

            loadScene("../view/dashboardGroups.fxml", event);
        }
        else
        {
            CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Input");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
        }
    }
}