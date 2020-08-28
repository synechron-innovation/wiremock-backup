package org.wiremockbackup.controller;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.wiremockbackup.exception.WiremockUIException;
import org.wiremockbackup.service.SystemService;
import org.wiremockbackup.util.AppConstants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(AppConstants.URI_SYSTEM)
@Api(tags={"6 - System Settings"})
public class SystemController {
	private static final Logger L = LogManager.getLogger(SystemController.class);

	private SystemService systemService;

	public SystemController(SystemService systemService) {
		this.systemService = systemService;
	}

	@RequestMapping(value = "/setFixedDelay/{instanceId}/fixedDelay/{fixedDelay}", method = RequestMethod.GET)
	@ApiOperation(value = "Set fixed delay for wiremock instance")
	public void setFixedDelay(@PathVariable("instanceId") Long instanceId, @PathVariable("fixedDelay") BigDecimal fixedDelay) throws WiremockUIException {
		L.info("Start : RecordingsController.setFixedDelay() : instanceId = {}, fixedDelay = {}", instanceId, fixedDelay);

		systemService.setFixedDelay(instanceId, fixedDelay);

		L.info("End : RecordingsController.setFixedDelay() : instanceId = {}, fixedDelay = {}", instanceId, fixedDelay);
	}

	@RequestMapping(value = "/shutDown/{instanceId}", method = RequestMethod.GET)
	@ApiOperation(value = "Shutdown Wiremock server based on InstanceID")
	public void shutDown(@PathVariable("instanceId") Long instanceId) throws WiremockUIException {
		L.info("Start : RecordingsController.shutDown() : instanceId = {}", instanceId);

		systemService.shutdown(instanceId);

		L.info("End : RecordingsController.shutDown() : instanceId = {}", instanceId);
	}
}