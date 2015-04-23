package org.nhnnext.guinness.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

@Controller
public class EditorController {
	private static final Logger logger = LoggerFactory.getLogger(EditorController.class);
	
	@RequestMapping(value="/notes/editor", method=RequestMethod.POST)
	private String Editor (WebRequest req, HttpSession session, Model model)  {
		logger.debug("들어옴?");
		//String userId = (String) session.getAttribute("sessionUserId");
		String groupId = req.getParameter("groupId");
		model.addAttribute("groupId", groupId);
		return "editor";
	}
}
