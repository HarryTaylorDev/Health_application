/*
Name:           DashboardDietController.java

Authors:        Chris, Conor, Harry, Milo, Yacine

Description:    Class controls the Dashboard diet screen, which forms a part
                of the main application. Users can update and manage their
                diet related information through this controller and the
                associated view.
*/
package MVC.controller;

import MVC.model.*;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.ResourceBundle;

public class DashboardDietController extends Controller implements Initializable
{
    long start;
    long end;
    /*********    Pane:  Today              ************/
    Meal meal;
    /*********    Pane:  Side buttons              ************/
    @FXML
    private Button buttonHome;
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
    /*********    Pane:  Today              ************/
    @FXML
    private Button buttonAddFood;
    @FXML
    private Button buttonAddDrink;
    @FXML
    private ComboBox<String> comboBoxMealType;
    @FXML
    private ComboBox<FoodItem> comboBoxFood;
    @FXML
    private ComboBox<FoodItem> comboBoxDrink;
    @FXML
    private TableView<FoodItem> tableViewCurrentMeal;
    @FXML
    private TableColumn<FoodItem, String> tableColumnMealName;
    @FXML
    private TableColumn<FoodItem, Float> tableColumnMealCalories;
    @FXML
    private TableView<Meal> tableViewMealsToday;
    @FXML
    private TableColumn<Meal, String> tableColumnToadyMealType;
    @FXML
    private TableColumn<Meal, String> tableColumnToadyMealContents;
    @FXML
    private TableColumn<Meal, Float> tableColumnToadyMealTypeCalories;
    @FXML
    private ProgressBar progressBarCalories;
    @FXML
    private Label labelCaloriesTodayTarget;
    /*********    Pane:  History              ************/
    @FXML
    private TableView<Day> tableViewWeight;
    @FXML
    private TableColumn<Day, LocalDate> tableColumnDate;
    @FXML
    private TableColumn<Day, Float> tableColumnWeight;
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
    /*********    Pane:  Custom              ************/
    @FXML
    private ComboBox comboBoxCustomMealType;
    @FXML
    private TextField textFieldCustomName;
    @FXML
    private TextField textFieldCustomCalories;
    @FXML
    private TableView<FoodItem> tableViewCurrentFoodAndDrink;
    @FXML
    private TableColumn<FoodItem, String> tableColumnCurrentFoodDrinkType;
    @FXML
    private TableColumn<FoodItem, String> tableColumnCurrentFoodDrinkName;
    @FXML
    private TableColumn<FoodItem, Float> tableColumnCurrentFoodDrinkCalories;

    public void buttonMenuClicked(ActionEvent event) throws IOException, SQLException
    {
        StringBuilder path = new StringBuilder("../view/");

        if (event.getSource() == buttonHome)
        {
            path.append("dashboardHome.fxml");
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
        /*********    Pane:  Today              ************/
        Callback<ListView<FoodItem>, ListCell<FoodItem>> cellFactory = new Callback<ListView<FoodItem>, ListCell<FoodItem>>()
        {
            @Override
            public ListCell<FoodItem> call(ListView<FoodItem> l)
            {
                return new ListCell<FoodItem>()
                {
                    @Override
                    protected void updateItem(FoodItem item, boolean empty)
                    {
                        super.updateItem(item, empty);
                        if (item == null || empty)
                        {
                            setGraphic(null);
                        }
                        else
                        {
                            setText(item.getName());
                        }
                    }
                };
            }
        };

        comboBoxFood.setButtonCell(cellFactory.call(null));
        comboBoxFood.setCellFactory(cellFactory);
        comboBoxDrink.setButtonCell(cellFactory.call(null));
        comboBoxDrink.setCellFactory(cellFactory);

        tableViewCurrentMeal.setPlaceholder(new Label("Add food and drink now!"));
        tableColumnMealName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnMealCalories.setCellValueFactory(new PropertyValueFactory<>("Calories"));

        tableViewMealsToday.setPlaceholder(new Label("Add meals now!"));
        tableColumnToadyMealType.setCellValueFactory(new PropertyValueFactory<>("type"));
        tableColumnToadyMealContents.setCellValueFactory(new PropertyValueFactory<>("contents"));
        tableColumnToadyMealTypeCalories.setCellValueFactory(new PropertyValueFactory<>("calories"));

        comboBoxMealType.getItems().add("Breakfast");
        comboBoxMealType.getItems().add("Lunch");
        comboBoxMealType.getItems().add("Dinner");
        comboBoxMealType.getItems().add("Snacks");

        buttonAddFood.setVisible(false);
        buttonAddDrink.setVisible(false);


        /*********    Pane:  History              ************/
        tableViewWeight.setPlaceholder(new Label("Add meals now!"));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<>("date"));            // CHANGE TO DD/MM/YYYY
        tableColumnWeight.setCellValueFactory(new PropertyValueFactory<>("totalCalories"));

        calorieValues = FXCollections.observableArrayList();
        calorieSeries = new XYChart.Series<>();
        xAxisDate.setAutoRanging(false);
        lineChartWeightHistory.setLegendVisible(false);
        lineChartWeightHistory.setAnimated(false);
        xAxisDate.setTickLabelFormatter(new NumberAxis.DefaultFormatter(xAxisDate)
        {
            @Override
            public String toString(final Number object)
            {
                LocalDate localDate = LocalDate.ofEpochDay(object.longValue());
                return localDate.toString();
            }
        });

        /*********    Pane:  Custom              ************/
        tableViewCurrentFoodAndDrink.setPlaceholder(new Label("You have no food and drink!"));
        tableColumnCurrentFoodDrinkType.setCellValueFactory(new PropertyValueFactory<>("foodType"));
        tableColumnCurrentFoodDrinkName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnCurrentFoodDrinkCalories.setCellValueFactory(new PropertyValueFactory<>("calories"));

        comboBoxCustomMealType.getItems().add("Food");
        comboBoxCustomMealType.getItems().add("Drink");
    }

    @Override
    public void initUser(User user)
    {
        this.user = user;

        labelUsername.setText(user.getUsername());
        labelPoints.setText(String.valueOf(user.getProfile().getPoints().getTotal()));

        /*********    Pane:  Today              ************/
        Collections.sort(user.getProfile().getFoods());
        for (FoodItem f : user.getProfile().getFoods())
        {
            if (f.getFoodType().equals("Food"))
            {
                comboBoxFood.getItems().add(f);
            }
            else
            {
                comboBoxDrink.getItems().add(f);
            }
        }

        tableViewMealsToday.getItems().setAll(user.getProfile().getCurrentDay().getMeals());

        setProgressBarCalories();
        setCaloriesTodayTarget();

        /*********    Pane:  History              ************/
        for (Day d : user.getProfile().getDays())
        {
            if (d.getTotalCalories() > 0)
            {
                tableViewWeight.getItems().add(d);
            }
        }

        //Set the range of the graph
        start = user.getProfile().getDays().get(0).getDate().toEpochDay();
        end = user.getProfile().getCurrentDay().getDate().toEpochDay();
        datePickerStartDate.setValue(LocalDate.ofEpochDay(start - 1));
        datePickerEndDate.setValue(LocalDate.ofEpochDay(end + 1));

        xAxisDate.setLowerBound(start - 1);
        xAxisDate.setUpperBound(end + 1);
        xAxisDate.setTickUnit(1);

        lineChartWeightHistory.setData(calorieValues);
        updateCalorieGraph();
        /*********    Pane:  Custom              ************/
        tableViewCurrentFoodAndDrink.getItems().addAll(user.getProfile().getFoods());

    }

    public void setTypeMealToday(ActionEvent event)
    {
        if (!comboBoxMealType.getSelectionModel().isEmpty())
        {
            tableViewCurrentMeal.getItems().clear();
            meal = new Meal(comboBoxMealType.getValue().toUpperCase());

            comboBoxFood.setDisable(false);
            comboBoxDrink.setDisable(false);
            buttonAddFood.setVisible(true);
            buttonAddDrink.setVisible(true);
        }
        else
        {
            CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Input");
            alert.setContentText("You must enter a type");
            alert.showAndWait();
        }
    }

    public void addFoodToMealToday(ActionEvent event)
    {
        if (!comboBoxFood.getSelectionModel().isEmpty())
        {
            meal.addFoodItem(comboBoxFood.getValue());
            tableViewCurrentMeal.getItems().addAll(comboBoxFood.getValue());
        }
        else
        {
            CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Input");
            alert.setContentText("You must enter a food item");
            alert.showAndWait();
        }
    }

    public void addDrinkToMealToday(ActionEvent event)
    {
        if (!comboBoxDrink.getSelectionModel().isEmpty())
        {
            meal.addFoodItem(comboBoxDrink.getValue());
            tableViewCurrentMeal.getItems().addAll(comboBoxDrink.getValue());
        }
        else
        {
            CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Input");
            alert.setContentText("You must enter a drink item");
            alert.showAndWait();
        }
    }

    public void removeFromMealToday(ActionEvent event)
    {
        if (!tableViewCurrentMeal.getSelectionModel().isEmpty())
        {
            FoodItem foodItem = tableViewCurrentMeal.getSelectionModel().getSelectedItem();
            tableViewCurrentMeal.getItems().remove(foodItem);
            meal.removeFoodItem(foodItem);
        }
        else
        {
            CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Input");
            alert.setContentText("You must select a meal to remove");
            alert.showAndWait();
        }
    }

    public void addMealToday(ActionEvent event)
    {
        StringBuilder errorMessage = new StringBuilder();

        if (meal == null)
        {
            errorMessage.append("You must enter a meal first!");
        }
        else if (meal.getTotalCalories() <= 0)
        {
            errorMessage.append("You must enter some items first!");
        }

        if (errorMessage.toString().length() == 0)
        {
            //add meal to user
            user.getProfile().getCurrentDay().addMeal(meal);
            //clear current meal table
            tableViewCurrentMeal.getItems().clear();
            //add the new meal to the meal table
            tableViewMealsToday.getItems().add(meal);
            //clear the combo boxes and hide buttons
            comboBoxMealType.getSelectionModel().clearSelection();
            comboBoxFood.getSelectionModel().clearSelection();
            comboBoxDrink.getSelectionModel().clearSelection();
            comboBoxFood.setDisable(true);
            comboBoxDrink.setDisable(true);
            buttonAddFood.setVisible(false);
            buttonAddDrink.setVisible(false);
            updateCalorieGraph();
            tableViewWeight.getItems().clear();
            for (Day d : user.getProfile().getDays())
            {
                if (d.getTotalCalories() > 0)
                {
                    tableViewWeight.getItems().add(d);
                }
            }
            meal = null;
        }
        else
        {
            CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Input");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
        }

        User.writeExistingUserToDatabase(this.user);
        setProgressBarCalories();
        setCaloriesTodayTarget();
    }

    public void removeMealToday(ActionEvent event)
    {
        if (tableViewMealsToday.getSelectionModel().getSelectedItem() == null)
        {
            CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Input");
            alert.setContentText("Please select a meal before attempting to remove one.");
        }
        else
        {
            Meal removedMeal = tableViewMealsToday.getSelectionModel().getSelectedItem();
            tableViewMealsToday.getItems().remove(removedMeal);
            user.getProfile().getCurrentDay().removeMeal(removedMeal);
            updateCalorieGraph();
            tableViewWeight.getItems().clear();

            for (Day d : user.getProfile().getDays())
            {
                if (d.getTotalCalories() > 0)
                {
                    tableViewWeight.getItems().add(d);
                }
            }

            setProgressBarCalories();
            setCaloriesTodayTarget();
        }
    }

    /*********    Pane:  History              ************/

    public void setDateRange(ActionEvent event) throws IOException
    {
        if (datePickerStartDate.getValue() != null && datePickerEndDate.getValue() != null)
        {
            LocalDate startDate = datePickerStartDate.getValue();
            LocalDate endDate = datePickerEndDate.getValue();

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

    /**
     * function to update the calories graph
     */
    private void updateCalorieGraph()
    {
        calorieSeries.getData().clear();
        calorieValues.clear();
        for (Day d : user.getProfile().getDays())
        {
            float totalCal = 0;
            for (Meal m : d.getMeals())
            {
                totalCal += m.getTotalCalories();
            }
            if (totalCal > 0)
            {
                calorieSeries.getData().add(new XYChart.Data<>(d.getDate().toEpochDay(), totalCal));
            }
        }
        calorieValues.addAll(calorieSeries);

    }

    /*********    Pane:  Custom              ************/

    public void addCustomFoodItem(ActionEvent event)
    {
        StringBuilder errorMessage = new StringBuilder();
        if (comboBoxCustomMealType.getSelectionModel().isEmpty())
        {
            errorMessage.append("You must enter a type\n");
        }
        if (!InputValidation.checkOnlyLetters(textFieldCustomName.getText()) || !InputValidation.checkLength(textFieldCustomName.getText(), 30))
        {
            errorMessage.append("You must enter a valid name\n");
        }
        if (!InputValidation.checkOnlyNumbers(textFieldCustomCalories.getText()))
        {
            errorMessage.append("You must enter a valid number of calories\n");
        }
        if (!(errorMessage.toString().length() > 0))
        {
            String type = comboBoxCustomMealType.getValue().toString().toUpperCase();
            String name = textFieldCustomName.getText();
            Integer calories = new Integer(textFieldCustomCalories.getText());


            FoodItem foodItem = new FoodItem(name, calories, type);
            user.getProfile().addFoodItem(foodItem);
            tableViewCurrentFoodAndDrink.getItems().add(foodItem);
            if (type.equals("FOOD"))
            {
                comboBoxFood.getItems().add(foodItem);
            }
            else
            {
                comboBoxDrink.getItems().add(foodItem);
            }
        }
        else
        {
            CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Input");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
        }
    }

    public void removeCustomFoodItem(ActionEvent event)
    {
        if (!tableViewCurrentFoodAndDrink.getSelectionModel().isEmpty())
        {
            FoodItem foodItem = tableViewCurrentFoodAndDrink.getSelectionModel().getSelectedItem();
            tableViewCurrentFoodAndDrink.getItems().remove(foodItem);
            user.getProfile().removeFoodItem(foodItem);

            if (foodItem.getFoodType().equals("FOOD"))
            {
                comboBoxFood.getItems().remove(foodItem);
            }
            else
            {
                comboBoxDrink.getItems().remove(foodItem);
            }
        }
        else
        {
            CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Input");
            alert.setContentText("You must select a food item to remove");
            alert.showAndWait();
        }
    }

    /**
     * set the calories progress bar
     */
    private void setProgressBarCalories()
    {
        Profile p = user.getProfile();

        Float target = p.getTargetCalories();
        Float caloriesEaten = p.getCurrentDay().getTotalCalories();
        float percentage = caloriesEaten / target;

        if (percentage < 1.0)
        {
            progressBarCalories.setProgress(percentage);
            progressBarCalories.setStyle("-fx-accent: green");
        }
        else if (percentage >= 1.0)
        {
            progressBarCalories.setProgress(1.0);
            progressBarCalories.setStyle("-fx-accent: red");
        }
    }

    /**
     * set the calories target for today
     */
    private void setCaloriesTodayTarget()
    {
        Profile p = user.getProfile();

        float caloriesToday = p.getCurrentDay().getTotalCalories();
        float caloriesTarget = p.getTargetCalories();
        String display = (int) caloriesToday + "/" + (int) caloriesTarget;

        labelCaloriesTodayTarget.setText(display);
    }
}