package in.ravikalla.wiremockbackup.repo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import in.ravikalla.wiremockbackup.document.InstanceMappingForExport;

public interface InstanceMappingForExportRepository extends MongoRepository<InstanceMappingForExport, Long> {
	Optional<InstanceMappingForExport> findByInstanceName(String instanceName);
}