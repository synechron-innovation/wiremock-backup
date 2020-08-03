package in.ravikalla.wiremockbackup.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import in.ravikalla.wiremockbackup.service.WiremockOperationsService;
import in.ravikalla.wiremockbackup.util.AppConstants;

@RestController
@RequestMapping(AppConstants.URI_OPERATIONS)
public class WiremockOperationsController {
	private static final Logger L = LogManager.getLogger(WiremockOperationsController.class);

	private WiremockOperationsService wiremockOperationsService;

	public WiremockOperationsController(WiremockOperationsService wiremockOperationsService) {
		this.wiremockOperationsService = wiremockOperationsService;
	}

	@RequestMapping(value = "/importFromWiremock/instanceId/{instanceId}/limit/{limit}/offset/{offset}", method = RequestMethod.GET)
	public void importAllFromWiremockByInstanceId(@PathVariable("instanceId") Long instanceId, @PathVariable("limit") Integer limit, @PathVariable("offset") Integer offset) {
		L.info("Start : WiremockOperationsController.importAllFromWiremockByInstanceId() : instanceId = {}, limit = {}, offset = {}", instanceId, limit, offset);

		wiremockOperationsService.importWiremockRecordings(instanceId, limit, offset);

		L.info("End : WiremockOperationsController.importAllFromWiremockByInstanceId() : instanceId = {}, limit = {}, offset = {}", instanceId, limit, offset);
	}
	@RequestMapping(value = "/exportToWiremock/instanceId/{instanceId}", method = RequestMethod.POST)
	public boolean exportAllFromWiremockByInstanceId(@PathVariable("instanceId") Long instanceId) {
		L.info("Start : WiremockOperationsController.exportAllFromWiremockByInstanceId() : instanceId = {}", instanceId);

		boolean exported = wiremockOperationsService.exportWiremockRecordings(instanceId);

		L.info("End : WiremockOperationsController.exportAllFromWiremockByInstanceId() : instanceId = {}, Exported = {}", instanceId, exported);
		return exported;
	}
	@RequestMapping(value = "/deleteFromWiremock/instanceId/{instanceId}", method = RequestMethod.DELETE)
	public boolean deleteAllFromWiremockByInstanceId(@PathVariable("instanceId") Long instanceId) {
		L.info("Start : WiremockOperationsController.deleteAllFromWiremockByInstanceId() : instanceId = {}", instanceId);

		boolean deleted = wiremockOperationsService.deleteWiremockRecordings(instanceId);

		L.info("End : WiremockOperationsController.deleteAllFromWiremockByInstanceId() : instanceId = {}, deleted = {}", instanceId, deleted);
		return deleted;
	}
}