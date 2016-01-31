package devloop0.cs61a.org.cs61a;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by nathr on 1/27/2016.
 */
public class EE16ABParser extends DictionaryParser {
    String source;
    ArrayList<String[]> assignmentList;
    PreferenceHolder preferenceHolder = null;
    String season = null, year = null;
    String ee16a_b = null;

    public EE16ABParser(String src, PreferenceHolder ph) {
        source = src;
        assignmentList = new ArrayList<String[]>();
        preferenceHolder = ph;
        season = ph.getSeason() == PreferenceHolder.SeasonKind.KIND_FALL ? "fa" : "sp";
        year = ph.getTwoDigitYear();
        ee16a_b = ph.getCurrentClass();
        parsePageSource();
    }

    public ArrayList<String[]> getAssignments() {
        return assignmentList;
    }

    private void parsePageSource() {
        String startRelevantPart = source.substring(source.indexOf("<h2>Calendar</h2>") + 1);
        int start = 1;
        while(true) {
            String wk = new Integer(start).toString();
            if(wk.length() == 1)
                wk = "0" + wk;
            wk = "<tbody id='wk" + wk + "'>";
            if(!startRelevantPart.contains(wk))
                break;
            startRelevantPart = startRelevantPart.substring(startRelevantPart.indexOf(wk) + wk.length() + 1); // keep trimming the relevant part of the page
            String dateInfo = startRelevantPart.substring(startRelevantPart.indexOf("<td>"));
            dateInfo = dateInfo.substring(new String("<td>").length());
            startRelevantPart = dateInfo;
            dateInfo = dateInfo.substring(0, dateInfo.indexOf('<'));
            String[] split = dateInfo.split(" ");
            String actualDate = split[0];
            actualDate += "/" + Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yy");
            String dueDate = "";
            Calendar calendar = Calendar.getInstance();
            try {
                calendar.setTime(simpleDateFormat.parse(actualDate));
                calendar.add(Calendar.DATE, 7);
                dueDate = simpleDateFormat.format(calendar.getTime());
            }
            catch (ParseException ex) {
                ex.printStackTrace();
            }
            String hwSearch = "<a href=\"#homework\">";
            if(!startRelevantPart.contains(hwSearch))
                break;
            startRelevantPart = startRelevantPart.substring(startRelevantPart.indexOf(hwSearch) + hwSearch.length());
            String assignmentName = startRelevantPart.substring(0, startRelevantPart.indexOf('<'));
            assignmentName = assignmentName.trim();
            Log.i("AssignmentName", assignmentName);
            String[] assignmentSplit = assignmentName.split(" ");
            String assignmentNumber = "";
            if(assignmentSplit.length > 1)
                assignmentNumber = assignmentSplit[1];
            // At this point, we have everything we can get, just populate the ArrayList with the info
            assignmentList.add(new String[]{assignmentName, actualDate, dueDate, "Homework", "http://inst.eecs.berkeley.edu/~" + ee16a_b + "/" + season + year + "/hw/hw" + assignmentNumber + "/prob" + assignmentNumber + ".pdf",
                    "Homework"});
            start += 1;
        }
    }
}
