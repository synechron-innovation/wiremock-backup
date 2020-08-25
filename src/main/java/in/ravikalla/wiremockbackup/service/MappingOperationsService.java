package in.ravikalla.wiremockbackup.service;

import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import in.ravikalla.wiremockbackup.document.InstanceMapping;
import in.ravikalla.wiremockbackup.document.InstanceMappingForExport;
import in.ravikalla.wiremockbackup.exception.WiremockUIException;
import in.ravikalla.wiremockbackup.repo.InstanceMappingForExportRepository;
import in.ravikalla.wiremockbackup.repo.InstanceMappingRepository;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.StubMappingsApi;
import io.swagger.client.model.Body;
import io.swagger.client.model.Body1;
import io.swagger.client.model.InlineResponse200;

@Service
public class MappingOperationsService {
	private static final Logger L = LogManager.getLogger(MappingOperationsService.class);

	private InstanceMappingRepository instanceMappingRepository;
	private InstanceMappingForExportRepository instanceMappingForExportRepository;

	public MappingOperationsService(InstanceMappingRepository instanceMappingRepository, InstanceMappingForExportRepository instanceMappingForExportRepository) {
		this.instanceMappingRepository = instanceMappingRepository;
		this.instanceMappingForExportRepository = instanceMappingForExportRepository;
	}

	public InstanceMapping importWiremockRecordings(Long instanceId, Integer limit, Integer offset) throws WiremockUIException {
		return importWiremockRecordings(limit, offset, instanceMappingRepository.findById(instanceId));
	}
	private InstanceMapping importWiremockRecordings(Integer limit, Integer offset, Optional<InstanceMapping> instanceMappingOptional) throws WiremockUIException {
		L.debug("Start : MappingOperationsService.importWiremockRecordings(...) : limit = {}, offset = {}", limit, offset);
		InstanceMapping instanceMapping = null;
		if (instanceMappingOptional.isPresent()) {
			instanceMapping = instanceMappingOptional.get();

			L.debug("43 : MappingOperationsService.importWiremockRecordings(...) : instanceMapping = {}", instanceMapping);

			ApiClient apiClient = new ApiClient();
			apiClient.setBasePath(instanceMapping.getProtocol() + "://" + instanceMapping.getHost() + ":" + instanceMapping.getPort());
			StubMappingsApi apiInstance = new StubMappingsApi(apiClient);
			try {
				InlineResponse200 inlineResponse200 = apiInstance.adminMappingsGet(limit, offset);
				L.debug("50 : MappingOperationsService.importWiremockRecordings(...) : result = {}", inlineResponse200);

				if ((null != inlineResponse200) && CollectionUtils.isNotEmpty(inlineResponse200.getMappings())) {
					List<Body1> mappings = inlineResponse200.getMappings();
					if (CollectionUtils.isNotEmpty(mappings)) {
						instanceMapping.setMappings(mappings);
						instanceMapping = instanceMappingRepository.save(instanceMapping);
						L.debug("57 : MappingOperationsService.importWiremockRecordings(...) : saved size = {}", mappings.size());
					}
				}
			} catch (ApiException e) {
				L.error("62 : MappingOperationsService.importWiremockRecordings(...) : Exception when calling StubMappingsApi#adminMappingsGet : ApiException e.Code = {}, e.Body = {}", e.getCode(), e.getResponseBody());
				throw new WiremockUIException("Custom exception while importing Wiremock Recordings", e);
			}
		} else {
			L.error("Error : MappingOperationsService.importWiremockRecordings(...) : couldn\'t find anything to import");
		}

		L.debug("End : MappingOperationsService.importWiremockRecordings(...) : limit = {}, offset = {}", limit, offset);
		return instanceMapping;
	}

	public boolean exportWiremockRecordings(Long instanceId) throws WiremockUIException {
		return exportWiremockRecordings(instanceMappingForExportRepository.findById(instanceId));
	}
	private boolean exportWiremockRecordings(Optional<InstanceMappingForExport> instanceMappingForExportOptional) throws WiremockUIException {
		L.debug("Start : MappingOperationsService.exportWiremockRecordings(...)");
		boolean uploadSuccess = true;
		InstanceMappingForExport instanceMappingForExport = null;
		if (instanceMappingForExportOptional.isPresent()) {
			instanceMappingForExport = instanceMappingForExportOptional.get();

			if (CollectionUtils.isNotEmpty(instanceMappingForExport.getMappings())) {
				ApiClient apiClient = new ApiClient();
				apiClient.setBasePath(instanceMappingForExport.getProtocol() + "://" + instanceMappingForExport.getHost() + ":" + instanceMappingForExport.getPort());
				StubMappingsApi apiInstance = new StubMappingsApi(apiClient);
				try {
					apiInstance.adminMappingsDelete();
					for (Body body : instanceMappingForExport.getMappings()) {
						apiInstance.adminMappingsPost(body);
					}
					apiInstance.adminMappingsSavePost();
				} catch (ApiException e) {
					L.error("92 : MappingOperationsService.exportWiremockRecordings(...) : Exception when calling StubMappingsApi#adminMappingsGet : ApiException e.Code = {}, e.Body = {}", e.getCode(), e.getResponseBody());
					throw new WiremockUIException("Custom exception while exporting Wiremock Recordings", e);
//					uploadSuccess = false;
				}
			}
		} else {
			L.error("Error : MappingOperationsService.exportWiremockRecordings(...) : couldn\'t find anything to export");
			uploadSuccess = false;
		}

		L.debug("End : MappingOperationsService.exportWiremockRecordings(...) : uploadSuccess = {}", uploadSuccess);
		return uploadSuccess;
	}

	public boolean deleteWiremockRecordings(Long instanceId) throws WiremockUIException {
		return deleteWiremockRecordings(instanceMappingForExportRepository.findById(instanceId));
	}
	private boolean deleteWiremockRecordings(Optional<InstanceMappingForExport> instanceMappingForExportOptional) throws WiremockUIException {
		L.debug("Start : MappingOperationsService.exportWiremockRecordings(...)");
		boolean uploadSuccess = true;
		InstanceMappingForExport instanceMappingForExport = null;
		if (instanceMappingForExportOptional.isPresent()) {
			instanceMappingForExport = instanceMappingForExportOptional.get();

			if (CollectionUtils.isNotEmpty(instanceMappingForExport.getMappings())) {
				ApiClient apiClient = new ApiClient();
				apiClient.setBasePath(instanceMappingForExport.getProtocol() + "://" + instanceMappingForExport.getHost() + ":" + instanceMappingForExport.getPort());
				StubMappingsApi apiInstance = new StubMappingsApi(apiClient);
				try {
					apiInstance.adminMappingsDelete();
				} catch (ApiException e) {
					L.error("122 : MappingOperationsService.exportWiremockRecordings(...) : Exception when calling StubMappingsApi#adminMappingsGet : ApiException e.Code = {}, e.Body = {}", e.getCode(), e.getResponseBody());
					throw new WiremockUIException("Custom exception while deleting Wiremock Recordings", e);
//					uploadSuccess = false;
				}
			}
		} else {
			L.error("Error : MappingOperationsService.exportWiremockRecordings(...) : couldn\'t find anything to export");
			uploadSuccess = false;
		}

		L.debug("End : MappingOperationsService.exportWiremockRecordings(...) : uploadSuccess = {}", uploadSuccess);
		return uploadSuccess;
	}

	public List<Body1> importRecordingsFromDB(Long instanceId, Integer limit, Integer offset) {
		return instanceMappingRepository.findMappingDetailsById(instanceId);
	}

	public InstanceMapping exportRecordingsToDB(Long instanceId, List<Body1> mappings) {
		InstanceMapping instanceMapping = instanceMappingRepository.findBasicDetailsById(instanceId);
		instanceMapping.setMappings(mappings);
		return instanceMappingRepository.save(instanceMapping);
	}

	public void deleteMappingsOfInstanceFromDB(Long instanceId) {
		InstanceMapping instanceMapping = instanceMappingRepository.findBasicDetailsById(instanceId);
		// We are already getting the details without Mappings above. So, no need to set the mappings field to null while saving it below.
		instanceMappingRepository.save(instanceMapping);
	}
}
