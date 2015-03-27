package org.nhnnext.guinness.controller.groups;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.nhnnext.guinness.common.Forwarding;
import org.nhnnext.guinness.common.MyValidatorFactory;
import org.nhnnext.guinness.common.ParameterKey;
import org.nhnnext.guinness.common.WebServletURL;
import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;
import org.nhnnext.guinness.model.Group;
import org.nhnnext.guinness.model.GroupDao;

@WebServlet(WebServletURL.GROUP_CREATE)
public class CreateGroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		HttpSession session = req.getSession();
		String groupCaptainUserId = (String) session.getAttribute(ParameterKey.SESSION_USERID);
		String groupName = req.getParameter("groupName");

		// 그룹 공개/비공개 여부 판단
		char isPublic = 'F';
		if ("public".equals(req.getParameter("isPublic")))
			isPublic = 'T';

		// 그룹 클래스 생성
		Group group = null;
		try {
			group = new Group(groupName, groupCaptainUserId, isPublic);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			Forwarding.forwardForError(req, resp, "errorMessage", "데이터 베이스 연결 실패", "/exception.jsp");
			return;
		} catch (MakingObjectListFromJdbcException e) {
			e.printStackTrace();
			Forwarding.forwardForError(req, resp, "errorMessage", "접속이 원활하지 않습니다.", "/exception.jsp");
			return;
		}

		// 유효성 검사
		Validator validator = MyValidatorFactory.createValidator();
		Set<ConstraintViolation<Group>> constraintViolation = validator.validate(group);

		if (constraintViolation.size() > 0) {
			String errorMessage = constraintViolation.iterator().next().getMessage();
			Forwarding.forwardForError(req, resp, "errorMessage", errorMessage, "/groups.jsp");
			return;
		}

		// 그룹 다오 생성
		GroupDao groupDao = new GroupDao();
		try {
			groupDao.createGroup(group);
			groupDao.createGroupUser(groupCaptainUserId, group.getGroupId());
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			Forwarding.forwardForError(req, resp, "errorMessage", "데이터 베이스 연결 실패", "/exception.jsp");
			return;
		}

		resp.sendRedirect("/groups.jsp");
	}

}
