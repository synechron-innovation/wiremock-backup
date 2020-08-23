package in.ravikalla.wiremockbackup.dto;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import in.ravikalla.wiremockbackup.document.InstanceMapping;
import io.swagger.client.model.Body1;

public class InstanceMappingDTO {
	private Long id;
	private String instanceName;
	private String host;
	private String port;
	private String targetURL;
	private List<Body1> mappings;

	public InstanceMappingDTO() {
		super();
	}
	public InstanceMappingDTO(Long id, String instanceName, String host, String port, String targetURL, List<Body1> mappings) {
		super();
		this.id = id;
		this.instanceName = instanceName;
		this.host = host;
		this.port = port;
		this.targetURL = targetURL;
		this.mappings = mappings;
	}
	public InstanceMappingDTO(String instanceName, String host, String port, String targetURL, List<Body1> mappings) {
		super();
		this.id = null;
		this.instanceName = instanceName;
		this.host = host;
		this.port = port;
		this.targetURL = targetURL;
		this.mappings = mappings;
	}
	public InstanceMappingDTO(InstanceMapping instanceMapping) {
		super();
		this.id = instanceMapping.getId();
		this.instanceName = instanceMapping.getInstanceName();
		this.host = instanceMapping.getHost();
		this.port = instanceMapping.getPort();
		this.targetURL = instanceMapping.getTargetURL();
		this.mappings = instanceMapping.getMappings();
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
	public List<Body1> getMappings() {
		return mappings;
	}
	public void setMappings(List<Body1> mappings) {
		this.mappings = mappings;
	}
	public String getTargetURL() {
		return targetURL;
	}
	public void setTargetURL(String targetURL) {
		this.targetURL = targetURL;
	}

	@Override
	public String toString() {
		return "InstanceMappingDTO [id=" + id + ",instanceName=" + instanceName + ", host=" + host + ", port=" + port + ", MappingSize=" + (CollectionUtils.isEmpty(mappings)?0:mappings.size()) + ",targetURL=" + targetURL + "]";
	}
}
