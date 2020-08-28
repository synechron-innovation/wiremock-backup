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
import io.swagger.client.ApiResponse;
import io.swagger.client.api.NearMissesApi;
import io.swagger.client.model.InlineResponse2002;

@Service
public class NearMissesService {
	private static final Logger L = LogManager.getLogger(NearMissesService.class);

	private InstanceMappingRepository instanceMappingRepository;

	public NearMissesService(InstanceMappingRepository instanceMappingRepository) {
		this.instanceMappingRepository = instanceMappingRepository;
	}

	public ApiResponse<InlineResponse2002> unmatchedNearMisses(Long instanceId) throws WiremockUIException {
		return unmatchedNearMisses(instanceMappingRepository.findById(instanceId));
	}
	private ApiResponse<InlineResponse2002> unmatchedNearMisses(Optional<InstanceMapping> instanceMappingOptional) throws WiremockUIException {
		L.debug("Start : NearMissesService.unmatchedNearMisses(...)");
		InstanceMapping instanceMapping = null;
		ApiResponse<InlineResponse2002> adminRequestsUnmatchedNearMissesGetWithHttpInfo = null;
		if (instanceMappingOptional.isPresent()) {
			instanceMapping = instanceMappingOptional.get();

			L.debug("43 : NearMissesService.unmatchedNearMisses(...) : instanceMapping = {}", instanceMapping);

			ApiClient apiClient = new ApiClient();
			apiClient.setBasePath(instanceMapping.getWiremockURL());
			NearMissesApi nearMissesApi = new NearMissesApi(apiClient);
			try {
				adminRequestsUnmatchedNearMissesGetWithHttpInfo = nearMissesApi.adminRequestsUnmatchedNearMissesGetWithHttpInfo();
			} catch (ApiException e) {
				L.error("61 : NearMissesService.unmatchedNearMisses(...) : Exception when getting near misses : ApiException e.Code = {}, e.Body = {}", e.getCode(), e.getResponseBody());
				throw new WiremockUIException("Custom exception while getting unmatchedNearMisses", e);
			}
		} else {
			L.error("Error : NearMissesService.unmatchedNearMisses(...) : couldn\'t find anything to get near misses");
		}
		L.debug("End : NearMissesService.unmatchedNearMisses(...)");
		return adminRequestsUnmatchedNearMissesGetWithHttpInfo;
	}
}
