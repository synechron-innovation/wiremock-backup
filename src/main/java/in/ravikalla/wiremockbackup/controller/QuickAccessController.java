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
import in.ravikalla.wiremockbackup.dto.InstanceMappingDTO;
import in.ravikalla.wiremockbackup.exception.WiremockUIException;
import in.ravikalla.wiremockbackup.service.InstanceMappingService;
import in.ravikalla.wiremockbackup.service.MappingFolderService;
import in.ravikalla.wiremockbackup.service.MappingOperationsService;
import in.ravikalla.wiremockbackup.service.RecordingService;
import in.ravikalla.wiremockbackup.util.AppConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.client.model.Body1;
import io.swagger.client.model.InlineResponse200;

@RestController
@RequestMapping(AppConstants.URI_QUICK)
@Api(tags={"0 - Daily usage for Testers and Developers"})
public class QuickAccessController {
	private static final Logger L = LogManager.getLogger(QuickAccessController.class);

	@Autowired
	private InstanceMappingService instanceMappingService;
	@Autowired
	private MappingOperationsService mappingOperationsService;
	@Autowired
	private MappingFolderService mappingFolderService;
	@Autowired
	private RecordingService recordingService;

	@ApiOperation(value = "1 - Create a new Wiremock instance")
	@RequestMapping(value = "/1/createWiremockInstance", method = RequestMethod.POST)
	public Long create(@RequestParam(required = false) String instanceName, @RequestParam(required = true) String wiremockURL, @RequestParam(required = true) String targetURL) {
		L.info("Start : QuickAccessController.create() : instanceName = {}, wiremockURL = {}, targetURL = {}", instanceName, wiremockURL, targetURL);

		Long id = instanceMappingService.create(instanceName, wiremockURL, targetURL);

		L.info("End : QuickAccessController.create() : instanceName = {}, wiremockURL = {}, targetURL = {}", instanceName, wiremockURL, targetURL);
		return id;
	}

	@ApiOperation(value = "2 - View a list of all Wiremock instances")
	@RequestMapping(value = "/2/viewAllWiremockInstances", method = RequestMethod.GET)
	public List<InstanceMappingDTO> getAll() {
		L.info("Start : QuickAccessController.getAll()");

		List<InstanceMappingDTO> allMappings = instanceMappingService.getAllMappings();

		L.info("End : QuickAccessController.getAll() : Size = {}", (CollectionUtils.isNotEmpty(allMappings)?allMappings.size():0));
		return allMappings;
	}

	@ApiOperation(value = "3 - Start recording on Wiremock server")
	@RequestMapping(value = "/3/startRecording/{instanceId}", method = RequestMethod.GET)
	public void startRecoding(@PathVariable("instanceId") Long instanceId) throws WiremockUIException {
		L.info("Start : QuickAccessController.startRecoding() : instanceId = {}", instanceId);

		recordingService.startRecord(instanceId);

		L.info("End : QuickAccessController.startRecoding() : instanceId = {}", instanceId);
	}

	@ApiOperation(value = "4 - Stop recording on Wiremock server")
	@RequestMapping(value = "/4/stopRecording/{instanceId}", method = RequestMethod.GET)
	public InlineResponse200 stopRecoding(@PathVariable("instanceId") Long instanceId) throws WiremockUIException {
		L.info("Start : QuickAccessController.stopRecoding() : instanceId = {}", instanceId);

		InlineResponse200 stopRecordResponse = recordingService.stopRecord(instanceId);

		L.info("End : QuickAccessController.stopRecoding() : instanceId = {}", instanceId);
		return stopRecordResponse;
	}

	@ApiOperation(value = "5 - Import mappings from Wiremock server to DB")
	@RequestMapping(value = "/5/importFromWiremock/instanceId/{instanceId}", method = RequestMethod.GET)
	public Integer importAllFromWiremockByInstanceId(@PathVariable("instanceId") Long instanceId, @RequestParam(defaultValue="100") Integer limit, @RequestParam(defaultValue="0") Integer offset) throws WiremockUIException {
		L.info("Start : QuickAccessController.importAllFromWiremockByInstanceId() : instanceId = {}, limit = {}, offset = {}", instanceId, limit, offset);

		InstanceMapping instanceMapping = mappingOperationsService.importWiremockRecordingsToDB(instanceId, limit, offset);
		Integer importedInstanceCount = (CollectionUtils.isEmpty(instanceMapping.getMappings())?0:instanceMapping.getMappings().size());

		L.info("End : QuickAccessController.importAllFromWiremockByInstanceId() : instanceId = {}, limit = {}, offset = {}", instanceId, limit, offset, importedInstanceCount);
		return importedInstanceCount;
	}

	@ApiOperation(value = "6 - Get all mappings from DB to local file system")
	@RequestMapping(value = "/6/importFromDB/instanceId/{instanceId}", method = RequestMethod.GET)
	public List<Body1> importAllFromDBByInstanceId(@PathVariable("instanceId") Long instanceId, @RequestParam(required = true) String downloadToFolder) throws WiremockUIException {
		L.info("Start : QuickAccessController.importAllFromDBByInstanceId() : instanceId = {}", instanceId);

		List<Body1> mappings = mappingOperationsService.importRecordingsFromDB(instanceId);
		try {
			downloadToFolder = mappingFolderService.updateWinFolderPath(downloadToFolder);
			mappingFolderService.createFilesInFolder(mappings, downloadToFolder);
		} catch (IOException e) {
			L.error("49 : QuickAccessController.importAllFromDBByInstanceId() : IOException e = {}", e);
			throw new WiremockUIException("Exception while writing the file content to local disk", e);
		}

		L.info("End : QuickAccessController.importAllFromDBByInstanceId() : instanceId = {}, MappingSize = {}", instanceId, (CollectionUtils.isEmpty(mappings)?0:mappings.size()));
		return mappings;
	}

	@ApiOperation(value = "7 - Export all mappings from local file system to DB")
	@RequestMapping(value = "/7/exportToDB/instanceId/{instanceId}", method = RequestMethod.POST)
	public InstanceMapping exportAllToDBByInstanceId(@PathVariable("instanceId") Long instanceId, @RequestParam(required = true) String exportFolder) throws WiremockUIException {
		L.info("Start : QuickAccessController.exportAllToDBByInstanceId() : instanceId = {}, mappingsFolder = {}", instanceId, exportFolder);

		List<String> lstFileNames = new ArrayList<String>();
		exportFolder = mappingFolderService.updateWinFolderPath(exportFolder);

		mappingFolderService.findFiles(exportFolder, lstFileNames);
		List<Body1> mappings;
		try {
			mappings = mappingFolderService.convertFilesToMappings(exportFolder, lstFileNames);
		} catch (IOException e) {
			L.error("62 : QuickAccessController.exportAllToDBByInstanceId() : IOException e = {}", e);
			throw new WiremockUIException("Exception while extracting the file content", e);
		}

		InstanceMapping instanceMapping = mappingOperationsService.exportRecordingsToDB(instanceId, mappings);

		L.info("End : QuickAccessController.exportAllToDBByInstanceId() : instanceId = {}, mappingsFolder = {}", instanceId, exportFolder);
		return instanceMapping;
	}

	@ApiOperation(value = "8 - Export all mappings from DB to Wiremock server")
	@RequestMapping(value = "/8/exportToWiremock/instanceId/{instanceId}", method = RequestMethod.POST)
	public boolean exportAllToWiremockByInstanceId(@PathVariable("instanceId") Long instanceId) throws WiremockUIException {
		L.info("Start : QuickAccessController.exportAllToWiremockByInstanceId() : instanceId = {}", instanceId);

		boolean exported = mappingOperationsService.exportWiremockRecordings(instanceId);

		L.info("End : QuickAccessController.exportAllToWiremockByInstanceId() : instanceId = {}, Exported = {}", instanceId, exported);
		return exported;
	}
}
