package in.ravikalla.wiremockbackup.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.ravikalla.wiremockbackup.service.MappingOperationsService;
import in.ravikalla.wiremockbackup.util.AppConstants;

@RestController
@RequestMapping(AppConstants.URI_MAPPING_OPERATIONS_DB_WM)
public class MappingOperationsWMController {
	private static final Logger L = LogManager.getLogger(MappingOperationsWMController.class);

	private MappingOperationsService mappingOperationsService;

	public MappingOperationsWMController(MappingOperationsService mappingOperationsService) {
		this.mappingOperationsService = mappingOperationsService;
	}

	@RequestMapping(value = "/importFromWiremock/instanceId/{instanceId}", method = RequestMethod.GET)
	public void importAllFromWiremockByInstanceId(@PathVariable("instanceId") Long instanceId, @RequestParam(defaultValue="100") Integer limit, @RequestParam(defaultValue="0") Integer offset) {
		L.info("Start : MappingOperationsWMController.importAllFromWiremockByInstanceId() : instanceId = {}, limit = {}, offset = {}", instanceId, limit, offset);

		mappingOperationsService.importWiremockRecordings(instanceId, limit, offset);

		L.info("End : MappingOperationsWMController.importAllFromWiremockByInstanceId() : instanceId = {}, limit = {}, offset = {}", instanceId, limit, offset);
	}
	@RequestMapping(value = "/exportToWiremock/instanceId/{instanceId}", method = RequestMethod.POST)
	public boolean exportAllToWiremockByInstanceId(@PathVariable("instanceId") Long instanceId) {
		L.info("Start : MappingOperationsWMController.exportAllToWiremockByInstanceId() : instanceId = {}", instanceId);

		boolean exported = mappingOperationsService.exportWiremockRecordings(instanceId);

		L.info("End : MappingOperationsWMController.exportAllToWiremockByInstanceId() : instanceId = {}, Exported = {}", instanceId, exported);
		return exported;
	}
	@RequestMapping(value = "/deleteFromWiremock/instanceId/{instanceId}", method = RequestMethod.DELETE)
	public boolean deleteAllFromWiremockByInstanceId(@PathVariable("instanceId") Long instanceId) {
		L.info("Start : MappingOperationsWMController.deleteAllFromWiremockByInstanceId() : instanceId = {}", instanceId);

		boolean deleted = mappingOperationsService.deleteWiremockRecordings(instanceId);

		L.info("End : MappingOperationsWMController.deleteAllFromWiremockByInstanceId() : instanceId = {}, deleted = {}", instanceId, deleted);
		return deleted;
	}
}