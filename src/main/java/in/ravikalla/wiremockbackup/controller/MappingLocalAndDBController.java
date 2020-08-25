package in.ravikalla.wiremockbackup.controller;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import in.ravikalla.wiremockbackup.document.InstanceMapping;
import in.ravikalla.wiremockbackup.service.MappingOperationsService;
import in.ravikalla.wiremockbackup.util.AppConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

	@ApiOperation(value = "Get all mappings from DB based in its ID")
	@RequestMapping(value = "/importFromDB/instanceId/{instanceId}", method = RequestMethod.GET)
	public List<Body1> importAllFromDBByInstanceId(@PathVariable("instanceId") Long instanceId) {
		L.info("Start : MappingOperationsControllerDB.importAllFromDBByInstanceId() : instanceId = {}", instanceId);

		List<Body1> mappings = mappingOperationsService.importRecordingsFromDB(instanceId);

		L.info("End : MappingOperationsControllerDB.importAllFromDBByInstanceId() : instanceId = {}, MappingSize = {}", instanceId, (CollectionUtils.isEmpty(mappings)?0:mappings.size()));
		return mappings;
	}
	@ApiOperation(value = "Export all mappings to DB for an Instance")
	@RequestMapping(value = "/exportToDB/instanceId/{instanceId}", method = RequestMethod.POST)
	public InstanceMapping exportAllToDBByInstanceId(@PathVariable("instanceId") Long instanceId, @RequestBody List<Body1> mappings) {
		L.info("Start : MappingOperationsControllerDB.exportAllToDBByInstanceId()");

		InstanceMapping instanceMapping = mappingOperationsService.exportRecordingsToDB(instanceId, mappings);

		L.info("End : MappingOperationsControllerDB.exportAllToDBByInstanceId() : Exported = {}", instanceMapping);
		return instanceMapping;
	}
	@ApiOperation(value = "Delete all mappings of an instance from DB based on its ID")
	@RequestMapping(value = "/deleteFromDB/instanceId/{instanceId}", method = RequestMethod.DELETE)
	public void deleteMappingsOfInstanceFromDBByInstanceId(@PathVariable("instanceId") Long instanceId) {
		L.info("Start : MappingOperationsControllerDB.deleteMappingsOfInstanceFromDBByInstanceId() : instanceId = {}", instanceId);

		mappingOperationsService.deleteMappingsOfInstanceFromDB(instanceId);

		L.info("End : MappingOperationsControllerDB.deleteMappingsOfInstanceFromDBByInstanceId() : instanceId = {}", instanceId);
	}
}