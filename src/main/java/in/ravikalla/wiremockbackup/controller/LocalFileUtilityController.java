package in.ravikalla.wiremockbackup.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.ravikalla.wiremockbackup.document.InstanceMapping;
import in.ravikalla.wiremockbackup.exception.WiremockUIException;
import in.ravikalla.wiremockbackup.service.MappingFolderService;
import in.ravikalla.wiremockbackup.service.MappingOperationsService;
import in.ravikalla.wiremockbackup.util.AppConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.client.model.Body1;

@RestController
@RequestMapping(AppConstants.URI_LOCAL_FILE_UTIL)
@Api(tags={"7 - Utility for extracting and creating mapping files from local file system"})
public class LocalFileUtilityController {
	private static final Logger L = LogManager.getLogger(LocalFileUtilityController.class);

	@Autowired
	public MappingOperationsService mappingOperationsService;
	@Autowired
	public MappingFolderService mappingFolderService;

	@ApiOperation(value = "Get all mappings from DB based on its ID and download the file")
	@RequestMapping(value = "/importFromDB/instanceId/{instanceId}", method = RequestMethod.GET)
	public List<Body1> importAllFromDBByInstanceId(@PathVariable("instanceId") Long instanceId, @RequestParam(required = true) String downloadToFolder) throws WiremockUIException {
		L.info("Start : LocalFileUtilityController.importAllFromDBByInstanceId() : instanceId = {}", instanceId);

		List<Body1> mappings = mappingOperationsService.importRecordingsFromDB(instanceId);
		try {
			downloadToFolder = mappingFolderService.updateWinFolderPath(downloadToFolder);
			mappingFolderService.createFilesInFolder(mappings, downloadToFolder);
		} catch (IOException e) {
			L.error("49 : LocalFileUtilityController.importAllFromDBByInstanceId() : IOException e = {}", e);
			throw new WiremockUIException("Exception while writing the file content to local disk", e);
		}

		L.info("End : LocalFileUtilityController.importAllFromDBByInstanceId() : instanceId = {}, MappingSize = {}", instanceId, (CollectionUtils.isEmpty(mappings)?0:mappings.size()));
		return mappings;
	}

	@ApiOperation(value = "Export all mappings to DB for an Instance")
	@RequestMapping(value = "/exportToDB/instanceId/{instanceId}", method = RequestMethod.POST)
	public InstanceMapping exportAllToDBByInstanceId(@PathVariable("instanceId") Long instanceId, @RequestParam(required = true) String exportFolder) throws WiremockUIException {
		L.info("Start : LocalFileUtilityController.exportAllToDBByInstanceId() : instanceId = {}, mappingsFolder = {}", instanceId, exportFolder);

		List<String> lstFileNames = new ArrayList<String>();
		exportFolder = mappingFolderService.updateWinFolderPath(exportFolder);

		mappingFolderService.findFiles(exportFolder, lstFileNames);
		List<Body1> mappings;
		try {
			mappings = mappingFolderService.convertFilesToMappings(exportFolder, lstFileNames);
		} catch (IOException e) {
			L.error("62 : LocalFileUtilityController.exportAllToDBByInstanceId() : IOException e = {}", e);
			throw new WiremockUIException("Exception while extracting the file content", e);
		}

		InstanceMapping instanceMapping = mappingOperationsService.exportRecordingsToDB(instanceId, mappings);

		L.info("End : LocalFileUtilityController.exportAllToDBByInstanceId() : instanceId = {}, mappingsFolder = {}", instanceId, exportFolder);
		return instanceMapping;
	}
}
