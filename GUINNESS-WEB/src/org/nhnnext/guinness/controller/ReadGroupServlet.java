package org.nhnnext.guinness.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.SeekableByteChannel;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.nhnnext.guinness.common.*;
import org.nhnnext.guinness.model.Group;
import org.nhnnext.guinness.model.GroupDAO;

import com.google.gson.Gson;

@WebServlet(ServletName.GROUP_READ)
public class ReadGroupServlet extends HttpServlet {
	private static final long serialVersionUID = -7534646425281084154L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
			
		HttpSession session = req.getSession();
		String userId = (String)session.getAttribute(SessionKey.SESSION_USERID);
		
		if (userId == null) {
			resp.sendRedirect("/");
			return;
		}
		// DAO를 이용해 그룹유저맵에서 유저가 속한 그룹의 아이디를 받아온다.
		GroupDAO groupDao = new GroupDAO();
		List<String> list = new ArrayList<String>();

		list = groupDao.readGroupList(userId);

		// 받아온 그룹아이디 출력 테스트
		createJsonFile(list, resp);
	}

	public void createJsonFile(List<String> list, HttpServletResponse resp) {
		PrintWriter out = null;
		StringBuffer sb = new StringBuffer();
		ArrayList<Group> groups = new  ArrayList<Group>();
		GroupDAO groupDao = new GroupDAO();
		Gson gson = new Gson();
		
		try {
			out = resp.getWriter();
			for (String str : list) {
				groups.add(groupDao.findByGroupId(str));
			}
			sb.append(gson.toJson(groups));
			System.out.println("userId: " + sb.toString());
			out.write(sb.toString());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			out.close();
		}
	}

}
