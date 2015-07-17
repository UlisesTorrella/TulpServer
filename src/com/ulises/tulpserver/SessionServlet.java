package com.ulises.tulpserver;

import java.io.IOException;

import javax.servlet.http.*;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class SessionServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		UserService userService=UserServiceFactory.getUserService();
		com.google.appengine.api.users.User user=userService.getCurrentUser();
		resp.getWriter().println(user.getEmail());
	}
	
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp){
		
	}
}
