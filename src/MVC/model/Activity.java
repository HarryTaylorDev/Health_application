/*
Name:           Activity.java

Authors:        Chris, Conor, Harry, Milo, Yacine

Description:    Class defines an activity object. These objects model fitness
                activities that the users can take part in
*/
package MVC.model;

public class Activity
{
    private String exerciseName;
    private Float calories;
    private ExerciseType exerciseType;

    /**
     * Default constructor
     * Used for testing purposes
     */
    public Activity()
    {
        exerciseName = "";
        calories = 0F;
        exerciseType = ExerciseType.CARDIO;
    }

    /**
     * Constructs an Activity object with passed in fields
     *
     * @param exerciseName the name of the exercise
     * @param calories     the calories the exercise will burn in a standard time
     *                     (we take this to be 30 minutes, but can be altered if
     *                     necessary)
     * @param exerciseType the type of exercise (either Cardio or Strength)
     */
    public Activity(String exerciseName, Float calories, String exerciseType)
    {
        this.exerciseName = exerciseName;
        this.calories = calories;
        this.exerciseType = ExerciseType.valueOf(exerciseType.toUpperCase());
    }

    public String getExerciseName()
    {
        return exerciseName;
    }

    public Float getCalories()
    {
        return calories;
    }

    /**
     * Returns the exercise type and removes capitalisation of the enum object
     * e.g. CARDIO becomes Cardio (useful for displaying object type to the
     * user)
     *
     * @return the string representation of the exercise type
     */
    public String getExerciseType()
    {
        String output = exerciseType.toString().substring(0, 1) + exerciseType.toString().substring(1).toLowerCase();
        return output;
    }

    @Override
    public String toString()
    {
        return "Activity{" +
                "exercise='" + exerciseName + '\'' +
                ", calories=" + calories +
                '}';
    }

    enum ExerciseType
    {
        CARDIO,
        STRENGTH
    }
}
