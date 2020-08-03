package in.ravikalla.wiremockbackup.repo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import in.ravikalla.wiremockbackup.document.InstanceMapping;

public interface InstanceMappingRepository extends MongoRepository<InstanceMapping, Long> {
	Optional<InstanceMapping> findByInstanceName(String instanceName);
}