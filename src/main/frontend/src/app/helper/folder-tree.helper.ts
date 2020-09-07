import { FolderNode } from './../model/FolderNode';

export class FolderTreeHelper {
    public static getParentByAncestorPath(folderNode: FolderNode, nestedArray: FolderNode[]): FolderNode {
        if (!folderNode.ancestorPath) {
            return null;
        }
        const ancestorNameArray = folderNode.ancestorPath.split('::');
        let nodeArray = nestedArray;
        let parentNode: FolderNode;
        ancestorNameArray.forEach((ancestorName) => {
            parentNode = nodeArray.find(node => node.name === ancestorName);
            if (parentNode) {
                nodeArray = parentNode.children;
            }
        });
        return parentNode;
    }

    static traverseFolderTree(nestedTree: FolderNode[]): void {
        const nodeArray = nestedTree;
        nodeArray.forEach(node => {
            if (node.children && node.children.length > 0) {
                this.traverseFolderTree(node.children);
            }
        });
    }
}
