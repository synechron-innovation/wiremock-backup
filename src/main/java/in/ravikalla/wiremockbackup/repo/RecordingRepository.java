package in.ravikalla.wiremockbackup.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import in.ravikalla.wiremockbackup.document.Recording;

public interface RecordingRepository extends MongoRepository<Recording, Long> {
	public List<Recording> findAllByInstanceId(Long instanceId);
	public void deleteByInstanceId(Long lngInstanceId);
}