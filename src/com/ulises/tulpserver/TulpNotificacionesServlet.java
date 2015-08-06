package com.ulises.tulpserver;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;





import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.labs.repackaged.org.json.JSONArray;

@SuppressWarnings("serial")
public class TulpNotificacionesServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		
		String username = req.getParameter("user");
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Key keys = KeyFactory.createKey("User", req.getParameter("user")) ;
		Entity user;
		try {
			user = datastore.get(keys);
			resp.getWriter().print(user.getProperty("notificaciones"));
			user.setProperty("notificaciones", null);
			datastore.put(user);
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		String username = "test@example.com";
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Key keys = KeyFactory.createKey("User", username) ;
		Entity user;
		try {
			user = datastore.get(keys);
			JSONArray noticiasArray = new JSONArray();
			noticiasArray.put("noticia");
			noticiasArray.put("jose");
			user.setProperty("notificaciones",noticiasArray.toString());
			datastore.put(user);
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}