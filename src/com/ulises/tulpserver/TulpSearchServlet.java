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
import com.google.appengine.labs.repackaged.org.json.JSONException;

@SuppressWarnings("serial")
public class TulpSearchServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
	}
	
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Key keyUsuario = KeyFactory.createKey("User", req.getParameter("user"));
		try {
			Entity user = datastore.get(keyUsuario);
			String data= (String )user.getProperty("friends");
			if(data.contains(req.getParameter("busq"))){
				resp.getWriter().println("Is already your friend");
			}else{
				if(data!=null){
					if(data!=""){
						JSONArray friendsArray = new JSONArray(data);					
						friendsArray.put(req.getParameter("busq"));
						user.setProperty("friends", friendsArray.toString());
					}
					else{
						JSONArray friendsArray = new JSONArray();					
						friendsArray.put(req.getParameter("busq"));
						user.setProperty("friends", friendsArray.toString());
					}

				}
				datastore.put(user);
				resp.getWriter().println("New Friend Successfully Added... Wiiii");
			}

		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			resp.getWriter().println("Gmail Account Doesn't Found");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			resp.getWriter().println(e.getMessage());
		}
	}
}