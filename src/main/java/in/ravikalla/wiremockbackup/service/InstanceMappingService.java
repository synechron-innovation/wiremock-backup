package in.ravikalla.wiremockbackup.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.ravikalla.wiremockbackup.document.InstanceMapping;
import in.ravikalla.wiremockbackup.dto.InstanceMappingDTO;
import in.ravikalla.wiremockbackup.repo.InstanceMappingRepository;

@Service
public class InstanceMappingService {
	private static final Logger L = LogManager.getLogger(InstanceMappingService.class);

	@Autowired
	private InstanceMappingRepository instanceMappingRepository; 

	public List<InstanceMappingDTO> getAllMappings() {
		L.debug("Start : InstanceMappingService.getAllMappings()");
		List<InstanceMapping> lstInstanceMapping = instanceMappingRepository.findAll();
		List<InstanceMappingDTO> lstInstanceMappingDTO = null;
		if (null != lstInstanceMapping)
			lstInstanceMappingDTO = lstInstanceMapping.stream()
		        .map(instanceMapping -> new InstanceMappingDTO(instanceMapping))
		        .collect(Collectors.toList());
		L.debug("End : InstanceMappingService.getAllMappings()");
		return lstInstanceMappingDTO;
	}

	public void delete(Long id) {
		L.debug("Start : InstanceMappingService.delete() : id = {}", id);
		instanceMappingRepository.deleteById(id);
		L.debug("End : InstanceMappingService.delete() : id = {}", id);
	}

	public Long create(InstanceMappingDTO instanceMappingDTO) {
		L.debug("Start : InstanceMappingService.create() : instanceMappingDTO = {}", instanceMappingDTO);
		InstanceMapping insert = instanceMappingRepository.insert(new InstanceMapping(instanceMappingDTO));
		L.debug("End : InstanceMappingService.create() : instanceMappingDTO = {}, id = {}", instanceMappingDTO, insert.getId());
		return insert.getId();
	}

	public InstanceMappingDTO update(InstanceMappingDTO instanceMappingDTO) {
		L.debug("Start : InstanceMappingService.update() : instanceMappingDTO = {}", instanceMappingDTO);
		InstanceMapping instanceMapping = instanceMappingRepository.save(new InstanceMapping(instanceMappingDTO));
		instanceMappingDTO = new InstanceMappingDTO(instanceMapping);
		L.debug("End : InstanceMappingService.update() : instanceMappingDTO = {}", instanceMappingDTO);
		return instanceMappingDTO;
	}
}
