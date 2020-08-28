package in.ravikalla.wiremockbackup.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import in.ravikalla.wiremockbackup.exception.WiremockUIException;
import in.ravikalla.wiremockbackup.service.RecordingService;
import in.ravikalla.wiremockbackup.util.AppConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.client.model.InlineResponse200;
import io.swagger.client.model.InlineResponse2003;
import io.swagger.client.model.InlineResponse2003.StatusEnum;

@RestController
@RequestMapping(AppConstants.URI_RECORDINGS)
@Api(tags={"4 - Start / Stop recording"})
public class RecordingsController {
	private static final Logger L = LogManager.getLogger(RecordingsController.class);

	private RecordingService recordingService;

	public RecordingsController(RecordingService recordingService) {
		this.recordingService = recordingService;
	}

	@ApiOperation(value = "Start recording on Wiremock server based on InstanceID")
	@RequestMapping(value = "/start/{instanceId}", method = RequestMethod.GET)
	public void startRecoding(@PathVariable("instanceId") Long instanceId) throws WiremockUIException {
		L.info("Start : RecordingsController.startRecoding() : instanceId = {}", instanceId);

		recordingService.startRecord(instanceId);

		L.info("End : RecordingsController.startRecoding() : instanceId = {}", instanceId);
	}

	@ApiOperation(value = "Stop recording on Wiremock server based on InstanceID")
	@RequestMapping(value = "/stop/{instanceId}", method = RequestMethod.GET)
	public InlineResponse200 stopRecoding(@PathVariable("instanceId") Long instanceId) throws WiremockUIException {
		L.info("Start : RecordingsController.stopRecoding() : instanceId = {}", instanceId);

		InlineResponse200 stopRecordResponse = recordingService.stopRecord(instanceId);

		L.info("End : RecordingsController.stopRecoding() : instanceId = {}", instanceId);
		return stopRecordResponse;
	}

	@ApiOperation(value = "Status of the Wiremock server recording based on InstanceID")
	@RequestMapping(value = "/status/{instanceId}", method = RequestMethod.GET)
	public StatusEnum statusOfRecoding(@PathVariable("instanceId") Long instanceId) throws WiremockUIException {
		L.info("Start : RecordingsController.statusOfRecoding() : instanceId = {}", instanceId);

		InlineResponse2003 statusOfRecoding = recordingService.statusOfRecoding(instanceId);

		L.info("End : RecordingsController.statusOfRecoding() : instanceId = {}", instanceId);
		return statusOfRecoding.getStatus();
	}
}