package com.ulises.tulpserver;
import java.io.IOException;

import javax.servlet.http.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.repackaged.org.codehaus.jackson.util.TextBuffer;

@SuppressWarnings("serial")
public class TulpServerServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		Key keys = KeyFactory.createKey("User", req.getParameter("user")) ;
		try {
			Entity user = datastore.get(keys);
			
			resp.getWriter().println(user.getProperty("name")+"-"+user.getProperty("points"));
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			resp.getWriter().println("FAIL");
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
				resp.getWriter().println("Enviado");
			}
			else{
				resp.getWriter().println("Not enough points");
			}

		} catch (EntityNotFoundException e) {
			resp.sendError(400);
		}
		
	}
}
