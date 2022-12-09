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
    public static void main(String[] args) throws IOException, ParseException {
        File file = new File("C:\\Users\\conno\\OneDrive\\Documents\\GitHub\\ProDec\\SyllabusScanner\\SylPDF\\MTH3111.pdf");
        String test = readPDF(file);
        Map<String, List<Integer>> locations = getEventLocation(test.toLowerCase());
        findDates(locations, test);

        String input = "today is friday, december 9";
        String date = findDateInString(input);
        System.out.println(date); // Outputs: "12/9"

        input = "The date is 12/25";
        date = findDateInString(input);
        System.out.println(date); // Outputs: "12/25/2022"
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
    static void findDates(Map<String, List<Integer>> map, String input) {

        for (String key : map.keySet()) {
            List<Integer> indices = map.get(key);
            for (Integer index : indices) {
//                int lineAboveIndex = index - input.substring(0, index).lastIndexOf('\n');
//                String lineAbove = input.substring(lineAboveIndex, lineAboveIndex + input.substring(lineAboveIndex).indexOf('\n'));

                // having trouble with getting the line above the index
                try { // this is just for the first index as theres no way to check a line above if it doesn't exist
                    int lineAboveIndex2 = input.lastIndexOf("\n", index);
                    int lineAboveIndex1 = input.lastIndexOf("\n", lineAboveIndex2 - 1);
                    String lineAbove = input.substring(lineAboveIndex1, lineAboveIndex2);
                } catch (Exception e) {

                }
                try { // same thing again, line below could break so might as well wrap it since its existence isn't whats important its the scan on the relevant string
                    int lineBelowIndex = index + input.substring(index).indexOf('\n') + 1;
                    String lineBelow = input.substring(lineBelowIndex, lineBelowIndex + input.substring(lineBelowIndex).indexOf('\n'));
                } catch (Exception e) {

                }


                int newlineIndex1 = input.lastIndexOf("\n", index);
                int newlineIndex2 = input.indexOf("\n", index);
                String line = input.substring(newlineIndex1, newlineIndex2);


            }
        }
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
            // No date was found, check for a date in the format "XX/XX(/XXXX)"
            final String DATE_REGEX = "\\d{2}/\\d{2}(/\\d{4})?";
            Pattern pattern2 = Pattern.compile(DATE_REGEX);
            Matcher matcher2 = pattern2.matcher(input);

            if (matcher2.find()) {
                return matcher2.group();
            } else {
                return "";
            }
        }
    }
}