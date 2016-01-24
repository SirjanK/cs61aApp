package devloop0.cs61a.org.cs61a;

import java.util.ArrayList;

/**
 * Created by nathr on 1/6/2016.
 */

// every dictionary parser added in the future must extend this.
public abstract class DictionaryParser {
    public abstract ArrayList<String[]> getAssignments();

    public String processDate(String date) {
        int firstSlash = date.indexOf('/');
        if (date.substring(0, firstSlash).length() == 1) {
            date = "0" + date;
            firstSlash++;
        }

        int secondSlash = date.indexOf('/');

        if (date.substring(firstSlash, secondSlash).length() == 1)
            date = date.substring(0, firstSlash + 1) + "0" + date.substring(secondSlash - 1);

        return date;
    }
}
