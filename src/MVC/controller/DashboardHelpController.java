/*
Name:           DashboardHelpController.java

Authors:        Chris, Conor, Harry, Milo, Yacine

Description:    Class controls the main dashboard help screen. Helping the user
                with complicated tasks and features about our system such as
                points
*/

package MVC.controller;

import MVC.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class DashboardHelpController extends Controller implements Initializable
{
    ArrayList<String> dietEntries;
    ArrayList<String> fitnessEntries;
    ArrayList<String> groupsEntries;
    ArrayList<String> pointsEntries;
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
    private Button buttonSettings;
    @FXML
    private Button buttonSignOut;
    @FXML
    private Label labelUsername;
    @FXML
    private Label labelPoints;
    /*********    Pane:  Help                       ************/


    @FXML
    private TextFlow textFlowHelpContent;
    @FXML
    private Button buttonDietHelp;
    @FXML
    private Button buttonFitnessHelp;
    @FXML
    private Button buttonGroupsHelp;
    @FXML
    private Button buttonPointsHelp;

    private static String readFile(String filepath) throws IOException
    {

        StringBuilder readInFile = new StringBuilder();

        File file = new File(filepath);

        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        while ((st = br.readLine()) != null)
            readInFile.append(st + "\n");

        return readInFile.toString();


    }

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
        else if (event.getSource() == buttonGroups)
        {
            path.append("dashboardGroups.fxml");
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

        loadScene(path.toString(), event);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

    }

    @Override
    public void initUser(User user)
    {
        this.user = user;

        this.loadHelpInformation();

        labelUsername.setText(user.getUsername());
        labelPoints.setText(String.valueOf(user.getProfile().getPoints().getTotal()));
        textFlowHelpContent.setTextAlignment(TextAlignment.JUSTIFY);
        textFlowHelpContent.getChildren().add(new Text("Click one of the buttons below for help!"));
    }

    public void helpMenuClicked(ActionEvent event)
    {
        if (event.getSource() == buttonDietHelp)
        {
            textFlowHelpContent.getChildren().clear();
            textFlowHelpContent.getChildren().add(new Text("Diet\n\n"));

            for (int i = 0; i < dietEntries.size(); i++)
            {
                textFlowHelpContent.getChildren().add(new Text(dietEntries.get(i)));
            }

        }
        else if (event.getSource() == buttonFitnessHelp)
        {
            textFlowHelpContent.getChildren().clear();
            textFlowHelpContent.getChildren().add(new Text("Fitness\n\n"));

            for (int i = 0; i < fitnessEntries.size(); i++)
            {
                textFlowHelpContent.getChildren().add(new Text(fitnessEntries.get(i)));
            }

        }
        else if (event.getSource() == buttonGroupsHelp)
        {
            textFlowHelpContent.getChildren().clear();
            textFlowHelpContent.getChildren().add(new Text("Groups\n\n"));

            for (int i = 0; i < groupsEntries.size(); i++)
            {
                textFlowHelpContent.getChildren().add(new Text(groupsEntries.get(i)));
            }

        }
        else if (event.getSource() == buttonPointsHelp)
        {
            textFlowHelpContent.getChildren().clear();
            textFlowHelpContent.getChildren().add(new Text("Points\n\n"));
            for (int i = 0; i < pointsEntries.size(); i++)
            {
                textFlowHelpContent.getChildren().add(new Text(pointsEntries.get(i)));
            }

        }

    }

    /**
     * Function to load the help information into the controller
     */
    private void loadHelpInformation()
    {
        try
        {
            // Read helpfile contents in
            String diet = readFile("src\\MVC\\config\\help\\diet.csv");
            String fitness = readFile("src\\MVC\\config\\help\\fitness.csv");
            String groups = readFile("src\\MVC\\config\\help\\groups.csv");
            String points = readFile("src\\MVC\\config\\help\\points.csv");

            dietEntries = new ArrayList<>(Arrays.asList(diet.split("\\|")));
            fitnessEntries = new ArrayList<>(Arrays.asList(fitness.split("\\|")));
            groupsEntries = new ArrayList<>(Arrays.asList(groups.split("\\|")));
            pointsEntries = new ArrayList<>(Arrays.asList(points.split("\\|")));
        }
        catch (Exception ex)
        {
            //default to normal values
            dietEntries = new ArrayList<>();
            dietEntries.add("Use the Diet Page to track your calorie intake by tracking " +
                    "the meals you ate throughout the day.\nFirst select the type of meal from the Meal Type drop down " +
                    "and click Set Meal Type. Then select food and drinks you have eaten and add them to the meal.\nIf " +
                    "you want to remove an item from the meal, highlight the item and click Remove From Meal.\nWhen you " +
                    "are happy with the meal, click Add Meal.\nIf you want to remove a meal from your day, highlight the" +
                    " meal and click Remove Meal.\n\n");
            dietEntries.add("At the top of the page are three tabs: Today, History and C" +
                    "ustomise Food/Drink.\nThe History tab shows your caloric intake per day using a table and a graph." +
                    "\nYou can change the range of the graph by entering new dates and selecting Set Graph Range.\n\n");
            dietEntries.add("The Customise Food/Drink tab lets you change the food and dr" +
                    "ink you can select when creating a meal.\nTo add a new item select the type, fill in the text fields a" +
                    "nd select Add Item.\nTo remove an item, highlight the item and click Remove Item.");


            fitnessEntries = new ArrayList<>();
            fitnessEntries.add("Use the Fitness page to keep track of your exercise and s" +
                    "et goals for yourself.\nYou can add exercises you have done by entering the details of the exerci" +
                    "se and clicking Add Exercise.\nYou can remove an exercise by highlighting it and clicking Remove " +
                    "Exercise.\nStrength and Cardio exercises are tracked and inserted separated.\n\n");
            fitnessEntries.add("At the top of the screen there are four tabs: Today, Hist" +
                    "ory, Goals and Customise Exercises. \nThe History tab lets you see your exercise progress using gr" +
                    "aphs and tables for each exercise type.\nThe Goals tab lets you set fitness goals for yourself.\nAdd" +
                    " a new goal by inserting the information and clicking Add Goal. You can remove a goal by highlightin" +
                    "g it and clicking Remove Goal.\nThe Customise Exercises tab lets you change the exercises available w" +
                    "hen setting goals, viewing history and tracking exercises you have done.");

            groupsEntries = new ArrayList<>();
            groupsEntries.add("Use the Groups page to work on fitness goals with other pe" +
                    "ople. On the groups dashboard you will see announcements from the groups your in as well as a lis" +
                    "t of your groups.\nYou can go to a group page by highlighting a group and clicking Open Selected " +
                    "Group Page.\nYou can leave a group by highlight a group and clicking Leave Selected Group.\nYou " +
                    "can join a new group by entering in a group code and clicking Join Group With Code.\n\n");
            groupsEntries.add("You can create a group by clicking the Create Your Own Gr" +
                    "oup button, after doing so you will be prompted for a Group Name, Description and image selecti" +
                    "on.\nAfter filling in the information requested, clicking Create Group will make a new group.\n\n");
            groupsEntries.add("On a group’s Hub page you can see announcements, favourit" +
                    "e the group, see a list of members and join the group goal.\n\n");
            groupsEntries.add("At the top of the screen there are three tabs. The last t" +
                    "wo tabs are Group Goal and Manage Group which are only accessible to the owner of the group.\nUn" +
                    "der the Group Goal tab the owner can set a goal for all members of the group to opt in to.\n\n");
            groupsEntries.add("Under the Manage Group Tab the owner is able to make an " +
                    "announcement by typing in an announcement and clicking Send.\nThe owner can invite users by typ" +
                    "ing in a username and clicking Invite User.\nThe owner can kick users by highlight a user and c" +
                    "licking Kick Selected user.");

            pointsEntries = new ArrayList<>();
            pointsEntries.add("Use points to show off your fitness prowess.\nThe " +
                    "more points you have the more experienced you are with the app.\nYou can get a maximum o" +
                    "f 300 points in a day.\nLogging in every day gives you 50 points. Meeting and staying wi" +
                    "thin range of your calorie goal gives you 150 points. Completing a goal gives you 100 po" +
                    "ints.\nTry to see how many you can get.");
        }
    }
}
