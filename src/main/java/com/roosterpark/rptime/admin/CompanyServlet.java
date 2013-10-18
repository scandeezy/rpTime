package com.roosterpark.rptime.admin;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.googlecode.objectify.ObjectifyService;
import com.roosterpark.rptime.model.Company;
import com.roosterpark.rptime.model.User;
import com.roosterpark.rptime.service.CompanyService;

@Singleton
@SuppressWarnings("serial")
public class CompanyServlet extends HttpServlet {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	public static final String COMPANIES_KEY = "companies";

	public static final String COMPANY_KEY = "company";
	public static final String COUNT_KEY = "count";
	public static final String OFFSET_KEY = "offset";
	public static final String EDIT_PARAM = "edit";
	public static final String ERROR_STRING = "error";
	
	// Post Field Names
	public static final String ID_KEY = "id";
	public static final String COMPANY_NAME_KEY = "companyName";
	public static final String COMPANY_HEADER_KEY = "companyHeader";
	public static final String COMPANY_PHONE_KEY = "companyPhone";
	public static final String COMPANY_START_KEY = "companyStartDay";
	
	// JSP Paths
	public static final String COMPANIES_JSP = "/jsp/company/companies.jsp";
	public static final String COMPANY_JSP = "/jsp/company/company.jsp";
	public static final String COMPANY_EDIT_JSP = "/jsp/company/companyEdit.jsp";
	
	// Servlet Path
	public static final String ROUTE_PATH = "/rptime/company";

	@Inject
	CompanyService companyService;

	public CompanyServlet() {
		LOGGER.debug("init CompanyServlet");
		LOGGER.trace("registering Company class with ObjectifyService");
		ObjectifyService.register(Company.class);
		LOGGER.trace("registered Company");
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String key = request.getParameter(COMPANY_KEY);
		if (key != null) {
			Company company;
			try {
				company = companyService.get(key);
			} catch (EntityNotFoundException e) {
				LOGGER.warn("Unable to retrieve record for company " + key);
				// TODO log something here
				request.setAttribute(ERROR_STRING, e.getMessage());
				request.getRequestDispatcher(COMPANIES_JSP).forward(request, response);
				return;
			}

			request.setAttribute(COMPANY_KEY, company);
			String edit = request.getParameter(EDIT_PARAM);
			if(edit != null)
				request.getRequestDispatcher(COMPANY_EDIT_JSP).forward(request, response);
			else
				request.getRequestDispatcher(COMPANY_JSP).forward(request, response);
		} else {
			Integer count = request.getParameter(COUNT_KEY) == null ? null : Integer.valueOf(request.getParameter(COUNT_KEY));
			Integer offset = request.getParameter(OFFSET_KEY) == null ? null : Integer.valueOf(request.getParameter(OFFSET_KEY));
			count = count == null ? 5 : count;
			offset = offset == null ? 0 : count;

			List<Company> companies = companyService.getPage(count, offset);
			request.setAttribute(COMPANIES_KEY, companies);

			request.getRequestDispatcher(COMPANIES_JSP).forward(request, response);
		}
	}

	// Create a user
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String idString = request.getParameter(ID_KEY);
		String companyName = request.getParameter(COMPANY_NAME_KEY);
		String companyHeader = request.getParameter(COMPANY_HEADER_KEY);
		String companyPhone = request.getParameter(COMPANY_PHONE_KEY);
		Integer startDayOfWeek = Integer.valueOf(request.getParameter(COMPANY_START_KEY));
		Company company = new Company();
		if(idString != null && idString.length() > 0)
			company.setId(Long.valueOf(idString));
		company.setName(companyName);
		company.setHeader(companyHeader);
		company.setPhone(companyPhone);
		company.setStartDayOfWeek(startDayOfWeek);

		companyService.set(company);

		// Redirect back to the view page for this company
		response.sendRedirect(ROUTE_PATH);
	}
}
