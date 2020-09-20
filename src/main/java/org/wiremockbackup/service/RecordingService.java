package org.wiremockbackup.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.wiremockbackup.document.InstanceMapping;
import org.wiremockbackup.dto.InstanceSummaryDTO;
import org.wiremockbackup.exception.WiremockUIException;
import org.wiremockbackup.repo.InstanceMappingRepository;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.RecordingsApi;
import io.swagger.client.model.Body10;
import io.swagger.client.model.InlineResponse200;
import io.swagger.client.model.InlineResponse2003;

@Service
public class RecordingService {
	private static final Logger L = LogManager.getLogger(RecordingService.class);

	private InstanceMappingRepository instanceMappingRepository;

	public RecordingService(InstanceMappingRepository instanceMappingRepository) {
		this.instanceMappingRepository = instanceMappingRepository;
	}

	public void startRecord(Long instanceId) throws WiremockUIException {
		startRecord(instanceMappingRepository.findById(instanceId));
	}
	private void startRecord(Optional<InstanceMapping> instanceMappingOptional) throws WiremockUIException {
		L.debug("Start : RecordingService.startRecord(...)");
		InstanceMapping instanceMapping = null;
		if (instanceMappingOptional.isPresent()) {
			instanceMapping = instanceMappingOptional.get();

			L.debug("43 : RecordingService.startRecord(...) : instanceMapping = {}", instanceMapping);

			ApiClient apiClient = new ApiClient();
			apiClient.setBasePath(instanceMapping.getWiremockURL());
			RecordingsApi recordingsApi = new RecordingsApi(apiClient);
			try {
				Body10 body10 = new Body10();
				body10.setTargetBaseUrl(instanceMapping.getTargetURL());
				recordingsApi.adminRecordingsStartPost(body10);
			} catch (ApiException e) {
				L.error("61 : RecordingService.startRecord(...) : Exception when Starting recording : ApiException e.Code = {}, e.Body = {}", e.getCode(), e.getResponseBody());
				throw new WiremockUIException("Custom exception while starting recording", e);
			}
		} else {
			L.error("Error : RecordingService.startRecord(...) : couldn\'t find anything to start record");
		}

		L.debug("End : RecordingService.startRecord(...)");
	}

	public InlineResponse200 stopRecord(Long instanceId) throws WiremockUIException {
		L.debug("Start : RecordingService.stopRecord(...) : instanceId = {}", instanceId);
		InlineResponse200 inlineResponse200 = stopRecord(instanceMappingRepository.findById(instanceId));
		L.debug("End : RecordingService.stopRecord(...) : instanceId = {}", instanceId);
		return inlineResponse200;
	}
	private InlineResponse200 stopRecord(Optional<InstanceMapping> instanceMappingOptional) throws WiremockUIException {
		InstanceMapping instanceMapping = null;
		InlineResponse200 inlineResponse200 = null;
		if (instanceMappingOptional.isPresent()) {
			instanceMapping = instanceMappingOptional.get();

			L.debug("63 : RecordingService.record(...) : instanceMapping = {}", instanceMapping);

			ApiClient apiClient = new ApiClient();
			apiClient.setBasePath(instanceMapping.getWiremockURL());
			RecordingsApi recordingsApi = new RecordingsApi(apiClient);
			try {
				inlineResponse200 = recordingsApi.adminRecordingsStopPost();
			} catch (ApiException e) {
				L.error("73 : RecordingService.stopRecord(...) stopRecordption when Stopping recording : ApiException e.Code = {}, e.Body = {}", e.getCode(), e.getResponseBody());
				throw new WiremockUIException("Custom exception while stopping", e);
			}
		} else {
			L.error("Error : RecordingService.stopRecord(...) : couldn\'t find anything to stop recording");
		}
		return inlineResponse200;
	}

	public InlineResponse2003 statusOfRecoding(Long instanceId) throws WiremockUIException {
		return statusOfRecoding(instanceMappingRepository.findById(instanceId));
	}
	public List<InstanceSummaryDTO> statusOfRecoding(List<Long> lstInstanceId) throws WiremockUIException {
		// TODO : Lazy load Mappings inside "InstanceMapping" object
		return statusOfRecodingList(instanceMappingRepository.findAllById(lstInstanceId));
	}
	public InlineResponse2003 statusOfRecoding(Optional<InstanceMapping> instanceMappingOptional) throws WiremockUIException {
		L.debug("Start : RecordingService.statusOfRecoding(...)");
		InlineResponse2003 adminRecordingsStatusGet = null;
		InstanceMapping instanceMapping = null;
		if (instanceMappingOptional.isPresent()) {
			instanceMapping = instanceMappingOptional.get();

			L.debug("102 : RecordingService.statusOfRecoding(...) : instanceMapping = {}", instanceMapping);

			ApiClient apiClient = new ApiClient();
			apiClient.setBasePath(instanceMapping.getWiremockURL());
			RecordingsApi recordingsApi = new RecordingsApi(apiClient);
			try {
				adminRecordingsStatusGet = recordingsApi.adminRecordingsStatusGet();
			} catch (ApiException e) {
				L.error("110 : RecordingService.statusOfRecoding(...) : Exception when Starting recording : ApiException e.Code = {}, e.Body = {}", e.getCode(), e.getResponseBody());
				throw new WiremockUIException("Custom exception while getting status", e);
			}
		} else {
			L.error("Error : RecordingService.statusOfRecoding(...) : couldn\'t find anything to start record");
		}

		L.debug("End : RecordingService.statusOfRecoding(...)");
		return adminRecordingsStatusGet;
	}
	public List<InstanceSummaryDTO> statusOfRecodingList(Iterable<InstanceMapping> instanceMappingIterable) throws WiremockUIException {
		L.debug("Start : RecordingService.statusOfRecodingList(...)");
		List<InstanceSummaryDTO> lstAdminRecordingsStatusGet = new ArrayList<InstanceSummaryDTO>();
		InlineResponse2003 adminRecordingsStatusGet = null;
		InstanceMapping instanceMapping = null;
		if (null != instanceMappingIterable) {
			Iterator<InstanceMapping> iterator = instanceMappingIterable.iterator();
			while (iterator.hasNext()) {
				instanceMapping = iterator.next();

				// TODO : Get this count in a separate query to DB as "InstanceMapping" object should be lazy loaded
				int intMappingCount = (CollectionUtils.isEmpty(instanceMapping.getMappings())?0:instanceMapping.getMappings().size());

				L.debug("131 : RecordingService.statusOfRecodingList(...) : instanceMapping = {}", instanceMapping);

				ApiClient apiClient = new ApiClient();
				apiClient.setBasePath(instanceMapping.getWiremockURL());
				RecordingsApi recordingsApi = new RecordingsApi(apiClient);
				try {
					adminRecordingsStatusGet = recordingsApi.adminRecordingsStatusGet();
					lstAdminRecordingsStatusGet.add(new InstanceSummaryDTO(instanceMapping.getId(), adminRecordingsStatusGet.getStatus(),new Long(intMappingCount)));
					L.debug("137 : RecordingService.statusOfRecodingList(...) : Status = {}, instanceMapping = {}", adminRecordingsStatusGet.getStatus(), instanceMapping);
				} catch (ApiException e) {
					L.error("61 : RecordingService.statusOfRecodingList(...) : Exception when Starting recording : ApiException e.Code = {}, e.Body = {}", e.getCode(), e.getResponseBody());
					throw new WiremockUIException("Custom exception while getting status", e);
				}
			}
		} else {
			L.error("Error : RecordingService.statusOfRecodingList(...) : couldn\'t find anything to start record");
		}

		L.debug("End : RecordingService.statusOfRecodingList(...)");
		return lstAdminRecordingsStatusGet;
	}
}
