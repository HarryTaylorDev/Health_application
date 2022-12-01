/******************************************************************************
 * PLEASE NOTE:
 * THIS IS A DUMMY CLASS TO ENSURE THAT THE DATABASE IS WORKING
 * CLASS IS IN CONSTANT USE DURING TESTING
 * DO NOT DELETE!
 ******************************************************************************/
package MVC.utils;

import MVC.model.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Random;

public class DBDataGenerator
{
    /**
     * Populate the table with test data
     */
    public static void populateTable()
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Exercise.class, new ExerciseAdaptor());
        gsonBuilder.registerTypeAdapter(Goal.class, new GoalAdaptor());

        Gson gson = gsonBuilder.create();

        ArrayList<String> readData = new ArrayList<>();
        String dataFilePath = "mock_user_data.csv";

        try
        {
            FileReader fr = new FileReader(new File(dataFilePath));
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

        DatabaseManagement db = new DatabaseManagement();

        for (int i = 0; i < readData.size(); i++)
        {
            //System.out.println(readData.get(i));

            // Insert statement (for database)
            String insertStatement = "INSERT INTO users(first_name, last_name, email, username, salt, password, question1_id, question1_answer, question2_id, question2_answer, profile) VALUES(?,?,?,?,?,?,?,?,?,?,?)";

            // Splitting read in data
            String readLine = readData.get(i);


            // Adding each field to split line ArrayList
            ArrayList<String> splitLines = new ArrayList<>();

            for (int j = 0; j < readLine.split(",").length; j++)
            {
                splitLines.add(readLine.split(",")[j]);
            }

            // Quick way to add serialised profile data to the database (Not
            // used for now)

            //splitLines.add("{\"DoB\":{\"year\":1994,\"month\":4,\"day\":4},\"sex\":\"MALE\",\"age\":25,\"height\":180.0,\"lifeStyle\":\"MODERATE\",\"units\":true,\"points\":0,\"groups\":[],\"weightGoal\":{\"currentWeight\":80.0,\"targetWeight\":75.0,\"setDate\":{\"year\":2020,\"month\":3,\"day\":27},\"targetDate\":{\"year\":2020,\"month\":7,\"day\":16},\"activity\":\"weight\"},\"cardioGoal\":[],\"strengthGoal\":[],\"days\":[{\"meals\":[],\"exercises\":[],\"weight\":80.0,\"date\":{\"year\":2020,\"month\":3,\"day\":27}}],\"calorieCount\":[{\"exercise\":\"General weight lifting\",\"calories\":112.0,\"exerciseType\":\"STRENGTH\"},{\"exercise\":\"Calisthenics\",\"calories\":298.0,\"exerciseType\":\"STRENGTH\"},{\"exercise\":\"Cycling (Spinning)\",\"calories\":260.0,\"exerciseType\":\"CARDIO\"},{\"exercise\":\"Rowing (Indoor)\",\"calories\":316.0,\"exerciseType\":\"CARDIO\"},{\"exercise\":\"Bowling\",\"calories\":112.0,\"exerciseType\":\"CARDIO\"},{\"exercise\":\"Golf\",\"calories\":130.0,\"exerciseType\":\"CARDIO\"},{\"exercise\":\"General gymnastics\",\"calories\":149.0,\"exerciseType\":\"CARDIO\"},{\"exercise\":\"Football\",\"calories\":260.0,\"exerciseType\":\"CARDIO\"},{\"exercise\":\"Tennis\",\"calories\":260.0,\"exerciseType\":\"CARDIO\"},{\"exercise\":\"Walking\",\"calories\":149.0,\"exerciseType\":\"CARDIO\"}],\"foods\":[{\"name\":\"Apple\",\"calories\":95,\"foodType\":\"FOOD\"},{\"name\":\"Apricot\",\"calories\":17,\"foodType\":\"FOOD\"},{\"name\":\"Avocado\",\"calories\":320,\"foodType\":\"FOOD\"},{\"name\":\"Banana\",\"calories\":111,\"foodType\":\"FOOD\"},{\"name\":\"Blackberries\",\"calories\":62,\"foodType\":\"FOOD\"},{\"name\":\"Blueberries\",\"calories\":84,\"foodType\":\"FOOD\"},{\"name\":\"Cherries\",\"calories\":4,\"foodType\":\"FOOD\"},{\"name\":\"Dates\",\"calories\":20,\"foodType\":\"FOOD\"},{\"name\":\"Grapes\",\"calories\":104,\"foodType\":\"FOOD\"},{\"name\":\"Guava\",\"calories\":37,\"foodType\":\"FOOD\"},{\"name\":\"Kiwi\",\"calories\":112,\"foodType\":\"FOOD\"},{\"name\":\"Lemon\",\"calories\":17,\"foodType\":\"FOOD\"},{\"name\":\"Lime\",\"calories\":20,\"foodType\":\"FOOD\"},{\"name\":\"Mango\",\"calories\":202,\"foodType\":\"FOOD\"},{\"name\":\"Nectarine\",\"calories\":66,\"foodType\":\"FOOD\"},{\"name\":\"Olives\",\"calories\":2,\"foodType\":\"FOOD\"},{\"name\":\"Orange\",\"calories\":62,\"foodType\":\"FOOD\"},{\"name\":\"Peach\",\"calories\":59,\"foodType\":\"FOOD\"},{\"name\":\"Pear\",\"calories\":101,\"foodType\":\"FOOD\"},{\"name\":\"Pineapple\",\"calories\":453,\"foodType\":\"FOOD\"},{\"name\":\"Raisin\",\"calories\":434,\"foodType\":\"FOOD\"},{\"name\":\"Strawberries\",\"calories\":49,\"foodType\":\"FOOD\"},{\"name\":\"Watermelon\",\"calories\":86,\"foodType\":\"FOOD\"},{\"name\":\"Asparagus\",\"calories\":7,\"foodType\":\"FOOD\"},{\"name\":\"Carrot\",\"calories\":17,\"foodType\":\"FOOD\"},{\"name\":\"Potato\",\"calories\":867,\"foodType\":\"FOOD\"},{\"name\":\"Broccoli\",\"calories\":9,\"foodType\":\"FOOD\"},{\"name\":\"Brussel Sprout\",\"calories\":10,\"foodType\":\"FOOD\"},{\"name\":\"Button Mushroom\",\"calories\":7,\"foodType\":\"FOOD\"},{\"name\":\"Sweetcorn\",\"calories\":79,\"foodType\":\"FOOD\"},{\"name\":\"Pepper\",\"calories\":4,\"foodType\":\"FOOD\"},{\"name\":\"Mushy Peas\",\"calories\":173,\"foodType\":\"FOOD\"},{\"name\":\"Onion\",\"calories\":55,\"foodType\":\"FOOD\"},{\"name\":\"Spinach\",\"calories\":7,\"foodType\":\"FOOD\"},{\"name\":\"Cucumber\",\"calories\":1,\"foodType\":\"FOOD\"},{\"name\":\"Celery\",\"calories\":3,\"foodType\":\"FOOD\"},{\"name\":\"Tomato\",\"calories\":18,\"foodType\":\"FOOD\"},{\"name\":\"Slice of White Bread\",\"calories\":95,\"foodType\":\"FOOD\"},{\"name\":\"Slice of White Toast\",\"calories\":87,\"foodType\":\"FOOD\"},{\"name\":\"Slice of Fried White Bread\",\"calories\":141,\"foodType\":\"FOOD\"},{\"name\":\"Slice of Brown Bread\",\"calories\":74,\"foodType\":\"FOOD\"},{\"name\":\"Slice of Brown Toast\",\"calories\":65,\"foodType\":\"FOOD\"},{\"name\":\"Slice of Rye Bread\",\"calories\":55,\"foodType\":\"FOOD\"},{\"name\":\"Scone\",\"calories\":145,\"foodType\":\"FOOD\"},{\"name\":\"Naan\",\"calories\":538,\"foodType\":\"FOOD\"},{\"name\":\"Jaffa Cake\",\"calories\":45,\"foodType\":\"FOOD\"},{\"name\":\"Jam Donut\",\"calories\":252,\"foodType\":\"FOOD\"},{\"name\":\"Hot Cross Bun\",\"calories\":155,\"foodType\":\"FOOD\"},{\"name\":\"Ricotta\",\"calories\":67,\"foodType\":\"FOOD\"},{\"name\":\"Cheddar\",\"calories\":210,\"foodType\":\"FOOD\"},{\"name\":\"Feta\",\"calories\":131,\"foodType\":\"FOOD\"},{\"name\":\"Goats Cheese\",\"calories\":130,\"foodType\":\"FOOD\"},{\"name\":\"Parmesan\",\"calories\":200,\"foodType\":\"FOOD\"},{\"name\":\"Red Leicester\",\"calories\":200,\"foodType\":\"FOOD\"},{\"name\":\"Long Grain Rice\",\"calories\":175,\"foodType\":\"FOOD\"},{\"name\":\"Egg Noodles\",\"calories\":279,\"foodType\":\"FOOD\"},{\"name\":\"Instant Noodles\",\"calories\":170,\"foodType\":\"FOOD\"},{\"name\":\"Fusilli\",\"calories\":316,\"foodType\":\"FOOD\"},{\"name\":\"Macaroni\",\"calories\":99,\"foodType\":\"FOOD\"},{\"name\":\"Penne\",\"calories\":452,\"foodType\":\"FOOD\"},{\"name\":\"Steak\",\"calories\":42,\"foodType\":\"FOOD\"},{\"name\":\"Mince Beef\",\"calories\":214,\"foodType\":\"FOOD\"},{\"name\":\"Chicken Breast\",\"calories\":148,\"foodType\":\"FOOD\"},{\"name\":\"Burger with Cheese\",\"calories\":530,\"foodType\":\"FOOD\"},{\"name\":\"Chicken Wings\",\"calories\":471,\"foodType\":\"FOOD\"},{\"name\":\"Chicken Nuggets\",\"calories\":510,\"foodType\":\"FOOD\"},{\"name\":\"White Fish (Baked)\",\"calories\":210,\"foodType\":\"FOOD\"},{\"name\":\"Pale Fish (Baked)\",\"calories\":230,\"foodType\":\"FOOD\"},{\"name\":\"Oily Fish (Baked)\",\"calories\":275,\"foodType\":\"FOOD\"},{\"name\":\"Cornflakes\",\"calories\":121,\"foodType\":\"FOOD\"},{\"name\":\"Coco Pops\",\"calories\":116,\"foodType\":\"FOOD\"},{\"name\":\"Rice Krispies\",\"calories\":115,\"foodType\":\"FOOD\"},{\"name\":\"Cheerios\",\"calories\":111,\"foodType\":\"FOOD\"},{\"name\":\"Special K\",\"calories\":114,\"foodType\":\"FOOD\"},{\"name\":\"Weetabix\",\"calories\":134,\"foodType\":\"FOOD\"},{\"name\":\"Shreddies\",\"calories\":111,\"foodType\":\"FOOD\"},{\"name\":\"Pizza (Slice)\",\"calories\":197,\"foodType\":\"FOOD\"},{\"name\":\"Chips\",\"calories\":380,\"foodType\":\"FOOD\"},{\"name\":\"Chocolate\",\"calories\":600,\"foodType\":\"FOOD\"},{\"name\":\"Crisps\",\"calories\":150,\"foodType\":\"FOOD\"},{\"name\":\"Yoghurt (plain)\",\"calories\":145,\"foodType\":\"FOOD\"},{\"name\":\"Lager\",\"calories\":223,\"foodType\":\"DRINK\"},{\"name\":\"Wine\",\"calories\":133,\"foodType\":\"DRINK\"},{\"name\":\"Cider\",\"calories\":197,\"foodType\":\"DRINK\"},{\"name\":\"Cream Liqueur\",\"calories\":153,\"foodType\":\"DRINK\"},{\"name\":\"Alcopop\",\"calories\":172,\"foodType\":\"DRINK\"},{\"name\":\"Port\",\"calories\":77,\"foodType\":\"DRINK\"},{\"name\":\"Gin\",\"calories\":95,\"foodType\":\"DRINK\"},{\"name\":\"Vodka\",\"calories\":97,\"foodType\":\"DRINK\"},{\"name\":\"Orange Juice\",\"calories\":110,\"foodType\":\"DRINK\"},{\"name\":\"Whole Milk\",\"calories\":165,\"foodType\":\"DRINK\"},{\"name\":\"Cola\",\"calories\":60,\"foodType\":\"DRINK\"},{\"name\":\"Fanta\",\"calories\":33,\"foodType\":\"DRINK\"},{\"name\":\"Dr Pepper\",\"calories\":50,\"foodType\":\"DRINK\"},{\"name\":\"Coffee\",\"calories\":2,\"foodType\":\"DRINK\"},{\"name\":\"Energy Drink\",\"calories\":80,\"foodType\":\"DRINK\"}]}");

            // Old version
            //String profileString = "{\"DoB\":{\"year\":2000,\"month\":2,\"day\":2},\"sex\":\"FEMALE\",\"age\":20,\"height\":180.0,\"lifeStyle\":\"MODERATE\",\"units\":true,\"points\":{\"total\":0,\"totalToday\":0,\"todayGoalPoints\":0,\"calorieTodayMet\":false,\"loggedInToday\":false},\"dateWeightLastEntered\":{\"year\":2020,\"month\":3,\"day\":31},\"weightEntryFrequency\":2,\"weightGoal\":{\"currentWeight\":60.0,\"targetWeight\":60.0,\"setDate\":{\"year\":2020,\"month\":3,\"day\":31},\"targetDate\":{\"year\":2020,\"month\":6,\"day\":23},\"activity\":\"weight\"},\"groups\":[],\"cardioGoal\":[],\"strengthGoal\":[],\"days\":[{\"meals\":[],\"exercises\":[],\"weight\":60.0,\"date\":{\"year\":2020,\"month\":3,\"day\":31}}],\"activity\":[{\"exerciseName\":\"General weight lifting\",\"calories\":112.0,\"exerciseType\":\"STRENGTH\"},{\"exerciseName\":\"Calisthenics\",\"calories\":298.0,\"exerciseType\":\"STRENGTH\"},{\"exerciseName\":\"Cycling (Spinning)\",\"calories\":260.0,\"exerciseType\":\"CARDIO\"},{\"exerciseName\":\"Rowing (Indoor)\",\"calories\":316.0,\"exerciseType\":\"CARDIO\"},{\"exerciseName\":\"Bowling\",\"calories\":112.0,\"exerciseType\":\"CARDIO\"},{\"exerciseName\":\"Golf\",\"calories\":130.0,\"exerciseType\":\"CARDIO\"},{\"exerciseName\":\"General gymnastics\",\"calories\":149.0,\"exerciseType\":\"CARDIO\"},{\"exerciseName\":\"Football\",\"calories\":260.0,\"exerciseType\":\"CARDIO\"},{\"exerciseName\":\"Tennis\",\"calories\":260.0,\"exerciseType\":\"CARDIO\"},{\"exerciseName\":\"Walking\",\"calories\":149.0,\"exerciseType\":\"CARDIO\"}],\"foods\":[{\"name\":\"Apple\",\"calories\":95,\"foodType\":\"FOOD\"},{\"name\":\"Apricot\",\"calories\":17,\"foodType\":\"FOOD\"},{\"name\":\"Avocado\",\"calories\":320,\"foodType\":\"FOOD\"},{\"name\":\"Banana\",\"calories\":111,\"foodType\":\"FOOD\"},{\"name\":\"Blackberries\",\"calories\":62,\"foodType\":\"FOOD\"},{\"name\":\"Blueberries\",\"calories\":84,\"foodType\":\"FOOD\"},{\"name\":\"Cherries\",\"calories\":4,\"foodType\":\"FOOD\"},{\"name\":\"Dates\",\"calories\":20,\"foodType\":\"FOOD\"},{\"name\":\"Grapes\",\"calories\":104,\"foodType\":\"FOOD\"},{\"name\":\"Guava\",\"calories\":37,\"foodType\":\"FOOD\"},{\"name\":\"Kiwi\",\"calories\":112,\"foodType\":\"FOOD\"},{\"name\":\"Lemon\",\"calories\":17,\"foodType\":\"FOOD\"},{\"name\":\"Lime\",\"calories\":20,\"foodType\":\"FOOD\"},{\"name\":\"Mango\",\"calories\":202,\"foodType\":\"FOOD\"},{\"name\":\"Nectarine\",\"calories\":66,\"foodType\":\"FOOD\"},{\"name\":\"Olives\",\"calories\":2,\"foodType\":\"FOOD\"},{\"name\":\"Orange\",\"calories\":62,\"foodType\":\"FOOD\"},{\"name\":\"Peach\",\"calories\":59,\"foodType\":\"FOOD\"},{\"name\":\"Pear\",\"calories\":101,\"foodType\":\"FOOD\"},{\"name\":\"Pineapple\",\"calories\":453,\"foodType\":\"FOOD\"},{\"name\":\"Raisin\",\"calories\":434,\"foodType\":\"FOOD\"},{\"name\":\"Strawberries\",\"calories\":49,\"foodType\":\"FOOD\"},{\"name\":\"Watermelon\",\"calories\":86,\"foodType\":\"FOOD\"},{\"name\":\"Asparagus\",\"calories\":7,\"foodType\":\"FOOD\"},{\"name\":\"Carrot\",\"calories\":17,\"foodType\":\"FOOD\"},{\"name\":\"Potato\",\"calories\":867,\"foodType\":\"FOOD\"},{\"name\":\"Broccoli\",\"calories\":9,\"foodType\":\"FOOD\"},{\"name\":\"Brussel Sprout\",\"calories\":10,\"foodType\":\"FOOD\"},{\"name\":\"Button Mushroom\",\"calories\":7,\"foodType\":\"FOOD\"},{\"name\":\"Sweetcorn\",\"calories\":79,\"foodType\":\"FOOD\"},{\"name\":\"Pepper\",\"calories\":4,\"foodType\":\"FOOD\"},{\"name\":\"Mushy Peas\",\"calories\":173,\"foodType\":\"FOOD\"},{\"name\":\"Onion\",\"calories\":55,\"foodType\":\"FOOD\"},{\"name\":\"Spinach\",\"calories\":7,\"foodType\":\"FOOD\"},{\"name\":\"Cucumber\",\"calories\":1,\"foodType\":\"FOOD\"},{\"name\":\"Celery\",\"calories\":3,\"foodType\":\"FOOD\"},{\"name\":\"Tomato\",\"calories\":18,\"foodType\":\"FOOD\"},{\"name\":\"Slice of White Bread\",\"calories\":95,\"foodType\":\"FOOD\"},{\"name\":\"Slice of White Toast\",\"calories\":87,\"foodType\":\"FOOD\"},{\"name\":\"Slice of Fried White Bread\",\"calories\":141,\"foodType\":\"FOOD\"},{\"name\":\"Slice of Brown Bread\",\"calories\":74,\"foodType\":\"FOOD\"},{\"name\":\"Slice of Brown Toast\",\"calories\":65,\"foodType\":\"FOOD\"},{\"name\":\"Slice of Rye Bread\",\"calories\":55,\"foodType\":\"FOOD\"},{\"name\":\"Scone\",\"calories\":145,\"foodType\":\"FOOD\"},{\"name\":\"Naan\",\"calories\":538,\"foodType\":\"FOOD\"},{\"name\":\"Jaffa Cake\",\"calories\":45,\"foodType\":\"FOOD\"},{\"name\":\"Jam Donut\",\"calories\":252,\"foodType\":\"FOOD\"},{\"name\":\"Hot Cross Bun\",\"calories\":155,\"foodType\":\"FOOD\"},{\"name\":\"Ricotta\",\"calories\":67,\"foodType\":\"FOOD\"},{\"name\":\"Cheddar\",\"calories\":210,\"foodType\":\"FOOD\"},{\"name\":\"Feta\",\"calories\":131,\"foodType\":\"FOOD\"},{\"name\":\"Goats Cheese\",\"calories\":130,\"foodType\":\"FOOD\"},{\"name\":\"Parmesan\",\"calories\":200,\"foodType\":\"FOOD\"},{\"name\":\"Red Leicester\",\"calories\":200,\"foodType\":\"FOOD\"},{\"name\":\"Long Grain Rice\",\"calories\":175,\"foodType\":\"FOOD\"},{\"name\":\"Egg Noodles\",\"calories\":279,\"foodType\":\"FOOD\"},{\"name\":\"Instant Noodles\",\"calories\":170,\"foodType\":\"FOOD\"},{\"name\":\"Fusilli\",\"calories\":316,\"foodType\":\"FOOD\"},{\"name\":\"Macaroni\",\"calories\":99,\"foodType\":\"FOOD\"},{\"name\":\"Penne\",\"calories\":452,\"foodType\":\"FOOD\"},{\"name\":\"Steak\",\"calories\":42,\"foodType\":\"FOOD\"},{\"name\":\"Mince Beef\",\"calories\":214,\"foodType\":\"FOOD\"},{\"name\":\"Chicken Breast\",\"calories\":148,\"foodType\":\"FOOD\"},{\"name\":\"Burger with Cheese\",\"calories\":530,\"foodType\":\"FOOD\"},{\"name\":\"Chicken Wings\",\"calories\":471,\"foodType\":\"FOOD\"},{\"name\":\"Chicken Nuggets\",\"calories\":510,\"foodType\":\"FOOD\"},{\"name\":\"White Fish (Baked)\",\"calories\":210,\"foodType\":\"FOOD\"},{\"name\":\"Pale Fish (Baked)\",\"calories\":230,\"foodType\":\"FOOD\"},{\"name\":\"Oily Fish (Baked)\",\"calories\":275,\"foodType\":\"FOOD\"},{\"name\":\"Cornflakes\",\"calories\":121,\"foodType\":\"FOOD\"},{\"name\":\"Coco Pops\",\"calories\":116,\"foodType\":\"FOOD\"},{\"name\":\"Rice Krispies\",\"calories\":115,\"foodType\":\"FOOD\"},{\"name\":\"Cheerios\",\"calories\":111,\"foodType\":\"FOOD\"},{\"name\":\"Special K\",\"calories\":114,\"foodType\":\"FOOD\"},{\"name\":\"Weetabix\",\"calories\":134,\"foodType\":\"FOOD\"},{\"name\":\"Shreddies\",\"calories\":111,\"foodType\":\"FOOD\"},{\"name\":\"Pizza (Slice)\",\"calories\":197,\"foodType\":\"FOOD\"},{\"name\":\"Chips\",\"calories\":380,\"foodType\":\"FOOD\"},{\"name\":\"Chocolate\",\"calories\":600,\"foodType\":\"FOOD\"},{\"name\":\"Crisps\",\"calories\":150,\"foodType\":\"FOOD\"},{\"name\":\"Yoghurt (plain)\",\"calories\":145,\"foodType\":\"FOOD\"},{\"name\":\"Lager\",\"calories\":223,\"foodType\":\"DRINK\"},{\"name\":\"Wine\",\"calories\":133,\"foodType\":\"DRINK\"},{\"name\":\"Cider\",\"calories\":197,\"foodType\":\"DRINK\"},{\"name\":\"Cream Liqueur\",\"calories\":153,\"foodType\":\"DRINK\"},{\"name\":\"Alcopop\",\"calories\":172,\"foodType\":\"DRINK\"},{\"name\":\"Water\",\"calories\":0,\"foodType\":\"DRINK\"},{\"name\":\"Tea (Milk+Sugar)\",\"calories\":44,\"foodType\":\"DRINK\"},{\"name\":\"Tea (Milk)\",\"calories\":19,\"foodType\":\"DRINK\"},{\"name\":\"Tea (Sugar)\",\"calories\":24,\"foodType\":\"DRINK\"},{\"name\":\"Tea\",\"calories\":1,\"foodType\":\"DRINK\"},{\"name\":\"Port\",\"calories\":77,\"foodType\":\"DRINK\"},{\"name\":\"Gin\",\"calories\":95,\"foodType\":\"DRINK\"},{\"name\":\"Vodka\",\"calories\":97,\"foodType\":\"DRINK\"},{\"name\":\"Orange Juice\",\"calories\":110,\"foodType\":\"DRINK\"},{\"name\":\"Whole Milk\",\"calories\":165,\"foodType\":\"DRINK\"},{\"name\":\"Cola\",\"calories\":60,\"foodType\":\"DRINK\"},{\"name\":\"Fanta\",\"calories\":33,\"foodType\":\"DRINK\"},{\"name\":\"Dr Pepper\",\"calories\":50,\"foodType\":\"DRINK\"},{\"name\":\"Coffee\",\"calories\":2,\"foodType\":\"DRINK\"},{\"name\":\"Energy Drink\",\"calories\":80,\"foodType\":\"DRINK\"}]}";
            String profileString = "{\"DoB\":{\"year\":1988,\"month\":6,\"day\":3},\"sex\":\"MALE\",\"age\":32,\"height\":180.0,\"lifeStyle\":\"MODERATE\",\"units\":true,\"points\":{\"total\":0,\"totalToday\":0,\"todayGoalPoints\":0,\"calorieTodayMet\":false,\"loggedInToday\":false},\"dateWeightLastEntered\":{\"year\":2020,\"month\":6,\"day\":10},\"weightEntryFrequency\":2,\"weightGoal\":{\"currentWeight\":80.0,\"targetWeight\":75.0,\"setDate\":{\"year\":2020,\"month\":6,\"day\":10},\"targetDate\":{\"year\":2025,\"month\":6,\"day\":5},\"activity\":\"weight\"},\"cardioGoal\":[],\"strengthGoal\":[],\"days\":[{\"meals\":[],\"exercises\":[],\"weight\":80.0,\"date\":{\"year\":2020,\"month\":6,\"day\":10}}],\"activity\":[{\"exerciseName\":\"General weight lifting\",\"calories\":112.0,\"exerciseType\":\"STRENGTH\"},{\"exerciseName\":\"Intensive weight lifting\",\"calories\":252.0,\"exerciseType\":\"STRENGTH\"},{\"exerciseName\":\"Calisthenics\",\"calories\":298.0,\"exerciseType\":\"STRENGTH\"},{\"exerciseName\":\"Cycling (Spinning)\",\"calories\":260.0,\"exerciseType\":\"CARDIO\"},{\"exerciseName\":\"Rowing (Indoor)\",\"calories\":316.0,\"exerciseType\":\"CARDIO\"},{\"exerciseName\":\"General gymnastics\",\"calories\":149.0,\"exerciseType\":\"CARDIO\"},{\"exerciseName\":\"Walking\",\"calories\":149.0,\"exerciseType\":\"CARDIO\"},{\"exerciseName\":\"Jogging\",\"calories\":203.0,\"exerciseType\":\"CARDIO\"},{\"exerciseName\":\"Running\",\"calories\":269.0,\"exerciseType\":\"CARDIO\"},{\"exerciseName\":\"Swimming\",\"calories\":295.0,\"exerciseType\":\"CARDIO\"},{\"exerciseName\":\"Skiing\",\"calories\":180.0,\"exerciseType\":\"CARDIO\"},{\"exerciseName\":\"Rollerblading\",\"calories\":400.0,\"exerciseType\":\"CARDIO\"},{\"exerciseName\":\"Surfing\",\"calories\":100.0,\"exerciseType\":\"CARDIO\"}],\"foods\":[{\"name\":\"Apple\",\"calories\":95,\"foodType\":\"FOOD\"},{\"name\":\"Apricot\",\"calories\":17,\"foodType\":\"FOOD\"},{\"name\":\"Avocado\",\"calories\":320,\"foodType\":\"FOOD\"},{\"name\":\"Banana\",\"calories\":111,\"foodType\":\"FOOD\"},{\"name\":\"Blackberries\",\"calories\":62,\"foodType\":\"FOOD\"},{\"name\":\"Blueberries\",\"calories\":84,\"foodType\":\"FOOD\"},{\"name\":\"Cherries\",\"calories\":4,\"foodType\":\"FOOD\"},{\"name\":\"Dates\",\"calories\":20,\"foodType\":\"FOOD\"},{\"name\":\"Grapes\",\"calories\":104,\"foodType\":\"FOOD\"},{\"name\":\"Guava\",\"calories\":37,\"foodType\":\"FOOD\"},{\"name\":\"Kiwi\",\"calories\":112,\"foodType\":\"FOOD\"},{\"name\":\"Lemon\",\"calories\":17,\"foodType\":\"FOOD\"},{\"name\":\"Lime\",\"calories\":20,\"foodType\":\"FOOD\"},{\"name\":\"Mango\",\"calories\":202,\"foodType\":\"FOOD\"},{\"name\":\"Nectarine\",\"calories\":66,\"foodType\":\"FOOD\"},{\"name\":\"Olives\",\"calories\":2,\"foodType\":\"FOOD\"},{\"name\":\"Orange\",\"calories\":62,\"foodType\":\"FOOD\"},{\"name\":\"Peach\",\"calories\":59,\"foodType\":\"FOOD\"},{\"name\":\"Pear\",\"calories\":101,\"foodType\":\"FOOD\"},{\"name\":\"Pineapple\",\"calories\":453,\"foodType\":\"FOOD\"},{\"name\":\"Raisin\",\"calories\":434,\"foodType\":\"FOOD\"},{\"name\":\"Strawberries\",\"calories\":49,\"foodType\":\"FOOD\"},{\"name\":\"Watermelon\",\"calories\":86,\"foodType\":\"FOOD\"},{\"name\":\"Asparagus\",\"calories\":7,\"foodType\":\"FOOD\"},{\"name\":\"Carrot\",\"calories\":17,\"foodType\":\"FOOD\"},{\"name\":\"Potato\",\"calories\":867,\"foodType\":\"FOOD\"},{\"name\":\"Broccoli\",\"calories\":9,\"foodType\":\"FOOD\"},{\"name\":\"Brussel Sprout\",\"calories\":10,\"foodType\":\"FOOD\"},{\"name\":\"Button Mushroom\",\"calories\":7,\"foodType\":\"FOOD\"},{\"name\":\"Sweetcorn\",\"calories\":79,\"foodType\":\"FOOD\"},{\"name\":\"Pepper\",\"calories\":4,\"foodType\":\"FOOD\"},{\"name\":\"Mushy Peas\",\"calories\":173,\"foodType\":\"FOOD\"},{\"name\":\"Onion\",\"calories\":55,\"foodType\":\"FOOD\"},{\"name\":\"Spinach\",\"calories\":7,\"foodType\":\"FOOD\"},{\"name\":\"Cucumber\",\"calories\":1,\"foodType\":\"FOOD\"},{\"name\":\"Celery\",\"calories\":3,\"foodType\":\"FOOD\"},{\"name\":\"Tomato\",\"calories\":18,\"foodType\":\"FOOD\"},{\"name\":\"Slice of White Bread\",\"calories\":95,\"foodType\":\"FOOD\"},{\"name\":\"Slice of White Toast\",\"calories\":87,\"foodType\":\"FOOD\"},{\"name\":\"Slice of Fried White Bread\",\"calories\":141,\"foodType\":\"FOOD\"},{\"name\":\"Slice of Brown Bread\",\"calories\":74,\"foodType\":\"FOOD\"},{\"name\":\"Slice of Brown Toast\",\"calories\":65,\"foodType\":\"FOOD\"},{\"name\":\"Slice of Rye Bread\",\"calories\":55,\"foodType\":\"FOOD\"},{\"name\":\"Scone\",\"calories\":145,\"foodType\":\"FOOD\"},{\"name\":\"Naan\",\"calories\":538,\"foodType\":\"FOOD\"},{\"name\":\"Jaffa Cake\",\"calories\":45,\"foodType\":\"FOOD\"},{\"name\":\"Jam Donut\",\"calories\":252,\"foodType\":\"FOOD\"},{\"name\":\"Hot Cross Bun\",\"calories\":155,\"foodType\":\"FOOD\"},{\"name\":\"Ricotta\",\"calories\":67,\"foodType\":\"FOOD\"},{\"name\":\"Cheddar\",\"calories\":210,\"foodType\":\"FOOD\"},{\"name\":\"Feta\",\"calories\":131,\"foodType\":\"FOOD\"},{\"name\":\"Goats Cheese\",\"calories\":130,\"foodType\":\"FOOD\"},{\"name\":\"Parmesan\",\"calories\":200,\"foodType\":\"FOOD\"},{\"name\":\"Red Leicester\",\"calories\":200,\"foodType\":\"FOOD\"},{\"name\":\"Long Grain Rice\",\"calories\":175,\"foodType\":\"FOOD\"},{\"name\":\"Egg Noodles\",\"calories\":279,\"foodType\":\"FOOD\"},{\"name\":\"Instant Noodles\",\"calories\":170,\"foodType\":\"FOOD\"},{\"name\":\"Fusilli\",\"calories\":316,\"foodType\":\"FOOD\"},{\"name\":\"Macaroni\",\"calories\":99,\"foodType\":\"FOOD\"},{\"name\":\"Penne\",\"calories\":452,\"foodType\":\"FOOD\"},{\"name\":\"Steak\",\"calories\":42,\"foodType\":\"FOOD\"},{\"name\":\"Mince Beef\",\"calories\":214,\"foodType\":\"FOOD\"},{\"name\":\"Chicken Breast\",\"calories\":148,\"foodType\":\"FOOD\"},{\"name\":\"Burger with Cheese\",\"calories\":530,\"foodType\":\"FOOD\"},{\"name\":\"Chicken Wings\",\"calories\":471,\"foodType\":\"FOOD\"},{\"name\":\"Chicken Nuggets\",\"calories\":510,\"foodType\":\"FOOD\"},{\"name\":\"White Fish (Baked)\",\"calories\":210,\"foodType\":\"FOOD\"},{\"name\":\"Pale Fish (Baked)\",\"calories\":230,\"foodType\":\"FOOD\"},{\"name\":\"Oily Fish (Baked)\",\"calories\":275,\"foodType\":\"FOOD\"},{\"name\":\"Cornflakes\",\"calories\":121,\"foodType\":\"FOOD\"},{\"name\":\"Coco Pops\",\"calories\":116,\"foodType\":\"FOOD\"},{\"name\":\"Rice Krispies\",\"calories\":115,\"foodType\":\"FOOD\"},{\"name\":\"Cheerios\",\"calories\":111,\"foodType\":\"FOOD\"},{\"name\":\"Special K\",\"calories\":114,\"foodType\":\"FOOD\"},{\"name\":\"Weetabix\",\"calories\":134,\"foodType\":\"FOOD\"},{\"name\":\"Shreddies\",\"calories\":111,\"foodType\":\"FOOD\"},{\"name\":\"Pizza (Slice)\",\"calories\":197,\"foodType\":\"FOOD\"},{\"name\":\"Chips\",\"calories\":380,\"foodType\":\"FOOD\"},{\"name\":\"Chocolate\",\"calories\":600,\"foodType\":\"FOOD\"},{\"name\":\"Crisps\",\"calories\":150,\"foodType\":\"FOOD\"},{\"name\":\"Yoghurt (plain)\",\"calories\":145,\"foodType\":\"FOOD\"},{\"name\":\"Lager\",\"calories\":223,\"foodType\":\"DRINK\"},{\"name\":\"Wine\",\"calories\":133,\"foodType\":\"DRINK\"},{\"name\":\"Cider\",\"calories\":197,\"foodType\":\"DRINK\"},{\"name\":\"Cream Liqueur\",\"calories\":153,\"foodType\":\"DRINK\"},{\"name\":\"Alcopop\",\"calories\":172,\"foodType\":\"DRINK\"},{\"name\":\"Water\",\"calories\":0,\"foodType\":\"DRINK\"},{\"name\":\"Tea (Milk+Sugar)\",\"calories\":44,\"foodType\":\"DRINK\"},{\"name\":\"Tea (Milk)\",\"calories\":19,\"foodType\":\"DRINK\"},{\"name\":\"Tea (Sugar)\",\"calories\":24,\"foodType\":\"DRINK\"},{\"name\":\"Tea\",\"calories\":1,\"foodType\":\"DRINK\"},{\"name\":\"Port\",\"calories\":77,\"foodType\":\"DRINK\"},{\"name\":\"Gin\",\"calories\":95,\"foodType\":\"DRINK\"},{\"name\":\"Vodka\",\"calories\":97,\"foodType\":\"DRINK\"},{\"name\":\"Orange Juice\",\"calories\":110,\"foodType\":\"DRINK\"},{\"name\":\"Whole Milk\",\"calories\":165,\"foodType\":\"DRINK\"},{\"name\":\"Cola\",\"calories\":60,\"foodType\":\"DRINK\"},{\"name\":\"Fanta\",\"calories\":33,\"foodType\":\"DRINK\"},{\"name\":\"Dr Pepper\",\"calories\":50,\"foodType\":\"DRINK\"},{\"name\":\"Coffee\",\"calories\":2,\"foodType\":\"DRINK\"},{\"name\":\"Energy Drink\",\"calories\":80,\"foodType\":\"DRINK\"}],\"groupIDList\":[]}";

            // Generating profile from pre-serialised profile
            Profile tempProfile = gson.fromJson(profileString, Profile.class);

            Random rand = new Random();

            // Generating 14 days of test data
            for (int k = 0; k < 14; k++)
            {
                // Generating a date and day
                LocalDate ld = LocalDate.now().minusDays(14 - k);
                Day day = new Day();
                day.setDate(ld);

                // Generating a meal with random food items and adding to the
                // day object
                Meal meal = new Meal("LUNCH");
                ArrayList<FoodItem> foods = tempProfile.getFoods();
                meal.addFoodItem(foods.get(rand.nextInt(foods.size())));
                meal.addFoodItem(foods.get(rand.nextInt(foods.size())));
                day.addMeal(meal);

                // Gettinga random exercise and adding to the day
                Exercise exercise;
                ArrayList<Activity> exerciseList = tempProfile.getActivity();

                Activity tempcaloriecount = exerciseList.get(rand.nextInt(exerciseList.size()));

                if (tempcaloriecount.getExerciseType().equals("Cardio"))
                {
                    // Constructor(exercise type, distance, time)
                    exercise = new Cardio(tempcaloriecount, 30, 30);
                }
                else
                {
                    // Constructor(exercise type, weight, sets, reps)
                    exercise = new Strength(tempcaloriecount, 50, 5, 15);
                }

                day.addExercise(exercise);

                tempProfile.addDay(day);
            }

            // LocalDate setDate, LocalDate targetDate, String activity, int startDistance, int startTime, int targetDistance, int targetTime
            LocalDate setDate = LocalDate.now();
            LocalDate targetDate = LocalDate.of(2020, 8, 31);
            int startDistance = 5;
            int startTime = 30;
            String activity = "RUNNING";
            int targetDistance = 10;
            int targetTime = 30;
            tempProfile.addCardioGoal(new CardioGoal(setDate, targetDate, activity, startDistance, startTime, targetDistance, targetTime, null));

            // Reserialising profile
            String addedProfileString = gson.toJson(tempProfile);

            // Test deserialisation
            Profile testProfileTwo = gson.fromJson(addedProfileString, Profile.class);

            splitLines.add(addedProfileString);

            // Generating the salt and hashing the the data with this salt
            String salt = generateSalt();
            splitLines.add(4, salt);
            splitLines.set(5, hashDataWithSalt(splitLines.get(5), salt));

//            // Hashing passwords and security question answers
//            splitLines[4] = hashData(splitLines[4]);
//            splitLines[6] = hashData(splitLines[6]);
//            splitLines[8] = hashData(splitLines[8]);

            //System.out.println(Arrays.toString(splitLines));

            String[] splitLinesArray = new String[splitLines.size()];

            // Convert back to array
            for (int j = 0; j < splitLines.size(); j++)
            {
                splitLinesArray[j] = splitLines.get(j);
            }

            //System.out.println("Testing profile: " + testProfileTwo);

//            System.out.println("\nTesting profile: ");
//            testProfileTwo.getDays().remove(0);
//            ArrayList<Day> testDays = testProfileTwo.getDays();
//            for(int x = 0; x < testDays.size(); x++) {
//                System.out.println(testDays.get(x));
//            }

            System.out.println(testProfileTwo.getCardioGoal());

            // Inserting the test data into the database
            db.insert(insertStatement, splitLinesArray);
        }
    }

    /**
     * Generate a random 32 byte salt and return it (to be used when hashing
     * the password data)
     *
     * @return A random 32 byte salt
     */
    public static String generateSalt()
    {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[32];
        random.nextBytes(salt);
        Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
        String saltStr = encoder.encodeToString(salt);
        return saltStr;
    }

    public static String hashDataWithSalt(String inputData, String salt)
    {
        //https://www.geeksforgeeks.org/sha-256-hash-in-java/
        MessageDigest md = null;

        try
        {
            md = MessageDigest.getInstance("SHA-256");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        String toHash = salt + inputData;

        byte[] digest = md.digest(toHash.getBytes(StandardCharsets.UTF_8));

        BigInteger number = new BigInteger(1, digest);
        StringBuilder hexString = new StringBuilder(number.toString(16));

        // Pad with leading zeros
        while (hexString.length() < 32)
        {
            hexString.insert(0, '0');
        }
        return hexString.toString();
    }

    public static void main(String[] args) throws SQLException
    {
        DatabaseManagement db = new DatabaseManagement();
        populateTable();

        // Getting data
        String queryTwo = "SELECT * FROM users WHERE email='fschirak0@imdb.com' or username='WadeGrigorian'";

        //ResultSet rsTwo = state.executeQuery(queryTwo);
//
//        ResultSet rsTwo = db.select(queryTwo);
//
//        while (rsTwo.next()) {
//            String user_id = rsTwo.getString("user_id");
//            String firstName = rsTwo.getString("first_name");
//            String lastName = rsTwo.getString("last_name");
//            String email = rsTwo.getString("email");
//            String username = rsTwo.getString("username");
//            String password = rsTwo.getString("password");
//            String question1_id = rsTwo.getString("question1_id");
//            String question1_answer = rsTwo.getString("question1_answer");
//            String question2_id = rsTwo.getString("question2_id");
//            String question2_answer = rsTwo.getString("question2_answer");
//
//            System.out.println(user_id + " " + firstName + " " + lastName
//                    + " " + email + " " + username+ " " + password + " " + question1_id + " " + question1_answer + " " + question2_id + " " + question2_answer);
//        }
    }
}
