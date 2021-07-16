package org.wiremockbackup.controller;

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
import org.wiremockbackup.document.InstanceMapping;
import org.wiremockbackup.dto.InstanceMappingDTO;
import org.wiremockbackup.dto.InstanceSummaryDTO;
import org.wiremockbackup.exception.WiremockUIException;
import org.wiremockbackup.service.InstanceMappingService;
import org.wiremockbackup.service.MappingOperationsService;
import org.wiremockbackup.service.RecordingService;
import org.wiremockbackup.util.AppConstants;
import org.wiremockbackup.util.MappingTarget;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.client.model.InlineResponse200;
import io.swagger.client.model.InlineResponse2003;

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

	@ApiOperation(value = "5 - Download mappings")
	@RequestMapping(value = "/5/importFromWiremock/instanceId/{instanceId}", method = RequestMethod.GET)
	public Integer importAllFromWiremockByInstanceId(@PathVariable("instanceId") Long instanceId, @RequestParam String downloadFolder, @RequestParam(defaultValue="10000") Integer limit, @RequestParam(defaultValue="0") Integer offset) throws WiremockUIException {
		L.info("Start : QuickAccessController.importAllFromWiremockByInstanceId() : instanceId = {}, limit = {}, offset = {}", instanceId, limit, offset);

		InstanceMapping instanceMapping = mappingOperationsService.importWiremockRecordings(instanceId, MappingTarget.LOCAL, downloadFolder, limit, offset);
		Integer importedInstanceCount = (CollectionUtils.isEmpty(instanceMapping.getMappings())?0:instanceMapping.getMappings().size());

		L.info("End : QuickAccessController.importAllFromWiremockByInstanceId() : instanceId = {}, limit = {}, offset = {}", instanceId, limit, offset, importedInstanceCount);
		return importedInstanceCount;
	}

	@ApiOperation(value = "6 - Upload mappings")
	@RequestMapping(value = "/6/exportToWiremock/instanceId/{instanceId}", method = RequestMethod.POST)
	public boolean exportAllToWiremockByInstanceId(@PathVariable("instanceId") Long instanceId, @RequestParam String uploadFolder) throws WiremockUIException {
		L.info("Start : QuickAccessController.exportAllToWiremockByInstanceId() : instanceId = {}, uploadFolder = {}", instanceId, uploadFolder);

		boolean exported = mappingOperationsService.exportWiremockRecordings(instanceId, MappingTarget.LOCAL, uploadFolder);

		L.info("End : QuickAccessController.exportAllToWiremockByInstanceId() : instanceId = {}, Exported = {}, uploadFolder = {}", instanceId, exported, uploadFolder);
		return exported;
	}

	@ApiOperation(value = "7 - Get Recording Status and Mapping Count")
	@RequestMapping(value = "/7/recStatusAndMapCnt", method = RequestMethod.POST)
	public List<InstanceSummaryDTO> recStatusAndMapCnt(@RequestParam List<Long> instanceIds) throws WiremockUIException {
		L.info("Start : QuickAccessController.recStatusAndMapCnt() : instanceIdCount = {}", ((null == instanceIds)?0:instanceIds.size()));

		List<InstanceSummaryDTO> lstInstanceSummaryDTO = recordingService.statusOfRecoding(instanceIds);

		L.info("End : QuickAccessController.recStatusAndMapCnt() : instanceIdCount = {}", ((null == instanceIds)?0:instanceIds.size()));
		return lstInstanceSummaryDTO;
	}
//	@ApiOperation(value = "8 - View History for an Instance")
//	@RequestMapping(value = "/8/history/instanceId/{instanceId}", method = RequestMethod.GET)
//	public boolean getHistory(@PathVariable("instanceId") Long instanceId) throws WiremockUIException {
//		L.info("Start : QuickAccessController.getHistory() : instanceId = {}", instanceId);
//
//		boolean exported = mappingOperationsService.exportWiremockRecordings(instanceId, MappingTarget.LOCAL);
//
//		L.info("End : QuickAccessController.getHistory() : instanceId = {}", instanceId);
//		return exported;
//	}
}
