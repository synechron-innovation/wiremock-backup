package in.ravikalla.wiremockbackup.controller;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.ravikalla.wiremockbackup.dto.RecordingDTO;
import in.ravikalla.wiremockbackup.service.RecordingService;
import in.ravikalla.wiremockbackup.util.AppConstants;

@RestController
@RequestMapping(AppConstants.URI_RECORDING)
public class RecordingController {
	private static final Logger L = LogManager.getLogger(RecordingController.class);

	@Autowired
	public RecordingService recordingService;

	@RequestMapping(value = "/all/{instanceId}", method = RequestMethod.GET)
	public List<RecordingDTO> getAllByInstanceId(@PathVariable("instanceId") Long instanceId) {
		L.info("Start : RecordingController.getAll() : instanceId = {}", instanceId);

		List<RecordingDTO> allMappings = recordingService.getAllMappingsForInstance(instanceId);

		L.info("End : RecordingController.getAll() : instanceId = {}, Size = {}", instanceId, (CollectionUtils.isNotEmpty(allMappings)?allMappings.size():0));
		return allMappings;
	}
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public RecordingDTO get(@PathVariable("id") Long id) {
		L.info("Start : RecordingController.getAll() : id = {}", id);

		RecordingDTO recordingDTO = recordingService.getById(id);

		L.info("End : RecordingController.getAll() : id = {}, recordingDTO = {}", id, recordingDTO);
		return recordingDTO;
	}
	@RequestMapping(value = "/all/{instanceId}", method = RequestMethod.DELETE)
	public void deleteAllByInstanceId(@PathVariable("instanceId") Long instanceId) {
		L.info("Start : RecordingController.deleteAllByInstanceId() : instanceId = {}", instanceId);

		recordingService.deleteForInstanceId(instanceId);

		L.info("End : RecordingController.deleteAllByInstanceId() : instanceId = {}", instanceId);
	}
	@RequestMapping(value = "", method = RequestMethod.DELETE)
	public void delete(@RequestParam(value = "id", required = true) Long id) {
		L.info("Start : RecordingController.delete() : id = {}", id);

		recordingService.delete(id);

		L.info("End : RecordingController.delete() : id = {}", id);
	}
	@RequestMapping(value = "", method = RequestMethod.POST)
	public Long create(@RequestBody RecordingDTO recordingDTO) {
		L.info("Start : RecordingController.create() : recordingDTO = {}", recordingDTO);

		Long id = recordingService.create(recordingDTO);

		L.info("End : RecordingController.create() : recordingDTO = {}, id = {}", recordingDTO, id);
		return id;
	}
	@RequestMapping(value = "", method = RequestMethod.PUT)
	public void update(@RequestBody RecordingDTO recordingDTO) {
		L.info("Start : RecordingController.update() : recordingDTO = {}", recordingDTO);

		recordingService.update(recordingDTO);

		L.info("End : RecordingController.update() : recordingDTO = {}", recordingDTO);
	}
}
