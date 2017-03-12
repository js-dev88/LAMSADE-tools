package com.github.lamsadetools.yearbookInfos;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class jax_rs_test {

	public static void main(String[] args) {
	
		Client client = ClientBuilder.newClient();
		WebTarget t1 = client.target("https://www.ent.dauphine.fr/Annuaire/index.php?param0=fiche&");
		WebTarget t4 = t1.queryParam("param1", "ocailloux");
		String result = t4.request(MediaType.TEXT_PLAIN).get(String.class);
		System.out.println(t4);
		System.out.println(result);
		client.close();

	}

}
