package in.ravikalla.wiremockbackup.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import in.ravikalla.wiremockbackup.service.MappingOperationsService;
import in.ravikalla.wiremockbackup.util.AppConstants;

@RestController
@RequestMapping(AppConstants.URI_OPERATIONS)
public class MappingOperationsController {
	private static final Logger L = LogManager.getLogger(MappingOperationsController.class);

	private MappingOperationsService mappingOperationsService;

	public MappingOperationsController(MappingOperationsService wiremockOperationsService) {
		this.mappingOperationsService = wiremockOperationsService;
	}

	@RequestMapping(value = "/importFromWiremock/instanceId/{instanceId}/limit/{limit}/offset/{offset}", method = RequestMethod.GET)
	public void importAllFromWiremockByInstanceId(@PathVariable("instanceId") Long instanceId, @PathVariable("limit") Integer limit, @PathVariable("offset") Integer offset) {
		L.info("Start : MappingOperationsController.importAllFromWiremockByInstanceId() : instanceId = {}, limit = {}, offset = {}", instanceId, limit, offset);

		mappingOperationsService.importWiremockRecordings(instanceId, limit, offset);

		L.info("End : MappingOperationsController.importAllFromWiremockByInstanceId() : instanceId = {}, limit = {}, offset = {}", instanceId, limit, offset);
	}
	@RequestMapping(value = "/exportToWiremock/instanceId/{instanceId}", method = RequestMethod.POST)
	public boolean exportAllFromWiremockByInstanceId(@PathVariable("instanceId") Long instanceId) {
		L.info("Start : MappingOperationsController.exportAllFromWiremockByInstanceId() : instanceId = {}", instanceId);

		boolean exported = mappingOperationsService.exportWiremockRecordings(instanceId);

		L.info("End : MappingOperationsController.exportAllFromWiremockByInstanceId() : instanceId = {}, Exported = {}", instanceId, exported);
		return exported;
	}
	@RequestMapping(value = "/deleteFromWiremock/instanceId/{instanceId}", method = RequestMethod.DELETE)
	public boolean deleteAllFromWiremockByInstanceId(@PathVariable("instanceId") Long instanceId) {
		L.info("Start : MappingOperationsController.deleteAllFromWiremockByInstanceId() : instanceId = {}", instanceId);

		boolean deleted = mappingOperationsService.deleteWiremockRecordings(instanceId);

		L.info("End : MappingOperationsController.deleteAllFromWiremockByInstanceId() : instanceId = {}, deleted = {}", instanceId, deleted);
		return deleted;
	}
}