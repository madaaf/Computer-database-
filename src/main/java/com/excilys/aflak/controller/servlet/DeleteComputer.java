package com.excilys.aflak.controller.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.excilys.aflak.service.ComputerService;

/**
 * Servlet implementation class deleteComputer
 */
@WebServlet("/deleteComputer")
public class DeleteComputer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ComputerService serviceComputer;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DeleteComputer() {
		super();
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"/applicationContext.xml", DeleteComputer.class);

		serviceComputer = applicationContext.getBean(ComputerService.class);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String computersId[] = request.getParameter("selection").split(",");

		for (String computerId : computersId) {
			serviceComputer.deleteComputer(Long.parseLong(computerId));
		}

		response.sendRedirect("index?deletedSuccess");

	}
}
