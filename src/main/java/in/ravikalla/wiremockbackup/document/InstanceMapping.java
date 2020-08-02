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
	private String host;
	private String port;

	@DBRef
    private Collection<Recording> recordings;

	public InstanceMapping() {
		super();
	}
	public InstanceMapping(String instanceName, String host, String port) {
		super();
		this.instanceName = instanceName;
		this.host = host;
		this.port = port;
	}
	public InstanceMapping(InstanceMappingDTO instanceMappingDTO) {
		super();
		this.id = instanceMappingDTO.getId();
		this.instanceName = instanceMappingDTO.getInstanceName();
		this.host = instanceMappingDTO.getHost();
		this.port = instanceMappingDTO.getPort();
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
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public Collection<Recording> getRecordings() {
		return recordings;
	}
	public void setRecordings(Collection<Recording> recordings) {
		this.recordings = recordings;
	}

	@Override
	public String toString() {
		return "InstanceMappingDTO [id=" + getId() + ",instanceName=" + instanceName + ", host=" + host + ", port=" + port + "]";
	}
}