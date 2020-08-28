package org.wiremockbackup.component;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;
import org.wiremockbackup.document.InstanceMapping;
import org.wiremockbackup.service.SequenceGeneratorService;

@Component
public class InstanceMappingListener extends AbstractMongoEventListener<InstanceMapping> {

	private SequenceGeneratorService sequenceGenerator;

	public InstanceMappingListener(SequenceGeneratorService sequenceGenerator) {
		this.sequenceGenerator = sequenceGenerator;
	}

	@Override
	public void onBeforeConvert(BeforeConvertEvent<InstanceMapping> event) {
	    if (event.getSource().getId() < 1)
	        event.getSource().setId(sequenceGenerator.generateSequence(InstanceMapping.SEQUENCE_NAME));
	}
}
