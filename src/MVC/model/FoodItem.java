/*
Name:           FoodItem.java

Authors:        Chris, Conor, Harry, Milo, Yacine

Description:    Class defines a FoodItem. These form a part of a Meal object.
                Each food item stores the name of the item, how many calories
                a standard portion contains and the type of food it is (food
                or drink)
*/
package MVC.model;

public class FoodItem implements Comparable<FoodItem>
{
    private String name;
    private int calories;
    private FoodType foodType;

    /**
     * Constructs a FoodItem object
     *
     * @param name     the name of the food item
     * @param calories how many calories that a standard portion of this food
     *                 contains
     * @param foodType the type of food (FOOD or DRINK)
     */
    public FoodItem(String name, int calories, String foodType)
    {
        this.name = name;
        this.calories = calories;
        this.foodType = FoodType.valueOf(foodType);
    }

    @Override
    public int compareTo(FoodItem o)
    {
        return this.name.compareTo(o.name);
    }

    public String getName()
    {
        return name;
    }

    public int getCalories()
    {
        return calories;
    }

    public void setCalories(int calories)
    {
        this.calories = calories;
    }

    /**
     * Returns the food type and removes capitalisation of the enum object
     * e.g. FOOD becomes Food (useful for displaying object type to the
     * user)
     *
     * @return the string representation of the food type
     */
    public String getFoodType()
    {
        String output = foodType.toString().substring(0, 1) + foodType.toString().substring(1).toLowerCase();
        return output;
    }

    @Override
    public String toString()
    {
        return "FoodItem{" +
                "name='" + name + '\'' +
                ", calories=" + calories +
                ", foodType=" + foodType +
                '}';
    }

    /**
     * Food type enum
     */
    private enum FoodType
    {
        FOOD,
        DRINK
    }
}
