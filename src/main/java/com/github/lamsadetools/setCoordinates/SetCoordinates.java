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

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import com.github.lamsadetools.yearbookInfos.GetInfosFromYearbook;
import com.sun.tools.javac.util.Name;


public class SetCoordinates {
	
	
	final static Logger LOGGER = Logger.getLogger(SetCoordinates.class);
	// Set the properties file's path
	static final String path = "src/main/resources/com/github/lamsadetools/log4j.properties"; 
    
	
	    public static void main(String [] args) throws Exception{  

	    	PropertyConfigurator.configure(path);
	    	
	    	File source = new File("src/main/resources/com/github/lamsadetools/setCoordinates/papier_en_tete.docx");  
	        File destinationFile = new File("src/main/resources/com/github/lamsadetools/setCoordinates/papier_en_tete_CloneTest.docx");  
	        
	        String firstname = "Olivier";
			String surname= "CAILLOUX";
			
			GetInfosFromYearbook user = new GetInfosFromYearbook(firstname, surname);
	      
			
	        copy(source, destinationFile);
	        
	        setDetails(source, destinationFile, user, firstname, surname);
	        
	        System.out.println("Done");
	        
	           
	    } 
	    
	    public static void setDetails(File source, File destinationFile, GetInfosFromYearbook user, String firstname, String name ) throws IOException, InvalidFormatException{
	    	
	    	XWPFDocument doc = new XWPFDocument(OPCPackage.open("src/main/resources/com/github/lamsadetools/setCoordinates/papier_en_tete.docx"));
	    	XWPFHeaderFooterPolicy policy = doc.getHeaderFooterPolicy();
	    	
	    	for (XWPFParagraph p : policy.getFirstPageHeader().getParagraphs()) {
	    	    List<XWPFRun> runs = p.getRuns();
	    	    if (runs != null) {
	    	        for (XWPFRun r : runs) {
	    	            String text = r.getText(0);
	    	            
	    	            if (text != null && text.contains("Prenom")) {
	    	                text = text.replace("Prenom", firstname );
	    	                
	    	                r.setText(text, 0);
	 
	    	            }
	    	            
	    	            else if(text != null && text.contains("Nom")){
		    	            text = text.replace("Nom", name);
	    	                
	    	                r.setText(text, 0);
	    	                
		    	            }
	    	            
	    	            else if(text != null && text.contains("e-mail")){
		    	            text = text.replace("e-mail", user.getCourrier());
	    	                
	    	                r.setText(text, 0);
	    	                
		    	            }
	    	            
	    	            
	    	            
	    	            else if(text != null && text.contains("tel.")){
		    	            text = text.replace("tel.", user.getTelephone());
	    	                r.setText(text, 0);
	    	                
		    	            }
	    	            
	    	            
	    	            else if(text != null && text.contains("Fonction")){
		    	            text = text.replace("Fonction", user.getFonction());
	    	                
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
	    	//doc.write(new FileOutputStream("papier_en_tete_CloneTest.docx"));
	    	
	    	
	    }
	    
	    
	    
	    

	    	
	    	//File inputFile = new File("papier_en_tete_CloneTest.docx");
	    	//File outputFile = new File("papier_en_tete_CloneTest.odt");
	    	//DefaultOfficeManagerConfiguration configuration = new DefaultOfficeManagerConfiguration(); 
	        //configuration.setOfficeHome(new File("C:\\Program Files (x86)\\LibreOffice 5"));
	    	//configuration
	        
    	    /*OfficeManager officeManager = new DefaultOfficeManagerConfiguration().buildOfficeManager(); officeManager.start();
  		    OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
        	converter.convert(new File("papier_en_tete_CloneTest.docx"), new File("papier_en_tete_CloneTest.odt"));*/

//	    	officeManager.stop();
	    	
	    

		public static void copy ( File source,  File target) throws IOException {  
	        FileChannel sourceChannel = null;  
	        FileChannel targetChannel =null;  
	        try {  
	            sourceChannel =new FileInputStream(source).getChannel();  
	            targetChannel=  new FileOutputStream(target).getChannel();  
	            targetChannel.transferFrom(sourceChannel, 0,sourceChannel.size()); 
	        }finally{  
			        targetChannel.close();  
			        sourceChannel.close();  
		    }  
	    }

	
	           


}
