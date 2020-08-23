package in.ravikalla.wiremockbackup.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import in.ravikalla.wiremockbackup.service.RecordingService;
import in.ravikalla.wiremockbackup.util.AppConstants;

@RestController
@RequestMapping(AppConstants.URI_RECORDINGS)
public class RecordingsController {
	private static final Logger L = LogManager.getLogger(RecordingsController.class);

	private RecordingService recordingService;

	public RecordingsController(RecordingService recordingService) {
		this.recordingService = recordingService;
	}

	@RequestMapping(value = "/start/{instanceId}", method = RequestMethod.GET)
	public void startRecoding(@PathVariable("instanceId") Long instanceId) {
		L.info("Start : RecordingsController.startRecoding() : instanceId = {}", instanceId);

		recordingService.startRecord(instanceId);

		L.info("End : RecordingsController.startRecoding() : instanceId = {}", instanceId);
	}

	@RequestMapping(value = "/stop/{instanceId}", method = RequestMethod.GET)
	public void stopRecoding(@PathVariable("instanceId") Long instanceId) {
		L.info("Start : RecordingsController.stopRecoding() : instanceId = {}", instanceId);

		recordingService.stopRecord(instanceId);

		L.info("End : RecordingsController.stopRecoding() : instanceId = {}", instanceId);
	}
}