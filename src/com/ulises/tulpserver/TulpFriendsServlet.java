package com.ulises.tulpserver;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.*;

import com.google.appengine.api.datastore.DataTypeUtils;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.labs.repackaged.org.json.*;

@SuppressWarnings("serial")
public class TulpFriendsServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		Query q = new Query("User");
		PreparedQuery pq = datastore.prepare(q);

		JSONArray a = new JSONArray();
		
		Key keyUsuario = KeyFactory.createKey("User", req.getParameter("user"));
		Entity user;
		try {
			user = datastore.get(keyUsuario);
			String friends = (String) user.getProperty("friends");
			String[] aux = friends.split("%");
			ArrayList<User> friendsArray = null;
			String parse ="";
			for (Entity result : pq.asIterable()) {
				
				if(friends.contains(result.getKey().getName())){
					User aux1 = new User();
					aux1.setName((String) result.getProperty("name"));
					aux1.setMail(result.getKey().getName());
					aux1.setPoints((long) result.getProperty("points"));
					parse=parse+aux1.toString()+"{";
				}
	
			 }
			resp.getWriter().println(parse);
		} catch (EntityNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

	}
	
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp){
		
	}
}
