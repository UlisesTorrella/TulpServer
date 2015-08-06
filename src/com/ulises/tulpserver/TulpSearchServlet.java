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
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;

@SuppressWarnings("serial")
public class TulpSearchServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
	}
	
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		
		UserService userService=UserServiceFactory.getUserService();
		com.google.appengine.api.users.User googleuser=userService.getCurrentUser();
		String username= googleuser.getEmail();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Key keyUsuario = KeyFactory.createKey("User", username);
		Key keyBusq = KeyFactory.createKey("User", req.getParameter("busq"));
		try {
			Entity user = datastore.get(keyUsuario);
			Entity buscado = datastore.get(keyBusq);
			String data= (String )user.getProperty("friends");
			if(req.getParameter("busq")==username){
				resp.getWriter().println("You are not your friend");
			}
			else{
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
				user.setProperty("points", (Long)user.getProperty("points")+1);
				buscado.setProperty("points", (Long)buscado.getProperty("points")+2);
				datastore.put(buscado);
				datastore.put(user);
				resp.getWriter().println("New Friend Successfully Added... Wiiii");
			}
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