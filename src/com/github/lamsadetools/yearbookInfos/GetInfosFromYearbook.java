package com.github.lamsadetools.yearbookInfos;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * GetInfosFromYearbook will get information from Dauphine's yearbook
 * It needs the name and the surname of the person you want to get information on OR
 * the URL corresponding to the person's Dauphine yearbook page.
 * @author Abdelkader ZEROUALI
 *
 */
public class GetInfosFromYearbook {
	private URL url;
	private HashMap<String, String> informations = new HashMap<String, String>();
	
	
	/**
	 * Constructor using a person's Dauphine yearbook URL
	 * @param url
	 * @throws IOException 
	 */
	public GetInfosFromYearbook(URL url) throws IOException{
		this.url= url;
		RetrieveYearbookData(url);
	}
	
	/**
	 * Constructor using a person's firstname and surname
	 * @param url
	 * @throws IOException 
	 */
	public GetInfosFromYearbook(String firstname, String surname) throws IOException,MalformedURLException  {
		assert(firstname != null && surname !=null);
		try {
			url = new URL("https://www.ent.dauphine.fr/Annuaire/index.php?param0=fiche&param1=" + firstname.toLowerCase().charAt(0)+surname.toLowerCase());
		} catch (MalformedURLException e) {
			throw new MalformedURLException ("Error when trying to reach URL");
		}
		RetrieveYearbookData(url);		
	}
	
	/**
	 * RetrieveYearbookData takes a URL in parameter. It will connect to this URL and
	 * read the entire HTML code of the web page
	 * @param url
	 * @throws IOException 
	 */
	public void RetrieveYearbookData (URL url) throws IOException{
		URLConnection connection;
		InputStream is = null;
		try {
			connection = url.openConnection();
			is = connection.getInputStream();
		} catch (IOException e1) {
			throw new IOException("Data not Found");
		}
		
        BufferedReader br = new BufferedReader(new InputStreamReader(is,StandardCharsets.UTF_8));

        String line = null;
        String nextLine =null;
    	ArrayList<String> rawInfos = new ArrayList<String>();

        // read each line and add it into rawInfos
        try {
			while ((line = br.readLine()) != null) {
			    if (line.contains("class=\"label\"")){ // class ="label" contains the name of the needed info
			    	rawInfos.add(line); 
			    	boolean found=false;
			    	while ((nextLine=br.readLine()) != null && found != true){
			    		if (nextLine.contains("</li>")){
				    		rawInfos.add(nextLine);
				    		found = true;
			    		}		    		
			    	}	 
			    }
			}
		} catch (IOException e2) {
			throw new IOException("Error when trying to read Nextline");
		}
        superfluousRemover(rawInfos);
        
        int j=0;
        while (j< rawInfos.size()){
        	informations.put(rawInfos.get(j), rawInfos.get(j+1));
        	j+=2;        	
        }
	}
	
	/**superfluousRemover remove HTML tags and useless spaces (beginning and end)
	 * in an ArrayList of Strings
	 * @param infos
	 */
	private void superfluousRemover(ArrayList<String> infos){    	
    	for (int i=0;i<infos.size();++i){
    		infos.set(i,infos.get(i).replaceAll("<[^>]+>", "")); // delete every HTML tags
    		infos.set(i,infos.get(i).trim()); // delete every spaces surrounding the string
    	}
    }
	
	@Override
	public String toString() {
		String toString = "";
		for (String i : informations.keySet()){
    		toString += i+": "+informations.get(i)+"\n";
    	}
		return toString;
	}
	public URL getUrl() {
		return url;
	}

	/**
	 * set the URL
	 * @param url
	 */
	public void setUrl(URL url) {
		this.url = url;
	}
	
	public String getCourrier(){
		return informations.get("Courriel");
	}
	
	public String getFonction(){
		return informations.get("Fonction");
	}
	
	public String getTelephone(){
		return informations.get("Téléphone");
	}
	
	public String getGroupes(){
		return informations.get("Groupes");
	}
	
	public String getFax(){
		return informations.get("Fax");
	}
	
	public String getBureau(){
		return informations.get("Bureau");
	}

	public static void main(String[] args) {
		try{
			String prenom = "Olivier";
			String nom = "CAILLOUX";
			GetInfosFromYearbook profJava = new GetInfosFromYearbook(prenom, nom);
			System.out.println(profJava.toString());
		}catch(Exception e){
			//the message of the original exception is displayed
			System.out.println("Error: " + e.getMessage());
		}
	}
}