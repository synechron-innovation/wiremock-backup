package org.wiremockbackup.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.wiremockbackup.document.InstanceMapping;
import org.wiremockbackup.document.InstanceMappingForExport;
import org.wiremockbackup.exception.WiremockUIException;
import org.wiremockbackup.repo.InstanceMappingForExportRepository;
import org.wiremockbackup.repo.InstanceMappingRepository;
import org.wiremockbackup.util.MappingTarget;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.StubMappingsApi;
import io.swagger.client.model.Body;
import io.swagger.client.model.Body1;
import io.swagger.client.model.InlineResponse200;

@Service
public class MappingOperationsService {
	private static final Logger L = LogManager.getLogger(MappingOperationsService.class);

	public MappingFolderService mappingFolderService;
	private InstanceMappingRepository instanceMappingRepository;
	private InstanceMappingForExportRepository instanceMappingForExportRepository;

	public MappingOperationsService(InstanceMappingRepository instanceMappingRepository, InstanceMappingForExportRepository instanceMappingForExportRepository, MappingFolderService mappingFolderService) {
		this.instanceMappingRepository = instanceMappingRepository;
		this.instanceMappingForExportRepository = instanceMappingForExportRepository;
		this.mappingFolderService = mappingFolderService;
	}

	public InstanceMapping importWiremockRecordings(Long instanceId, MappingTarget mappingTarget, String downloadToFolder, Integer limit, Integer offset) throws WiremockUIException {
		return importWiremockRecordings(limit, offset, mappingTarget, downloadToFolder, instanceMappingRepository.findById(instanceId));
	}
	private InstanceMapping importWiremockRecordings(Integer limit, Integer offset, MappingTarget mappingTarget, String downloadToFolder, Optional<InstanceMapping> instanceMappingOptional) throws WiremockUIException {
		L.debug("Start : MappingOperationsService.importWiremockRecordings(...) : limit = {}, offset = {}, mappingTarget = {}", limit, offset, mappingTarget);
		InstanceMapping instanceMapping = null;
		if (instanceMappingOptional.isPresent()) {
			instanceMapping = instanceMappingOptional.get();

			L.debug("43 : MappingOperationsService.importWiremockRecordings(...) : instanceMapping = {}", instanceMapping);

			ApiClient apiClient = new ApiClient();
			apiClient.setBasePath(instanceMapping.getWiremockURL());
			StubMappingsApi apiInstance = new StubMappingsApi(apiClient);
			try {
				InlineResponse200 inlineResponse200 = apiInstance.adminMappingsGet(limit, offset);
				L.debug("50 : MappingOperationsService.importWiremockRecordings(...) : result = {}", inlineResponse200);

				if ((null != inlineResponse200) && CollectionUtils.isNotEmpty(inlineResponse200.getMappings())) {
					List<Body1> mappings = inlineResponse200.getMappings();
					if (CollectionUtils.isNotEmpty(mappings)) {
						instanceMapping.setMappings(mappings);
						if (MappingTarget.DB == mappingTarget)
							instanceMapping = instanceMappingRepository.save(instanceMapping);
						else
							mappingFolderService.createFilesInFolder(mappings, downloadToFolder);
						L.debug("57 : MappingOperationsService.importWiremockRecordings(...) : saved size = {}", mappings.size());
					}
				}
			} catch (ApiException e) {
				L.error("62 : MappingOperationsService.importWiremockRecordings(...) : Exception when calling StubMappingsApi#adminMappingsGet : ApiException e.Code = {}, e.Body = {}", e.getCode(), e.getResponseBody());
				throw new WiremockUIException("Custom exception while importing Wiremock Recordings", e);
			} catch (JsonParseException e) {
				L.error("78 : MappingOperationsService.importWiremockRecordings(...) : Exception when parsing JSON content | JsonParseException e = {}", e);
				throw new WiremockUIException("Exception when parsing JSON content", e);
			} catch (JsonMappingException e) {
				L.error("81 : MappingOperationsService.importWiremockRecordings(...) : Exception when mapping JSON content | JsonMappingException e = {}", e);
				throw new WiremockUIException("Exception when mapping JSON content", e);
			} catch (IOException e) {
				L.error("84 : MappingOperationsService.importWiremockRecordings(...) : IO Exception while writing content to file system | IOException e = {}", e);
				throw new WiremockUIException("IO Exception while writing content to file system", e);
			}
		} else {
			L.error("Error : MappingOperationsService.importWiremockRecordings(...) : couldn\'t find anything to import");
		}

		L.debug("End : MappingOperationsService.importWiremockRecordings(...) : limit = {}, offset = {}, mappingTarget = {}", limit, offset, mappingTarget);
		return instanceMapping;
	}

	public boolean exportWiremockRecordings(Long instanceId, MappingTarget mappingTarget, String uploadFromFolder) throws WiremockUIException {
		if (MappingTarget.DB == mappingTarget)
			return exportWiremockRecordingsFromDB(instanceMappingForExportRepository.findById(instanceId));
		else
			return exportWiremockRecordingsFromFileSystem(instanceMappingForExportRepository.findBasicDetailsById(instanceId), uploadFromFolder);
	}
	private boolean exportWiremockRecordingsFromDB(Optional<InstanceMappingForExport> instanceMappingForExportOptional) throws WiremockUIException {
		L.debug("Start : MappingOperationsService.exportWiremockRecordings(...)");
		boolean uploadSuccess = true;
		InstanceMappingForExport instanceMappingForExport = null;
		if (instanceMappingForExportOptional.isPresent()) {
			instanceMappingForExport = instanceMappingForExportOptional.get();

			if (CollectionUtils.isNotEmpty(instanceMappingForExport.getMappings())) {
				ApiClient apiClient = new ApiClient();
				apiClient.setBasePath(instanceMappingForExport.getWiremockURL());
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
				}
			}
		} else {
			L.error("Error : MappingOperationsService.exportWiremockRecordings(...) : couldn\'t find anything to export");
			uploadSuccess = false;
		}

		L.debug("Start : MappingOperationsService.exportWiremockRecordings(...) : uploadSuccess = {}", uploadSuccess);
		return uploadSuccess;
	}
	private boolean exportWiremockRecordingsFromFileSystem(InstanceMappingForExport instanceMappingForExport, String downloadToFolder) throws WiremockUIException {
		L.debug("Start : MappingOperationsService.exportWiremockRecordings(...) : downloadToFolder = {}", downloadToFolder);
		boolean uploadSuccess = true;
		if (null != instanceMappingForExport) {
			ApiClient apiClient = new ApiClient();
			apiClient.setBasePath(instanceMappingForExport.getWiremockURL());
			StubMappingsApi apiInstance = new StubMappingsApi(apiClient);
			try {
				apiInstance.adminMappingsDelete();

				List<Body> mappings = mappingFolderService.convertFilesToMappings(downloadToFolder);

				for (Body body : mappings)
					apiInstance.adminMappingsPost(body);

				apiInstance.adminMappingsSavePost();
			} catch (ApiException e) {
				L.error("145 : MappingOperationsService.exportWiremockRecordings(...) : Exception when calling StubMappingsApi#adminMappingsGet : ApiException e.Code = {}, e.Body = {}", e.getCode(), e.getResponseBody());
				throw new WiremockUIException("Custom exception while exporting Wiremock Recordings", e);
			} catch (JsonParseException e) {
				L.error("148 : MappingOperationsService.exportWiremockRecordings(...) : Exception while parsing JSON : JsonParseException = {}", e);
				throw new WiremockUIException("Exception while parsing JSON", e);
			} catch (JsonMappingException e) {
				L.error("151 : MappingOperationsService.exportWiremockRecordings(...) : Exception while Mapping JSON : JsonMappingException = {}", e);
				throw new WiremockUIException("Exception while mapping JSON", e);
			} catch (IOException e) {
				L.error("154 : MappingOperationsService.exportWiremockRecordings(...) : Exception while reading files from local file system : IOException = {}", e);
				throw new WiremockUIException("Exception while reading files from local file system", e);
			}
		} else {
			L.error("Error : MappingOperationsService.exportWiremockRecordings(...) : couldn\'t find anything to export");
			uploadSuccess = false;
		}

		L.debug("Start : MappingOperationsService.exportWiremockRecordings(...) : uploadSuccess = {}, downloadToFolder = {}", uploadSuccess, downloadToFolder);
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
				apiClient.setBasePath(instanceMappingForExport.getWiremockURL());
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

	// TODO : Optimize logic to get mappings. This retrieves history from the DB even if it is not necessary
	public List<Body1> importRecordingsFromDB(Long instanceId) {
		InstanceMapping instanceMapping = instanceMappingRepository.findMappingDetailsById(instanceId);
		List<Body1> mappings = null;
		if (null != instanceMapping)
			mappings = instanceMapping.getMappings();
		return mappings;
	}

	// TODO : Optimize logic to get history. Maintain history in a separate collection
	public List<String> importHistoryFromDB(Long instanceId) {
		return instanceMappingRepository.findHistoryById(instanceId).getHistory();
	}

	public InstanceMappingForExport exportRecordingsToDB(Long instanceId, List<Body> mappings, String comment) {
		InstanceMappingForExport instanceMappingForExport = instanceMappingForExportRepository.findBasicDetailsAndHistoryById(instanceId);
		instanceMappingForExport.setMappings(mappings);
		List<String> history = instanceMappingForExport.getHistory();
		if (null == history)
			history = new ArrayList<String>();
		history.add(comment);
		// TODO : Don't add history to this collection. Maintain it separately
		instanceMappingForExport.setHistory(history);
		return instanceMappingForExportRepository.save(instanceMappingForExport);
	}

	public void deleteMappingsOfInstanceFromDB(Long instanceId) {
		InstanceMapping instanceMapping = instanceMappingRepository.findBasicDetailsById(instanceId);
		// We are already getting the details without Mappings above. So, no need to set the mappings field to null while saving it below.
		instanceMappingRepository.save(instanceMapping);
	}
}
