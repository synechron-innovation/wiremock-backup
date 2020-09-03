
export interface Recording {
  id: string;
  uuid: string;
  name: string;
  request: {
    method: string;
    url: string;
    urlPath: string;
    urlPathPattern: string;
    urlPattern: string;
    queryParameters: any;
    headers: any;
    basicAuthCredentials: any;
    cookies: any;
    bodyPatterns: any;
  };
  response: {
    status: number;
    statusMessage: string;
    headers: any;
    additionalProxyRequestHeaders: any;
    body: string;
    base64Body: any;
    jsonBody: any;
    bodyFileName: any;
    fault: any;
    fixedDelayMilliseconds: number;
    fromConfiguredStub: any;
    proxyBaseUrl: string;
    transformerParameters: any;
    transformers: any;
  };
  persistent: boolean;
  priority: any;
  scenarioName: any;
  requiredScenarioState: any;
  newScenarioState: any;
  postServeActions: any;
  metadata: any;
}
