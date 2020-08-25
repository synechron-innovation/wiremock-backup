package in.ravikalla.wiremockbackup.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import in.ravikalla.wiremockbackup.document.InstanceMapping;
import in.ravikalla.wiremockbackup.exception.WiremockUIException;
import in.ravikalla.wiremockbackup.repo.InstanceMappingRepository;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.SystemApi;
import io.swagger.client.model.Body12;

@Service
public class SystemService {
	private static final Logger L = LogManager.getLogger(SystemService.class);

	private InstanceMappingRepository instanceMappingRepository;

	public SystemService(InstanceMappingRepository instanceMappingRepository) {
		this.instanceMappingRepository = instanceMappingRepository;
	}

	public void setFixedDelay(Long instanceId, BigDecimal fixedDelay) throws WiremockUIException {
		setFixedDelay(instanceMappingRepository.findById(instanceId), fixedDelay);
	}
	private void setFixedDelay(Optional<InstanceMapping> instanceMappingOptional, BigDecimal fixedDelay) throws WiremockUIException {
		L.debug("Start : SystemService.setFixedDelay(...) : fixedDelay = {}", fixedDelay);
		InstanceMapping instanceMapping = null;
		if (instanceMappingOptional.isPresent()) {
			instanceMapping = instanceMappingOptional.get();

			L.debug("36 : SystemService.setFixedDelay(...) : instanceMapping = {}", instanceMapping);

			ApiClient apiClient = new ApiClient();
			apiClient.setBasePath(instanceMapping.getHttps()?"https":"http" + "://" + instanceMapping.getHost() + ":" + instanceMapping.getPort());
			SystemApi systemApi = new SystemApi(apiClient);
			try {
				Body12 body12 = new Body12();
				body12.fixedDelay(fixedDelay);
				systemApi.adminSettingsPost(body12);
			} catch (ApiException e) {
				L.error("46 : SystemService.setFixedDelay(...) : Exception when Starting recording : ApiException e.Code = {}, e.Body = {}", e.getCode(), e.getResponseBody());
				throw new WiremockUIException("Custom exception while setting fixed delay", e);
			}
		} else {
			L.error("Error : SystemService.setFixedDelay(...) : couldn\'t find anything to start record");
		}

		L.debug("End : SystemService.setFixedDelay(...) : fixedDelay = {}", fixedDelay);
	}

	public void shutdown(Long instanceId) throws WiremockUIException {
		shutdown(instanceMappingRepository.findById(instanceId));
	}
	private void shutdown(Optional<InstanceMapping> instanceMappingOptional) throws WiremockUIException {
		L.debug("Start : SystemService.shutdown(...)");
		InstanceMapping instanceMapping = null;
		if (instanceMappingOptional.isPresent()) {
			instanceMapping = instanceMappingOptional.get();

			L.debug("64 : SystemService.shutdown(...) : instanceMapping = {}", instanceMapping);

			ApiClient apiClient = new ApiClient();
			apiClient.setBasePath(instanceMapping.getHttps()?"https":"http" + "://" + instanceMapping.getHost() + ":" + instanceMapping.getPort());
			SystemApi systemApi = new SystemApi(apiClient);
			try {
				systemApi.adminShutdownPost();
			} catch (ApiException e) {
				L.error("72 : SystemService.shutdown(...) : Exception when Starting recording : ApiException e.Code = {}, e.Body = {}", e.getCode(), e.getResponseBody());
				throw new WiremockUIException("Custom exception while shutting down", e);
			}
		} else {
			L.error("Error : SystemService.shutdown(...) : couldn\'t find anything to start record");
		}

		L.debug("End : SystemService.shutdown(...)");
	}
}
