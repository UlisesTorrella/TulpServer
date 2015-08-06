package com.ulises.tulpserver;
import java.io.IOException;

import javax.servlet.http.*;

import com.google.api.server.spi.config.Api;
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
@Api(name = "myapi",
clientIds = {"1091886568043-11g0h4ctll1k7bug058kk9smri5iia7s.apps.googleusercontent.com"},
audiences = {"1091886568043-ecrprpv21383k4rnnneme4h1bfi5kdil.apps.googleusercontent.com"})
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
		UserService userService=UserServiceFactory.getUserService();
		com.google.appengine.api.users.User user=userService.getCurrentUser();
		String username= user.getEmail();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Key keysReceptor = KeyFactory.createKey("User", req.getParameter("user")) ;
		Key keysEmitente = KeyFactory.createKey("User", username) ;
		Entity receptor;
		Entity emitente;
		int addpoints = Integer.parseInt(req.getParameter("addPoints"));
		try {
			emitente = datastore.get(keysEmitente);
			receptor = datastore.get(keysReceptor);
			if((Long)emitente.getProperty("toGive")>=addpoints){
				receptor.setProperty("points", (Long)receptor.getProperty("points")+addpoints);
				String notificaciones= (String )receptor.getProperty("notificaciones");
				if(notificaciones == null){
					JSONArray auxArray = new JSONArray();
					notificaciones = auxArray.toString();
				}
				JSONArray noticiasArray = new JSONArray(notificaciones);					
				noticiasArray.put("Recibiste "+addpoints+" puntos de "+ emitente.getProperty("name"));
				receptor.setProperty("notificaciones", noticiasArray.toString());
				datastore.put(receptor);				
				emitente.setProperty("toGive", (Long)emitente.getProperty("toGive")-addpoints);
				datastore.put(emitente);
				resp.getWriter().println("Send");
			}
			else{
				resp.getWriter().println("Not enough points");
			}

		} catch (EntityNotFoundException e) {
			resp.sendError(400);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
