package com.ulises.tulpserver;

import java.io.IOException;

import javax.servlet.http.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

@SuppressWarnings("serial")
public class TulpResetDailyPointsServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)  {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		Query q = new Query("User");
		PreparedQuery pq = datastore.prepare(q);


		for (Entity result : pq.asIterable()) {
			result.setProperty("toGive", 10);
			datastore.put(result);

		}
		
	}
	
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp){
		
	}
}
