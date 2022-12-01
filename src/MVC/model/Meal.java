/*
Name:           Meal.java

Authors:        Chris, Conor, Harry, Milo, Yacine

Description:    Class defines a Meal object, containing a list of food items
                and a meal type for a set meal
*/

package MVC.model;

import java.util.ArrayList;


public class Meal
{
    private ArrayList<FoodItem> foodItems;
    private MealType type;

    /**
     * Meal constructor
     *
     * @param type type of meal
     */
    public Meal(String type)
    {
        foodItems = new ArrayList<>();
        this.type = MealType.valueOf(type);
    }

    public ArrayList<FoodItem> getFoodItems()
    {
        return foodItems;
    }

    public String getType()
    {
        String output = type.toString().substring(0, 1) + type.toString().substring(1).toLowerCase();
        return output;
    }

    /**
     * Gets the contents of the food items and returns them in string format
     *
     * @return String containing all food items in the meal object
     */
    public String getContents()
    {
        StringBuilder output = new StringBuilder();

        for (FoodItem i : foodItems)
        {
            output.append(i.getName());
            output.append("\n");
        }

        return output.toString();
    }

    /**
     * Gets the calories of all meal types in string format
     *
     * @return output of all food items in the meal as a string
     */
    public String getCalories()
    {
        StringBuilder output = new StringBuilder();

        for (FoodItem i : foodItems)
        {
            output.append(i.getCalories());
            output.append("\n");
        }

        return output.toString();
    }

    /**
     * Gets the total calories of a meal
     *
     * @return Float containing total calories of a meal
     */
    public Float getTotalCalories()
    {
        Float calories = new Float(0);

        for (FoodItem i : foodItems)
        {
            calories += i.getCalories();
        }

        return calories;
    }

    public void addFoodItem(FoodItem foodItem)
    {
        foodItems.add(foodItem);
    }

    public void removeFoodItem(FoodItem foodItem)
    {
        foodItems.remove(foodItem);
    }

    @Override
    public String toString()
    {
        return "Meal{" +
                "foodItems=" + foodItems +
                ", type=" + type +
                '}';
    }

    // meal type enum
    private enum MealType
    {
        BREAKFAST,
        LUNCH,
        DINNER,
        SNACKS
    }
}
