package in.ravikalla.wiremockbackup.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import in.ravikalla.wiremockbackup.document.InstanceMapping;

public interface InstanceMappingRepository extends MongoRepository<InstanceMapping, Long> {
	@Query(value = "{}", fields = "{ 'id' : 1, 'instanceName' : 1, 'https' : 1, 'host' : 1, 'port' : 1, 'targetURL' : 1}")
	List<InstanceMapping> findBasicDetails();

	@Query(value = "{'id' : ?0 }", fields = "{ 'id' : 1, 'instanceName' : 1, 'https' : 1, 'host' : 1, 'port' : 1, 'targetURL' : 1}")
	InstanceMapping findBasicDetailsById(Long instanceId);

	@Query(value = "{'id' : ?0 }", fields = "{ 'mappings' : 1}")
	InstanceMapping findMappingDetailsById(Long instanceId);
}