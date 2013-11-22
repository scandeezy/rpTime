package com.roosterpark.rptime.web;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.inject.Inject;

import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.roosterpark.rptime.model.Contract;
import com.roosterpark.rptime.service.ContractService;

/**
 * {@link Controller} responsible for {@link Contract}-related MVC endpoints.
 * <p>
 * Per {@code web.xml} Administrator-only endpoint {@link RequestMapping RequestMappings} are secured by having {@code /admin/} in their URL
 * path.
 * 
 * @author jjzabkar
 */
@Controller
public class ContractController {

	@Inject
	ContractService service;

	@RequestMapping(value = "/admin/contract/worker/{workerId}/active", method = GET)
	@ResponseBody
	public List<Contract> getActiveByWorker(@PathVariable Long workerId) {
		return service.getActiveContractsForWorker(workerId, new Interval(new LocalDate()));
	}

	@RequestMapping(value = "/admin/contract/worker/{workerId}", method = GET)
	@ResponseBody
	public List<Contract> getAllByWorker(@PathVariable Long workerId) {
		return service.getContractsForWorker(workerId);
	}

	@RequestMapping(value = "/admin/contract/client/{clientId}/active", method = GET)
	@ResponseBody
	public List<Contract> getActiveByClient(@PathVariable Long clientId) {
		return service.getActiveContractsForClient(clientId, new LocalDate());
	}

	@RequestMapping(value = "/admin/contract/client/{clientId}", method = GET)
	@ResponseBody
	public List<Contract> getAllByClient(@PathVariable Long clientId) {
		return service.getContractsForClient(clientId);
	}

	@RequestMapping(value = "/admin/contract/{id}", method = GET)
	@ResponseBody
	public Contract getContract(@PathVariable Long id) {
		return service.getById(id);
	}

	@RequestMapping(value = "/admin/contract", method = GET)
	@ResponseBody
	public List<Contract> getAllContracts() {
		return service.getAll();
	}

	@RequestMapping(value = "/admin/contract/idmap", method = GET)
	@ResponseBody
	public SortedMap<Long, Contract> getMap() {
		final List<Contract> list = service.getAll();
		final SortedMap<Long, Contract> map = new TreeMap<Long, Contract>();
		for (Contract obj : list) {
			map.put(obj.getId(), obj);
		}
		return map;
	}

	@RequestMapping(value = "/admin/contract", method = POST)
	@ResponseBody
	public Contract saveContract(@RequestBody Contract contract) {
		service.set(contract);

		return contract;
	}

	@RequestMapping(value = "/admin/contract/{id}", method = DELETE)
	@ResponseStatus(NO_CONTENT)
	public void delete(@PathVariable("id") Long id) {
		service.delete(id);
	}

}
