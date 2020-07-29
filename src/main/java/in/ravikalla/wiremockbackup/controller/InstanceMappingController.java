package in.ravikalla.wiremockbackup.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import in.ravikalla.wiremockbackup.service.InstanceMappingService;
import in.ravikalla.wiremockbackup.util.AppConstants;

@RestController
@RequestMapping(AppConstants.URI_INSTANCE_MAPPING)
public class InstanceMappingController {
	private static final Logger L = LogManager.getLogger(InstanceMappingController.class);

	@Autowired
	public InstanceMappingService instanceMappingService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<String> getAll() {
		L.info("Start : InstanceMappingController.getAll()");

		List<String> allMappings = instanceMappingService.getAllMappings();

		L.info("End : InstanceMappingController.getAll()");
		return allMappings;
	}
}
