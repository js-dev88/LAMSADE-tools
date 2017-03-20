package com.github.lamsadetools.setCoordinates;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Scanner;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.artofsolving.jodconverter.office.OfficeException;


public class SetCoordinates implements AbstractFileConvertor {
	
	
	   
	
	    public static void main(String [] args) throws Exception{  
	           
	        File source = new File("papier_en_tete.docx");  
	        File destinationFile = new File("papier_en_tete_CloneTest.docx");  
	        copy(source, destinationFile);
	        
	        String tableauQuestion[] = {"Your name ?", "Your first name?", "Your telephone number?", "Your mail?", "Your function?"};
	        Scanner sc = new Scanner(System.in);
	        UserDetails user= new UserDetails();
	        for(int i=0;  i <= tableauQuestion.length-1 ; i++ ){
	        	
	        	System.out.println(tableauQuestion[i]);
	        	
	        	if(tableauQuestion[i].equals("Your telephone number?")){
	        		
	        		
	        		
	        		user.setNumber(sc.nextLine());
	        	
	        	}else if(tableauQuestion[i].equals("Your name ?")){
	        			
	        			user.setName(sc.nextLine());
	        			
	        		}else if(tableauQuestion[i].equals("Your first name?")){
	        			
	        			user.setFirstName(sc.nextLine());
	        		
	        	}else if(tableauQuestion[i].equals("Your function?")){
	        	
	        			user.setFunction(sc.nextLine());
	        }else if(tableauQuestion[i].equals("Your mail?")){
	        	
	        			user.setEmail(sc.nextLine());
	        	
	        }
	        	}
	        

	       sc.close();
	        setDetails(source, destinationFile, user);
	        
	        System.out.println("Done");
	        
	           
	    } 
	    
	    public static void setDetails(File source, File destinationFile, UserDetails user ) throws IOException, InvalidFormatException{
	    	
	    	XWPFDocument doc = new XWPFDocument(OPCPackage.open("papier_en_tete.docx"));
	    	XWPFHeaderFooterPolicy policy = doc.getHeaderFooterPolicy();
	    	
	    	for (XWPFParagraph p : policy.getFirstPageHeader().getParagraphs()) {
	    	    List<XWPFRun> runs = p.getRuns();
	    	    if (runs != null) {
	    	        for (XWPFRun r : runs) {
	    	            String text = r.getText(0);
	    	            
	    	            if (text != null && text.contains("Prenom")) {
	    	                text = text.replace("Prenom", user.getFirstName());
	    	                
	    	                r.setText(text, 0);
	 
	    	            }
	    	            
	    	            else if(text != null && text.contains("Nom")){
		    	            text = text.replace("Nom", user.getName());
	    	                
	    	                r.setText(text, 0);
	    	                
		    	            }
	    	            
	    	            else if(text != null && text.contains("e-mail")){
		    	            text = text.replace("e-mail", user.getEmail());
	    	                
	    	                r.setText(text, 0);
	    	                
		    	            }
	    	            
	    	            
	    	            
	    	            else if(text != null && text.contains("tel.")){
		    	            text = text.replace("tel.", user.getNumber());
	    	                
	    	                r.setText(text, 0);
	    	                
		    	            }
	    	            
	    	            
	    	            
	    	            else if(text != null && text.contains("Fonction")){
		    	            text = text.replace("Fonction", user.getFunction());
	    	                
	    	                r.setText(text, 0);
	    	                
		    	            }
	    	            
	    	           
	    	        }
	    	    }
	    	}
	    //System.out.println("Would you like an HTML version ?");
	    //Scanner sc = new Scanner(System.in);
	    //String type = sc.nextLine();
	    
	    //if (type == "no"){
	    //	doc.write(new FileOutputStream("papier_en_tete_CloneTest.docx"));
	    //}
	    //else if (type == "yes"){
	    	doc.write(new FileOutputStream("papier_en_tete_CloneTest.docx"));
	    	
	    	long start = System.currentTimeMillis();

			// 1) Load DOCX into XWPFDocument
			InputStream is = new FileInputStream(new File("papier_en_tete_CloneTest.docx"));
			XWPFDocument document = new XWPFDocument(is);

			// 2) Prepare Html options
			XHTMLOptions options = XHTMLOptions.create();

			// 3) Convert XWPFDocument to HTML
			OutputStream out = new FileOutputStream(new File(
					"papier_en_tete_CloneTest.html"));
			XHTMLConverter.getInstance().convert(document, out, options);

			System.err.println("Generate papier_en_tete.html with "
					+ (System.currentTimeMillis() - start) + "ms");
	    }
	    
	    
	    
	    

	    	
	    	//File inputFile = new File("papier_en_tete_CloneTest.docx");
	    	//File outputFile = new File("papier_en_tete_CloneTest.odt");
	    	//DefaultOfficeManagerConfiguration configuration = new DefaultOfficeManagerConfiguration(); 
	        //configuration.setOfficeHome(new File("C:\\Program Files (x86)\\LibreOffice 5"));
	    	//configuration
	        
//	    	OfficeManager officeManager = new DefaultOfficeManagerConfiguration().buildOfficeManager(); officeManager.start();
//			OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
//	    	converter.convert(new File("papier_en_tete_CloneTest.docx"), new File("papier_en_tete_CloneTest.odt"));

//	    	officeManager.stop();
	    	
	    

	    public static void copy ( File source,  File target) throws IOException {  
	        FileChannel sourceChannel = null;  
	        FileChannel targetChannel =null;  
	        try {  
	            sourceChannel =new FileInputStream(source).getChannel();  
	            targetChannel=  new FileOutputStream(target).getChannel();  
	        targetChannel.transferFrom(sourceChannel, 0,  
	        sourceChannel.size());  
	        }  
	        finally {  
	        targetChannel.close();  
	        sourceChannel.close();  
	        }  
	        }

		@Override
		public void convertToOdt(File source, File destination) throws OfficeException {
			// TODO Auto-generated method stub
			
		}  
	           


}