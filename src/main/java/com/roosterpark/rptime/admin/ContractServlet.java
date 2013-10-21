package com.roosterpark.rptime.admin;

import java.io.IOException;
import java.util.Date;
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
import com.roosterpark.rptime.model.Client;
import com.roosterpark.rptime.model.Contract;
import com.roosterpark.rptime.model.Worker;
import com.roosterpark.rptime.service.ClientService;
import com.roosterpark.rptime.service.ContractService;
import com.roosterpark.rptime.service.WorkerService;

@Singleton
@SuppressWarnings("serial")
public class ContractServlet extends HttpServlet {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	public static final String CONTRACT_KEY = "contract";
	public static final String CONTRACTS_KEY = "contracts";
	public static final String EDIT_PARAM = "edit";
	public static final String ERROR_STRING = "error";
	public static final String COUNT_KEY = "count";
	public static final String OFFSET_KEY = "offset";

	// Post Field Names
	public static final String ID_KEY = "id";
	public static final String WORKERS_KEY = "workers";
	public static final String WORKER_KEY = "worker";
	public static final String CLIENT_KEY = "client";
	public static final String CLIENTS_KEY = "clients";
	public static final String START_KEY = "start";
	public static final String END_KEY = "end";
	
	// JSP Locations
	public static final String CONTRACTS_JSP = "/jsp/contract/contracts.jsp";
	public static final String CONTRACT_JSP = "/jsp/contract/contract.jsp";
	public static final String CONTRACT_EDIT_JSP = "/jsp/contract/contractEdit.jsp";
	
	// Servlet Path
	public static final String ROUTE_PATH = "/rptime/contract";

	@Inject
	ContractService contractService;
	@Inject
	WorkerService workerService;
	@Inject
	ClientService clientService;

	public ContractServlet() {
		LOGGER.debug("init ContractServlet");
		LOGGER.trace("registering Contract class with ObjectifyService");
		ObjectifyService.register(Contract.class);
		LOGGER.trace("registered Contract");
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		List<Worker> workers = workerService.getAll();
		request.setAttribute(WORKERS_KEY, workers);
		List<Client> clients = clientService.getAll();
		request.setAttribute(CLIENTS_KEY, clients);
		
		String key = request.getParameter(CONTRACT_KEY);
		if (key != null) {
			Contract contract;
			try {
				contract = contractService.get(key);
			} catch (EntityNotFoundException e) {
				LOGGER.warn("Unable to retrieve record for company " + key);
				// TODO log something here
				request.setAttribute(ERROR_STRING, e.getMessage());
				request.getRequestDispatcher(CONTRACTS_JSP).forward(request, response);
				return;
			}

			request.setAttribute(CONTRACT_KEY, contract);
			String edit = request.getParameter(EDIT_PARAM);
			if(edit != null)
				request.getRequestDispatcher(CONTRACT_EDIT_JSP).forward(request, response);
			else
				request.getRequestDispatcher(CONTRACT_JSP).forward(request, response);
		} else {
			Integer count = request.getParameter(COUNT_KEY) == null ? null : Integer.valueOf(request.getParameter(COUNT_KEY));
			Integer offset = request.getParameter(OFFSET_KEY) == null ? null : Integer.valueOf(request.getParameter(OFFSET_KEY));
			count = count == null ? 5 : count;
			offset = offset == null ? 0 : count;

			List<Contract> companies = contractService.getPage(count, offset);
			request.setAttribute(CONTRACTS_KEY, companies);

			request.getRequestDispatcher(CONTRACTS_JSP).forward(request, response);
		}
	}

	// Create a user
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String idString = request.getParameter(ID_KEY);
		Long worker = Long.valueOf(request.getParameter(WORKER_KEY));
		Long client = Long.valueOf(request.getParameter(CLIENT_KEY));
		Date start = new Date(Long.valueOf(request.getParameter(START_KEY)));
		Date end = new Date(Long.valueOf(request.getParameter(END_KEY)));
		Contract contract = new Contract();
		if(idString != null && idString.length() > 0)
			contract.setId(Long.valueOf(idString));
		contract.setWorker(worker);;
		contract.setClient(client);
		contract.setStart(start);
		contract.setEnd(end);

		contractService.set(contract);

		// Redirect back to the view page for this company
		response.sendRedirect(ROUTE_PATH);
	}
}
