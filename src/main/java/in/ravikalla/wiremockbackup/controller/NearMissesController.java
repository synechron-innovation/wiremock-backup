package in.ravikalla.wiremockbackup.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import in.ravikalla.wiremockbackup.service.NearMissesService;
import in.ravikalla.wiremockbackup.util.AppConstants;
import io.swagger.client.model.InlineResponse2002;

@RestController
@RequestMapping(AppConstants.URI_NEARMISSES)
public class NearMissesController {
	private static final Logger L = LogManager.getLogger(NearMissesController.class);

	private NearMissesService nearMissesService;

	public NearMissesController(NearMissesService nearMissesService) {
		this.nearMissesService = nearMissesService;
	}

	@RequestMapping(value = "/unmatchedNearMisses/instanceId/{instanceId}", method = RequestMethod.GET)
	public InlineResponse2002 unmatchedNearMisses(@PathVariable("instanceId") Long instanceId) {
		L.info("Start : NearMissesController.unmatchedNearMisses() : instanceId = {}", instanceId);

		InlineResponse2002 unmatchedNearMisses = nearMissesService.unmatchedNearMisses(instanceId);

		L.info("End : NearMissesController.unmatchedNearMisses() : instanceId = {}", instanceId);
		return unmatchedNearMisses;
	}
}