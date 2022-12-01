/*
Name:           DashboardWeightController.java

Authors:        Chris, Conor, Harry, Milo, Yacine

Description:    Class controls the Dashboard weight page. Here, users can
                manage their fitness information, and view a history of their
                past exercises. They can also view and set goals related to
                weight lifting.
*/
package MVC.controller;

import MVC.model.Day;
import MVC.model.Profile;
import MVC.model.User;
import MVC.model.WeightGoal;
import MVC.utils.CustomAlert;
import MVC.utils.InputValidation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DashboardWeightController extends Controller implements Initializable
{
    long start;
    long end;
    @FXML
    private Button buttonDiet;
    @FXML
    private Button buttonHome;
    @FXML
    private Button buttonFitness;
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
    private LineChart lineChartWeightHistory;
    @FXML
    private NumberAxis xAxisDate;
    @FXML
    private DatePicker datePickerSetStartDate;
    @FXML
    private DatePicker datePickerSetEndDate;
    @FXML
    private Label BMIlabel;
    @FXML
    private TextField textFieldCurrentWeight;
    @FXML
    private TextField textFieldTargetWeight;
    @FXML
    private DatePicker datePickerTargetDate;
    @FXML
    private Label labelBMR;
    @FXML
    private Label labelCaloriesNeeded;
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private ObservableList<XYChart.Series<Long, Float>> weightValues;
    private XYChart.Series<Long, Float> weightSeries;
    private XYChart.Series<Long, Float> goalSeries;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        weightValues = FXCollections.observableArrayList();
        weightSeries = new XYChart.Series<>();
        xAxisDate.setAutoRanging(false);
        lineChartWeightHistory.setLegendVisible(false);

        xAxisDate.setTickLabelFormatter(new NumberAxis.DefaultFormatter(xAxisDate)
        {
            @Override
            public String toString(final Number object)
            {
                LocalDate localDate = LocalDate.ofEpochDay(object.longValue());
                return localDate.toString();
            }
        });
    }

    /**
     * Sets daily target calories to the target calories of the user
     */
    private void setDailyTargetCalories()
    {
        Profile p = user.getProfile();
        labelCaloriesNeeded.setText(decimalFormat.format(p.getTargetCalories()));
    }

    public void initUser(User user) throws IOException
    {
        this.user = user;

        labelUsername.setText(user.getUsername());
        labelPoints.setText(String.valueOf(user.getProfile().getPoints().getTotal()));

        setBMI();
        setBMR();
        setDailyTargetCalories();

        //Set the range of the graph
        start = user.getProfile().getDays().get(0).getDate().toEpochDay();
        end = user.getProfile().getCurrentDay().getDate().toEpochDay();

        datePickerSetStartDate.setValue(LocalDate.ofEpochDay(start - 1));
        datePickerSetEndDate.setValue(LocalDate.ofEpochDay(end + 1));

        xAxisDate.setLowerBound(start - 1);
        xAxisDate.setUpperBound(end + 1);
        xAxisDate.setTickUnit(1);

        //Add weights and dates to graph
        for (Day i : this.user.getProfile().getDays())
        {
            if (i.getWeight() != null)
            {
                weightSeries.getData().add(new XYChart.Data<>(i.getDate().toEpochDay(), i.getWeight()));
            }
        }


        weightValues.setAll(weightSeries);
        lineChartWeightHistory.setData(weightValues);

        goalSeries = new XYChart.Series<>();
        if (user.getProfile().getWeightGoal() != null)
        {
            drawWeightGoalLine();
        }

        long daysSinceWeightEntered = LocalDate.now().toEpochDay() - user.getProfile().getDateWeightLastEntered().toEpochDay();

        if (daysSinceWeightEntered >= user.getProfile().getWeightEntryFrequency())
        {
            CustomAlert alert = new CustomAlert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Please enter your current weight!");
            alert.setContentText("It has been " + daysSinceWeightEntered + " days since you entered your weight");
            alert.showAndWait();
        }

        if (user.getProfile().getWeight() != null)
        {
            textFieldCurrentWeight.setText(user.getProfile().getWeight().toString());
        }

        if (user.getProfile().getWeightGoal() != null)
        {
            textFieldTargetWeight.setText(user.getProfile().getWeightGoal().getTarget());
            datePickerTargetDate.setValue(user.getProfile().getWeightGoal().getTargetDate());
        }

        checkIfGoalMet();

        try
        {
            datePickerTargetDate.setPromptText(user.getProfile().getWeightGoal().getTargetDate().toString());
        }
        catch (NullPointerException e)
        {

        }
    }

    /**
     * Checks if the user has met their weight goal
     */
    private void checkIfGoalMet() throws IOException
    {
        if (user.getProfile().getWeightGoal().getTargetWeight() == user.getProfile().getWeight())
        {
            CustomAlert alert = new CustomAlert(Alert.AlertType.INFORMATION);
            alert.setContentText("You've met a weight goal. Set a new one!");
            alert.setHeaderText("Weight Goal Met");
            alert.showAndWait();
        }
    }

    /**
     * Sets the BMI to the users BMI
     */
    private void setBMI()
    {
        BMIlabel.setText("BMI: " + decimalFormat.format(user.getProfile().getBMI()));
    }

    /**
     * Sets the BMR to the users BMR
     */
    private void setBMR()
    {
        labelBMR.setText("BMR: " + decimalFormat.format(user.getProfile().getBMR()));
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

    public void setCustomRange(ActionEvent event)
    {
        if (datePickerSetStartDate.getValue() != null && datePickerSetEndDate.getValue() != null)
        {
            LocalDate startDate = datePickerSetStartDate.getValue();
            LocalDate endDate = datePickerSetEndDate.getValue();

            if (InputValidation.checkDateRanges(startDate, endDate))
            {
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

                drawWeightGoalLine();
            }
            else
            {
                CustomAlert alert = new CustomAlert(Alert.AlertType.WARNING);
                alert.setHeaderText("Invalid Input");
                alert.setContentText("Please enter an end date that is after the start date.");
                alert.showAndWait();
            }
        }
        else
        {
            CustomAlert alert = new CustomAlert(Alert.AlertType.WARNING);
            alert.setHeaderText("Invalid Input");
            alert.setContentText("Please enter a start date and an end date.");
            alert.showAndWait();
        }
    }

    public void setCurrentWeight(ActionEvent event) throws IOException
    {
        if (InputValidation.checkOnlyFloatingPoint(textFieldCurrentWeight.getText()) || InputValidation.checkLength(textFieldCurrentWeight.getText(), 5))
        {
            ArrayList<Day> days = user.getProfile().getDays();
            Float weight = new Float(textFieldCurrentWeight.getText());

            if (days.get(days.size() - 1).getWeight() != null)
            {
                weightSeries.getData().remove(weightSeries.getData().size() - 1);
            }

            days.get(days.size() - 1).setWeight(weight);
            weightSeries.getData().add(new XYChart.Data<>(days.get(days.size() - 1).getDate().toEpochDay(), days.get(days.size() - 1).getWeight()));
            user.getProfile().setDateWeightLastEntered(LocalDate.now());

            setBMI();
        }
        else
        {
            CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Input");
            alert.setContentText("You must enter a correct weight");
            alert.showAndWait();
        }
    }

    public void setWeightGoal(ActionEvent event)
    {
        StringBuilder errorMessage = new StringBuilder();

        if (!InputValidation.checkDateAfterToday(datePickerTargetDate.getValue()))
        {
            errorMessage.append("You must enter a date in the future\n");
        }
        if (!InputValidation.checkOnlyFloatingPoint(textFieldTargetWeight.getText()))
        {
            errorMessage.append("You must enter a valid target weight e.g. 70.0\n");
        }

        if (!(errorMessage.toString().length() > 0))
        {
            WeightGoal weightGoal = new WeightGoal(LocalDate.now(), datePickerTargetDate.getValue(), user.getProfile().getCurrentDay().getWeight(), new Float(textFieldTargetWeight.getText()), null);
            user.getProfile().setWeightGoal(weightGoal);
            drawWeightGoalLine();
        }
        else
        {
            CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Input");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
        }
    }

    /**
     * Adds the weight of the user as a line across the graph
     */
    private void drawWeightGoalLine()
    {
        WeightGoal weightGoal = user.getProfile().getWeightGoal();

        goalSeries = new XYChart.Series<>();
        goalSeries.getData().add(new XYChart.Data<>(new Long(start - 10), weightGoal.getTargetWeight()));
        goalSeries.getData().add(new XYChart.Data<>(new Long(end + 10), weightGoal.getTargetWeight()));

        if (lineChartWeightHistory.getData().size() > 1)
        {
            lineChartWeightHistory.getData().remove(1);
        }

        lineChartWeightHistory.getData().add(goalSeries);
    }
}