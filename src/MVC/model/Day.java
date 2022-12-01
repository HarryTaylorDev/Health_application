/*
Name:           Day.java

Authors:        Chris, Conor, Harry, Milo, Yacine

Description:    Class defines a Day object. This class forms the basis of the
                tracking element of the application, containing a list of meals
                eaten that day, the exercises the user has completed, and the
                weight of the user. The classes uses a localdate to keep track
                of the date
*/

package MVC.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Day implements Comparable<Day>
{
    private ArrayList<Meal> meals;
    private ArrayList<Exercise> exercises;
    private Float weight;
    private LocalDate date;

    /**
     * Constructor for a day object. This initialises empty lists to begin
     * with, with the accessor and mutator methods used to add to the object.
     */
    public Day()
    {
        this.meals = new ArrayList<>();
        this.exercises = new ArrayList<>();
        this.weight = null;
        LocalDate ld = LocalDate.now();
        this.date = ld;
    }

    public Day(LocalDate date)
    {
        this.date = date;
    }

    public ArrayList<Meal> getMeals()
    {
        return meals;
    }

    public void setMeals(ArrayList<Meal> meals)
    {
        this.meals = meals;
    }

    public ArrayList<Exercise> getExercises()
    {
        return exercises;
    }

    public void addExercise(Exercise exercise)
    {
        exercises.add(exercise);
    }

    public Float getWeight()
    {
        return weight;
    }

    public void setWeight(Float weight)
    {
        this.weight = weight;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    public void addMeal(Meal meal)
    {
        meals.add(meal);
    }

    public void removeMeal(Meal meal)
    {
        meals.remove(meal);
    }

    /**
     * Returns the total amount of calories the user has eaten that day
     *
     * @return A Float representation of the total number of calories consumed
     * that day
     */
    public Float getTotalCalories()
    {
        Float calories = (float) 0;

        for (Meal m : meals)
        {
            calories += m.getTotalCalories();
        }

        return calories;
    }

    @Override
    public int compareTo(Day o)
    {
        return this.date.compareTo(o.getDate());
    }

    @Override
    public String toString()
    {
        return "Day{" +
                "meals=" + meals +
                ", exercises=" + exercises +
                ", weight=" + weight +
                ", date=" + date +
                '}';
    }
}
