package in.ravikalla.wiremockbackup.dto;

import in.ravikalla.wiremockbackup.document.InstanceMapping;

public class InstanceMappingDTO {
	private Long id;
	private String instanceName;
	private String instanceUrl;

	public InstanceMappingDTO() {
		super();
	}
	public InstanceMappingDTO(Long id, String instanceName, String instanceUrl) {
		super();
		this.id = id;
		this.instanceName = instanceName;
		this.instanceUrl = instanceUrl;
	}
	public InstanceMappingDTO(String instanceName, String instanceUrl) {
		super();
		this.id = null;
		this.instanceName = instanceName;
		this.instanceUrl = instanceUrl;
	}
	public InstanceMappingDTO(InstanceMapping instanceMapping) {
		super();
		this.id = instanceMapping.getId();
		this.instanceName = instanceMapping.getInstanceName();
		this.instanceUrl = instanceMapping.getInstanceUrl();
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

	@Override
	public String toString() {
		return "InstanceMappingDTO [id=" + id + ",instanceName=" + instanceName + ", instanceUrl=" + instanceUrl + "]";
	}
}
