package com.roosterpark.rptime.admin;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.roosterpark.rptime.model.Company;
import com.roosterpark.rptime.service.CompanyService;

public class CompanyServlet extends HttpServlet {
	public static final String COMPANY_KEY = "company";
	public static final String COMPANIES_KEY = "companies";
	public static final String COUNT_KEY = "count";
	public static final String OFFSET_KEY = "offset";
	public static final String ERROR_STRING = "error";
	public static final String COMPANY_NAME_KEY = "companyName";
	public static final String COMPANY_HEADER_KEY = "companyHeader";
	public static final String COMPANY_PHONE_KEY = "companyPhone";
	public static final String COMPANY_START_KEY = "companyStartDay";

	@Inject
	CompanyService companyService;

	public CompanyServlet() {
		// companyService = new CompanyService();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String key = (String) request.getAttribute(COMPANY_KEY);
		if (key != null) {
			Company company;
			try {
				company = companyService.get(key);
			} catch (EntityNotFoundException e) {
				// TODO log something here
				request.setAttribute(ERROR_STRING, e.getMessage());
				request.getRequestDispatcher("/jsp/companies").forward(request, response);
				return;
			}

			request.setAttribute(COMPANY_KEY, company);

			request.getRequestDispatcher("/jsp/company").forward(request, response);
		} else {
			Integer count = (Integer) request.getAttribute(COUNT_KEY);
			Integer offset = (Integer) request.getAttribute(OFFSET_KEY);
			count = count == null ? 5 : count;
			offset = offset == null ? 0 : count;

			List<Company> companies = companyService.getPage(count, offset);
			request.setAttribute(COMPANIES_KEY, companies);

			request.getRequestDispatcher("/jsp/companies.jsp").forward(request, response);
		}
	}

	// Create a user
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String companyName = (String) request.getAttribute(COMPANY_NAME_KEY);
		String companyHeader = (String) request.getAttribute(COMPANY_HEADER_KEY);
		String companyPhone = (String) request.getAttribute(COMPANY_PHONE_KEY);
		Integer startDayOfWeek = (Integer) request.getAttribute(COMPANY_START_KEY);
		Company company = new Company();
		company.setName(companyName);
		company.setHeader(companyHeader);
		company.setPhone(companyPhone);
		company.setStartDayOfWeek(startDayOfWeek);

		companyService.set(company);

		// Redirect back to the view page for this company
		response.sendRedirect("/rptime/companies");
	}
}
