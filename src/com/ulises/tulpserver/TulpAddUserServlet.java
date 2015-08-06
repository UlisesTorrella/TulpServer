package com.ulises.tulpserver;

import java.io.IOException;

import javax.servlet.http.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class TulpAddUserServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
	}
	
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp){
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		UserService userService=UserServiceFactory.getUserService();
		com.google.appengine.api.users.User googleuser=userService.getCurrentUser();
		String username= googleuser.getEmail();
		Key keyUsuario = KeyFactory.createKey("User", username);
		try {
			Entity user = datastore.get(keyUsuario);
		} catch (EntityNotFoundException e) {
			Entity usuario = new Entity("User", username);
			usuario.setProperty("name",req.getParameter("name"));
			usuario.setProperty("points", 0);
			usuario.setProperty("toGive", 10);
			usuario.setProperty("friends", "");
			
			try {
				Key keyFriend = KeyFactory.createKey("User", req.getParameter("friend"));
				Entity friend = datastore.get(keyFriend);
				friend.setProperty("points", (Long)friend.getProperty("points")+10);
				datastore.put(friend);
				usuario.setProperty("points", 5);
			} catch (EntityNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			datastore.put(usuario);
		}

		
	}
}
