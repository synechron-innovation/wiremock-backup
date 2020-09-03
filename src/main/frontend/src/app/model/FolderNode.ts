export interface FolderNode {
    name: string;
    recordingPath?: string;
    children: FolderNode[];
}

export interface FlatFolderNode {
    name: string;
    expandable: boolean;
    level: number;
    isExpanded?: boolean;
}
