package in.ravikalla.wiremockbackup.document;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import in.ravikalla.wiremockbackup.dto.InstanceMappingDTO;
import in.ravikalla.wiremockbackup.util.Protocol;
import io.swagger.client.model.Body1;

@Document(collection = "instanceMapping")
public class InstanceMapping {
	@Transient
    public static final String SEQUENCE_NAME = "instance_sequence";

	@Id
	private Long id;
	private String instanceName;
	private Protocol protocol;
	private String host;
	private String port;
	private String targetURL;

	private List<Body1> mappings;

	public InstanceMapping() {
		super();
	}
	public InstanceMapping(InstanceMappingDTO instanceMappingDTO) {
		super();
		this.id = instanceMappingDTO.getId();
		this.instanceName = instanceMappingDTO.getInstanceName();
		this.protocol = instanceMappingDTO.getProtocol();
		this.host = instanceMappingDTO.getHost();
		this.port = instanceMappingDTO.getPort();
		this.targetURL = instanceMappingDTO.getTargetURL();
		this.mappings = instanceMappingDTO.getMappings();
	}

	public InstanceMapping(Long id, String instanceName, Protocol protocol, String host, String port, String targetURL, List<Body1> mappings) {
		this.id = id;
		this.instanceName = instanceName;
		this.protocol = protocol;
		this.host = host;
		this.port= port;
		this.targetURL = targetURL;
		this.mappings = mappings;
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
	public Protocol getProtocol() {
		return protocol;
	}
	public void setProtocol(Protocol protocol) {
		this.protocol = protocol;
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
		return "InstanceMappingDTO [id=" + getId() + ",instanceName=" + instanceName + ", protocol=" + protocol + ", host=" + host + ", port=" + port + ", MappingSize=" + (CollectionUtils.isEmpty(mappings)?0:mappings.size()) + ",targetURL=" + targetURL + "]";
	}
}