import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.util.Units;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;

public class Test2 {
	
	public static void main(String[] args) {
		
		
		 File file = null;
		 FileInputStream fis = null;
		  
		file = new File("papier_a_en_tete3.docx");
		
		 try {
			 
			 
			 

		        fis = new FileInputStream(file.getAbsolutePath());
		        
				XWPFDocument docx = new XWPFDocument(fis);
			        CTSectPr sectPr = docx.getDocument().getBody().addNewSectPr();
				XWPFHeaderFooterPolicy policy = new XWPFHeaderFooterPolicy(docx, sectPr);
					
				//write header content
				CTP ctpHeader = CTP.Factory.newInstance();
			        CTR ctrHeader = ctpHeader.addNewR();
				CTText ctHeader = ctrHeader.addNewT();
				String headerText = "This is header";
				ctHeader.setStringValue(headerText);
			
				XWPFParagraph headerParagraph = new XWPFParagraph(ctpHeader, docx);
			        XWPFParagraph[] parsHeader = new XWPFParagraph[1];
			        parsHeader[0] = headerParagraph;
			        policy.createHeader(XWPFHeaderFooterPolicy.DEFAULT, parsHeader);
			        
			        
			        
				//write footer content
				CTP ctpFooter = CTP.Factory.newInstance();
				CTR ctrFooter = ctpFooter.addNewR();
				CTText ctFooter = ctrFooter.addNewT();
				String footerText = "This is footer";
				ctFooter.setStringValue(footerText);	
				XWPFParagraph footerParagraph = new XWPFParagraph(ctpFooter, docx);
			        XWPFParagraph[] parsFooter = new XWPFParagraph[1];
			        parsFooter[0] = footerParagraph;
				policy.createFooter(XWPFHeaderFooterPolicy.DEFAULT, parsFooter);
					
				//write body content
				XWPFParagraph bodyParagraph = docx.createParagraph();
				bodyParagraph.setAlignment(ParagraphAlignment.LEFT);
				XWPFRun r = bodyParagraph.createRun();
				r.setBold(true);
			        r.setText("This is body content.");
			        String imgFile="Logo fond bleu.jpg";
			        r.addPicture(new FileInputStream(imgFile), XWPFDocument.PICTURE_TYPE_JPEG, imgFile, Units.toEMU(50), Units.toEMU(50));
			        
			       
			       
					
			        FileOutputStream out = new FileOutputStream("write-test.docx");
			        docx.write(out);
			        out.close();
			        System.out.println("Done");
			    } catch (Exception ex) {
				   ex.printStackTrace();
			    }
		
		
		
		
		
		
		
		
		
		
		
		
	}

}
