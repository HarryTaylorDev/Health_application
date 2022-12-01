/*
Name:           Exercise.java

Authors:        Chris, Conor, Harry, Milo, Yacine

Description:    Abstract class defines an exercise object. This stores an
                activity object, and contains abstract methods that are
                implemented by child classes.

*/
package MVC.model;

public abstract class Exercise
{
    private Activity activity;

    /**
     * Exercise constructor
     * @param activity activity the exercise is
     */
    public Exercise(Activity activity)
    {
        this.activity = activity;
    }

    public abstract String getAchieved();

    public abstract float getAmount();

    public Activity getActivity()
    {
        return activity;
    }
}
