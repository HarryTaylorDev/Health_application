/*
Name:           DashboardHomeController.java

Authors:        Chris, Conor, Harry, Milo, Yacine

Description:    Class controls the main dashboard home screen. Here, users can
                see an overview of their health information, including a quick
                summary of their calories consumed and exercise done in the
                relevant day.
*/
package MVC.controller;

import MVC.model.*;
import MVC.utils.InputValidation;
import MVC.utils.StylingHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class DashboardHomeController extends Controller implements Initializable
{
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
    private Button buttonHelp;
    @FXML
    private Label labelUsername;
    @FXML
    private Label labelPoints;


    @FXML
    private Label labelMaintenanceCalories;
    @FXML
    private Label labelTargetCalories;
    @FXML
    private Label labelCaloriesEatenToday;
    @FXML
    private Label labelExerciseOfTheWeek;
    @FXML
    private Label labelCaloriesBurntThisWeek;
    @FXML
    private Label labelCardioTimeThisWeek;
    @FXML
    private Label labelCurrentWeight;
    @FXML
    private Label labelStartWeight;
    @FXML
    private Label labelTargetWeight;
    @FXML
    private Label labelTargetWeightDate;
    @FXML
    private ProgressBar progressBarWeightTarget;
    @FXML
    private ProgressBar progressBarCaloriesToday;
    @FXML
    private Label labelNetCaloriesGained;
    @FXML
    private Label labelGroupName;
    @FXML
    private Label labelTotalPoints;
    @FXML
    private Label labelAvgPoints;
    @FXML
    private Label labelHighestPoints;
    @FXML
    private Label labelDaysLeft;
    @FXML
    private ProgressBar progressBarDaysLeft;

    public void buttonMenuClicked(ActionEvent event) throws IOException, SQLException
    {
        StringBuilder path = new StringBuilder("../view/");
        if (event.getSource() == buttonDiet)
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

    }

    public void initUser(User user) throws SQLException
    {
        this.user = user;

        labelUsername.setText(user.getUsername());
        labelPoints.setText(String.valueOf(user.getProfile().getPoints().getTotal()));

        //user.getProfile().getDays().remove(0);
        // page stuff
        if (user.getProfile().getFavouriteGroup() != null)
        {
            //Load data from DB
            Group favGroup = Group.loadGroupFromDatabase(user.getProfile().getFavouriteGroup());
            ArrayList<User> userList = new ArrayList<>();
            for (String s : favGroup.getUserList())
            {
                userList.add(User.loadFromDatabase(s));
            }

            //Set name
            labelGroupName.setText(favGroup.getName());

            //Set total points
            int pointsTotal = 0;
            int pointsHighest = 0;
            for (User u : userList)
            {
                if (user.getProfile().getPoints().getTotal() > pointsHighest)
                {
                    pointsHighest = user.getProfile().getPoints().getTotal();
                }
                pointsTotal += u.getProfile().getPoints().getTotal();
            }
            labelTotalPoints.setText("" + pointsTotal);

            //Set avg points
            labelAvgPoints.setText("" + (pointsTotal / userList.size()));

            //set highest points
            labelHighestPoints.setText("" + pointsHighest);

            if (favGroup.getGoal() != null)
            {
                long totalDays = InputValidation.calculateNumberOfDaysBetweenDates(LocalDate.now(), favGroup.getGoal().getTargetDate());
                labelDaysLeft.setText(String.valueOf(totalDays));
            }
            else
            {
                labelDaysLeft.setText("N/A");
            }

            // progress bar
            if (favGroup.getGoal() != null)
            {
                long totalDays = InputValidation.calculateNumberOfDaysBetweenDates(favGroup.getGoal().getSetDate(), favGroup.getGoal().getTargetDate());
                long currentDaysLeft = InputValidation.calculateNumberOfDaysBetweenDates(LocalDate.now(), favGroup.getGoal().getTargetDate());
                double percentage = (double) currentDaysLeft / (double) totalDays;

                StylingHelper.setProgressBarProgressAndColour(progressBarDaysLeft, percentage);
            }
        }
        else
        {
            labelGroupName.setText("You have not set a favourite group");
            labelTotalPoints.setText("");
            labelAvgPoints.setText("");
            labelHighestPoints.setText("");
            labelDaysLeft.setText("");

        }

        labelTotalPoints.setText("" + user.getProfile().getPoints().getTotal());
        int calories = 0;
        for (Exercise e : user.getProfile().getCurrentDay().getExercises())
        {
            calories += e.getActivity().getCalories();
        }

        int gained = user.getProfile().getCurrentDay().getTotalCalories().intValue();
        double mCal = user.getProfile().getMaintenanceCalories();

        labelNetCaloriesGained.setText("" + (gained - mCal - calories));

        setDietCaloriesMaintenance();
        setDietCaloriesNeededForTarget();
        setDietCaloriesEatenToday();
        setProgressBarCaloriesToday();
        user.getProfile().caloriesEatenPoints(user.getProfile().getCurrentDay().getTotalCalories().intValue(), user.getProfile().getTargetCalories().intValue());

        if (user.getProfile().getWeightGoal().getTargetWeight().equals(user.getProfile().getWeight()))
        {
            user.getProfile().goalCompletePoints();
        }

        setWeightLabel();
        setWeightTargetLabels();
        setProgressBarWeight();

        setFitnessWeekStuff();
    }

    /**
     * Sets the weight progress bar
     */
    private void setProgressBarWeight()
    {
        Profile p = user.getProfile();
        float startWeight = p.getWeightGoal().getCurrentWeight();
        float targetWeight = p.getWeightGoal().getTargetWeight();
        float currentWeight = p.getWeight();

        float difference;
        float progress;
        float percentageComplete;

        // loosing weight
        if (startWeight > targetWeight)
        {
            difference = startWeight - targetWeight;
            progress = startWeight - currentWeight;
            percentageComplete = progress / difference;

            StylingHelper.setProgressBarProgressAndColour(progressBarWeightTarget, percentageComplete);
        }
        // gaining weight
        else if (startWeight < targetWeight)
        {
            difference = targetWeight - startWeight;
            progress = currentWeight - startWeight;
            percentageComplete = progress / difference;

            StylingHelper.setProgressBarProgressAndColour(progressBarWeightTarget, percentageComplete);
        }
        else
        {
            StylingHelper.setProgressBarProgressAndColour(progressBarWeightTarget, 1);
        }
    }

    /**
     * Sets the calories today progress bar
     */
    private void setProgressBarCaloriesToday()
    {
        Profile p = user.getProfile();

        Float target = p.getTargetCalories();
        Float caloriesEaten = p.getCurrentDay().getTotalCalories();
        float percentage = caloriesEaten / target;

        if (percentage < 1.0)
        {
            progressBarCaloriesToday.setProgress(percentage);
            progressBarCaloriesToday.setStyle("-fx-accent: green");
        }
        else if (percentage >= 1.0)
        {
            progressBarCaloriesToday.setProgress(1.0);
            progressBarCaloriesToday.setStyle("-fx-accent: red");
        }
    }

    /**
     * Function to set the maintenance calories for the user
     */
    private void setDietCaloriesMaintenance()
    {
        Profile p = user.getProfile();
        labelMaintenanceCalories.setText(String.valueOf((int) p.getMaintenanceCalories()));
    }

    /**
     * Function to set the calories needed to meet their target for the user
     */
    private void setDietCaloriesNeededForTarget()
    {
        Profile p = user.getProfile();

        if (p.getTargetCalories() != null)
        {
            float targetCalories = p.getTargetCalories();
            labelTargetCalories.setText(String.valueOf((int) targetCalories));
        }
        else
        {
            labelTargetCalories.setText("You are your target weight, set a new one!");
        }
    }

    /**
     * Function to set the calories eaten today for the user
     */
    private void setDietCaloriesEatenToday()
    {
        Profile p = user.getProfile();
        int calories = p.getCurrentDay().getTotalCalories().intValue();
        labelCaloriesEatenToday.setText(String.valueOf(calories));
    }

    /**
     * Function to update the weekly fitness information statistics
     */
    private void setFitnessWeekStuff()
    {
        Profile p = user.getProfile();
        ArrayList<Day> week = p.getWeek();
        HashMap<String, Integer> activityCount = new HashMap<>();
        float calories = 0;
        int timeCardio = 0;

        for (Day d : week)
        {
            for (Exercise e : d.getExercises())
            {
                String act = e.getActivity().getExerciseName();
                calories += e.getActivity().getCalories();

                if (e.getClass() == Cardio.class)
                {
                    timeCardio += ((Cardio) e).getTime();
                }

                if (activityCount.containsKey(act))
                {
                    activityCount.put(act, activityCount.get(act) + 1);
                }
                else
                {
                    activityCount.put(act, 1);
                }
            }
        }

        // favourite exercise of the week
        String exercise = "Not exercised yet!";
        int max = 0;

        for (Map.Entry<String, Integer> entry : activityCount.entrySet())
        {
            if (entry.getValue() > max)
            {
                max = entry.getValue();
                exercise = entry.getKey();
            }
        }

        labelExerciseOfTheWeek.setText(exercise);
        labelCaloriesBurntThisWeek.setText(String.valueOf((int) calories));
        labelCardioTimeThisWeek.setText(timeCardio + " minutes");
    }

    /**
     * Sets the weight label to the weight of the user
     */
    private void setWeightLabel()
    {
        if (user.getProfile().getWeight() != null)
        {
            labelCurrentWeight.setText(user.getProfile().getWeight().toString() + " kg");
        }
        else
        {
            labelCurrentWeight.setText("Set Weight Now!");
        }
    }

    /**
     * Sets the weight target to the target weight of the user
     */
    private void setWeightTargetLabels()
    {
        if (user.getProfile().getWeightGoal() == null)
        {
            labelTargetWeight.setText("Not Set!");
            labelTargetWeight.setText("Not Set!");
        }
        else
        {
            WeightGoal w = user.getProfile().getWeightGoal();

            if (w.getTargetWeight() != null)
            {
                labelTargetWeight.setText(w.getTargetWeight().toString() + " kg");
            }

            if (w.getCurrentWeight() != null)
            {
                labelStartWeight.setText(w.getCurrentWeight().toString() + " kg");
            }

            if (w.getTargetDate() != null)
            {
                labelTargetWeightDate.setText(StylingHelper.formatDate(w.getTargetDate()));
            }
        }
    }
}