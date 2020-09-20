package org.wiremockbackup.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.client.model.InlineResponse2003.StatusEnum;

public class InstanceSummaryDTO {
	@ApiModelProperty(notes = "Primary key of wiremock instance. This is specific to UI application and not present in Wiremock server.")
	private Long id;
	@ApiModelProperty(notes = "Instance running status")
	private StatusEnum statusEnum;
	@ApiModelProperty(notes = "Mapping count of the instance")
	private Long mappingCount;

	public InstanceSummaryDTO() {
		super();
	}
	public InstanceSummaryDTO(Long id, StatusEnum statusEnum, Long mappingCount) {
		super();
		this.id = id;
		this.statusEnum = statusEnum;
		this.mappingCount = mappingCount;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public StatusEnum getStatusEnum() {
		return statusEnum;
	}
	public void setStatusEnum(StatusEnum statusEnum) {
		this.statusEnum = statusEnum;
	}
	public Long getMappingCount() {
		return mappingCount;
	}
	public void setMappingCount(Long mappingCount) {
		this.mappingCount = mappingCount;
	}

	@Override
	public String toString() {
		return "InstanceSummaryDTO [id=" + id + ", statusEnum=" + statusEnum + ", mappingCount=" + mappingCount + "]";
	}
}
