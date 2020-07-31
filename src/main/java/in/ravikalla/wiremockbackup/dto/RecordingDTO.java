package in.ravikalla.wiremockbackup.dto;

import in.ravikalla.wiremockbackup.document.InstanceMapping;
import in.ravikalla.wiremockbackup.document.Recording;

public class RecordingDTO {
	private Long id;
	private Long instanceId;
	private String recordName;
	private String request;
	private String response;
	private String folderHierarchy;

	public RecordingDTO() {
		super();
	}

	public RecordingDTO(Long id, Long instanceId, String recordName, String request, String response,
			String folderHierarchy) {
		super();
		this.setId(id);
		this.instanceId = instanceId;
		this.recordName = recordName;
		this.request = request;
		this.response = response;
		this.folderHierarchy = folderHierarchy;
	}

	public RecordingDTO(Long instanceId, String recordName, String request, String response, String folderHierarchy) {
		super();
		this.setId(null);
		this.instanceId = instanceId;
		this.recordName = recordName;
		this.request = request;
		this.response = response;
		this.folderHierarchy = folderHierarchy;
	}

	public RecordingDTO(Recording recording) {
		super();
		this.setId(recording.getId());
		this.instanceId = recording.getInstanceId();
		this.recordName = recording.getRecordName();
		this.request = recording.getRequest();
		this.response = recording.getResponse();
		this.folderHierarchy = recording.getFolderHierarchy();
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
		return "RecordingDTO [id=" + getId() + ", instanceId=" + instanceId + ", recordName=" + recordName + ", request="
				+ request + ", response=" + response + ", folderHierarchy=" + folderHierarchy + "]";
	}
}
