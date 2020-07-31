package in.ravikalla.wiremockbackup.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.ravikalla.wiremockbackup.document.Recording;
import in.ravikalla.wiremockbackup.dto.InstanceMappingDTO;
import in.ravikalla.wiremockbackup.dto.RecordingDTO;
import in.ravikalla.wiremockbackup.repo.RecordingRepository;

@Service
public class RecordingService {
	private static final Logger L = LogManager.getLogger(RecordingService.class);

	@Autowired
	private RecordingRepository recordingRepository; 

	public List<RecordingDTO> getAllMappingsForInstance(Long lngInstanceId) {
		L.debug("Start : RecordingService.getAllMappingsForInstance() : lngInstanceId = {}", lngInstanceId);

		List<RecordingDTO> lstRecordingDTO = null;
		List<Recording> lstRecording = recordingRepository.findAllByInstanceId(lngInstanceId);
		if (null != lstRecording)
			lstRecordingDTO = lstRecording.stream().map(recording -> new RecordingDTO(recording))
					.collect(Collectors.toList());

		L.debug("End : RecordingService.getAllMappingsForInstance() : lngInstanceId = {} : lstRecordingDTO = {}", lngInstanceId, (CollectionUtils.isEmpty(lstRecordingDTO)?0:lstRecordingDTO.size()));
		return lstRecordingDTO;
	}

	public RecordingDTO getById(Long id) {
		L.debug("Start : RecordingService.getById() :id = {}", id);
		Optional<Recording> recordingOptional = recordingRepository.findById(id);
		RecordingDTO recordingDTO = null;
		if (recordingOptional.isPresent())
			recordingDTO = new RecordingDTO(recordingOptional.get());
		L.debug("End : RecordingService.getById() :id = {}", id);
		return recordingDTO;
	}

	public void delete(Long id) {
		L.debug("Start : RecordingService.delete() : id = {}", id);
		recordingRepository.deleteById(id);
		L.debug("End : RecordingService.delete() : id = {}", id);
	}
	public void deleteForInstanceId(Long lngInstanceId) {
		L.debug("Start : RecordingService.deleteForInstance() : lngInstanceId = {}", lngInstanceId);
		recordingRepository.deleteByInstanceId(lngInstanceId);
		L.debug("End : RecordingService.deleteForInstance() : lngInstanceId = {}", lngInstanceId);
	}

	public Long create(RecordingDTO recordingDTO) {
		L.debug("Start : RecordingService.create() : recordingDTO = {}", recordingDTO);
		Recording insert = recordingRepository.insert(new Recording(recordingDTO));
		L.debug("End : RecordingService.create() : recordingDTO = {}, id = {}", recordingDTO, insert.getId());
		return insert.getId();
	}

	public RecordingDTO update(RecordingDTO recordingDTO) {
		L.debug("Start : RecordingService.update() : recordingDTO = {}", recordingDTO);
		Recording recording = recordingRepository.save(new Recording(recordingDTO));
		recordingDTO = new RecordingDTO(recording);
		L.debug("End : RecordingService.update() : recordingDTO = {}", recordingDTO);
		return recordingDTO;
	}
}
