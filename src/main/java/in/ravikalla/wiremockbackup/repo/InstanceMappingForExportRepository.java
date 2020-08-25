package in.ravikalla.wiremockbackup.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import in.ravikalla.wiremockbackup.document.InstanceMappingForExport;

public interface InstanceMappingForExportRepository extends MongoRepository<InstanceMappingForExport, Long> {
}