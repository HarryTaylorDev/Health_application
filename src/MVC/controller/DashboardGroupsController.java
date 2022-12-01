/*
Name:           DashboardGroupsController.java

Authors:        Chris, Conor, Harry, Milo, Yacine

Description:    Class controls the Dashboard Groups screen, which forms a part
                of the main application.
*/
package MVC.controller;

import MVC.model.Announcement;
import MVC.model.Goal;
import MVC.model.Group;
import MVC.model.User;
import MVC.utils.CustomAlert;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.ResourceBundle;

public class DashboardGroupsController extends Controller implements Initializable
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
    private Button buttonSettings;
    @FXML
    private Button buttonSignOut;
    @FXML
    private Button buttonHelp;

    @FXML
    private Label labelUsername;
    @FXML
    private Label labelPoints;


    /*********    Pane: Announcements               ************/
    @FXML
    private TextFlow textFlowAnnouncements;


    /*********    Pane: GroupsList               ************/
    @FXML
    private TableView<Group> tableViewGroupsList;
    @FXML
    private TableColumn<Goal, String> groupListNameColumn;


    @FXML
    private TextField textFieldCode;

    private ArrayList<Group> groupList = new ArrayList<>();

    private ArrayList<Announcement> announcementList = new ArrayList<>();

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
        groupListNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    @Override
    public void initUser(User user) throws SQLException
    {
        this.user = user;

        labelUsername.setText(user.getUsername());
        labelPoints.setText(String.valueOf(user.getProfile().getPoints().getTotal()));

        for (Integer i : user.getProfile().getUserGroups())
        {
            groupList.add(Group.loadGroupFromDatabase(i));
        }

        for (Group group : groupList)
        {
            tableViewGroupsList.getItems().add(group);
            for (Announcement a : group.getAnnouncementList())
            {
                a.setGroupName(group.getName());
                announcementList.add(a);
            }
            //Announcements are added to a list so that they can be sorted by the date they are posted.
            //The group name is added to the announcement so that it can be printed with the correct group name.
        }
        Collections.sort(announcementList);
        for (Announcement e : announcementList)
        {
            //Loops through the gathered announcements and prints them to the text flow.
            textFlowAnnouncements.getChildren().add(new Text(e.getGroupName() + e.printForDashboard()));
        }
    }

    public void createGroupAction(ActionEvent event) throws IOException, SQLException
    {
        loadScene("../view/createGroup.fxml", event);
    }

    public void openGroupHub(ActionEvent event) throws IOException, SQLException
    {
        if (!tableViewGroupsList.getSelectionModel().isEmpty())
        {
            user.getProfile().setCurrentGroup(tableViewGroupsList.getSelectionModel().getSelectedItem());
            loadScene("../view/groupHub.fxml", event);
        }
        else
        {
            CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Input");
            alert.setContentText("You must select a group to open");
            alert.showAndWait();
        }
    }

    public void leaveGroupAction(ActionEvent event)
    {

        if (!tableViewGroupsList.getSelectionModel().isEmpty())
        {
            CustomAlert a = new CustomAlert(Alert.AlertType.CONFIRMATION);
            a.setHeaderText("Please Confirm Action!");
            a.setContentText("Are you sure you want to leave " + tableViewGroupsList.getSelectionModel().getSelectedItem().getName() + "?");

            Optional<ButtonType> result = a.showAndWait();
            if (result.get() == ButtonType.OK)
            {
                // Check if user is the owner (compare username
                if (tableViewGroupsList.getSelectionModel().getSelectedItem().getOwner().equals(user.getUsername()))
                {
                    if (tableViewGroupsList.getSelectionModel().getSelectedItem().getUserList().size() < 2)
                    {
                        // Remove group from database
                        Group.removeGroupFromDatabase(tableViewGroupsList.getSelectionModel().getSelectedItem());

                        //Remove group id from user
                        user.getProfile().removeGroup(tableViewGroupsList.getSelectionModel().getSelectedItem().getGroupID());
                        User.writeExistingUserToDatabase(user);

                        //Remove from list of groups on page
                        tableViewGroupsList.getItems().remove(tableViewGroupsList.getSelectionModel().getSelectedItem());


                        CustomAlert alert = new CustomAlert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText("Group Removed");
                        alert.setContentText("You have been removed from the " +
                                "group and as you were the last user, the " +
                                "group has been removed from the system");
                        alert.showAndWait();
                    }

                    else
                    {
                        CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR);
                        alert.setHeaderText("Error");
                        alert.setContentText("You are the owner of this group, " +
                                "and there are still members of this group, " +
                                "so you cannot leave! Kick these users first");
                        alert.showAndWait();
                    }
                }

                // If the user is not the owner
                else
                {

                    //Remove user from group object
                    tableViewGroupsList.getSelectionModel().getSelectedItem().removeUser(user);
                    Group.writeExistingGroupToDatabase(tableViewGroupsList.getSelectionModel().getSelectedItem());

                    //Remove group id from user
                    user.getProfile().removeGroup(tableViewGroupsList.getSelectionModel().getSelectedItem().getGroupID());
                    User.writeExistingUserToDatabase(user);

                    //Remove from list of groups on page
                    tableViewGroupsList.getItems().remove(tableViewGroupsList.getSelectionModel().getSelectedItem());
                }
            }
        }
        else
        {
            CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Input");
            alert.setContentText("You must select a group to leave");
            alert.showAndWait();
        }
    }

    public void joinGroupWithCode(ActionEvent event) throws IOException, SQLException
    {
        if (textFieldCode.getText().length() != 0)
        {
            //if (Group.checkDatabaseForCode(textFieldCode.getText())){
            if (Group.verifyUserCode(textFieldCode.getText(), user.getUserID()))
            {
                //get group id here
                int groupID = Group.getGroupIDFromCode(textFieldCode.getText());

                Group group = Group.loadGroupFromDatabase(groupID);
                group.addUser(user);
                Group.writeExistingGroupToDatabase(group);
                User.writeExistingUserToDatabase(user);

                //Remove code from DB
                Group.removeInviteCodeFromDatabase(textFieldCode.getText());

                user.getProfile().setCurrentGroup(group);
                loadScene("../view/groupHub.fxml", event);
            }
            else
            {
                CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR);
                alert.setHeaderText("Invalid Input");
                alert.setContentText("This is not a valid invite code, try again!");
                alert.showAndWait();
            }
        }
        else
        {
            CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Input");
            alert.setContentText("You must enter a code.");
            alert.showAndWait();
        }
    }
}