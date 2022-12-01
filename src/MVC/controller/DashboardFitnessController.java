/*
Name:           DashboardFitnessController.java

Authors:        Chris, Conor, Harry, Milo, Yacine

Description:    Class controls the Dashboard Fitness screen, which forms a part
                of the main application. Users can update and manage their
                fitness related information through this controller and the
                associated view. They can also set fitness related goals.
*/
package MVC.controller;

import MVC.model.*;
import MVC.utils.CustomAlert;
import MVC.utils.InputValidation;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class DashboardFitnessController extends Controller implements Initializable
{
    long start;
    long end;
    /*********    Navigation buttons           ************/
    @FXML
    private Button buttonDiet;
    @FXML
    private Button buttonHome;
    @FXML
    private Button buttonWeight;
    @FXML
    private Button buttonGroups;
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
    /*********    Pane:  Today           ************/
    @FXML
    private ComboBox<Activity> comboBoxCardioNameToday;
    @FXML
    private TextField textFieldCardioDistanceToday;
    @FXML
    private TextField textFieldCardioTimeToday;
    @FXML
    private ComboBox<Activity> comboBoxStrengthNameToday;
    @FXML
    private TextField textFieldStrengthWeightToday;
    @FXML
    private TextField textFieldStrengthSetsToday;
    @FXML
    private TextField textFieldStrengthRepsToday;
    @FXML
    private TableView tableViewCardioDoneToday;
    @FXML
    private TableColumn<Cardio, String> currentCardioTableName;
    @FXML
    private TableColumn<Cardio, Integer> currentCardioTableDistance;
    @FXML
    private TableColumn<Cardio, Integer> currentCardioTableTime;
    @FXML
    private TableView tableViewStrengthDoneToday;
    @FXML
    private TableColumn<Strength, String> currentStrengthTableName;
    @FXML
    private TableColumn<Strength, Integer> currentStrengthTableWeight;
    @FXML
    private TableColumn<Strength, Integer> currentStrengthTableSets;
    @FXML
    private TableColumn<Strength, Integer> currentStrengthTableReps;
    /*********    Pane:  History           ************/
    @FXML
    private ComboBox<Activity> comboBoxSelectExercise;
    @FXML
    private TableView tableViewAchieved;
    @FXML
    private TableColumn<Achievement, String> tableColumnAchieved;
    @FXML
    private TableColumn<Achievement, LocalDate> tableColumnDate;
    @FXML
    private LineChart lineChartWeightHistory;
    @FXML
    private NumberAxis xAxisDate;
    @FXML
    private DatePicker datePickerStartDate;
    @FXML
    private DatePicker datePickerEndDate;
    private ObservableList<XYChart.Series<Long, Float>> calorieValues;
    private XYChart.Series<Long, Float> calorieSeries;
    /*********    Pane:  Goals           ************/
    @FXML
    private ComboBox<Activity> comboBoxGoalActivityCardio;
    @FXML
    private TextField textFieldStartingDistance;
    @FXML
    private TextField textFieldStartingTime;
    @FXML
    private TextField textFieldTargetDistance;
    @FXML
    private TextField textFieldTargetTime;
    @FXML
    private DatePicker datePickerTargetDateCardio;
    @FXML
    private ComboBox<Activity> comboBoxGoalActivityStrength;
    @FXML
    private TextField textFieldStartingWeight;
    @FXML
    private TextField textFieldStartingSets;
    @FXML
    private TextField textFieldStartingReps;
    @FXML
    private TextField textFieldTargetWeight;
    @FXML
    private TextField textFieldTargetSets;
    @FXML
    private TextField textFieldTargetReps;
    @FXML
    private DatePicker datePickerTargetDateStrength;
    @FXML
    private TableView<Goal> tableViewCurrentGoals;
    @FXML
    private TableColumn<Goal, String> tableColumnGoalActivity;
    @FXML
    private TableColumn<Goal, String> tableColumnGoalStarting;
    @FXML
    private TableColumn<Goal, String> tableColumnGoalTarget;
    @FXML
    private TableColumn<Goal, LocalDate> tableColumnGoalStartDate;
    @FXML
    private TableColumn<Goal, LocalDate> tableColumnGoalTargetDate;

    /*********    Pane:  Custom Exercise           ************/
    @FXML
    private ComboBox comboBoxExerciseType;
    @FXML
    private TextField textFieldName;
    @FXML
    private TextField textFieldCaloriesBurnt;
    @FXML
    private TableView<Activity> tableViewCurrentExercises;
    @FXML
    private TableColumn<Activity, String> tableColumnAddType;
    @FXML
    private TableColumn<Activity, String> tableColumnAddName;
    @FXML
    private TableColumn<Activity, Float> tableColumnAddBurnt;

    private ArrayList<Day> days;

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


        /*********    Pane:  Today           ************/
        //set the cell factory's for cardio table on today
        currentCardioTableName.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getActivity().getExerciseName()));
        currentCardioTableDistance.setCellValueFactory(new PropertyValueFactory<>("distance"));
        currentCardioTableTime.setCellValueFactory(new PropertyValueFactory<>("time"));

        //set the cell factory's for strength table on today
        currentStrengthTableName.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getActivity().getExerciseName()));
        currentStrengthTableWeight.setCellValueFactory(new PropertyValueFactory<>("weight"));
        currentStrengthTableSets.setCellValueFactory(new PropertyValueFactory<>("sets"));
        currentStrengthTableReps.setCellValueFactory(new PropertyValueFactory<>("reps"));

        //Set table place holder labels
        tableViewCardioDoneToday.setPlaceholder(new Label("Start adding your exercises now!"));
        tableViewStrengthDoneToday.setPlaceholder(new Label("Start adding your exercises now!"));

        //set the combo boxes cell factory
        comboBoxCardioNameToday.setButtonCell(cellFactory.call(null));
        comboBoxCardioNameToday.setCellFactory(cellFactory);
        comboBoxStrengthNameToday.setButtonCell(cellFactory.call(null));
        comboBoxStrengthNameToday.setCellFactory(cellFactory);

        /*********    Pane:  History            ************/
        //Set the combo box's cell factory
        comboBoxSelectExercise.setButtonCell(cellFactory.call(null));
        comboBoxSelectExercise.setCellFactory(cellFactory);
        //set place holder labels
        tableViewAchieved.setPlaceholder(new Label("Select your exercise!"));

        //set the achieved tables cell factory's
        tableColumnAchieved.setCellValueFactory(new PropertyValueFactory<>("exercise"));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        //set graph properties
        lineChartWeightHistory.setAnimated(false);
        calorieValues = FXCollections.observableArrayList();
        calorieSeries = new XYChart.Series<>();
        xAxisDate.setAutoRanging(false);
        lineChartWeightHistory.setLegendVisible(false);

        //set the labels to display a date
        xAxisDate.setTickLabelFormatter(new NumberAxis.DefaultFormatter(xAxisDate)
        {
            @Override
            public String toString(final Number object)
            {
                LocalDate localDate = LocalDate.ofEpochDay(object.longValue());
                return localDate.toString();
            }
        });

        /*********    Pane:  Goals              ************/

        //set current goal table cell factories
        tableViewCurrentGoals.setPlaceholder(new Label("Add goals now!"));
        tableColumnGoalActivity.setCellValueFactory(new PropertyValueFactory<>("activity"));
        tableColumnGoalStarting.setCellValueFactory(new PropertyValueFactory<>("start"));
        tableColumnGoalTarget.setCellValueFactory(new PropertyValueFactory<>("target"));
        tableColumnGoalStartDate.setCellValueFactory(new PropertyValueFactory<>("setDate"));
        tableColumnGoalTargetDate.setCellValueFactory(new PropertyValueFactory<>("targetDate"));

        //set combo boxes cell factories
        comboBoxGoalActivityCardio.setButtonCell(cellFactory.call(null));
        comboBoxGoalActivityCardio.setCellFactory(cellFactory);
        comboBoxGoalActivityStrength.setButtonCell(cellFactory.call(null));
        comboBoxGoalActivityStrength.setCellFactory(cellFactory);


        /*********    Pane:  Custom Exercise    ************/
        //set place holder label
        tableViewCurrentExercises.setPlaceholder(new Label("You have no exercises"));
        //set cell factories for custom exercise
        tableColumnAddType.setCellValueFactory(new PropertyValueFactory<>("exerciseType"));
        tableColumnAddName.setCellValueFactory(new PropertyValueFactory<>("exerciseName"));
        tableColumnAddBurnt.setCellValueFactory(new PropertyValueFactory<>("calories"));
        //add exercise types to combo box
        comboBoxExerciseType.getItems().add("Cardio");
        comboBoxExerciseType.getItems().add("Strength");
    }

    @Override
    public void initUser(User user)
    {
        this.user = user;

        labelUsername.setText(user.getUsername());
        labelPoints.setText(String.valueOf(user.getProfile().getPoints().getTotal()));

        days = user.getProfile().getDays();
        /*********    Pane:  Today              ************/
        addDataToTodayTable();

        //load data into all combo boxes
        for (Activity c : user.getProfile().getActivity())
        {
            if (c.getExerciseType().equals("Cardio"))
            {
                comboBoxCardioNameToday.getItems().add(c);
                /**add to goal combo box as well**/
                comboBoxGoalActivityCardio.getItems().add(c);
            }
            else
            {
                comboBoxStrengthNameToday.getItems().add(c);
                /**add to goal combo box as well**/
                comboBoxGoalActivityStrength.getItems().add(c);
            }
            /**add to history combo box as well**/
            comboBoxSelectExercise.getItems().add(c);
            /**add to Custom Exercise table as well**/
            tableViewCurrentExercises.getItems().add(c);
        }

        /*********    Pane:  History              **********/

        //set graph data and range
        lineChartWeightHistory.setData(calorieValues);
        start = user.getProfile().getDays().get(0).getDate().toEpochDay();
        end = user.getProfile().getCurrentDay().getDate().toEpochDay();

        //Set the range of the graph
        xAxisDate.setLowerBound(start - 1);
        xAxisDate.setUpperBound(end + 1);
        xAxisDate.setTickUnit(1);
        datePickerStartDate.setValue(LocalDate.ofEpochDay(start - 1));
        datePickerEndDate.setValue(LocalDate.ofEpochDay(end + 1));

        //display fitness alerts
        ArrayList<Goal> expiredGoal = new ArrayList<>();

        for (Goal cGoal : user.getProfile().getCardioGoal())
        {
            cGoal.getTarget();
            if (cGoal.getTargetDate().isBefore(LocalDate.now()))
            {
                expiredGoal.add(cGoal);
            }
        }

        for (Goal sGoal : user.getProfile().getStrengthGoal())
        {
            if (sGoal.getTargetDate().isBefore(LocalDate.now()))
            {
                expiredGoal.add(sGoal);
            }
        }

        if (expiredGoal.size() > 0)
        {
            CustomAlert alert = new CustomAlert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Fitness Goal Expired");
            alert.setContentText("One (or more) of your fitness goals has expired. Do you want to remove all overdue goals?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK)
            {
                user.getProfile().getCardioGoal().removeAll(expiredGoal);
            }
        }

        /*********    Pane:  Goal              ************/
        updateCurrentGoalTable();

        /*********    Pane:  Custom Exercise              ************/


    }

    /**
     * Update values in goal table
     */
    private void updateCurrentGoalTable()
    {
        tableViewCurrentGoals.getItems().clear();
        for (Goal g : user.getProfile().getCardioGoal())
        {
            tableViewCurrentGoals.getItems().add(g);
        }
        for (Goal g : user.getProfile().getStrengthGoal())
        {
            tableViewCurrentGoals.getItems().add(g);
        }
    }

    /*********    Pane:  Today              ************/
    public void addCardio(ActionEvent event)
    {
        //input validation
        StringBuilder errorMessage = new StringBuilder();
        if (comboBoxCardioNameToday.getSelectionModel().isEmpty())
        {
            errorMessage.append("You must enter a activity\n");
        }
        if (!InputValidation.checkOnlyNumbers(textFieldCardioDistanceToday.getText()))
        {
            errorMessage.append("You must enter a valid distance\n");
        }
        if (!InputValidation.checkOnlyNumbers(textFieldCardioTimeToday.getText()))
        {
            errorMessage.append("You must enter a valid time in minutes\n");
        }
        if (!(errorMessage.toString().length() > 0))
        {
            //create cardio object and add to table
            Activity exercise = comboBoxCardioNameToday.getValue();
            Integer distance = new Integer(textFieldCardioDistanceToday.getText());
            Integer time = new Integer(textFieldCardioTimeToday.getText());
            Cardio newCardio = new Cardio(exercise, distance, time);
            user.getProfile().getCurrentDay().addExercise(newCardio);
            addDataToTodayTable();
        }
        else
        {
            //show alert on invalid input
            CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Input");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
        }
    }

    public void addStrength(ActionEvent event)
    {
        //input validation
        StringBuilder errorMessage = new StringBuilder();
        if (comboBoxStrengthNameToday.getSelectionModel().isEmpty())
        {
            errorMessage.append("You must enter a activity\n");
        }
        if (!InputValidation.checkOnlyNumbers(textFieldStrengthWeightToday.getText()))
        {
            errorMessage.append("You must enter a valid weight\n");
        }
        if (!InputValidation.checkOnlyNumbers(textFieldStrengthSetsToday.getText()))
        {
            errorMessage.append("You must enter a valid number of sets\n");
        }
        if (!InputValidation.checkOnlyNumbers(textFieldStrengthRepsToday.getText()))
        {
            errorMessage.append("You must enter a valid number of reps\n");
        }
        if (!(errorMessage.toString().length() > 0))
        {
            //create strength object and add to table
            Activity exercise = comboBoxStrengthNameToday.getValue();
            Integer weight = new Integer(textFieldStrengthWeightToday.getText());
            Integer sets = new Integer(textFieldStrengthSetsToday.getText());
            Integer reps = new Integer(textFieldStrengthRepsToday.getText());
            Strength newStrength = new Strength(exercise, weight, sets, reps);
            user.getProfile().getCurrentDay().addExercise(newStrength);
            addDataToTodayTable();
        }
        else
        {
            //show alert on invalid input
            CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Input");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
        }
    }

    public void removeCardio(ActionEvent event)
    {
        //remove cardio object from table and user
        Cardio cardio = (Cardio) tableViewCardioDoneToday.getSelectionModel().getSelectedItem();
        user.getProfile().getCurrentDay().getExercises().remove(cardio);
        addDataToTodayTable();
    }

    public void removeStrength(ActionEvent event)
    {
        //remove strength object from table and user
        Strength strength = (Strength) tableViewStrengthDoneToday.getSelectionModel().getSelectedItem();
        user.getProfile().getCurrentDay().getExercises().remove(strength);
        addDataToTodayTable();
    }

    /**
     * Updates fitness done today table
     */
    private void addDataToTodayTable()
    {
        //clears data from table so they can be updated
        ArrayList<Day> days = user.getProfile().getDays();
        tableViewCardioDoneToday.getItems().clear();
        tableViewStrengthDoneToday.getItems().clear();

        //add the data to the tables with the new values
        for (Exercise exercise : days.get(days.size() - 1).getExercises())
        {
            if (exercise.getClass() == Cardio.class)
            {
                tableViewCardioDoneToday.getItems().add(exercise);
            }
            else
            {
                tableViewStrengthDoneToday.getItems().add(exercise);
            }
        }
    }

    /*********    Pane:  History              ************/
    public void selectExercise(ActionEvent event)
    {
        //clear data from the table and graph
        String exerciseName = comboBoxSelectExercise.getValue().getExerciseName();
        tableViewAchieved.getItems().clear();
        calorieSeries.getData().clear();
        calorieValues.clear();

        //add data to table
        for (Day d : days)
        {
            float totalCal = 0;
            for (Exercise e : d.getExercises())
            {
                if (e.getActivity().getExerciseName().equals(exerciseName))
                {
                    tableViewAchieved.getItems().add(new Achievement(e, d.getDate()));

                    // 0.0333333333 is used to convert the calories burnt from 30 minutes to 1 minute,
                    // multiplied by how many minutes they do the exercise for
                    totalCal += e.getActivity().getCalories() * e.getAmount() * 0.0333333333;
                }
            }
            if (totalCal > 0)
            {
                calorieSeries.getData().add(new XYChart.Data<>(d.getDate().toEpochDay(), totalCal));
            }
        }
        calorieValues.addAll(calorieSeries);
    }

    public void setCustomRange(ActionEvent event) throws IOException
    {
        //Checking input in date pickers is non null
        if (datePickerStartDate.getValue() != null && datePickerEndDate.getValue() != null)
        {
            LocalDate startDate = datePickerStartDate.getValue();
            LocalDate endDate = datePickerEndDate.getValue();

            //checking if the dates are in order
            if (InputValidation.checkDateRanges(startDate, endDate))
            {
                //set date range
                start = startDate.toEpochDay();
                end = endDate.toEpochDay();

                xAxisDate.setLowerBound(start - 1);
                xAxisDate.setUpperBound(end + 1);
                if ((end - start) > 12)
                {
                    xAxisDate.setTickUnit((end - start) / 12);
                }
                else
                {
                    xAxisDate.setTickUnit(1);
                }
            }
            else
            {
                //display alerts
                CustomAlert alert = new CustomAlert(Alert.AlertType.WARNING);
                alert.setHeaderText("Please enter an end date that is after the start date.");
                alert.showAndWait();
            }
        }
        else
        {
            CustomAlert alert = new CustomAlert(Alert.AlertType.WARNING);
            alert.setHeaderText("Please enter a start date and an end date.");
            alert.showAndWait();
        }
    }

    /*********    Pane:  Goals              ************/

    public void addCardioGoal(ActionEvent event)
    {
        //input validation
        StringBuilder errorMessage = new StringBuilder();
        if (comboBoxGoalActivityCardio.getSelectionModel().isEmpty())
        {
            errorMessage.append("You must enter a activity\n");
        }
        if (!InputValidation.checkOnlyNumbers(textFieldStartingDistance.getText()))
        {
            errorMessage.append("You must enter a valid distance\n");
        }
        if (!InputValidation.checkOnlyNumbers(textFieldTargetDistance.getText()))
        {
            errorMessage.append("You must enter a valid number of target distance\n");
        }
        if (!InputValidation.checkOnlyNumbers(textFieldStartingTime.getText()))
        {
            errorMessage.append("You must enter a time\n");
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
            int startDistance = new Integer(textFieldStartingDistance.getText());
            int targetDistance = new Integer(textFieldTargetDistance.getText());
            int startTime = new Integer(textFieldStartingTime.getText());
            int targetTime = new Integer(textFieldTargetTime.getText());
            LocalDate targetDate = datePickerTargetDateCardio.getValue();
            CardioGoal cardioGoal = new CardioGoal(days.get(days.size() - 1).getDate(), targetDate, activity, startDistance, startTime, targetDistance, targetTime, null);
            user.getProfile().addCardioGoal(cardioGoal);
            User.writeExistingUserToDatabase(this.user);
            updateCurrentGoalTable();
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
        if (!InputValidation.checkOnlyNumbers(textFieldStartingWeight.getText()))
        {
            errorMessage.append("You must enter a valid weight\n");
        }
        if (!InputValidation.checkOnlyNumbers(textFieldTargetWeight.getText()))
        {
            errorMessage.append("You must enter a valid number of target weight\n");
        }
        if (!InputValidation.checkOnlyNumbers(textFieldStartingSets.getText()))
        {
            errorMessage.append("You must enter a valid number sets\n");
        }
        if (!InputValidation.checkOnlyNumbers(textFieldTargetReps.getText()))
        {
            errorMessage.append("You must enter a valid number of target sets\n");
        }
        if (!InputValidation.checkOnlyNumbers(textFieldStartingReps.getText()))
        {
            errorMessage.append("You must enter a valid number of reps\n");
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
            int startWeight = new Integer(textFieldStartingWeight.getText());
            int targetWeight = new Integer(textFieldTargetWeight.getText());
            int startSets = new Integer(textFieldStartingSets.getText());
            int targetSets = new Integer(textFieldTargetSets.getText());
            int startReps = new Integer(textFieldStartingReps.getText());
            int targetReps = new Integer(textFieldTargetReps.getText());
            LocalDate targetDate = datePickerTargetDateStrength.getValue();
            StrengthGoal strengthGoal = new StrengthGoal(days.get(days.size() - 1).getDate(), targetDate, activity, startWeight, startReps, startSets, targetWeight, targetReps, targetSets, null);
            user.getProfile().addStrengthGoal(strengthGoal);
            updateCurrentGoalTable();
        }
        else
        {
            CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Input");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
        }
    }

    public void removeExerciseGoal(ActionEvent event)
    {
        if (!tableViewCurrentGoals.getSelectionModel().isEmpty())
        {
            Goal goal = tableViewCurrentGoals.getSelectionModel().getSelectedItem();
            if (goal.getClass() == CardioGoal.class)
            {
                user.getProfile().removeCardioGoal(goal);
            }
            else
            {
                user.getProfile().removeStrengthGoal(goal);
            }
            CustomAlert alert = new CustomAlert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Remove Goal?");
            alert.setContentText("Would you like to permanently remove this goal?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK)
            {
                tableViewCurrentGoals.getItems().remove(goal);
                User.writeExistingUserToDatabase(this.user);
            }
        }
        else
        {
            CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Input");
            alert.setContentText("You must select a row to remove");
            alert.showAndWait();
        }
    }

    public void completeExerciseGoal(ActionEvent event)
    {
        if (!tableViewCurrentGoals.getSelectionModel().isEmpty())
        {
            Goal goal = tableViewCurrentGoals.getSelectionModel().getSelectedItem();
            if (goal.getClass() == CardioGoal.class)
            {
                user.getProfile().removeCardioGoal(goal);
            }
            else
            {
                user.getProfile().removeStrengthGoal(goal);
            }
            CustomAlert alert = new CustomAlert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Goal Complete?");
            alert.setContentText("Would you like to set that goal to be complete?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK)
            {
                user.getProfile().goalCompletePoints();
                tableViewCurrentGoals.getItems().remove(goal);
                User.writeExistingUserToDatabase(this.user);
            }
        }
        else
        {
            CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Input");
            alert.setContentText("You must select a row to remove");
            alert.showAndWait();
        }
    }

    /*********    Pane:  custom              ************/

    public void addExercise(ActionEvent event)
    {
        //input validation
        StringBuilder errorMessage = new StringBuilder();
        if (comboBoxExerciseType.getSelectionModel().isEmpty())
        {
            errorMessage.append("You must enter a exercise type\n");
        }
        if (!InputValidation.checkOnlyLetters(textFieldName.getText()) || !InputValidation.checkLength(textFieldName.getText(), 50))
        {
            errorMessage.append("You must enter a valid name\n");
        }
        if (!InputValidation.checkOnlyNumbers(textFieldCaloriesBurnt.getText()))
        {
            errorMessage.append("You must enter a valid number of calories per minute\n");
        }
        if (!(errorMessage.toString().length() > 0))
        {
            //Add custom exercise to table and user
            String type = comboBoxExerciseType.getValue().toString().toUpperCase();
            String exercise = textFieldName.getText();
            Float caloriesBurnt = new Float(textFieldCaloriesBurnt.getText());
            Activity activity = new Activity(exercise, caloriesBurnt, type);
            if (activity.getExerciseType().equals("cardio"))
            {
                comboBoxCardioNameToday.getItems().add(activity);
                comboBoxGoalActivityCardio.getItems().add(activity);
            }
            else
            {
                comboBoxStrengthNameToday.getItems().add(activity);
                comboBoxGoalActivityStrength.getItems().add(activity);
            }
            comboBoxSelectExercise.getItems().add(activity);
            tableViewCurrentExercises.getItems().add(activity);
            user.getProfile().addActivity(activity);
        }
        else
        {
            CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Input");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
        }
    }

    public void removeExercise(ActionEvent event)
    {
        //input validation
        if (!tableViewCurrentExercises.getSelectionModel().isEmpty())
        {
            //remove custom exercise from table and combo boxes
            Activity activity = tableViewCurrentExercises.getSelectionModel().getSelectedItem();
            if (activity.getExerciseType().equals("cardio"))
            {
                comboBoxCardioNameToday.getItems().remove(activity);
                comboBoxGoalActivityCardio.getItems().remove(activity);
            }
            else
            {
                comboBoxStrengthNameToday.getItems().remove(activity);
                comboBoxGoalActivityStrength.getItems().remove(activity);
            }
            comboBoxSelectExercise.getItems().remove(activity);
            tableViewCurrentExercises.getItems().remove(activity);
            user.getProfile().removeActivity(activity);
        }
        else
        {
            CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Input");
            alert.setContentText("You must select an exercise to remove");
            alert.showAndWait();
        }
    }

    //wrapper class load data into the Achievement table
    public class Achievement
    {
        public Exercise exercise;
        public LocalDate date;

        public Achievement(Exercise e, LocalDate d)
        {
            exercise = e;
            date = d;
        }

        public String getExercise()
        {
            return exercise.getAchieved();
        }

        public LocalDate getDate()
        {
            return date;
        }
    }
}
