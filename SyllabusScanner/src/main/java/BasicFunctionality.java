import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdfparser.PDFParser;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class BasicFunctionality {
    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Users\\conno\\OneDrive\\Documents\\GitHub\\ProDec\\SyllabusScanner\\SylPDF\\MTH3111.pdf");
        String test = readPDF(file);
        Map<String, List<Integer>> locations = getEventLocation(test.toLowerCase());
        findDates(locations, test);


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
                    System.out.println("LINE ABOVE: ");
                    System.out.println(lineAbove);
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

    public static String findDateInString(String input) {
        // Create a Pattern object to match the regular expression
        final String DATE_REGEX = "\\d{2}/\\d{2}(/\\d{4})?";
        Pattern pattern = Pattern.compile(DATE_REGEX);

        // Create a Matcher object to search the input string for matches to the regular expression
        Matcher matcher = pattern.matcher(input);

        // If a match is found, return the matched date in its original format
        if (matcher.find()) {
            return matcher.group();
        }

        // If no match is found, return an empty string
        return "";
    }
}