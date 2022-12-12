import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdfparser.PDFParser;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class BasicFunctionality {
    //TODO: MWF notation, then break it down into object form
    public static void main(String[] args) throws IOException, ParseException {
        File file = new File("C:\\Users\\conno\\OneDrive\\Documents\\GitHub\\ProDec\\SyllabusScanner\\SylPDF\\MTH3111.pdf");
        String test = readPDF(file);
        Map<String, List<Integer>> locations = getEventLocation(test.toLowerCase());
        for (String key: locations.keySet()){
            List<Integer> indices = locations.get(key);
            System.out.println(key);
            for (Integer i: indices){
                System.out.print(i+" ");
            }
            System.out.println();
        }

        Map<String, List<String>> checkOutput = findDates(locations, test);
        for (String key: checkOutput.keySet()){
            List<String> indices = checkOutput.get(key);
            System.out.println(key);
            for (String s:indices){
                System.out.println(s);
            }
            System.out.println();
        }


//        System.out.println(findDateInString(" 6/7/13 Syllabus |  MTH 311 |  Summer I 2013"));
//        System.out.println(findDateInString("s on friday, july 5"));
//        System.out.println(findDateInString("12/5/2000"));
//        System.out.println(findDateInString("1/5/20"));
//        System.out.println(findDateInString("12/5"));
//        System.out.println(findDateInString("5/12"));
//        System.out.println(findDateInString("5/12/20"));
//        System.out.println(findDateInString("/"));
        // now I know that it is properly finding dates I have to assess why it isnt finding dates in the strings I've given the code


    }

    static String readPDF(File file) throws IOException {
        PDDocument document = Loader.loadPDF(file);
        PDFTextStripper pdfStripper = new PDFTextStripper();
        String text = pdfStripper.getText(document);
        document.close();
        return text;
    }

    // Want to add a function that will look through and find the index of specific strings and store what string they are
    // associated with
    // probably would return a map of some sort <String, int>
    //
    static Map<String, List<Integer>> getEventLocation(String syllabus) {
        Map<String, List<Integer>> result = new HashMap<String, List<Integer>>();

        // OFFICE HOURS:
        int index = syllabus.indexOf("office hours");
        List<Integer> officeHoursList = new ArrayList<>();
        while (index >= 0) {
            officeHoursList.add(new Integer(index));
            index = syllabus.indexOf("office hours", index + 1);
        }
        result.put("Office Hours", officeHoursList);
        // OFFICE HOURS:

        // EXAMS:
        List<Integer> examList = new ArrayList<>();
        String[] testVariants = {"test", "exam", "midterm"};
        for (int i = 0; i < testVariants.length; i++) {
            index = syllabus.indexOf(testVariants[i]);
            while (index >= 0) {
                examList.add(new Integer(index));
                index = syllabus.indexOf(testVariants[i], index + 1);
            }
        }
        result.put("Test", examList);
        // EXAMS:

        // HOMEWORK:
        List<Integer> homeworkList = new ArrayList<>();
        String[] homeworkVariants = {"homework", "home work"};
        for (int i = 0; i < homeworkVariants.length; i++) {
            index = syllabus.indexOf(homeworkVariants[i]);
            while (index >= 0) {
                homeworkList.add(new Integer(index));

                index = syllabus.indexOf(homeworkVariants[i], index + 1);
            }
        }
        result.put("Homework", homeworkList);
        //HOMEWORK:

        // PROJECTS:
        List<Integer> projectList = new ArrayList<>();
        String[] projectVariants = {"project"};
        for (int i = 0; i < projectVariants.length; i++) {
            index = syllabus.indexOf(projectVariants[i]);
            while (index >= 0) {
                projectList.add(new Integer(index));
                index = syllabus.indexOf(projectVariants[i], index + 1);
            }
        }
        result.put("Project", projectList);


        return result;
    }

    // Now we need a method to get nearby dates, I'm thinking the closest date one line up or three lines down
    // have to think of all of the ways a date might be written: DayOfWeek, Month | Day/Month | Day/Month/Year
    static Map<String, List<String>> findDates(Map<String, List<Integer>> map, String input) throws ParseException {
        Map<String, List<String>> eventDates= new HashMap<String, List<String>>();
        input = input.toLowerCase();
        for (String key : map.keySet()) {
            List<String> tempStrings = new ArrayList<>();
            List<Integer> indices = map.get(key);
            for (Integer index : indices) {

                int newlineIndex1 = input.lastIndexOf("\n", index-1);
                int newlineIndex2 = input.indexOf("\n", index+1);
                String line = input.substring(newlineIndex1, newlineIndex2);
                line = line.toLowerCase();

                if(findDateInString(line).length()!=0){
                    tempStrings.add(findDateInString(line));

                }

                try { // this is just for the first index as theres no way to check a line above if it doesn't exist
                    int lineAboveIndex2 = input.lastIndexOf("\n", index);
                    int lineAboveIndex1 = input.lastIndexOf("\n", lineAboveIndex2 - 1);
                    String lineAbove = input.substring(lineAboveIndex1, lineAboveIndex2);
                    lineAbove = lineAbove.toLowerCase();

                    if (findDateInString(lineAbove).length()!=0){
                        tempStrings.add(findDateInString(lineAbove));
                    }
                } catch (Exception e) {

                }
                try {
                    int lineBelowIndex = index + input.substring(index).indexOf('\n') + 1;
                    String lineBelow = input.substring(lineBelowIndex, lineBelowIndex + input.substring(lineBelowIndex).indexOf('\n'));
                    lineBelow = lineBelow.toLowerCase();
                    if (findDateInString(lineBelow).length()!=0){
                        tempStrings.add(findDateInString(lineBelow));
                    }
                } catch (Exception e) {

                }
            }
            eventDates.put(key, tempStrings);
        }
        return eventDates;
    }

    public static String findDateInString(String input) throws ParseException {
        // Define the date pattern that we are looking for

        String datePattern = "\\b[a-z]{3,}, [a-z]{3,} \\d{1,2}\\b";

        // Create a Pattern object
        Pattern pattern = Pattern.compile(datePattern);

        // Use the pattern to create a matcher object
        Matcher matcher = pattern.matcher(input);

        // Check if a match was found
        if (matcher.find()) {
            // Get the matched string
            String matched = matcher.group();

            // Split the matched string into its parts
            String[] parts = matched.split(", ");

            // Get the month and day parts
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