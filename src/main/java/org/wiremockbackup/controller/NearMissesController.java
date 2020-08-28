package org.wiremockbackup.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.wiremockbackup.exception.WiremockUIException;
import org.wiremockbackup.service.NearMissesService;
import org.wiremockbackup.util.AppConstants;

import io.swagger.annotations.Api;
import io.swagger.client.ApiResponse;
import io.swagger.client.model.InlineResponse2002;

@RestController
@RequestMapping(AppConstants.URI_NEARMISSES)
@Api(tags={"5 - Get Near Misses information"})
public class NearMissesController {
	private static final Logger L = LogManager.getLogger(NearMissesController.class);

	private NearMissesService nearMissesService;

	public NearMissesController(NearMissesService nearMissesService) {
		this.nearMissesService = nearMissesService;
	}

	@RequestMapping(value = "/unmatchedNearMisses/instanceId/{instanceId}", method = RequestMethod.GET)
	public ApiResponse<InlineResponse2002> unmatchedNearMisses(@PathVariable("instanceId") Long instanceId) throws WiremockUIException {
		L.info("Start : NearMissesController.unmatchedNearMisses() : instanceId = {}", instanceId);

		ApiResponse<InlineResponse2002> unmatchedNearMisses = nearMissesService.unmatchedNearMisses(instanceId);

		L.info("End : NearMissesController.unmatchedNearMisses() : instanceId = {}", instanceId);
		return unmatchedNearMisses;
	}
}