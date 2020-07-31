package in.ravikalla.wiremockbackup.document;

import java.util.Collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import in.ravikalla.wiremockbackup.dto.InstanceMappingDTO;

@Document
public class InstanceMapping {

	@Id
	private Long id;
	private String instanceName;
	private String instanceUrl;

	@DBRef
    private Collection<Recording> recordings;

	public InstanceMapping() {
		super();
	}
	public InstanceMapping(String instanceName, String instanceUrl) {
		super();
		this.instanceName = instanceName;
		this.instanceUrl = instanceUrl;
	}
	public InstanceMapping(InstanceMappingDTO instanceMappingDTO) {
		super();
		this.id = instanceMappingDTO.getId();
		this.instanceName = instanceMappingDTO.getInstanceName();
		this.instanceUrl = instanceMappingDTO.getInstanceUrl();
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getInstanceName() {
		return instanceName;
	}
	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}
	public String getInstanceUrl() {
		return instanceUrl;
	}
	public void setInstanceUrl(String instanceUrl) {
		this.instanceUrl = instanceUrl;
	}
	public Collection<Recording> getRecordings() {
		return recordings;
	}
	public void setRecordings(Collection<Recording> recordings) {
		this.recordings = recordings;
	}

	@Override
	public String toString() {
		return "InstanceMappingDTO [id=" + getId() + ",instanceName=" + instanceName + ", instanceUrl=" + instanceUrl + "]";
	}
}