package main.utility;

import java.util.Calendar;


public class CustomTimeMessage {
    /**
     * Returns a String object which represents the greeting
     * according to the current time.
     * @return A string greeting the user according by the current time
     * @see main.model.Message
    */
    public String getMessage() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        if (hour > 5 && hour < 12) {
            return "Good morning";
        } else if (hour < 17) {
            return "Good afternoon";
        } else if (hour < 19) {
            return "Good evening";
        } else {
            return "Good night";
        }
    }
}
