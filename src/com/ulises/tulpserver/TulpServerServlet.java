package com.ulises.tulpserver;
import java.io.IOException;

import javax.servlet.http.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@SuppressWarnings("serial")
public class TulpServerServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		Key keys = KeyFactory.createKey("User", req.getParameter("user")) ;
		try {
			Entity user = datastore.get(keys);
			User aux = new User();
			aux.setName((String) user.getProperty("name"));
			aux.setMail(user.getKey().getName());
			aux.setPoints((long) user.getProperty("points"));
			resp.getWriter().println(aux.toString());
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			resp.getWriter().print("FAIL");
		}	
	}
	
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Key keysReceptor = KeyFactory.createKey("User", req.getParameter("user")) ;
		Key keysEmitente = KeyFactory.createKey("User", req.getParameter("emitente")) ;
		Entity receptor;
		Entity emitente;
		try {
			emitente = datastore.get(keysEmitente);
			receptor = datastore.get(keysReceptor);
			if((Long)emitente.getProperty("toGive")>=Integer.parseInt(req.getParameter("addPoints"))){
				receptor.setProperty("points", (Long)receptor.getProperty("points")+Integer.parseInt(req.getParameter("addPoints")));
				datastore.put(receptor);

				emitente.setProperty("toGive", (Long)emitente.getProperty("toGive")-Integer.parseInt(req.getParameter("addPoints")));
				datastore.put(emitente);
				resp.getWriter().println("Send");
			}
			else{
				resp.getWriter().println("Not enough points");
			}

		} catch (EntityNotFoundException e) {
			resp.sendError(400);
		}
		
	}
}
