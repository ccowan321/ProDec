import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdfparser.PDFParser;

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class BasicFunctionality {
    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Users\\conno\\OneDrive\\Documents\\GitHub\\ProDec\\SyllabusScanner\\SylPDF\\MTH3111.pdf");
        String test = readPDF(file);
        Map<String, List<Integer>> locations = getEventLocation(test.toLowerCase());
        for (String keys: locations.keySet()){
            System.out.println(keys);
            for (int i:locations.get(keys)){
                System.out.print(i+" ");
            }
            System.out.println();
        }
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
    static Map<String, List<Integer>> getEventLocation(String syllabus){
        Map<String,List<Integer>> result = new HashMap<String, List<Integer>>();

        // OFFICE HOURS:
        int index = syllabus.indexOf("office hours");
        List<Integer> officeHoursList = new ArrayList<>();
        while (index >= 0){
            officeHoursList.add(new Integer(index));
            index= syllabus.indexOf("office hours", index+1);
        }
        result.put("Office Hours", officeHoursList);
        // OFFICE HOURS:

        // EXAMS:
        List<Integer> examList = new ArrayList<>();
        String[] testVariants = {"test", "exam", "midterm"};
        for (int i=0; i< testVariants.length; i++){
            index = syllabus.indexOf(testVariants[i]);
            while(index>=0){
                examList.add(new Integer(index));
                index = syllabus.indexOf(testVariants[i], index+1);
            }
        }
        result.put("Test",examList);
        // EXAMS:

        // HOMEWORK:
        List<Integer> homeworkList = new ArrayList<>();
        String[] homeworkVariants = {"homework", "home work"};
        for (int i=0; i< homeworkVariants.length; i++){
            index = syllabus.indexOf(homeworkVariants[i]);
            while (index>=0){
                homeworkList.add(new Integer(index));

                index = syllabus.indexOf(homeworkVariants[i],index+1);
            }
        }
        result.put("Homework",homeworkList);
        //HOMEWORK:

        // PROJECTS:
        List<Integer> projectList = new ArrayList<>();
        String[] projectVariants = {"project"};
        for (int i=0;i<projectVariants.length; i++){
            index = syllabus.indexOf(projectVariants[i]);
            while(index>=0){
                projectList.add(new Integer(index));
                index=syllabus.indexOf(projectVariants[i],index+1);
            }
        }
        result.put("Project",projectList);


        return result;
    }
    // Now we need a method to get nearby dates, I'm thinking the closest date one line up or three lines down
    static Map<String,String> getDates(Map<String,List<Integer>> input){
        Map<String,String> eventNType = new HashMap<>();


        return eventNType;
    }
}
