import { RecordingRequest } from './RecordingRequest';
import { RecordingResponse } from './RecordingResponse';

export class Recording {
  id: string = null;
  uuid: string = null;
  name: string = null;
  request: RecordingRequest;
  response: RecordingResponse;
  persistent: boolean = null;
  priority: any = null;
  scenarioName: any = null;
  requiredScenarioState: any = null;
  newScenarioState: any = null;
  postServeActions: any = null;
  metadata: any = null;
}
