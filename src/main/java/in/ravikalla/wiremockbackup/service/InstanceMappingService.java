package in.ravikalla.wiremockbackup.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class InstanceMappingService {
	public List<String> getAllMappings() {
		List<String> lstMappings = new ArrayList<String>();
		lstMappings.add("test1");
		lstMappings.add("test2");
		lstMappings.add("test3");
		return lstMappings;
	}
}
