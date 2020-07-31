package in.ravikalla.wiremockbackup.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import in.ravikalla.wiremockbackup.dto.RecordingDTO;

@Document
public class Recording {

	@Id
	private Long id;
	private Long instanceId;
	private String recordName;
	private String request;
	private String response;
	private String folderHierarchy;

	public Recording() {
		super();
	}
	public Recording(Long id, Long instanceId, String recordName, String request, String response, String folderHierarchy) {
		super();
		this.id = id;
		this.instanceId = instanceId;
		this.recordName = recordName;
		this.request = request;
		this.response = response;
		this.folderHierarchy = folderHierarchy;
	}
	public Recording(Long instanceId, String recordName, String request, String response, String folderHierarchy) {
		super();
		this.id = null;
		this.instanceId = instanceId;
		this.recordName = recordName;
		this.request = request;
		this.response = response;
		this.folderHierarchy = folderHierarchy;
	}
	public Recording(RecordingDTO recordingDTO) {
		super();
		this.id = recordingDTO.getId();
		this.instanceId = recordingDTO.getInstanceId();
		this.recordName = recordingDTO.getRecordName();
		this.request = recordingDTO.getRequest();
		this.response = recordingDTO.getResponse();
		this.folderHierarchy = recordingDTO.getFolderHierarchy();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}

	public String getRecordName() {
		return recordName;
	}

	public void setRecordName(String recordName) {
		this.recordName = recordName;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getFolderHierarchy() {
		return folderHierarchy;
	}

	public void setFolderHierarchy(String folderHierarchy) {
		this.folderHierarchy = folderHierarchy;
	}

	@Override
	public String toString() {
		return "Recording [id=" + id + ", instanceId=" + instanceId + ", recordName=" + recordName + ", request="
				+ request + ", response=" + response + ", folderHierarchy=" + folderHierarchy + "]";
	}
}