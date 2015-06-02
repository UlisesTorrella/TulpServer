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
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

@SuppressWarnings("serial")
public class TulpSearchServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
	}
	
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Key keyBusqueda = KeyFactory.createKey("User", req.getParameter("busq"));
		Key keyUsuario = KeyFactory.createKey("User", req.getParameter("user"));
		try {
			Entity busqueda = datastore.get(keyBusqueda);
			Entity user = datastore.get(keyUsuario);
			String verificacionRepeticiones= (String )user.getProperty("friends");
			if(verificacionRepeticiones.contains(req.getParameter("busq"))){
				resp.getWriter().println("Is already your friend");
			}else{
				if(user.getProperty("friends")!=null){
					Text friends = new Text(user.getProperty("friends")+"+"+req.getParameter("busq"));
					user.setProperty("friends", friends);
				}
				else{
					user.setProperty("friends", req.getParameter("busq"));
				}
				datastore.put(user);
				resp.getWriter().println("New Friend Successfully Added... Wiiii");
			}

		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			resp.getWriter().println("Gmail Account Doesn't Found");
		}
	}
}