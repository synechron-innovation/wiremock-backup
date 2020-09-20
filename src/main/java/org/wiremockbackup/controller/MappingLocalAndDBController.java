package org.wiremockbackup.controller;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.wiremockbackup.document.InstanceMappingForExport;
import org.wiremockbackup.service.MappingOperationsService;
import org.wiremockbackup.util.AppConstants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.client.model.Body;
import io.swagger.client.model.Body1;
@RestController
@RequestMapping(AppConstants.URI_MAPPING_OPERATIONS_LOCAL_DB)
@Api(tags={"3 - Import/Export Mappings of Wiremock from DB"})
public class MappingLocalAndDBController {
	private static final Logger L = LogManager.getLogger(MappingOperationsWMController.class);

	private MappingOperationsService mappingOperationsService;

	public MappingLocalAndDBController(MappingOperationsService mappingOperationsService) {
		this.mappingOperationsService = mappingOperationsService;
	}

	@ApiOperation(value = "Get all mappings from DB based on its ID")
	@RequestMapping(value = "/importFromDB/instanceId/{instanceId}", method = RequestMethod.GET)
	public List<Body1> importAllFromDBByInstanceId(@PathVariable("instanceId") Long instanceId) {
		L.info("Start : MappingLocalAndDBController.importAllFromDBByInstanceId() : instanceId = {}", instanceId);

		List<Body1> mappings = mappingOperationsService.importRecordingsFromDB(instanceId);

		L.info("End : MappingLocalAndDBController.importAllFromDBByInstanceId() : instanceId = {}, MappingSize = {}", instanceId, (CollectionUtils.isEmpty(mappings)?0:mappings.size()));
		return mappings;
	}
	@ApiOperation(value = "Get history from DB based on its ID")
	@RequestMapping(value = "/importHistoryFromDB/instanceId/{instanceId}", method = RequestMethod.GET)
	public List<String> importHistoryFromDBByInstanceId(@PathVariable("instanceId") Long instanceId) {
		L.info("Start : MappingLocalAndDBController.importHistoryFromDBByInstanceId() : instanceId = {}", instanceId);

		List<String> history = mappingOperationsService.importHistoryFromDB(instanceId);

		L.info("End : MappingLocalAndDBController.importHistoryFromDBByInstanceId() : instanceId = {}, HistorySize = {}", instanceId, (CollectionUtils.isEmpty(history)?0:history.size()));
		return history;
	}
	@ApiOperation(value = "Export all mappings to DB for an Instance")
	@RequestMapping(value = "/exportToDB/instanceId/{instanceId}", method = RequestMethod.POST)
	public InstanceMappingForExport exportAllToDBByInstanceId(@PathVariable("instanceId") Long instanceId, @RequestBody List<Body> mappings, @RequestParam String comment) {
		L.info("Start : MappingLocalAndDBController.exportAllToDBByInstanceId() : instanceId = {}, comment = {}, size = {}", instanceId, comment, ((null == mappings)?0:mappings.size()));

		InstanceMappingForExport instanceMappingForExport = mappingOperationsService.exportRecordingsToDB(instanceId, mappings, comment);

		L.info("End : MappingLocalAndDBController.exportAllToDBByInstanceId() : instanceId = {}, comment = {}, size = {}, instanceMappingForExport = {}", instanceId, comment, ((null == mappings)?0:mappings.size()), instanceMappingForExport);
		return instanceMappingForExport;
	}
	@ApiOperation(value = "Delete all mappings of an instance from DB based on its ID")
	@RequestMapping(value = "/deleteFromDB/instanceId/{instanceId}", method = RequestMethod.DELETE)
	public void deleteMappingsOfInstanceFromDBByInstanceId(@PathVariable("instanceId") Long instanceId) {
		L.info("Start : MappingLocalAndDBController.deleteMappingsOfInstanceFromDBByInstanceId() : instanceId = {}", instanceId);

		mappingOperationsService.deleteMappingsOfInstanceFromDB(instanceId);

		L.info("End : MappingLocalAndDBController.deleteMappingsOfInstanceFromDBByInstanceId() : instanceId = {}", instanceId);
	}
}