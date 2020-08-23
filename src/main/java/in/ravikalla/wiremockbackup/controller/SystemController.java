package in.ravikalla.wiremockbackup.controller;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import in.ravikalla.wiremockbackup.service.SystemService;
import in.ravikalla.wiremockbackup.util.AppConstants;

@RestController
@RequestMapping(AppConstants.URI_SYSTEM)
public class SystemController {
	private static final Logger L = LogManager.getLogger(SystemController.class);

	private SystemService systemService;

	public SystemController(SystemService systemService) {
		this.systemService = systemService;
	}

	@RequestMapping(value = "/setFixedDelay/{instanceId}/fixedDelay/{fixedDelay}", method = RequestMethod.GET)
	public void setFixedDelay(@PathVariable("instanceId") Long instanceId, @PathVariable("fixedDelay") BigDecimal fixedDelay) {
		L.info("Start : RecordingsController.setFixedDelay() : instanceId = {}, fixedDelay = {}", instanceId, fixedDelay);

		systemService.setFixedDelay(instanceId, fixedDelay);

		L.info("End : RecordingsController.setFixedDelay() : instanceId = {}, fixedDelay = {}", instanceId, fixedDelay);
	}

	@RequestMapping(value = "/shutDown/{instanceId}", method = RequestMethod.GET)
	public void shutDown(@PathVariable("instanceId") Long instanceId) {
		L.info("Start : RecordingsController.shutDown() : instanceId = {}", instanceId);

		systemService.shutdown(instanceId);

		L.info("End : RecordingsController.shutDown() : instanceId = {}", instanceId);
	}
}