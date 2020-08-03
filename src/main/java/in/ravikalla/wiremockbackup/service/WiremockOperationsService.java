package in.ravikalla.wiremockbackup.service;

import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import in.ravikalla.wiremockbackup.document.InstanceMapping;
import in.ravikalla.wiremockbackup.document.InstanceMappingForExport;
import in.ravikalla.wiremockbackup.repo.InstanceMappingForExportRepository;
import in.ravikalla.wiremockbackup.repo.InstanceMappingRepository;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.StubMappingsApi;
import io.swagger.client.model.Body;
import io.swagger.client.model.Body1;
import io.swagger.client.model.InlineResponse200;

@Service
public class WiremockOperationsService {
	private static final Logger L = LogManager.getLogger(WiremockOperationsService.class);

	private InstanceMappingRepository instanceMappingRepository;
	private InstanceMappingForExportRepository instanceMappingForExportRepository;

	public WiremockOperationsService(InstanceMappingRepository instanceMappingRepository, InstanceMappingForExportRepository instanceMappingForExportRepository) {
		this.instanceMappingRepository = instanceMappingRepository;
		this.instanceMappingForExportRepository = instanceMappingForExportRepository;
	}

	public InstanceMapping importWiremockRecordings(Long instanceId, Integer limit, Integer offset) {
		return importWiremockRecordings(limit, offset, instanceMappingRepository.findById(instanceId));
	}
	public InstanceMapping importWiremockRecordings(String instanceName, Integer limit, Integer offset) {
		return importWiremockRecordings(limit, offset, instanceMappingRepository.findByInstanceName(instanceName));
	}
	private InstanceMapping importWiremockRecordings(Integer limit, Integer offset, Optional<InstanceMapping> instanceMappingOptional) {
		L.debug("Start : WiremockOperationsService.importWiremockRecordings(...) : limit = {}, offset = {}", limit, offset);
		InstanceMapping instanceMapping = null;
		if (instanceMappingOptional.isPresent()) {
			instanceMapping = instanceMappingOptional.get();

			L.debug("43 : WiremockOperationsService.importWiremockRecordings(...) : instanceMapping = {}", instanceMapping);

			ApiClient apiClient = new ApiClient();
			apiClient.setBasePath("http://" + instanceMapping.getHost() + ":" + instanceMapping.getPort());
			StubMappingsApi apiInstance = new StubMappingsApi(apiClient);
			try {
				InlineResponse200 inlineResponse200 = apiInstance.adminMappingsGet(limit, offset);
				L.debug("50 : WiremockOperationsService.importWiremockRecordings(...) : result = {}", inlineResponse200);

				if ((null != inlineResponse200) && CollectionUtils.isNotEmpty(inlineResponse200.getMappings())) {
					List<Body1> mappings = inlineResponse200.getMappings();
					if (CollectionUtils.isNotEmpty(mappings)) {
						instanceMapping.setMappings(mappings);
						instanceMapping = instanceMappingRepository.save(instanceMapping);
						L.debug("57 : WiremockOperationsService.importWiremockRecordings(...) : saved size = {}", mappings.size());
					}
				}
			} catch (ApiException e) {
				L.error("61 : WiremockOperationsService.importWiremockRecordings(...) : Exception when calling StubMappingsApi#adminMappingsGet : ApiException e.Code = {}, e.Body = {}", e.getCode(), e.getResponseBody());
			}
		} else {
			L.error("Error : WiremockOperationsService.importWiremockRecordings(...) : couldn\'t find anything to import");
		}

		L.debug("End : WiremockOperationsService.importWiremockRecordings(...) : limit = {}, offset = {}", limit, offset);
		return instanceMapping;
	}

	public boolean exportWiremockRecordings(Long instanceId) {
		return exportWiremockRecordings(instanceMappingForExportRepository.findById(instanceId));
	}
	public boolean exportWiremockRecordings(String instanceName) {
		return exportWiremockRecordings(instanceMappingForExportRepository.findByInstanceName(instanceName));
	}
	private boolean exportWiremockRecordings(Optional<InstanceMappingForExport> instanceMappingForExportOptional) {
		L.debug("Start : WiremockOperationsService.exportWiremockRecordings(...)");
		boolean uploadSuccess = true;
		InstanceMappingForExport instanceMappingForExport = null;
		if (instanceMappingForExportOptional.isPresent()) {
			instanceMappingForExport = instanceMappingForExportOptional.get();

			if (CollectionUtils.isNotEmpty(instanceMappingForExport.getMappings())) {
				ApiClient apiClient = new ApiClient();
				apiClient.setBasePath("http://" + instanceMappingForExport.getHost() + ":" + instanceMappingForExport.getPort());
				StubMappingsApi apiInstance = new StubMappingsApi(apiClient);
				try {
					apiInstance.adminMappingsDelete();
					for (Body body : instanceMappingForExport.getMappings()) {
						apiInstance.adminMappingsPost(body);
					}
				} catch (ApiException e) {
					L.error("61 : WiremockOperationsService.exportWiremockRecordings(...) : Exception when calling StubMappingsApi#adminMappingsGet : ApiException e.Code = {}, e.Body = {}", e.getCode(), e.getResponseBody());
					uploadSuccess = false;
				}
			}
		} else {
			L.error("Error : WiremockOperationsService.exportWiremockRecordings(...) : couldn\'t find anything to export");
			uploadSuccess = false;
		}

		L.debug("End : WiremockOperationsService.exportWiremockRecordings(...) : uploadSuccess = {}", uploadSuccess);
		return uploadSuccess;
	}

	public boolean deleteWiremockRecordings(Long instanceId) {
		return deleteWiremockRecordings(instanceMappingForExportRepository.findById(instanceId));
	}
	public boolean deleteWiremockRecordings(String instanceName) {
		return deleteWiremockRecordings(instanceMappingForExportRepository.findByInstanceName(instanceName));
	}
	private boolean deleteWiremockRecordings(Optional<InstanceMappingForExport> instanceMappingForExportOptional) {
		L.debug("Start : WiremockOperationsService.exportWiremockRecordings(...)");
		boolean uploadSuccess = true;
		InstanceMappingForExport instanceMappingForExport = null;
		if (instanceMappingForExportOptional.isPresent()) {
			instanceMappingForExport = instanceMappingForExportOptional.get();

			if (CollectionUtils.isNotEmpty(instanceMappingForExport.getMappings())) {
				ApiClient apiClient = new ApiClient();
				apiClient.setBasePath("http://" + instanceMappingForExport.getHost() + ":" + instanceMappingForExport.getPort());
				StubMappingsApi apiInstance = new StubMappingsApi(apiClient);
				try {
					apiInstance.adminMappingsDelete();
				} catch (ApiException e) {
					L.error("61 : WiremockOperationsService.exportWiremockRecordings(...) : Exception when calling StubMappingsApi#adminMappingsGet : ApiException e.Code = {}, e.Body = {}", e.getCode(), e.getResponseBody());
					uploadSuccess = false;
				}
			}
		} else {
			L.error("Error : WiremockOperationsService.exportWiremockRecordings(...) : couldn\'t find anything to export");
			uploadSuccess = false;
		}

		L.debug("End : WiremockOperationsService.exportWiremockRecordings(...) : uploadSuccess = {}", uploadSuccess);
		return uploadSuccess;
	}
}
