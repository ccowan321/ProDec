package syllander.data;

import org.springframework.stereotype.Repository;
import syllander.models.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Repository
public class EventRepository {
    //private static String input;
    public EventRepository() { // should just be a null constructor that takes nothing, this is more of a dump of methods than anything
    }
    public List<Event> generateEventList(String input) throws ParseException {
        Map<String, List<Integer>> locations = getEventLocation(input);
        List<Event> result = findDates(locations,input);
        return result;
    }
    public static LocalDate parseDate(String s){
    LocalDate temp;
    s =s.replace(" ","");
    String[] dateParts = s.split("/");

    String year;
    String month = dateParts[0];
    if (month.length()==1){
        month = "0"+month;
    }
    String day = dateParts[1];
    if (day.length()==1){
        day = "0"+day;
    }
    try{
        year = dateParts[2];
        if (year.length()==4){
            year = year.charAt(2)+""+year.charAt(3);
        }
    } catch (Exception e){
        year = "22";
    }

    String generalizedFormat = month+"/"+day+"/"+year;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy",Locale.US);

    try{
        temp = LocalDate.parse(generalizedFormat, formatter);
    } catch (Exception e){
        return null;
    }
    // generalize the string patterns break them up etc into the format mm-dd-yy
    return temp;
    }
    public static List<Event> findDates(Map<String, List<Integer>> map, String input) throws ParseException {
        List<Event> result = new ArrayList<>();
        input = input.toLowerCase();
        for (String key : map.keySet()) {
            List<String> tempStrings = new ArrayList<>();
            List<Integer> indices = map.get(key);
            for (Integer index : indices) {
                try{
                    int newlineIndex1 = input.lastIndexOf("\n", index-1);
                    int newlineIndex2 = input.indexOf("\n", index+1);
                    String line = input.substring(newlineIndex1, newlineIndex2);
                    line = line.toLowerCase();

                    if(findDateInString(line).length()!=0){
                        //tempStrings.add(findDateInString(line));
                        result.add(new Event(key, parseDate(findDateInString(line)))); // have to convert the string found into a local date
                    }
                }
                catch (Exception e){

                }


                try { // this is just for the first index as theres no way to check a line above if it doesn't exist
                    int lineAboveIndex2 = input.lastIndexOf("\n", index);
                    int lineAboveIndex1 = input.lastIndexOf("\n", lineAboveIndex2 - 1);
                    String lineAbove = input.substring(lineAboveIndex1, lineAboveIndex2);
                    lineAbove = lineAbove.toLowerCase();

                    if (findDateInString(lineAbove).length()!=0){
                        //tempStrings.add(findDateInString(lineAbove));
                        result.add(new Event(key,parseDate(findDateInString(lineAbove))));
                    }
                } catch (Exception e) {

                }
                try {
                    int lineBelowIndex = index + input.substring(index).indexOf('\n') + 1;
                    String lineBelow = input.substring(lineBelowIndex, lineBelowIndex + input.substring(lineBelowIndex).indexOf('\n'));
                    lineBelow = lineBelow.toLowerCase();
                    if (findDateInString(lineBelow).length()!=0){
                       // tempStrings.add(findDateInString(lineBelow));
                        result.add(new Event(key,parseDate(findDateInString(lineBelow))));
                    }
                } catch (Exception e) {

                }
            }

        }
        return result;
    }

    public static Map<String, List<Integer>> getEventLocation(String input) {
        Map<String, List<Integer>> result = new HashMap<String, List<Integer>>();

        // OFFICE HOURS:
        int index = input.indexOf("office hours");
        List<Integer> officeHoursList = new ArrayList<>();
        while (index >= 0) {
            officeHoursList.add(new Integer(index));
            index = input.indexOf("office hours", index + 1);
        }
        result.put("Office Hours", officeHoursList);
        // OFFICE HOURS:

        // EXAMS:
        List<Integer> examList = new ArrayList<>();
        String[] testVariants = {"test", "exam", "midterm"};
        for (int i = 0; i < testVariants.length; i++) {
            index = input.indexOf(testVariants[i]);
            while (index >= 0) {
                examList.add(new Integer(index));
                index = input.indexOf(testVariants[i], index + 1);
            }
        }
        result.put("Test", examList);
        // EXAMS:

        // HOMEWORK:
        List<Integer> homeworkList = new ArrayList<>();
        String[] homeworkVariants = {"homework", "home work"};
        for (int i = 0; i < homeworkVariants.length; i++) {
            index = input.indexOf(homeworkVariants[i]);
            while (index >= 0) {
                homeworkList.add(new Integer(index));

                index = input.indexOf(homeworkVariants[i], index + 1);
            }
        }
        result.put("Homework", homeworkList);
        //HOMEWORK:

        // PROJECTS:
        List<Integer> projectList = new ArrayList<>();
        String[] projectVariants = {"project"};
        for (int i = 0; i < projectVariants.length; i++) {
            index = input.indexOf(projectVariants[i]);
            while (index >= 0) {
                projectList.add(new Integer(index));
                index = input.indexOf(projectVariants[i], index + 1);
            }
        }
        result.put("Project", projectList);


        return result;
    }

    public static String findDateInString(String input) throws ParseException {
        String datePattern = "\\b[a-z]{3,}, [a-z]{3,} \\d{1,2}\\b";
        Pattern pattern = Pattern.compile(datePattern);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            String matched = matcher.group();
            String[] parts = matched.split(", ");

            String month = parts[1].split(" ")[0];
            String day = parts[1].split(" ")[1];
            month = month.substring(0,1).toUpperCase() + month.substring(1);

            try{ // have to put this try catch here, because the regex isn't 100 percent effective and may give bad values
                Date date = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(month);
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                int monthnum = cal.get(Calendar.MONTH);
                // seems like I have to reconstruct the value of month so that the first letter is uppercase
                return monthnum+1 + "/" + day;
            } catch (Exception e){
                return "";
            }

        } else {
            String datePattern2 = "\\s*\\d+/\\d+/\\d+\\s*";
            Pattern pattern2 = Pattern.compile(datePattern2);
            Matcher matcher2 = pattern2.matcher(input);
            if (matcher2.find()){
                return matcher2.group();
            } else{
                String datePattern3 = "\\s*\\d/\\d\\s*";
                Pattern pattern3 = Pattern.compile(datePattern3);
                Matcher matcher3 = pattern3.matcher(input);
                if (matcher3.find()){
                    return matcher3.group();
                } else{
                    return "";
                }
            }
        }
    }


}
