package com.github.yearbookInfos;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * GetInfosFromYearbook will get information from Dauphine's yearbook
 * It needs the name and the surname of the person you want to get information on OR
 * the url corresponding to the person's dauphine yearbook page.
 * @author Abdelkader ZEROUALI
 *
 */
public class GetInfosFromYearbook {
	private ArrayList<String> rawInfos = new ArrayList();
	private String [][] orderedInfos;
	private URL url;
	
	/**
	 * Constructor using a person's dauphine yearbook URL
	 * @param url
	 */
	public GetInfosFromYearbook(URL url){
		this.url= url;
		treatYbData(url);
	}
	
	public GetInfosFromYearbook(String firstname, String surname) {
		assert(firstname != null && surname !=null);
		try {
			url = new URL("https://www.ent.dauphine.fr/Annuaire/index.php?param0=fiche&param1=" + firstname.toLowerCase().charAt(0)+surname.toLowerCase());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		treatYbData(url);		
	}
	
	/**
	 * treatYbData takes a URL in parameter. It will connect to this URL and
	 * read the entire html code of the web page
	 * @param url
	 */
	public void treatYbData (URL url){
		URLConnection con;
		InputStream is = null;
		try {
			con = url.openConnection();
			is = con.getInputStream();
		} catch (IOException e1) {
			System.out.println("Impossible to connect to URL");
			e1.printStackTrace();
		}
		
        BufferedReader br = new BufferedReader(new InputStreamReader(is,StandardCharsets.UTF_8));

        String line = null;
        String nextLine =null;

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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Impossible to read the next line");
			e.printStackTrace();
		}
        superfluRemover(rawInfos);
        orderedInfos = new String[rawInfos.size()/2][2];
        int j =0;
        for (int i=0;i<orderedInfos.length;++i){
        	orderedInfos[i][0]=rawInfos.get(j);
        	orderedInfos[i][1]=rawInfos.get(j+1);
        	j+=2;
        }
	}
	
	/**superfluRemover remove html tags and useless spaces (beginning and end)
	 * in an ArrayList of Strings
	 * @param infos
	 */
	public void superfluRemover(ArrayList<String> infos){    	
    	for (int i=0;i<infos.size();++i){
    		infos.set(i,infos.get(i).trim()); // delete every spaces surrounding the string
    		infos.set(i,infos.get(i).replaceAll("<[^>]+>", "")); // delete every html tags
    	}
    }
	
	public ArrayList<String> getInfos() {
		return rawInfos;
	}
	public String[][] getOrderedInfos() {
		return orderedInfos;
	}
	
	@Override
	public String toString() {
		String toString = "";
		for (int i=0;i<orderedInfos.length;++i){
			toString += orderedInfos[i][0] + " : " + orderedInfos[i][1] + "\n";
		}	
		return toString;
	}
	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public static void main(String[] args) {
		String prenom = "Olivier";
		String nom = "CAILLOUX";
		GetInfosFromYearbook profJava = new GetInfosFromYearbook(prenom, nom);
		ArrayList<String> rawInfos = profJava.getInfos();
		String[][] orderedInfos = profJava.getOrderedInfos();		
		String test = profJava.toString();
		System.out.println(test);
	}
}
