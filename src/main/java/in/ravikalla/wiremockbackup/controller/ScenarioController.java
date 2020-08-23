package in.ravikalla.wiremockbackup.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import in.ravikalla.wiremockbackup.service.ScenarioService;
import in.ravikalla.wiremockbackup.util.AppConstants;
import io.swagger.client.model.InlineResponse2004;

@RestController
@RequestMapping(AppConstants.URI_SCENARIO)
public class ScenarioController {
	private static final Logger L = LogManager.getLogger(ScenarioController.class);

	private ScenarioService scenarioService;

	public ScenarioController(ScenarioService scenarioService) {
		this.scenarioService = scenarioService;
	}

	@RequestMapping(value = "/{instanceId}", method = RequestMethod.GET)
	public InlineResponse2004 getScenarios(@PathVariable("instanceId") Long instanceId) {
		L.info("Start : RecordingsController.getScenarios() : instanceId = {}", instanceId);

		InlineResponse2004 scenarios = scenarioService.getScenarios(instanceId);

		L.info("End : RecordingsController.getScenarios() : instanceId = {}", instanceId);
		return scenarios;
	}

	@RequestMapping(value = "/reset/{instanceId}", method = RequestMethod.POST)
	public void resetScenarios(@PathVariable("instanceId") Long instanceId) {
		L.info("Start : RecordingsController.resetScenarios() : instanceId = {}", instanceId);

		scenarioService.resetScenarios(instanceId);

		L.info("End : RecordingsController.resetScenarios() : instanceId = {}", instanceId);
	}
}