package com.github.lamsadetools.yearbookInfos;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.sun.star.lang.IllegalArgumentException;

/**
 * This class let the application connect to a professor in the Yearbook
 * You can access to the html page in a String format from another class
 * @author Julien Saussier
 *
 */
public class ConnectionToYearbook {
		
		// Set logger
		final static Logger LOGGER = Logger.getLogger(ConnectionToYearbook.class);
		// Set the properties file's path
		static final String path = "src/main/resources/com/github/lamsadetools/log4j.properties";
		//html page from Yearbook
		private String htmlPage;
		
		/**
		 * 
		 * @param firstname may not be null
		 * @param surname may not be null
		 * @throws IllegalArgumentException if null parameters
		 */
	    public ConnectionToYearbook(String firstname, String surname) throws IllegalArgumentException {
			
			if(firstname == null || surname == null){
				throw new IllegalArgumentException("Firstname or surname is null");
			}
			
			//Build the URL parameter with the firstname's first letter and the person's surname
			String param = firstname.toLowerCase().charAt(0)+surname.toLowerCase();
			String param0 = "fiche";
			//This part of the url is always the same
			String urlConstantPArt = "https://www.ent.dauphine.fr/Annuaire/index.php?";
			//First step of the JAX rs class => Client initialization
			Client client = ClientBuilder.newClient();
			//Build the targeted URL
			WebTarget t1 = client.target(urlConstantPArt)
					             .queryParam("param0", param0)
						         .queryParam("param1", param);
			//The entire HTML page is stocked in result
			try{
				htmlPageFiller(t1);
			}catch(Throwable t){
				PropertyConfigurator.configure(path);
				LOGGER.error(t);
			}

	        client.close();
		}
	    
	    /**
	     * 
	     * @param webtrgt is the webtarget (url with parameters)
	     * @return a filled htmlpage
	     * @throws IllegalArgumentException if webtarget is null
	     * @throws NullPointerException if no data found
	     */
	    private String htmlPageFiller(WebTarget webtrgt) throws  IllegalArgumentException,NullPointerException{
	 
	    	if(webtrgt == null){
	    		throw new IllegalArgumentException("Web Target is null, error during the target construction");
	    	}
	    	
	    	htmlPage = webtrgt.request(MediaType.TEXT_PLAIN).get(String.class);
	    	
	    	if(htmlPage == null || htmlPage.isEmpty()){
				throw new NullPointerException("htmlPage has no data, the yearbook site may be down");
			}else{
				return htmlPage;
			}
	    }
	    
	    /**
	     * @return the htmlPage in  a string format
	     */
	    
	    public String getHtmlPage(){
			return htmlPage;
		}
	    

}
