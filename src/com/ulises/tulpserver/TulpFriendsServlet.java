package com.ulises.tulpserver;

import java.io.IOException;

import javax.servlet.http.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.labs.repackaged.org.json.*;

@SuppressWarnings("serial")
public class TulpFriendsServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();		
		Key keyUsuario = KeyFactory.createKey("User", req.getParameter("user"));
		Entity user;
		try {
			user = datastore.get(keyUsuario);
			String friendsString = (String)user.getProperty("friends");
			JSONArray friendsArray = new JSONArray(friendsString);
			//ArrayList<User> friendsArray = null;
			JSONArray userarray= new JSONArray();
			//String parse ="";
			for (int i =0;i<friendsArray.length();i++) {
				Key auxKey = KeyFactory.createKey("User", (String)friendsArray.get(i));
				Entity auxEntity = datastore.get(auxKey);
				User aux1 = new User();
				aux1.setName((String) auxEntity.getProperty("name"));
				aux1.setMail(auxEntity.getKey().getName());
				aux1.setPoints((long) auxEntity.getProperty("points"));
				userarray.put(aux1.toString());
	
			 }
			resp.getWriter().println(userarray.toString());
		} catch (EntityNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp){
		
	}
}
