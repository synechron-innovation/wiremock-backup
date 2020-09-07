import { Recording } from './Recording';

export class Instance {
    id: number;
    instanceName: string;
    wiremockURL: string;
    targetURL: string;
    mappings: Recording[];
}
