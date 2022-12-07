import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdfparser.PDFParser;

import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class BasicFunctionality {
    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Users\\conno\\OneDrive\\Documents\\GitHub\\ProDec\\SyllabusScanner\\SylPDF\\MTH3111.pdf");
        String test = readPDF(file);
    }

    static String readPDF(File file) throws IOException {
        PDDocument document = Loader.loadPDF(file);
        PDFTextStripper pdfStripper = new PDFTextStripper();
        String text = pdfStripper.getText(document);
        document.close();
        return text;
    }
}
