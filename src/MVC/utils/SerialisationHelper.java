/*
Name:           SerialisationHelper.java

Authors:        Chris, Conor, Harry, Milo, Yacine

Description:    Small helper class to assist with serialisation and
                deserialisation of profile objects. The class contains static
                methods which can be called as and when required to serialise
                and deserialise a class.
*/
package MVC.utils;

import MVC.model.Exercise;
import MVC.model.Goal;
import MVC.model.Group;
import MVC.model.Profile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SerialisationHelper
{
    /**
     * Converts a profile to a JSON format serialised class, which can then
     * be stored in a database or manipulated as required.
     *
     * @param profile the profile to be serialised
     * @return A String containing the passed profile in JSON format
     */
    public static String serializeProfile(Profile profile)
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Exercise.class, new ExerciseAdaptor());
        gsonBuilder.registerTypeAdapter(Goal.class, new GoalAdaptor());
        Gson gson = gsonBuilder.create();

        String profileString = gson.toJson(profile);

        return profileString;
    }

    /**
     * Converts a JSON serialised class to a Java profile object, which can
     * then be used in the application.
     *
     * @param profileString the JSON serialised class, stored as a string
     * @return A profile object, initialised from the JSON data
     */
    public static Profile deserializeProfile(String profileString)
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Exercise.class, new ExerciseAdaptor());
        gsonBuilder.registerTypeAdapter(Goal.class, new GoalAdaptor());
        Gson gson = gsonBuilder.create();

        Profile profile = gson.fromJson(profileString, Profile.class);

        return profile;
    }

    /**
     * Converts a group to a JSON format serialised class, which can then
     * be stored in a database or manipulated as required.
     *
     * @param group the group to be serialised
     * @return A String containing the passed group in JSON format
     */
    public static String serializeGroup(Group group)
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Goal.class, new GoalAdaptor());
        Gson gson = gsonBuilder.create();

        String groupString = gson.toJson(group);

        return groupString;
    }

    /**
     * Converts a JSON serialised class to a Java group object, which can
     * then be used in the application.
     *
     * @param groupString the JSON serialised class, stored as a string
     * @return A group object, initialised from the JSON data
     */
    public static Group deserializeGroup(String groupString)
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Goal.class, new GoalAdaptor());
        Gson gson = gsonBuilder.create();

        Group group = gson.fromJson(groupString, Group.class);

        return group;
    }
}
