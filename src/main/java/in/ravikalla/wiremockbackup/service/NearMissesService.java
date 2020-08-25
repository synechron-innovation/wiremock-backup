package in.ravikalla.wiremockbackup.service;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import in.ravikalla.wiremockbackup.document.InstanceMapping;
import in.ravikalla.wiremockbackup.exception.WiremockUIException;
import in.ravikalla.wiremockbackup.repo.InstanceMappingRepository;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.NearMissesApi;
import io.swagger.client.model.InlineResponse2002;

@Service
public class NearMissesService {
	private static final Logger L = LogManager.getLogger(NearMissesService.class);

	private InstanceMappingRepository instanceMappingRepository;

	public NearMissesService(InstanceMappingRepository instanceMappingRepository) {
		this.instanceMappingRepository = instanceMappingRepository;
	}

	public InlineResponse2002 unmatchedNearMisses(Long instanceId) throws WiremockUIException {
		return unmatchedNearMisses(instanceMappingRepository.findById(instanceId));
	}
	private InlineResponse2002 unmatchedNearMisses(Optional<InstanceMapping> instanceMappingOptional) throws WiremockUIException {
		L.debug("Start : RecordingService.startRecord(...)");
		InstanceMapping instanceMapping = null;
		InlineResponse2002 adminRequestsUnmatchedNearMissesGet = null;
		if (instanceMappingOptional.isPresent()) {
			instanceMapping = instanceMappingOptional.get();

			L.debug("43 : RecordingService.startRecord(...) : instanceMapping = {}", instanceMapping);

			ApiClient apiClient = new ApiClient();
			apiClient.setBasePath(instanceMapping.getHttps()?"https":"http" + "://" + instanceMapping.getHost() + ":" + instanceMapping.getPort());
			NearMissesApi nearMissesApi = new NearMissesApi(apiClient);
			try {
				adminRequestsUnmatchedNearMissesGet = nearMissesApi.adminRequestsUnmatchedNearMissesGet();
			} catch (ApiException e) {
				L.error("61 : RecordingService.startRecord(...) : Exception when Starting recording : ApiException e.Code = {}, e.Body = {}", e.getCode(), e.getResponseBody());
				throw new WiremockUIException("Custom exception while getting unmatchedNearMisses", e);
			}
		} else {
			L.error("Error : RecordingService.startRecord(...) : couldn\'t find anything to start record");
		}
		L.debug("End : RecordingService.startRecord(...)");
		return adminRequestsUnmatchedNearMissesGet;
	}
}
