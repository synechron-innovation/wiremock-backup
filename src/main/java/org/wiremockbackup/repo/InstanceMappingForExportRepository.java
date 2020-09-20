package org.wiremockbackup.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.wiremockbackup.document.InstanceMappingForExport;

public interface InstanceMappingForExportRepository extends MongoRepository<InstanceMappingForExport, Long> {
	@Query(value = "{'id' : ?0 }", fields = "{ 'id' : 1, 'instanceName' : 1, 'wiremockURL' : 1, 'targetURL' : 1}")
	InstanceMappingForExport findBasicDetailsById(Long instanceId);

	@Query(value = "{'id' : ?0 }", fields = "{ 'id' : 1, 'instanceName' : 1, 'wiremockURL' : 1, 'targetURL' : 1, 'history' : 1}")
	InstanceMappingForExport findBasicDetailsAndHistoryById(Long instanceId);
}