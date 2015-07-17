package com.ulises.tulpserver;

import java.io.IOException;

import javax.servlet.http.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.labs.repackaged.org.json.*;

@SuppressWarnings("serial")
public class TulpAllUsersServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		Query q = new Query("User");
		PreparedQuery pq = datastore.prepare(q);

		JSONArray a = new JSONArray();
		

		for (Entity result : pq.asIterable()) {
			JSONObject o = new JSONObject();
			try {
				long puntos = (long) result.getProperty("points");			 
				o.append(result.getKey().getName(), puntos);
				a.put(o);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		resp.getWriter().println(a.toString());
	}
	
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp){
		
	}
}
