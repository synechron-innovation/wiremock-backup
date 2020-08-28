package org.wiremockbackup.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.wiremockbackup.document.InstanceMapping;

public interface InstanceMappingRepository extends MongoRepository<InstanceMapping, Long> {
	@Query(value = "{}", fields = "{ 'id' : 1, 'instanceName' : 1, 'wiremockURL' : 1, 'targetURL' : 1}")
	List<InstanceMapping> findBasicDetails();

	@Query(value = "{'id' : ?0 }", fields = "{ 'id' : 1, 'instanceName' : 1, 'wiremockURL' : 1, 'targetURL' : 1}")
	InstanceMapping findBasicDetailsById(Long instanceId);

	@Query(value = "{'id' : ?0 }", fields = "{ 'mappings' : 1}")
	InstanceMapping findMappingDetailsById(Long instanceId);
}