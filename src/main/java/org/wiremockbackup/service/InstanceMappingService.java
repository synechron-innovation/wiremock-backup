package org.wiremockbackup.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wiremockbackup.document.InstanceMapping;
import org.wiremockbackup.dto.InstanceMappingDTO;
import org.wiremockbackup.repo.InstanceMappingRepository;

@Service
public class InstanceMappingService {
	private static final Logger L = LogManager.getLogger(InstanceMappingService.class);

	@Autowired
	private InstanceMappingRepository instanceMappingRepository;
	@Autowired
	private SequenceGeneratorService sequenceGeneratorService;

	public List<InstanceMappingDTO> getAllMappings() {
		L.debug("Start : InstanceMappingService.getAllMappings()");
		List<InstanceMapping> lstInstanceMapping = instanceMappingRepository.findBasicDetails();
		List<InstanceMappingDTO> lstInstanceMappingDTO = null;
		if (null != lstInstanceMapping)
			lstInstanceMappingDTO = lstInstanceMapping.stream()
		        .map(instanceMapping -> new InstanceMappingDTO(instanceMapping))
		        .collect(Collectors.toList());
		L.debug("End : InstanceMappingService.getAllMappings()");
		return lstInstanceMappingDTO;
	}

	public List<InstanceMappingDTO> getAllMappingsFullDetails() {
		L.debug("Start : InstanceMappingService.getAllMappingsFullDetails()");
		List<InstanceMapping> lstInstanceMapping = instanceMappingRepository.findAll();
		List<InstanceMappingDTO> lstInstanceMappingDTO = null;
		if (null != lstInstanceMapping)
			lstInstanceMappingDTO = lstInstanceMapping.stream()
		        .map(instanceMapping -> new InstanceMappingDTO(instanceMapping))
		        .collect(Collectors.toList());
		L.debug("End : InstanceMappingService.getAllMappingsFullDetails()");
		return lstInstanceMappingDTO;
	}

	public InstanceMappingDTO getById(Long id) {
		L.debug("Start : InstanceMappingService.getById() : id = {}", id);
		InstanceMapping instanceMappingOptional = instanceMappingRepository.findBasicDetailsById(id);
		InstanceMappingDTO instanceMappingDTO = null;
		instanceMappingDTO = new InstanceMappingDTO(instanceMappingOptional);
		L.debug("End : InstanceMappingService.getById() : id = {}", id);
		return instanceMappingDTO;
	}

	public InstanceMappingDTO getByIdFullDetails(Long id) {
		L.debug("Start : InstanceMappingService.getById() : id = {}", id);
		Optional<InstanceMapping> instanceMappingOptional = instanceMappingRepository.findById(id);
		InstanceMappingDTO instanceMappingDTO = null;
		if (instanceMappingOptional.isPresent())
			instanceMappingDTO = new InstanceMappingDTO(instanceMappingOptional.get());
		L.debug("End : InstanceMappingService.getById() : id = {}", id);
		return instanceMappingDTO;
	}

	public void delete(Long id) {
		L.debug("Start : InstanceMappingService.delete() : id = {}", id);
		instanceMappingRepository.deleteById(id);
		L.debug("End : InstanceMappingService.delete() : id = {}", id);
	}

	public Long create(String instanceName, String wiremockURL, String targetURL) {
		L.debug("Start : InstanceMappingService.create() : instanceName = {}, wiremockURL = {}, targetURL = {}", instanceName, wiremockURL, targetURL);
		Long id = sequenceGeneratorService.generateSequence(InstanceMapping.SEQUENCE_NAME);
		InstanceMapping instanceMapping = instanceMappingRepository.insert(new InstanceMapping(id, instanceName, wiremockURL, targetURL, null));
		L.debug("End : InstanceMappingService.create() : id = {}, instanceName = {}, wiremockURL = {}, targetURL = {}", id, instanceName, wiremockURL, targetURL);
		return instanceMapping.getId();
	}

	public InstanceMappingDTO update(InstanceMappingDTO instanceMappingDTO) {
		L.debug("Start : InstanceMappingService.update() : instanceMappingDTO = {}", instanceMappingDTO);
		InstanceMapping instanceMapping = instanceMappingRepository.save(new InstanceMapping(instanceMappingDTO));
		instanceMappingDTO = new InstanceMappingDTO(instanceMapping);
		L.debug("End : InstanceMappingService.update()");
		return instanceMappingDTO;
	}
}
