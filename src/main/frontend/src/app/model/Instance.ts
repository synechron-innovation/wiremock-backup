import { Recording } from './Recording';

export interface Instance {
    id: number;
    instanceName: string;
    wiremockURL: string;
    targetURL: string;
    mappings: Recording[];
}
