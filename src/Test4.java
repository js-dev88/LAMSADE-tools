import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class Test4 {
	
	public static void main(String[] args) {
		
		try {
	        InputStream is = new FileInputStream("papier_en_tete.docx"); 
	        XWPFDocument doc = new XWPFDocument(is);

	        List<XWPFParagraph> paras = doc.getParagraphs();  

	        XWPFDocument newdoc = new XWPFDocument();                                     
	        for (XWPFParagraph para : paras) {  

	            if (!para.getParagraphText().isEmpty()) {       
	                XWPFParagraph newpara = newdoc.createParagraph();
	                copyAllRunsToAnotherParagraph(para, newpara);
	            }

	        }

	        FileOutputStream fos = new FileOutputStream(new File("papier_en_tete.docx"));
	        newdoc.write(fos);
	        fos.flush();
	        fos.close();
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	// Copy all runs from one paragraph to another, keeping the style unchanged
	private static void copyAllRunsToAnotherParagraph(XWPFParagraph oldPar, XWPFParagraph newPar) {
	    final int DEFAULT_FONT_SIZE = 10;

	    for (XWPFRun run : oldPar.getRuns()) {  
	        String textInRun = run.getText(0);
	        String toto ="je suis un bg de ouf plppmp";
	        if (textInRun == null || textInRun.isEmpty()) {
	            continue;
	        }

	        int fontSize = run.getFontSize();
	        System.out.println("run text = '" + textInRun + "' , fontSize = " + fontSize); 

	        XWPFRun newRun = newPar.createRun();

	        // Copy text
	        newRun.setText(toto);

	        // Apply the same style
	        newRun.setFontSize( ( fontSize == -1) ? DEFAULT_FONT_SIZE : run.getFontSize() );    
	        newRun.setFontFamily( run.getFontFamily() );
	        newRun.setBold( run.isBold() );
	        newRun.setItalic( run.isItalic() );
	        newRun.setStrike( run.isStrike() );
	        newRun.setColor( run.getColor() );
	    }   
	

}
}
