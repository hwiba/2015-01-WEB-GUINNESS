package org.nhnnext.guinness.controller.users;

import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.nhnnext.guinness.model.User;
import org.nhnnext.guinness.model.UserDAO;

@WebServlet("/users/login")
public class LoginUsersServlet extends HttpServlet{
	private static final long serialVersionUID = -7135687406875475113L;

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws javax.servlet.ServletException ,java.io.IOException {
		String userId = (String) req.getParameter("userId");
		String userPassword = (String) req.getParameter("userPassword");
		UserDAO userDao = new UserDAO();
		
		try {
			User user = userDao.readUser(userId);
			PrintWriter out = resp.getWriter();
			if (user == null || !user.getUserPassword().equals(userPassword)) {
				out.print("loginFailed");
				return;
			}
			out.print("/groups.jsp");
			HttpSession session = req.getSession();
			session.setAttribute("sessionUserId", userId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
}
