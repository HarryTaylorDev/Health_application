/*
Name:           Profile.java

Authors:        Chris, Conor, Harry, Milo, Yacine

Description:    Class defines a Profile object, this has all the information regarding
                health, exercise and weight tracking, including goals about the user
*/

package MVC.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.time.temporal.ChronoUnit.DAYS;

public class Profile
{
    private LocalDate DoB;
    private Sex sex;
    private int age;
    private Float height;
    private LifeStyle lifeStyle;
    private String bio;
    private Points points;
    private LocalDate dateWeightLastEntered;
    private Integer weightEntryFrequency;
    private WeightGoal weightGoal;
    private ArrayList<CardioGoal> cardioGoal;
    private ArrayList<StrengthGoal> strengthGoal;
    private ArrayList<Day> days;
    private ArrayList<Activity> activity;
    private ArrayList<FoodItem> foods;
    private LocalDate lastLoggedIn;
    private ArrayList<Integer> groupIDList;
    private Integer favouriteGroup;
    private transient Group currentGroup;
    private transient Map<String, Float> formulaValue;
    /**
     * Default Profile Constructor
     */
    public Profile()
    {
        DoB = null;
        sex = null;
        height = null;
        //targetWeight = null;
        lifeStyle = null;
        bio = null;
        points = new Points();
        dateWeightLastEntered = LocalDate.now();
        weightEntryFrequency = 2;
        weightGoal = null;
        cardioGoal = new ArrayList<>();
        strengthGoal = new ArrayList<>();
        days = new ArrayList<>();
        activity = new ArrayList<>();
        foods = new ArrayList<>();
        groupIDList = new ArrayList<>();
        favouriteGroup = null;
    }

    /**
     * Gets the most up to date weight of the user
     *
     * @return Float weight if set, or null if not set
     */
    public Float getWeight()
    {
        List<Day> days = this.getDays();

        boolean found = false;
        int counter = days.size() - 1;
        Float weight = null;

        while (!found && counter >= 0)
        {
            if (days.get(counter).getWeight() != null)
            {
                found = true;
                weight = days.get(counter).getWeight();
            }

            counter--;
        }

        return weight;
    }

    /**
     * Gets the maintenance calories for the current user
     *
     * @return double maintenance calories
     */
    public double getMaintenanceCalories()
    {
        double activity = this.getLifeStyle().getActivityValue();

        return (activity * this.getBMR());
    }

    /**
     * Gets the days between two dates
     *
     * @param d1 date 1
     * @param d2 date 2
     * @return days
     */
    public long daysBetween(LocalDate d1, LocalDate d2)
    {
        return DAYS.between(d1, d2);
    }

    /**
     * Gets the target calories needed for the user to reach their goal
     *
     * @return target calories
     */
    public Float getTargetCalories()
    {
        LocalDate setDate = this.getWeightGoal().getSetDate();
        LocalDate targetDate = this.getWeightGoal().getTargetDate();
        long daysBetween = daysBetween(setDate, targetDate);

        Float caloriesToChange = (Math.abs(this.getWeight() - this.getWeightGoal().getTargetWeight()) * 7716);
        double caloriesToEat = this.getMaintenanceCalories();

        Float caloriesNeededForTarget;

        // gain weight
        if (this.getWeight() < this.getWeightGoal().getTargetWeight())
        {
            caloriesNeededForTarget = (float) (caloriesToEat + (caloriesToChange / daysBetween));
        }
        // lose weight
        else if (this.getWeight() > this.getWeightGoal().getTargetWeight())
        {
            caloriesNeededForTarget = (float) caloriesToEat - (caloriesToChange / daysBetween);
        }
        else
        {
            return new Float(this.getMaintenanceCalories());
        }

        return caloriesNeededForTarget;
    }

    /**
     * Gets the Basal Metabolic Rate (BMR)
     *
     * @return BMR in double format
     */
    public Float getBMR()
    {
        if (this.getSex().equals(Sex.MALE))
        {
            return (formulaValue.get("weight") * this.getWeight()) + (formulaValue.get("height") * this.getHeight()) - (formulaValue.get("age") * this.getAge()) + formulaValue.get("male");
        }
        else
        {
            return (formulaValue.get("weight") * this.getWeight()) + (formulaValue.get("height") * this.getHeight()) - (formulaValue.get("age") * this.getAge()) + formulaValue.get("female");
        }
    }

    /**
     * Gets the current Day of this user
     *
     * @return The current Day
     */
    public Day getCurrentDay()
    {
        return this.getDays().get(this.getDays().size() - 1);
    }

    public ArrayList<Day> getWeek()
    {
        List<Day> days = this.getDays();
        ArrayList<Day> week = new ArrayList<>();

        for (int i = days.size() - 1; i > 0 && i > days.size() - 8; i--)
        {
            week.add(days.get(i));
        }

        return week;
    }

    /**
     * Gets the Body Mass Index (BMI)
     *
     * @return BMI in double format
     */
    public Float getBMI()
    {
        return (this.getWeight() / ((this.getHeight() / 100) * (this.getHeight() / 100)));
    }

    public LocalDate getDoB()
    {
        return DoB;
    }

    public void setDoB(LocalDate doB)
    {
        DoB = doB;
    }

    public Float getHeight()
    {
        return height;
    }

    public void setHeight(Float height)
    {
        this.height = height;
    }

    public LifeStyle getLifeStyle()
    {
        return lifeStyle;
    }

    public void setLifeStyle(LifeStyle lifeStyle)
    {
        this.lifeStyle = lifeStyle;
    }

    public int getLifeStyleValue()
    {
        return lifeStyle.ordinal();
    }

    public void setLifeStyleValue(int value)
    {
        lifeStyle = LifeStyle.values()[value];
    }

    public Points getPoints()
    {
        return points;
    }

    public void setPoints(Points points)
    {
        this.points = points;
    }

    /**
     * function to add points to a specific point type
     * @param pointsToAdd number of points to add
     * @param pointType type of point to add to
     */
    public void addPoints(int pointsToAdd, Points.PointType pointType)
    {
        this.points.addPoints(pointsToAdd, pointType);
    }

    /**
     * function to give the user points for logging in
     */
    public void loggedInPoints()
    {
        this.points.addPoints(50, Points.PointType.LOGIN);
    }

    /**
     * Gets the points for calories eaten
     *
     * @param caloriesEatenToday calories eaten today
     * @param caloriesGoal       calorie target today
     */
    public void caloriesEatenPoints(int caloriesEatenToday, int caloriesGoal)
    {
        if (caloriesEatenToday > caloriesGoal * 0.95 && caloriesEatenToday < caloriesGoal * 1.05)
        {
            this.addPoints(150, Points.PointType.CALORIEMET);
        }
        else if (points.isCalorieTodayMet())
        {
            this.points.removeCalorieMet();
        }
    }

    /**
     * add points to the user when they complete a goal
     */
    public void goalCompletePoints()
    {
        this.points.addPoints(150, Points.PointType.GOAL);
    }

    public WeightGoal getWeightGoal()
    {
        return weightGoal;
    }

    public void setWeightGoal(WeightGoal weightGoal)
    {
        this.weightGoal = weightGoal;
    }

    public ArrayList<CardioGoal> getCardioGoal()
    {
        return cardioGoal;
    }

    public ArrayList<StrengthGoal> getStrengthGoal()
    {
        return strengthGoal;
    }

    public ArrayList<Day> getDays()
    {
        return days;
    }

    public void addDay(Day day)
    {
        days.add(day);
    }

    public Sex getSex()
    {
        return sex;
    }

    public void setSex(String sex)
    {
        this.sex = Sex.valueOf(sex);
    }

    public int getAge()
    {
        return age;
    }

    public LocalDate getDateWeightLastEntered()
    {
        return dateWeightLastEntered;
    }

    public void setDateWeightLastEntered(LocalDate dateWeightLastEntered)
    {
        this.dateWeightLastEntered = dateWeightLastEntered;
    }

    public Integer getWeightEntryFrequency()
    {
        return weightEntryFrequency;
    }

    public void setWeightEntryFrequency(Integer weightEntryFrequency)
    {
        this.weightEntryFrequency = weightEntryFrequency;
    }

    public void setAgeFromDoB()
    {
        LocalDate currentDate = LocalDate.now();
        Period age = Period.between(currentDate, DoB);
        int years = Math.abs(age.getYears());
        this.age = years;
    }

    public ArrayList<Activity> getActivity()
    {
        return activity;
    }

    public ArrayList<FoodItem> getFoods()
    {
        return foods;
    }

    public LocalDate getLastLoggedIn()
    {
        return lastLoggedIn;
    }

    public void setLoggedIn()
    {
        lastLoggedIn = LocalDate.now();
    }

    @Override
    public String toString()
    {
        return "Profile{" +
                "DoB=" + DoB +
                ", sex=" + sex +
                ", age=" + age +
                ", height=" + height +
                ", lifeStyle=" + lifeStyle +
                ", bio='" + bio + '\'' +
                ", points=" + points +
                ", daysSinceWeightEntered=" + dateWeightLastEntered +
                ", weightEntryFrequency=" + weightEntryFrequency +
                ", weightGoal=" + weightGoal +
                ", cardioGoal=" + cardioGoal +
                ", strengthGoal=" + strengthGoal +
                ", days=" + days +
                ", activity=" + activity +
                ", foods=" + foods +
                '}';
    }

    /**
     * Load the default exercise and food item data into the profile for use
     * later
     */
    public void loadDefaultData()
    {
        ArrayList<String> foods = readData("src\\MVC\\model\\food_items.csv");
        ArrayList<String> exercises = readData("src\\MVC\\model\\exercises.csv");

        // Loading in the list of foods
        // Start at 1 (as data has headers)
        for (int i = 1; i < foods.size(); i++)
        {
            String temp = foods.get(i);

            // Split the string
            ArrayList<String> splitLines = new ArrayList<>();
            for (int j = 0; j < temp.split(",").length; j++)
            {
                splitLines.add(temp.split(",")[j]);
            }

            // Create FoodItem from relevant fields, add to Array
            FoodItem foodItem = new FoodItem(splitLines.get(0), Integer.parseInt(splitLines.get(2)), splitLines.get(3));
            this.foods.add(foodItem);
        }

        // Loading in the list of exercises
        // Start at 1 (as data has headers)
        for (int i = 1; i < exercises.size(); i++)
        {
            String temp = exercises.get(i);

            // Split the string
            ArrayList<String> splitLines = new ArrayList<>();
            for (int j = 0; j < temp.split(",").length; j++)
            {
                splitLines.add(temp.split(",")[j]);
            }

            // Create object from relevant fields, add to Array
            Activity exercise = new Activity(splitLines.get(0), Float.parseFloat(splitLines.get(1)), splitLines.get(3));
            this.activity.add(exercise);
        }
    }

    /**
     * Private helper method for reading in exercise and food item data. Called
     * within the loadDefaultData method
     *
     * @param filepath the path to the file to be loaded
     * @return An ArrayList of strings, each string representing a line of
     * data
     */
    private ArrayList<String> readData(String filepath)
    {
        ArrayList<String> readData = new ArrayList<>();
        try
        {
            FileReader fr = new FileReader(new File(filepath));
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null)
            {
                readData.add(line);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return readData;

    }

    public void addCardioGoal(CardioGoal cardioGoal)
    {
        this.cardioGoal.add(cardioGoal);
    }

    public void removeCardioGoal(Goal cardioGoal)
    {
        this.cardioGoal.remove(cardioGoal);
    }

    public void addStrengthGoal(StrengthGoal strengthGoal)
    {
        this.strengthGoal.add(strengthGoal);
    }

    public void removeStrengthGoal(Goal strengthGoal)
    {
        this.strengthGoal.remove(strengthGoal);
    }

    public void addFoodItem(FoodItem foodItem)
    {
        this.foods.add(foodItem);
    }

    public void removeFoodItem(FoodItem foodItem)
    {
        this.foods.remove(foodItem);
    }

    /**
     * Generates a new day if it is a new day since the user last logged in
     *
     * @return true if new day is created, false if not
     */
    public boolean checkCurrentDay()
    {
        boolean dateChanged = false;
        LocalDate lastDate = days.get(days.size() - 1).getDate();
        LocalDate currentDate = LocalDate.now();

        if (!currentDate.isEqual(lastDate))
        {
            days.add(new Day());
            dateChanged = true;
        }

        return dateChanged;
    }

    public void addActivity(Activity activity)
    {
        this.activity.add(activity);
    }

    public void removeActivity(Activity activity)
    {
        this.activity.remove(activity);
    }

    public ArrayList<Integer> getUserGroups()
    {
        return groupIDList;
    }

    public void addGroup(Integer groupID)
    {
        groupIDList.add(groupID);
    }

    public void removeGroup(Integer groupID)
    {
        groupIDList.remove(groupID);
    }

    public Group getCurrentGroup()
    {
        return currentGroup;
    }

    public void setCurrentGroup(Group currentGroup)
    {
        this.currentGroup = currentGroup;
    }

    public Integer getFavouriteGroup()
    {
        return favouriteGroup;
    }

    public void setFavouriteGroup(Integer favouriteGroup)
    {
        this.favouriteGroup = favouriteGroup;
    }

    public void setFormulaValue(Map<String, Float> map)
    {
        formulaValue = map;
    }

    public Float getFormulaValue(String value)
    {
        return formulaValue.get(value);
    }

    // enum for lifestyles
    public enum LifeStyle
    {
        SEDENTARY(1.2),
        LOW(1.35),
        MODERATE(1.5),
        HIGH(1.7),
        VERY_HIGH(1.9);

        final double activityValue;

        // sets the value of the activity to be used to calculate BMR
        LifeStyle(double x)
        {
            activityValue = x;
        }

        public double getActivityValue()
        {
            return this.activityValue;
        }
    }

    // Sex enum
    public enum Sex
    {
        MALE,
        FEMALE
    }

}