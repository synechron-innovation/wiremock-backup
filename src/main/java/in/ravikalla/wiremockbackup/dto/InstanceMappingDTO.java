package in.ravikalla.wiremockbackup.dto;

import in.ravikalla.wiremockbackup.document.InstanceMapping;

public class InstanceMappingDTO {
	private Long id;
	private String instanceName;
	private String host;
	private String port;

	public InstanceMappingDTO() {
		super();
	}
	public InstanceMappingDTO(Long id, String instanceName, String host, String port) {
		super();
		this.id = id;
		this.instanceName = instanceName;
		this.host = host;
		this.port = port;
	}
	public InstanceMappingDTO(String instanceName, String host, String port) {
		super();
		this.id = null;
		this.instanceName = instanceName;
		this.host = host;
		this.port = port;
	}
	public InstanceMappingDTO(InstanceMapping instanceMapping) {
		super();
		this.id = instanceMapping.getId();
		this.instanceName = instanceMapping.getInstanceName();
		this.host = instanceMapping.getHost();
		this.port = instanceMapping.getPort();
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

	@Override
	public String toString() {
		return "InstanceMappingDTO [id=" + id + ",instanceName=" + instanceName + ", host=" + host + ", port=" + port + "]";
	}
}
