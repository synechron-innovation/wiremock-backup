package in.ravikalla.wiremockbackup.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.ravikalla.wiremockbackup.document.InstanceMapping;
import in.ravikalla.wiremockbackup.dto.InstanceMappingDTO;
import in.ravikalla.wiremockbackup.service.MappingOperationsService;
import in.ravikalla.wiremockbackup.util.AppConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
	public InstanceMapping importAllFromDBByInstanceId(@PathVariable("instanceId") Long instanceId, @RequestParam(defaultValue="100") Integer limit, @RequestParam(defaultValue="0") Integer offset) {
		L.info("Start : MappingOperationsControllerDB.importAllFromDBByInstanceId() : instanceId = {}, limit = {}, offset = {}", instanceId, limit, offset);

		InstanceMapping instanceMapping = mappingOperationsService.importRecordingsFromDB(instanceId, limit, offset);

		L.info("End : MappingOperationsControllerDB.importAllFromDBByInstanceId() : instanceId = {}, limit = {}, offset = {}", instanceId, limit, offset);
		return instanceMapping;
	}
	@ApiOperation(value = "Export all mappings to DB for an Instance")
	@RequestMapping(value = "/exportToDB", method = RequestMethod.POST)
	public InstanceMapping exportAllToDBByInstanceId(@RequestBody InstanceMappingDTO instanceMappingDTO) {
		L.info("Start : MappingOperationsControllerDB.exportAllToDBByInstanceId()");

		InstanceMapping instanceMapping = mappingOperationsService.exportRecordingsToDB(instanceMappingDTO);

		L.info("End : MappingOperationsControllerDB.exportAllToDBByInstanceId() : Exported = {}", instanceMapping);
		return instanceMapping;
	}
	@ApiOperation(value = "Delete all mappings from DB based on its ID")
	@RequestMapping(value = "/deleteFromDB/instanceId/{instanceId}", method = RequestMethod.DELETE)
	public void deleteAllFromDBByInstanceId(@PathVariable("instanceId") Long instanceId) {
		L.info("Start : MappingOperationsControllerDB.deleteAllFromDBByInstanceId() : instanceId = {}", instanceId);

		mappingOperationsService.deleteRecordingsFromDB(instanceId);

		L.info("End : MappingOperationsControllerDB.deleteAllFromDBByInstanceId() : instanceId = {}", instanceId);
	}
}