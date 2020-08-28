package org.wiremockbackup.service;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.wiremockbackup.document.InstanceMapping;
import org.wiremockbackup.exception.WiremockUIException;
import org.wiremockbackup.repo.InstanceMappingRepository;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.ScenariosApi;
import io.swagger.client.model.InlineResponse2004;

@Service
public class ScenarioService {
	private static final Logger L = LogManager.getLogger(ScenarioService.class);

	private InstanceMappingRepository instanceMappingRepository;

	public ScenarioService(InstanceMappingRepository instanceMappingRepository) {
		this.instanceMappingRepository = instanceMappingRepository;
	}

	public InlineResponse2004 getScenarios(Long instanceId) throws WiremockUIException {
		return getScenarios(instanceMappingRepository.findById(instanceId));
	}
	private InlineResponse2004 getScenarios(Optional<InstanceMapping> instanceMappingOptional) throws WiremockUIException {
		L.debug("Start : SystemService.getScenarios(...)");
		InstanceMapping instanceMapping = null;
		InlineResponse2004 adminScenariosGet = null;
		if (instanceMappingOptional.isPresent()) {
			instanceMapping = instanceMappingOptional.get();

			L.debug("39 : SystemService.getScenarios(...) : instanceMapping = {}", instanceMapping);

			ApiClient apiClient = new ApiClient();
			apiClient.setBasePath(instanceMapping.getWiremockURL());
			ScenariosApi scenariosApi = new ScenariosApi(apiClient);
			try {
				adminScenariosGet = scenariosApi.adminScenariosGet();
			} catch (ApiException e) {
				L.error("47 : SystemService.getScenarios(...) : Exception when Starting recording : ApiException e.Code = {}, e.Body = {}", e.getCode(), e.getResponseBody());
				throw new WiremockUIException("Custom exception while getting scenarios", e);
			}
		} else {
			L.error("Error : SystemService.getScenarios(...) : couldn\'t find anything to start record");
		}

		L.debug("End : SystemService.getScenarios(...)");
		return adminScenariosGet;
	}

	public void resetScenarios(Long instanceId) throws WiremockUIException {
		resetScenarios(instanceMappingRepository.findById(instanceId));
	}

	private void resetScenarios(Optional<InstanceMapping> instanceMappingOptional) throws WiremockUIException {
		L.debug("Start : SystemService.resetScenarios(...)");
		InstanceMapping instanceMapping = null;
		if (instanceMappingOptional.isPresent()) {
			instanceMapping = instanceMappingOptional.get();

			L.debug("39 : SystemService.resetScenarios(...) : instanceMapping = {}", instanceMapping);

			ApiClient apiClient = new ApiClient();
			apiClient.setBasePath(instanceMapping.getWiremockURL());
			ScenariosApi scenariosApi = new ScenariosApi(apiClient);
			try {
				scenariosApi.adminScenariosResetPost();
			} catch (ApiException e) {
				L.error("47 : SystemService.resetScenarios(...) : Exception when resetting scenarios : ApiException e.Code = {}, e.Body = {}", e.getCode(), e.getResponseBody());
				throw new WiremockUIException("Custom exception while resetting scenarios", e);
			}
		} else {
			L.error("Error : SystemService.resetScenarios(...) : couldn\'t find anything to reset the post");
		}

		L.debug("End : SystemService.resetScenarios(...)");
	}
}
