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
        // The problem here is that there are so many methods of saying exams: test, midterm, exam, final
        // I'm not sure if the best method here is to just check each of them individually or not
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
        return result;
    }
}
