import { Component, OnInit, Input, OnChanges, SimpleChanges, EventEmitter, Output, AfterViewInit } from '@angular/core';
import { ArrayDataSource } from '@angular/cdk/collections';
import { FlatTreeControl, NestedTreeControl } from '@angular/cdk/tree';
import { MatSnackBar } from '@angular/material/snack-bar';

import { Recording } from 'src/app/model/Recording';
import { FolderNode, FlatFolderNode, FolderNodeTypes, TreeActionTypes } from 'src/app/model/FolderNode';
import { BehaviorSubject } from 'rxjs';
import { TreeAction } from './../../../model/FolderNode';
import { FolderTreeHelper } from './../../../helper/folder-tree.helper';

@Component({
  selector: 'app-folder-tree',
  templateUrl: './folder-tree.component.html',
  styleUrls: ['./folder-tree.component.scss']
})
export class FolderTreeComponent implements OnInit, OnChanges, AfterViewInit {
  @Input() recordings: Recording[];
  @Output() selectRecording = new EventEmitter<string>();
  @Output() treeAction = new EventEmitter<TreeAction>();

  nestedTreeData: FolderNode[];
  nestedTreeDataSubject = new BehaviorSubject<FolderNode[]>([]);
  checkedFolderNode: FolderNode;

  folderNodeTypes = FolderNodeTypes;
  // flatTreeData: FlatFolderNode[];

  // treeControl: FlatTreeControl<FlatFolderNode>;
  treeControl: NestedTreeControl<FolderNode>;

  dataSource: ArrayDataSource<any>;

  constructor(
    private snackbar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.treeControl = new NestedTreeControl<FolderNode>((node: FolderNode) => node.children);
    this.dataSource = new ArrayDataSource(this.nestedTreeDataSubject.asObservable());
  }

  ngOnChanges(changes: SimpleChanges): void {
    console.log('changes: ', changes);
    if (changes.recordings && changes.recordings.previousValue !== changes.recordings.currentValue) {
      this.generateNestedTree();
      console.log('nested tree: ', this.nestedTreeData);
      // this.generateTreeView();
      // this.dataSource = new ArrayDataSource(this.nestedTreeData);
      this.nestedTreeDataSubject.next(this.nestedTreeData);
    }
  }

  ngAfterViewInit(): void {
    this.treeControl.dataNodes = this.nestedTreeData;
    setTimeout(() => this.treeControl.expandAll(), 0);
  }

  generateNestedTree(): void {
    console.log('json structure: ', this.recordings);
    this.nestedTreeData = [];
    this.recordings.forEach((recording: Recording) => {
      const [folderMapping, recordingName] = recording.name.split(':::');
      let nodeArray = this.nestedTreeData;
      let ancestorPath = null;

      folderMapping.split('::').forEach(folder => {
        let folderNode = this.getMatchingNode(nodeArray, folder);
        if (!folderNode) {
          folderNode = {
            name: folder,
            isChecked: false,
            ancestorPath,
            nodeType: FolderNodeTypes.FOLDER,
            children: []
          };
          nodeArray.push(folderNode);
        }
        nodeArray = folderNode.children;
        ancestorPath = (!ancestorPath) ? folderNode.name : ancestorPath + '::' + folderNode.name;
      });

      nodeArray.push({
        name: recordingName,
        isChecked: false,
        ancestorPath,
        recordingPath: recording.name,
        nodeType: FolderNodeTypes.RECORDING,
        children: null
      });
    });
  }

  getMatchingNode(nodeArray: FolderNode[], nodeName: string): FolderNode {
    return nodeArray.find((node: FolderNode) => node.name === nodeName);
  }

  hasChild = (_: number, node: FolderNode) => !!node.children;

  onRecordingClick(node: FolderNode): void {
    this.selectRecording.emit(node.recordingPath);
  }

  deleteNode(node: FolderNode): void {
    const parent = FolderTreeHelper.getParentByAncestorPath(node, this.nestedTreeData);

    if (!node.children) { // leaf nodes
      const nodeIndex = parent.children.findIndex(childNode => childNode.name === node.name);
      if (nodeIndex !== -1) {
        parent.children.splice(nodeIndex, 1);
      }

      this.refreshFolderTree();

      // emit event to update the recording array
      this.treeAction.emit({
        type: TreeActionTypes.REMOVE,
        node
      });
    } else {

      // update ancestor path recursively for each child node
      // determine which array to be updated - parent.children or the root tree array
      let nodeArray: FolderNode[];
      if (parent) {
        this.updateDescendants(node.children, node.ancestorPath);
        nodeArray = parent.children;
      } else {
        this.updateDescendants(node.children, null);
        nodeArray = this.nestedTreeData;
      }

      // delete the node from the parent children and replace it with the children of the node
      const nodeToBeRemovedIndex = nodeArray.findIndex(child => child.name === node.name);
      if (nodeToBeRemovedIndex !== -1) {
        nodeArray.splice(nodeToBeRemovedIndex, 1, ...node.children);
      }

      this.refreshFolderTree();
    }
  }

  updateDescendants(nestedTree: FolderNode[], ancestorPath: string): void {
    const nodeArray = nestedTree;
    nodeArray.forEach(node => {
      node.ancestorPath = ancestorPath;
      const updatedAncestorPath = (!ancestorPath) ? node.name : ancestorPath + '::' + node.name;

      if (node.children && node.children.length > 0) {
        this.updateDescendants(node.children, updatedAncestorPath);
      } else if (!node.children) {
        // leaf node
        const updatedRecordingPath = node.ancestorPath + ':::' + node.name;
        this.treeAction.emit({
          node,
          type: TreeActionTypes.UPDATE,
          updatedRecordingPath
        });
        node.recordingPath = updatedRecordingPath;
      }

    });
  }

  addTempNode(node: FolderNode, tempNodeType: FolderNodeTypes): void {
    const parent = node;
    const tempNode: FolderNode = {
      name: '',
      isChecked: false,
      ancestorPath: (!!parent.ancestorPath) ? parent.ancestorPath + '::' + parent.name : parent.name,
      nodeType: tempNodeType,
      children: (tempNodeType === FolderNodeTypes.TEMP_FOLDER) ? [] : null,
      recordingPath: null
    };

    parent.children.push(tempNode);

    this.refreshFolderTree();
  }

  addTempCloneNode(node: FolderNode): void {
    const parent = FolderTreeHelper.getParentByAncestorPath(node, this.nestedTreeData);
    parent.children.push({ ...node, ...{ name: '', nodeType: FolderNodeTypes.TEMP_RECORDING } });
    this.refreshFolderTree();
  }

  saveTempNode(node: FolderNode, nodeType: FolderNodeTypes): void {
    node.nodeType = nodeType;
    if (nodeType === FolderNodeTypes.RECORDING) {
      if (!!node.recordingPath) { // clone action
        const updatedRecordingPath = node.ancestorPath + ':::' + node.name;
        this.treeAction.emit({
          type: TreeActionTypes.CLONE,
          node,
          updatedRecordingPath
        });
        node.recordingPath = updatedRecordingPath;
      } else {
        node.recordingPath = node.ancestorPath + ':::' + node.name;
        this.treeAction.emit({
          type: TreeActionTypes.ADD,
          node
        });
      }
    }
    this.refreshFolderTree();
  }

  showSnackbarMessage(message: string): void {
    this.snackbar.open(message, 'Close', {
      duration: 2500
    });
  }

  refreshFolderTree(): void {
    this.nestedTreeData = JSON.parse(JSON.stringify(this.nestedTreeData));
    this.nestedTreeDataSubject.next(this.nestedTreeData);
    this.treeControl.dataNodes = this.nestedTreeData;
    // expanding all nodes to prevent the tree from completely collapsing
    setTimeout(() => this.treeControl.expandAll(), 0);
  }

  // FOR REFERENCE
  // addChildToRootNode(): void {
  //   console.log('this.nestedTreeData: ', this.nestedTreeData);
  //   this.nestedTreeData.push({
  //     name: 'newTreeFolder1',
  //     children: []
  //   });
  //   this.nestedTreeDataSubject.next(this.nestedTreeData);
  // }

  // deleteNewlyAddedNode(): void {
  //   const foundNodeIndex = this.nestedTreeData.findIndex(node => node.name === 'newTreeFolder1');
  //   this.nestedTreeData.splice(foundNodeIndex, 1);
  //   this.nestedTreeDataSubject.next(this.nestedTreeData);
  // }

  // deleteNode(): void {
  //   if (this.checkedFolderNode) {
  //     const parent = FolderTreeHelper.getParentByAncestorPath(this.checkedFolderNode, this.nestedTreeData);
  //     if (!this.checkedFolderNode.children) { // leaf nodes
  //       const nodeIndex = parent.children.findIndex(childNode => childNode.name === this.checkedFolderNode.name);
  //       if (nodeIndex !== -1) {
  //         parent.children.splice(nodeIndex, 1);
  //       }

  //       this.refreshFolderTree();

  //       // emit event to update the recording array
  //       this.treeAction.emit({
  //         type: TreeActionTypes.REMOVE,
  //         node: this.checkedFolderNode
  //       });
  //       this.checkedFolderNode = null;
  //     }
  //   } else {
  //     this.snackbar.open('Failed: Item was not selected.', 'Close', {
  //       duration: 2500
  //     });
  //   }
  // }

  // onNodeCheck(node: FolderNode): void {
  //   // first time any node is checked
  //   if (!this.checkedFolderNode) {
  //     node.isChecked = true;
  //     this.checkedFolderNode = node;
  //   } else if (this.checkedFolderNode === node) {
  //     node.isChecked = false;
  //     this.checkedFolderNode = null;
  //   } else {
  //     this.checkedFolderNode.isChecked = false;
  //     node.isChecked = true;
  //     this.checkedFolderNode = node;
  //   }
  // }

  // addTempNode(tempNodeType: FolderNodeTypes): void {
  //   if (this.checkedFolderNode) {
  //     let parent: FolderNode;
  //     if (!this.checkedFolderNode.children) {
  //       parent = FolderTreeHelper.getParentByAncestorPath(this.checkedFolderNode, this.nestedTreeData);
  //     } else {
  //       parent = this.checkedFolderNode;
  //     }
  //     const tempNode: FolderNode = {
  //       name: '',
  //       isChecked: false,
  //       ancestorPath: parent.ancestorPath + '::' + parent.name,
  //       nodeType: tempNodeType,
  //       children: (tempNodeType === FolderNodeTypes.TEMP_FOLDER) ? [] : null
  //     };
  //     parent.children.push(tempNode);

  //     this.checkedFolderNode.isChecked = false;
  //     this.checkedFolderNode = null;

  //     this.refreshFolderTree();
  //   } else {
  //     this.showSnackbarMessage('Please select a node before adding a new folder.');
  //   }
  // }

  // generateTreeView(): void {
  //   console.log('json structure: ', this.recordings);
  //   this.flatTreeData = [];
  //   this.recordings.forEach((recording: Recording) => {
  //     const [folderMapping, recordingName] = recording.name.split(':::');
  //     let index = 0;
  //     folderMapping.split('::').forEach(folder => {

  //       if (!this.flatTreeData.find((node: FlatFolderNode) => node.name === folder && node.level === index)) {
  //         this.flatTreeData.push({
  //           name: folder,
  //           expandable: true,
  //           level: index
  //         });
  //       }
  //       index++;
  //     });
  //     this.flatTreeData.push({
  //       name: recordingName,
  //       expandable: false,
  //       level: index
  //     });

  //   });
  //   console.log('flatTreeData', this.flatTreeData);
  // }

  // hasChild = (_: number, node: FlatFolderNode) => node.expandable;

  // getParentNode(node: FlatFolderNode): FlatFolderNode {
  //   const nodeIndex = this.flatTreeData.indexOf(node);

  //   for (let i = nodeIndex - 1; i >= 0; i--) {
  //     if (this.flatTreeData[i].level === node.level - 1) {
  //       return this.flatTreeData[i];
  //     }
  //   }

  //   return null;
  // }

  // shouldRender(node: FlatFolderNode): boolean {
  //   const parent = this.getParentNode(node);
  //   return !parent || parent.isExpanded;
  // }

}
