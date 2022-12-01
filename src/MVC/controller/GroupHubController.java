/*
Name:           CreateGroupController.java

Authors:        Chris, Conor, Harry, Milo, Yacine

Description:    Class controls the group hub screen, which forms a part
                of the main application.
*/
package MVC.controller;

import MVC.model.*;
import MVC.utils.CustomAlert;
import MVC.utils.InputValidation;
import MVC.utils.StylingHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.ResourceBundle;

public class GroupHubController extends Controller implements Initializable
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
    private Button buttonGroups;
    @FXML
    private Button buttonHelp;

    @FXML
    private Label labelUsername;
    @FXML
    private Label labelPoints;


    /*********    Pane: Group Header                     ************/
    @FXML
    private Label labelGroupName;
    @FXML
    private Label labelGroupDescription;
    @FXML
    private ImageView imageViewPicture;


    /*********    Pane: Group Members List               ************/
    @FXML
    private TableView<User> tableViewMembersList;
    @FXML
    private TableColumn<User, String> tableColumnUsername;


    /*********    Pane: Group Goal                       ************/
    @FXML
    private Label labelActivity;
    @FXML
    private Label labelTarget;
    @FXML
    private Label labelTimeLeft;
    @FXML
    private ProgressBar progressBarTimeLeft;


    /*********    Pane: Admin Group Goal                       ************/
    @FXML
    private ComboBox<Activity> comboBoxGoalActivityCardio;
    @FXML
    private TextField textFieldTargetDistance;
    @FXML
    private TextField textFieldTargetTime;
    @FXML
    private DatePicker datePickerTargetDateCardio;
    @FXML
    private ComboBox<Activity> comboBoxGoalActivityStrength;
    @FXML
    private TextField textFieldTargetWeight;
    @FXML
    private TextField textFieldTargetSets;
    @FXML
    private TextField textFieldTargetReps;
    @FXML
    private DatePicker datePickerTargetDateStrength;


    /*********    Pane: Admin Group Goal                       ************/
    @FXML
    private Label labelCurrentActivity;
    @FXML
    private Label labelCurrentTarget;
    @FXML
    private Label labelCurrentTimeToComplete;
    @FXML
    private ProgressBar progressBarCurrentTimeLeft;


    /*********    Pane: Group Announcements              ************/
    @FXML
    private TextFlow textFlowAnnouncements;

    @FXML
    private TextFlow adminTextFlow;
    @FXML
    private TextField announcementTextField;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab groupTab;
    @FXML
    private Tab goalTab;


    @FXML
    private TableView<User> tableViewEditGroup;
    @FXML
    private TableColumn<User, String> tableColumnEditGroupUsername;


    private ArrayList<User> userList = new ArrayList<>();


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
        tableColumnUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        tableColumnEditGroupUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        //cell factory to set the name of strength and cardio combo boxes
        Callback<ListView<Activity>, ListCell<Activity>> cellFactory = new Callback<ListView<Activity>, ListCell<Activity>>()
        {
            @Override
            public ListCell<Activity> call(ListView<Activity> l)
            {
                return new ListCell<Activity>()
                {
                    @Override
                    protected void updateItem(Activity item, boolean empty)
                    {
                        super.updateItem(item, empty);
                        if (item == null || empty)
                        {
                            setGraphic(null);
                        }
                        else
                        {
                            setText(item.getExerciseName());
                        }
                    }
                };
            }
        };

        //set combo boxes cell factories
        comboBoxGoalActivityCardio.setButtonCell(cellFactory.call(null));
        comboBoxGoalActivityCardio.setCellFactory(cellFactory);
        comboBoxGoalActivityStrength.setButtonCell(cellFactory.call(null));
        comboBoxGoalActivityStrength.setCellFactory(cellFactory);

        Callback<TableView<User>, TableRow<User>> rowFactory = new Callback<TableView<User>, TableRow<User>>()
        {
            @Override
            public TableRow<User> call(TableView<User> param)
            {
                return new TableRow<User>()
                {
                    @Override
                    public void updateItem(User item, boolean empty)
                    {
                        super.updateItem(item, empty);
                        if (item == null)
                        {
                            setStyle("");
                        }
                        else if (item.getUsername().equals(user.getProfile().getCurrentGroup().getOwner()))
                        {
                            setStyle("-fx-background-color: gold;");
                        }
                        else
                        {
                            setStyle("");
                        }
                    }
                };
            }
        };
        tableViewEditGroup.setRowFactory(rowFactory);
        tableViewMembersList.setRowFactory(rowFactory);
    }

    @Override
    public void initUser(User user) throws SQLException
    {
        this.user = user;

        labelUsername.setText(user.getUsername());
        labelPoints.setText(String.valueOf(user.getProfile().getPoints().getTotal()));

        if (!user.getUsername().equals(user.getProfile().getCurrentGroup().getOwner()))
        {
            tabPane.getTabs().remove(goalTab);
            tabPane.getTabs().remove(groupTab);
        }

        labelGroupName.setText(user.getProfile().getCurrentGroup().getName());
        labelGroupDescription.setText(user.getProfile().getCurrentGroup().getDescription());
        imageViewPicture.setImage(new Image(GroupHubController.class.getResource("../images/group_icons/" + user.getProfile().getCurrentGroup().getImgID() + ".png").toExternalForm()));

        updateCurrentGroupGoalInfo();


        for (String userName : user.getProfile().getCurrentGroup().getUserList())
        {
            userList.add(User.loadFromDatabase(userName));
        }

        tableViewMembersList.getItems().addAll(userList);
        tableViewEditGroup.getItems().addAll(userList);

        Collections.sort(user.getProfile().getCurrentGroup().getAnnouncementList());
        for (Announcement a : user.getProfile().getCurrentGroup().getAnnouncementList())
        {

            textFlowAnnouncements.getChildren().add(new Text(a.printForHub()));
            adminTextFlow.getChildren().add(new Text(a.printForHub()));
        }

        //load data into all combo boxes
        for (Activity c : user.getProfile().getActivity())
        {
            if (c.getExerciseType().equals("Cardio"))
            {
                comboBoxGoalActivityCardio.getItems().add(c);
            }
            else
            {
                comboBoxGoalActivityStrength.getItems().add(c);
            }
        }
    }

    /**
     *  function to update the labels and progress bar for the group goal
     */
    private void updateCurrentGroupGoalInfo()
    {
        Goal goal = user.getProfile().getCurrentGroup().getGoal();
        if (goal != null)
        {
            labelActivity.setText(goal.getActivity());
            labelCurrentActivity.setText(goal.getActivity());
            labelTarget.setText(goal.getTarget());
            labelCurrentTarget.setText(goal.getTarget());
            labelTimeLeft.setText(goal.getTargetDate().toString());
            labelCurrentTimeToComplete.setText(goal.getTargetDate().toString());

            //progress bar set here
            long totalDays = InputValidation.calculateNumberOfDaysBetweenDates(goal.getSetDate(), goal.getTargetDate());
            long currentDaysLeft = InputValidation.calculateNumberOfDaysBetweenDates(LocalDate.now(), goal.getTargetDate());
            double percentage = (double) currentDaysLeft / (double) totalDays;

            StylingHelper.setProgressBarProgressAndColour(progressBarTimeLeft, percentage);
            StylingHelper.setProgressBarProgressAndColour(progressBarCurrentTimeLeft, percentage);
        }
        else
        {
            //default values for when there is no goal
            labelActivity.setText("N/A");
            labelCurrentActivity.setText("N/A");
            labelTarget.setText("N/A");
            labelCurrentTarget.setText("N/A");
            labelTimeLeft.setText("N/A");
            labelCurrentTimeToComplete.setText("N/A");
        }
    }

    public void addCardioGoal(ActionEvent event)
    {
        //input validation
        StringBuilder errorMessage = new StringBuilder();
        if (comboBoxGoalActivityCardio.getSelectionModel().isEmpty())
        {
            errorMessage.append("You must enter a activity\n");
        }
        if (!InputValidation.checkOnlyNumbers(textFieldTargetDistance.getText()))
        {
            errorMessage.append("You must enter a valid number of target distance\n");
        }
        if (!InputValidation.checkOnlyNumbers(textFieldTargetTime.getText()))
        {
            errorMessage.append("You must enter a valid target time\n");
        }
        if (!InputValidation.checkDateAfterToday(datePickerTargetDateCardio.getValue()))
        {
            errorMessage.append("You must enter a valid date in the future\n");
        }
        if (!(errorMessage.toString().length() > 0))
        {
            //set cardio goal
            String activity = comboBoxGoalActivityCardio.getValue().getExerciseName();
            int startDistance = new Integer(0);
            int targetDistance = new Integer(textFieldTargetDistance.getText());
            int startTime = new Integer(0);
            int targetTime = new Integer(textFieldTargetTime.getText());
            LocalDate targetDate = datePickerTargetDateCardio.getValue();
            CardioGoal cardioGoal = new CardioGoal(LocalDate.now(), targetDate, activity, startDistance, startTime, targetDistance, targetTime, user.getProfile().getCurrentGroup().getGroupID());
            user.getProfile().getCurrentGroup().setGoal(cardioGoal);

            updateCurrentGroupGoalInfo();


            // Get current group information
            StringBuilder emails = new StringBuilder();
            for (int i = 0; i < userList.size(); i++)
            {
                if (i == (userList.size() - 1))
                    emails.append(userList.get(i).getEmail());
                else
                    emails.append(userList.get(i).getEmail() + ",");
            }

            // Send emails to each group member
            try
            {
                Group.sendGoalEmail(emails.toString(), user.getProfile().getCurrentGroup().getName());
            }
            catch (Exception e)
            {
                e.printStackTrace();
                CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR);
                alert.setHeaderText("Warning");
                alert.setContentText("The goal has been added to the group. However, something went wrong with emailing group members");
                Optional<ButtonType> result = alert.showAndWait();

            }

            Group.writeExistingGroupToDatabase(user.getProfile().getCurrentGroup());
        }
        else
        {
            CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Input");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
        }
    }

    public void addStrengthGoal(ActionEvent event)
    {
        //input validation
        StringBuilder errorMessage = new StringBuilder();
        if (comboBoxGoalActivityStrength.getSelectionModel().isEmpty())
        {
            errorMessage.append("You must enter a activity\n");
        }
        if (!InputValidation.checkOnlyNumbers(textFieldTargetWeight.getText()))
        {
            errorMessage.append("You must enter a valid number of target weight\n");
        }
        if (!InputValidation.checkOnlyNumbers(textFieldTargetReps.getText()))
        {
            errorMessage.append("You must enter a valid number of target sets\n");
        }
        if (!InputValidation.checkOnlyNumbers(textFieldTargetReps.getText()))
        {
            errorMessage.append("You must enter a valid number of target reps\n");
        }
        if (!InputValidation.checkDateAfterToday(datePickerTargetDateStrength.getValue()))
        {
            errorMessage.append("You must enter a valid date in the future\n");
        }
        if (!(errorMessage.toString().length() > 0))
        {
            //set strength goal
            String activity = comboBoxGoalActivityStrength.getValue().getExerciseName();
            int startWeight = new Integer(0);
            int targetWeight = new Integer(textFieldTargetWeight.getText());
            int startSets = new Integer(0);
            int targetSets = new Integer(textFieldTargetSets.getText());
            int startReps = new Integer(0);
            int targetReps = new Integer(textFieldTargetReps.getText());
            LocalDate targetDate = datePickerTargetDateStrength.getValue();
            StrengthGoal strengthGoal = new StrengthGoal(LocalDate.now(), targetDate, activity, startWeight, startReps, startSets, targetWeight, targetReps, targetSets, user.getProfile().getCurrentGroup().getGroupID());
            user.getProfile().getCurrentGroup().setGoal(strengthGoal);
            updateCurrentGroupGoalInfo();

            // Get current group information
            StringBuilder emails = new StringBuilder();
            for (int i = 0; i < userList.size(); i++)
            {
                if (i == (userList.size() - 1))
                    emails.append(userList.get(i).getEmail());
                else
                    emails.append(userList.get(i).getEmail() + ",");
            }

            // Send emails to each group member
            try
            {
                Group.sendGoalEmail(emails.toString(), user.getProfile().getCurrentGroup().getName());
            }
            catch (Exception e)
            {
                e.printStackTrace();
                CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR);
                alert.setHeaderText("Warning");
                alert.setContentText("The goal has been added to the group. However, something went wrong with emailing group members");
                Optional<ButtonType> result = alert.showAndWait();

            }

            Group.writeExistingGroupToDatabase(user.getProfile().getCurrentGroup());
        }
        else
        {
            CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Input");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
        }
    }

    public void removeGoal(ActionEvent event)
    {
        if (user.getProfile().getCurrentGroup().getGoal() != null)
        {
            user.getProfile().getCurrentGroup().setGoal(null);
            updateCurrentGroupGoalInfo();
            Group.writeExistingGroupToDatabase(user.getProfile().getCurrentGroup());
        }
        else
        {
            CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Input");
            alert.setContentText("Your group has no goal");
            alert.showAndWait();
        }
    }

    public void addAnnouncement(ActionEvent event)
    {
        String message = announcementTextField.getText();

        if (InputValidation.checkLength(message, 200))
        {
            if (message.length() > 4)
            {
                Announcement announcement = new Announcement(message, user.getUsername());
                user.getProfile().getCurrentGroup().addAnnouncement(announcement);

                announcementTextField.clear();
                adminTextFlow.getChildren().add(new Text(announcement.printForHub() + "\n"));
                textFlowAnnouncements.getChildren().add(new Text(announcement.printForHub() + "\n"));

                Group.writeExistingGroupToDatabase(user.getProfile().getCurrentGroup());
            }
            else
            {
                CustomAlert a = new CustomAlert(Alert.AlertType.WARNING);
                a.setHeaderText("Invalid Input!");
                a.setContentText("Too few characters in announcement, minimum is 5.");
                a.showAndWait();
            }
        }
        else
        {
            CustomAlert a = new CustomAlert(Alert.AlertType.WARNING);
            a.setHeaderText("Invalid Input!");
            a.setContentText("Too many characters in announcement, maximum is 200.");
            a.showAndWait();
        }

    }

    public void addUser(ActionEvent event)
    {
        String enteredUsername = usernameTextField.getText();

        // Generate a code
        // Add code to table
        String code = Group.generateInviteCode();
        int userID = 0;
        int groupID = user.getProfile().getCurrentGroup().getGroupID();

        try
        {
            // Get userID and add an invite code to database for them
            User tempUser = User.loadFromDatabase(enteredUsername);
            userID = tempUser.getUserID();
            String userEmail = tempUser.getEmail();
            String username = tempUser.getUsername();

            if (!user.getProfile().getCurrentGroup().checkGroupForUser(enteredUsername))
            {
                // If user is not already invited and not already in group
                if (!Group.checkForAlreadyExistingInvite(groupID, userID))
                {

                    // Give user an invite code
                    Group.addInviteCodeToDatabase(code, groupID, userID);


                    try
                    {
                        // Send the invitation and inform the user this has completed
                        String groupName = user.getProfile().getCurrentGroup().getName();
                        String groupOwner = user.getProfile().getCurrentGroup().getOwner();
                        String groupDescription = user.getProfile().getCurrentGroup().getDescription();
                        Group.sendInviteEmail(userEmail, username, groupName, groupOwner, groupDescription, code);

                        CustomAlert alert = new CustomAlert(Alert.AlertType.CONFIRMATION);
                        alert.setHeaderText("Success");
                        alert.setContentText("User " + enteredUsername + " has been sent an invitation email");
                        Optional<ButtonType> result = alert.showAndWait();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        // Alert the user that something went wrong with emailing
                        // user and give them the code for manual adding
                        CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR);
                        alert.setHeaderText("Alert");
                        alert.setContentText("Something went wrong with sending user "
                                + enteredUsername
                                + " their invite code. Please contact them manually and give them the code " + code);
                        Optional<ButtonType> result = alert.showAndWait();
                    }

                }
                else
                {
                    // User is already invited to group
                    CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Error");
                    alert.setContentText(enteredUsername + " is already invited to this group!");
                    Optional<ButtonType> result = alert.showAndWait();
                }
            }
            else
            {
                // User is already in the group
                CustomAlert alert = new CustomAlert(Alert.AlertType.WARNING);
                alert.setHeaderText("Error");
                alert.setContentText(enteredUsername + " is already in this group!");
                Optional<ButtonType> result = alert.showAndWait();
            }
        }
        catch (Exception e)
        {
            // User does not exist
            CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Username");
            alert.setContentText("The username \"" + enteredUsername + "\" does not exist!");
            Optional<ButtonType> result = alert.showAndWait();
        }
    }

    public void removeUser(ActionEvent event)
    {
        if (!tableViewEditGroup.getSelectionModel().isEmpty())
        {
            if (!tableViewEditGroup.getSelectionModel().getSelectedItem().equals(user))
            {
                //Remove user from group object
                User userToBeRemoved = tableViewEditGroup.getSelectionModel().getSelectedItem();

                user.getProfile().getCurrentGroup().removeUser(userToBeRemoved);
                Group.writeExistingGroupToDatabase(user.getProfile().getCurrentGroup());

                //Remove group id from user
                userToBeRemoved.getProfile().removeGroup(user.getProfile().getCurrentGroup().getGroupID());
                User.writeExistingUserToDatabase(userToBeRemoved);

                tableViewEditGroup.getItems().remove(userToBeRemoved);
                tableViewMembersList.getItems().remove(userToBeRemoved);
            }
            else
            {
                CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR);
                alert.setHeaderText("Invalid Input");
                alert.setContentText("You cant kick yourself from the group");
                alert.showAndWait();
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

    public void joinGoal(ActionEvent event)
    {
        Goal goal = user.getProfile().getCurrentGroup().getGoal();
        boolean check = false;

        //Check if there is a goal for the group
        if (goal != null)
        {
            //Check if the user has already joined the goal
            for (CardioGoal c : user.getProfile().getCardioGoal())
            {
                if (c.equals(goal))
                {
                    check = true;
                    break;
                }
            }
            for (StrengthGoal s : user.getProfile().getStrengthGoal())
            {
                if (s.equals(goal))
                {
                    check = true;
                    break;
                }
            }
            if (!check)
            {
                //Assign the goal to the correct goal list
                if (goal instanceof CardioGoal)
                {
                    user.getProfile().addCardioGoal((CardioGoal) goal);
                }
                else
                {
                    user.getProfile().addStrengthGoal((StrengthGoal) goal);
                }
                CustomAlert alert = new CustomAlert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("Goal joined!");
                alert.setContentText("Goal joined successfully!");
                alert.showAndWait();
            }
            else
            {
                CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error");
                alert.setContentText("You have already joined this goal");
                alert.showAndWait();
            }
        }
        else
        {
            CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("The owner has not set a goal for you");
            alert.showAndWait();
        }
    }

    public void favouriteGroup(ActionEvent event)
    {
        if (user.getProfile().getCurrentGroup().getGroupID() != user.getProfile().getFavouriteGroup())
        {
            user.getProfile().setFavouriteGroup(user.getProfile().getCurrentGroup().getGroupID());

            //Maybe change this, i dont know
            CustomAlert alert = new CustomAlert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Conformation");
            alert.setContentText("You have set this group as your favourite");
            alert.showAndWait();

        }
        else
        {
            CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("You have already selected this group as your favourite");
            alert.showAndWait();
        }
    }
}
