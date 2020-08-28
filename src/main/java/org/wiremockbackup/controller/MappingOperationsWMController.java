package org.wiremockbackup.controller;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.wiremockbackup.document.InstanceMapping;
import org.wiremockbackup.exception.WiremockUIException;
import org.wiremockbackup.service.MappingOperationsService;
import org.wiremockbackup.util.AppConstants;
import org.wiremockbackup.util.MappingTarget;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(AppConstants.URI_MAPPING_OPERATIONS_DB_WM)
@Api(tags={"2 - Import/Export Mappings of Wiremock"})
public class MappingOperationsWMController {
	private static final Logger L = LogManager.getLogger(MappingOperationsWMController.class);

	private MappingOperationsService mappingOperationsService;

	public MappingOperationsWMController(MappingOperationsService mappingOperationsService) {
		this.mappingOperationsService = mappingOperationsService;
	}

	@ApiOperation(value = "Import all mappings of a Wiremock server to DB based in its ID")
	@RequestMapping(value = "/importFromWiremock/instanceId/{instanceId}", method = RequestMethod.GET)
	public Integer importAllFromWiremockByInstanceId(@PathVariable("instanceId") Long instanceId, @RequestParam(defaultValue="100") Integer limit, @RequestParam(defaultValue="0") Integer offset) throws WiremockUIException {
		L.info("Start : MappingOperationsWMController.importAllFromWiremockByInstanceId() : instanceId = {}, limit = {}, offset = {}", instanceId, limit, offset);

		InstanceMapping instanceMapping = mappingOperationsService.importWiremockRecordings(instanceId, MappingTarget.DB, null, limit, offset);
		Integer importedInstanceCount = (CollectionUtils.isEmpty(instanceMapping.getMappings())?0:instanceMapping.getMappings().size());

		L.info("End : MappingOperationsWMController.importAllFromWiremockByInstanceId() : instanceId = {}, limit = {}, offset = {}", instanceId, limit, offset, importedInstanceCount);
		return importedInstanceCount;
	}
	@ApiOperation(value = "Export all mappings from DB to Wiremock server based on its ID")
	@RequestMapping(value = "/exportToWiremock/instanceId/{instanceId}", method = RequestMethod.POST)
	public boolean exportAllToWiremockByInstanceId(@PathVariable("instanceId") Long instanceId) throws WiremockUIException {
		L.info("Start : MappingOperationsWMController.exportAllToWiremockByInstanceId() : instanceId = {}", instanceId);

		boolean exported = mappingOperationsService.exportWiremockRecordings(instanceId, MappingTarget.DB, null);

		L.info("End : MappingOperationsWMController.exportAllToWiremockByInstanceId() : instanceId = {}, Exported = {}", instanceId, exported);
		return exported;
	}
	@ApiOperation(value = "Delete all mappings from a Wiremock server based in its ID")
	@RequestMapping(value = "/deleteFromWiremock/instanceId/{instanceId}", method = RequestMethod.DELETE)
	public boolean deleteAllFromWiremockByInstanceId(@PathVariable("instanceId") Long instanceId) throws WiremockUIException {
		L.info("Start : MappingOperationsWMController.deleteAllFromWiremockByInstanceId() : instanceId = {}", instanceId);

		boolean deleted = mappingOperationsService.deleteWiremockRecordings(instanceId);

		L.info("End : MappingOperationsWMController.deleteAllFromWiremockByInstanceId() : instanceId = {}, deleted = {}", instanceId, deleted);
		return deleted;
	}
}