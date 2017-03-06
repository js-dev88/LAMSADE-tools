import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabStop;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabJc;

public class test3 {

	
	public static void main(String[] args) throws InvalidFormatException, FileNotFoundException, IOException {
		
		
		
		 XWPFDocument doc= new XWPFDocument();
		 

		  // the body content
		  XWPFParagraph paragraph = doc.createParagraph();
		  XWPFRun run=paragraph.createRun();  
		  run.setText("The Body:");

		  paragraph = doc.createParagraph();
		  run=paragraph.createRun();  
		  run.setText("Lorem ipsum....");
		  run.setText("This is body content.");
	        String imgFile1="toto.png";
	        run.addPicture(new FileInputStream(imgFile1), XWPFDocument.PICTURE_TYPE_JPEG, imgFile1, Units.toEMU(50), Units.toEMU(50));
	        paragraph.setAlignment(ParagraphAlignment.RIGHT);

	        
	        
	        
		  // create header start
		  CTSectPr sectPr = doc.getDocument().getBody().addNewSectPr();
		  XWPFHeaderFooterPolicy headerFooterPolicy = new XWPFHeaderFooterPolicy(doc, sectPr);

		  XWPFHeader header = null;
		try {
			header = toto();
			headerFooterPolicy.createHeader(XWPFHeaderFooterPolicy.FIRST);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		  paragraph = header.
		//paragraph.setAlignment(ParagraphAlignment.LEFT);

		  CTTabStop tabStop = paragraph.getCTP().getPPr().addNewTabs().addNewTab();
		  tabStop.setVal(STTabJc.CENTER);
		  int twipsPerInch =  1440;
		  tabStop.setPos(BigInteger.valueOf(6 * twipsPerInch));

		  run = paragraph.createRun(); 
		  
		 
		  run.setText("The Header:");
		  run.addTab();

		  run = paragraph.createRun();  
		  //String imgFile="toto.png";
		 // run.addPicture(new FileInputStream(imgFile), XWPFDocument.PICTURE_TYPE_JPEG, imgFile, Units.PIXEL_DPI, Units.PIXEL_DPI);


		  
		  
		  
		  
		  // create footer start
		  XWPFFooter footer = headerFooterPolicy.createFooter(XWPFHeaderFooterPolicy.DEFAULT);

		  paragraph = footer.getParagraphArray(0);
		  paragraph.setAlignment(ParagraphAlignment.CENTER);

		  run = paragraph.createRun();  
		  run.setText("The Footer:");
		  
		  String imgFile3="toto.png";
		  
		  run.addBreak();
		  
		 
		  run.addPicture(new FileInputStream(imgFile3), XWPFDocument.PICTURE_TYPE_JPEG, imgFile3, Units.toEMU(50), Units.toEMU(50));

		  doc.write(new FileOutputStream("test.docx"));
		  System.out.println("done");
		
	}
	
	
	public static void cloneParagraph(XWPFParagraph clone, XWPFParagraph source) {
	    CTPPr pPr = clone.getCTP().isSetPPr() ? clone.getCTP().getPPr() : clone.getCTP().addNewPPr();
	    pPr.set(source.getCTP().getPPr());
	    for (XWPFRun r : source.getRuns()) {
	        XWPFRun nr = clone.createRun();
	        cloneRun(nr, r);
	    }
	}

	public static void cloneRun(XWPFRun clone, XWPFRun source) {
	    CTRPr rPr = clone.getCTR().isSetRPr() ? clone.getCTR().getRPr() : clone.getCTR().addNewRPr();
	    rPr.set(source.getCTR().getRPr());
	    clone.setText(source.getText(0));
	}
	
	public static XWPFHeader toto() throws IOException{
		
		FileInputStream fis = new FileInputStream("papier_en_tete.docx");
		
		XWPFDocument docx = new XWPFDocument(fis);
		XWPFHeader paragraphList = docx.getHeaderFooterPolicy().getFirstPageHeader();
		return paragraphList;
		

		
	}
	
}
