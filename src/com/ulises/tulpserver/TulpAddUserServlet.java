package com.ulises.tulpserver;

import java.io.IOException;

import javax.servlet.http.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

@SuppressWarnings("serial")
public class TulpAddUserServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
	}
	
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp){
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Entity usuario = new Entity("User", req.getParameter("mail"));
		usuario.setProperty("name",req.getParameter("name"));
		usuario.setProperty("points", 0);
		usuario.setProperty("toGive", 10);
		usuario.setProperty("friends", "");
		datastore.put(usuario);
		
	}
}
