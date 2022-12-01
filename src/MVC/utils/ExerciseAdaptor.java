/*
Name:           ExerciseAdaptor.java

Authors:        Chris, Conor, Harry, Milo, Yacine

Description:    Custom adaptor class to allow Exercise objects to be
                serialised. This is required by the GSON library in order to
                serialise instances of objects that extend abstract classes.

Note:           This code was reused from here:
https://www.javacodegeeks.com/2012/04/json-with-gson-and-abstract-classes.html
*/
package MVC.utils;

import MVC.model.Exercise;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ExerciseAdaptor implements JsonSerializer<Exercise>, JsonDeserializer<Exercise>
{
    @Override
    public JsonElement serialize(Exercise src, Type typeOfSrc, JsonSerializationContext context)
    {
        JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(src.getClass().getSimpleName()));
        result.add("properties", context.serialize(src, src.getClass()));

        return result;
    }

    @Override
    public Exercise deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException
    {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        JsonElement element = jsonObject.get("properties");

        try
        {
            return context.deserialize(element, Class.forName("MVC.model." + type));
        }
        catch (ClassNotFoundException cnfe)
        {
            throw new JsonParseException("Unknown element type: " + type, cnfe);
        }
    }
}
