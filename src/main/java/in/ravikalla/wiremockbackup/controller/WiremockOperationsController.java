package in.ravikalla.wiremockbackup.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import in.ravikalla.wiremockbackup.dto.InstanceMappingDTO;
import in.ravikalla.wiremockbackup.service.InstanceMappingService;
import in.ravikalla.wiremockbackup.service.WiremockOperationsService;
import in.ravikalla.wiremockbackup.util.AppConstants;

@RestController
@RequestMapping(AppConstants.URI_OPERATIONS)
public class WiremockOperationsController {
	private static final Logger L = LogManager.getLogger(WiremockOperationsController.class);

	private InstanceMappingService instanceMappingService;
	private WiremockOperationsService wiremockOperationsService;

	public WiremockOperationsController(InstanceMappingService instanceMappingService, WiremockOperationsService wiremockOperationsService) {
		this.instanceMappingService = instanceMappingService;
		this.wiremockOperationsService = wiremockOperationsService;
	}

	@RequestMapping(value = "/import/instanceId/{instanceId}", method = RequestMethod.GET)
	public void importAllFromWiremockByInstanceId(@PathVariable("instanceId") Long instanceId) {
		L.info("Start : WiremockOperationsController.importAllFromWiremockByInstanceId() : instanceId = {}", instanceId);

		InstanceMappingDTO instanceMappingDTO = instanceMappingService.getById(instanceId);
		wiremockOperationsService.importWiremock(instanceMappingDTO.getHost(), instanceMappingDTO.getPort());

		L.info("End : WiremockOperationsController.importAllFromWiremockByInstanceId() : instanceId = {}", instanceId);
	}
	@RequestMapping(value = "/import/instanceName/{instanceName}", method = RequestMethod.GET)
	public void importAllFromWiremockByInstanceName(@PathVariable("instanceId") String instanceName) {
		L.info("Start : WiremockOperationsController.importAllFromWiremockByInstanceName() : instanceName = {}", instanceName);

		InstanceMappingDTO instanceMappingDTO = instanceMappingService.getByInstanceName(instanceName);
		wiremockOperationsService.importWiremock(instanceMappingDTO.getHost(), instanceMappingDTO.getPort());

		L.info("End : WiremockOperationsController.importAllFromWiremockByInstanceName() : instanceName = {}", instanceName);
	}
}