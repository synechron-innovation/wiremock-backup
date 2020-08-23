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

import in.ravikalla.wiremockbackup.dto.InstanceMappingDTO;
import in.ravikalla.wiremockbackup.service.InstanceMappingService;
import in.ravikalla.wiremockbackup.util.AppConstants;

@RestController
@RequestMapping(AppConstants.URI_INSTANCE)
public class InstanceMappingController {
	private static final Logger L = LogManager.getLogger(InstanceMappingController.class);

	@Autowired
	public InstanceMappingService instanceMappingService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<InstanceMappingDTO> getAll() {
		L.info("Start : InstanceMappingController.getAll()");

		List<InstanceMappingDTO> allMappings = instanceMappingService.getAllMappings();

		L.info("End : InstanceMappingController.getAll() : Size = {}", (CollectionUtils.isNotEmpty(allMappings)?allMappings.size():0));
		return allMappings;
	}
	@RequestMapping(value = "/withMappings", method = RequestMethod.GET)
	public List<InstanceMappingDTO> getAllWithMappings() {
		L.info("Start : InstanceMappingController.getAllWithMappings()");

		List<InstanceMappingDTO> allMappings = instanceMappingService.getAllMappingsFullDetails();

		L.info("End : InstanceMappingController.getAllWithMappings() : Size = {}", (CollectionUtils.isNotEmpty(allMappings)?allMappings.size():0));
		return allMappings;
	}
	@RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
	public InstanceMappingDTO getById(@PathVariable("id") Long id) {
		L.info("Start : InstanceMappingController.getById() : id = {}", id);

		InstanceMappingDTO instanceMappingDTO = instanceMappingService.getById(id);

		L.info("End : InstanceMappingController.getById() : id = {}, instanceMappingDTO = {}", id, instanceMappingDTO);
		return instanceMappingDTO;
	}
	@RequestMapping(value = "/id/{id}/withMappings", method = RequestMethod.GET)
	public InstanceMappingDTO getByIdWithMappings(@PathVariable("id") Long id) {
		L.info("Start : InstanceMappingController.getByIdWithMappings() : id = {}", id);

		InstanceMappingDTO instanceMappingDTO = instanceMappingService.getByIdFullDetails(id);

		L.info("End : InstanceMappingController.getByIdWithMappings() : id = {}, instanceMappingDTO = {}", id, instanceMappingDTO);
		return instanceMappingDTO;
	}

	@RequestMapping(value = "", method = RequestMethod.DELETE)
	public void delete(@RequestParam(value = "id", required = true) Long id) {
		L.info("Start : InstanceMappingController.delete() : id = {}", id);

		instanceMappingService.delete(id);

		L.info("End : InstanceMappingController.delete() : id = {}", id);
	}
	@RequestMapping(value = "", method = RequestMethod.POST)
	public Long create(@RequestBody InstanceMappingDTO instanceMappingDTO) {
		L.info("Start : InstanceMappingController.create() : instanceMappingDTO = {}", instanceMappingDTO);

		Long id = instanceMappingService.create(instanceMappingDTO);

		L.info("End : InstanceMappingController.create() : instanceMappingDTO = {}, id = {}", instanceMappingDTO, id);
		return id;
	}
	@RequestMapping(value = "", method = RequestMethod.PUT)
	public void update(@RequestBody InstanceMappingDTO instanceMappingDTO) {
		L.info("Start : InstanceMappingController.update() : instanceMappingDTO = {}", instanceMappingDTO);

		instanceMappingService.update(instanceMappingDTO);

		L.info("End : InstanceMappingController.update() : instanceMappingDTO = {}", instanceMappingDTO);
	}
}
