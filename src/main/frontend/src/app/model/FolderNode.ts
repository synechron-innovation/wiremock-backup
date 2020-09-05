export interface FolderNode {
    name: string;
    isChecked: boolean;
    ancestorPath: string;
    recordingPath?: string;
    nodeType: FolderNodeTypes;
    children: FolderNode[];
}

export interface TreeAction {
    type: TreeActionTypes;
    node: FolderNode;
}

export enum TreeActionTypes {
    ADD = 'add', REMOVE = 'remove'
}

export enum FolderNodeTypes {
    FOLDER = 'folder', RECORDING = 'recording', TEMP = 'temp'
}

export interface FlatFolderNode {
    name: string;
    expandable: boolean;
    level: number;
    isExpanded?: boolean;
}
