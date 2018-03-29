package com.roosterpark.rptime.exceptions;

import org.joda.time.LocalDate;

import com.roosterpark.rptime.model.Contract;

/**
 * Indication that a {@link Contract} does not exist for a given {@code workerId}/{@code date} pairing.
 * 
 * @author jjzabkar
 */
public class ContractNotFoundException extends RuntimeException implements ResolvableException {

	private static final long serialVersionUID = 5377921797870419789L;
	private static final String RESOLUTION = "create Contract on the /contracts page for WorkerId='%s' with beginDate < %s < endDate.";
	private Long workerId;
	private LocalDate date;

	public ContractNotFoundException(final Long workerId, final LocalDate date) {
		super("No active Contracts found for Worker id='" + workerId + "' and date '" + date + "."
				+ "'.  Solution: for Worker, create Contract on the /contracts page with beginDate < " + date + "< endDate.");
		this.date = date;
		this.workerId = workerId;
	}

	public Long getWorkerId() {
		return workerId;
	}

	public String getResolution() {
		return String.format(RESOLUTION, workerId, date);
	}

}
