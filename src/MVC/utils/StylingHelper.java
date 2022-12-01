/*
Name:           StylingHelper.java

Authors:        Chris, Conor, Harry, Milo, Yacine

Description:    Small util class containing a static method to style progress
                bars contained within the application.
*/
package MVC.utils;

import javafx.scene.control.ProgressBar;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StylingHelper
{
    /**
     * Sets the progress bar passed to colour depending on value passed
     * Also sets the value for the progress bar
     *
     * @param pb    progress bar passed
     * @param value value to put in progress bar, between 0.0 and 1.0
     */
    public static void setProgressBarProgressAndColour(ProgressBar pb, double value)
    {
        pb.setProgress(value);

        if (value <= 0.2)
        {
            pb.setStyle("-fx-accent: red");
        }
        else if (value <= 0.4)
        {
            pb.setStyle("-fx-accent: orange");
        }
        else if (value <= 0.6)
        {
            pb.setStyle("-fx-accent: gold");
        }
        else if (value <= 0.8)
        {
            pb.setStyle("-fx-accent: yellowgreen");
        }
        else
        {
            pb.setStyle("-fx-accent: green");
        }
    }

    /**
     * Function to format a date in the format: DD/MM/YYYY
     *
     * @param date date to be format
     * @return string containing the date in DD/MM/YYYY format
     */
    public static String formatDate(LocalDate date)
    {
        return date.format(DateTimeFormatter.ofPattern("dd-MM-uuuu"));
    }
}
