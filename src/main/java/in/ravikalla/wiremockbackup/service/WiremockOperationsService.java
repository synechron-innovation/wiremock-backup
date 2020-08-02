package in.ravikalla.wiremockbackup.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.StubMappingsApi;
import io.swagger.client.model.InlineResponse200;

@Service
public class WiremockOperationsService {
	private static final Logger L = LogManager.getLogger(WiremockOperationsService.class);

	public void importWiremock(String host, String port) {
		L.debug("Start : WiremockOperationsService.importWiremock(...) : Host = {}, Port = {}", host, port);

		ApiClient apiClient = new ApiClient();
		apiClient.setBasePath("http://" + host + ":" + port);
		StubMappingsApi apiInstance = new StubMappingsApi(apiClient);
		Integer limit = 56; // Integer | The maximum number of results to return
		Integer offset = 56; // Integer | The start index of the results to return
		try {
		    InlineResponse200 result = apiInstance.adminMappingsGet(limit, offset);
		    L.debug("23 : WiremockOperationsService.importWiremock(...) : result = {}", result);
		} catch (ApiException e) {
			L.error("25 : WiremockOperationsService.importWiremock(...) : Exception when calling StubMappingsApi#adminMappingsGet : ApiException e.Code = {}, e.Body = {}", e.getCode(), e.getResponseBody());
		}

		L.debug("End : WiremockOperationsService.importWiremock(...) : Host = {}, Port = {}", host, port);
	}
}
