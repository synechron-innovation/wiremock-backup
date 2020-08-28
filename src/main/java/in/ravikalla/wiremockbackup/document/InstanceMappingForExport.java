package in.ravikalla.wiremockbackup.document;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.client.model.Body;

/**
 *  
 * @author ravi_kalla
 *
 * This is a redundant collection for "InstanceMapping" class. This is created to use "Body" instead of "Body1" which is used in "InstanceMapping" class.
 * "Body" is used for exporting the instances to Wiremock. "Body1" is used for importing the instances from Wiremock.
 *
 */
@Document(collection = "instanceMapping")
public class InstanceMappingForExport {

	@Id
	private Long id;
	private String instanceName;
	private String wiremockURL;
	private String targetURL;

	private List<Body> mappings;

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
	public String getWiremockURL() {
		return wiremockURL;
	}
	public void setWiremockURL(String wiremockURL) {
		this.wiremockURL = wiremockURL;
	}
	public List<Body> getMappings() {
		return mappings;
	}
	public void setMappings(List<Body> mappings) {
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
		return "InstanceMappingDTO [id=" + getId() + ",instanceName=" + instanceName + ", wiremockURL=" + wiremockURL + ", MappingSize=" + (CollectionUtils.isEmpty(mappings)?0:mappings.size()) + ",targetURL=" + targetURL + "]";
	}
}