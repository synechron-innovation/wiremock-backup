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
  @Output() editRecording = new EventEmitter<string>();
  @Output() editMappings = new EventEmitter<Set<string>>();
  @Output() treeAction = new EventEmitter<TreeAction>();

  nestedTreeData: FolderNode[];
  nestedTreeDataSubject = new BehaviorSubject<FolderNode[]>([]);
  expandedNodeSet: Set<string>;
  selectedRecordingPaths: Set<string>;

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
    this.selectedRecordingPaths = new Set();

    if (changes.recordings && changes.recordings.previousValue !== changes.recordings.currentValue) {
      this.generateNestedTree();
      this.nestedTreeDataSubject.next(this.nestedTreeData);
    }
  }

  ngAfterViewInit(): void {
    this.treeControl.dataNodes = this.nestedTreeData;
  }

  generateNestedTree(): void {
    this.nestedTreeData = [];
    this.recordings.forEach((recording: Recording) => {
      const mappingArray = recording.name.split(':::');
      const folderMapping = mappingArray[0];
      let recordingName = mappingArray[1];

      let nodeArray = this.nestedTreeData;
      let ancestorPath = null;

      if (recordingName) {
        folderMapping.split('::').forEach(folder => {
          let folderNode = this.getMatchingNode(nodeArray, folder);
          if (!folderNode) {
            folderNode = {
              id: `${(Math.random() * 10000).toFixed(0)}_${folder}`,
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
      } else {
        recordingName = folderMapping;
      }

      nodeArray.push({
        id: `${(Math.random() * 10000).toFixed(0)}_${recordingName}`,
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
    this.editRecording.emit(node.recordingPath);
  }

  deleteNode(node: FolderNode): void {
    let nodeArray: FolderNode[];
    if (node.ancestorPath) {
      nodeArray = FolderTreeHelper.getParentByAncestorPath(node, this.nestedTreeData).children;
    } else {
      nodeArray = this.nestedTreeData;
    }

    if (!node.children) { // leaf nodes
      const nodeIndex = nodeArray.findIndex(childNode => childNode.name === node.name);
      if (nodeIndex !== -1) {
        nodeArray.splice(nodeIndex, 1);
      }

      this.refreshFolderTree();

      // emit event to update the recording array
      this.treeAction.emit({
        type: TreeActionTypes.REMOVE,
        node
      });
    } else {

      this.updateDescendants(node.children, node.ancestorPath);

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
        const updatedRecordingPath = (node.ancestorPath) ? node.ancestorPath + ':::' + node.name : node.name;
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
      id: `${(Math.random() * 10000).toFixed(0)}_`,
      name: '',
      isChecked: false,
      ancestorPath: (!!parent.ancestorPath) ? parent.ancestorPath + '::' + parent.name : parent.name,
      nodeType: tempNodeType,
      children: (tempNodeType === FolderNodeTypes.TEMP_FOLDER) ? [] : null,
      recordingPath: null
    };

    parent.children.unshift(tempNode);

    this.refreshFolderTree();
  }

  addTempCloneNode(node: FolderNode): void {
    const parent = FolderTreeHelper.getParentByAncestorPath(node, this.nestedTreeData);
    const nodeToBeClonedIndex = parent.children.findIndex(child => child.id === node.id);
    parent.children.splice(nodeToBeClonedIndex + 1, 0, { ...node, ...{ name: '', nodeType: FolderNodeTypes.TEMP_RECORDING } });
    this.refreshFolderTree();
  }

  saveTempNode(node: FolderNode, nodeType: FolderNodeTypes): void {
    node.id += node.name;
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

  onRecordingSelection(node: FolderNode): void {
    if (node.isChecked) {
      this.selectedRecordingPaths.add(node.recordingPath);
    } else {
      if (this.selectedRecordingPaths.has(node.recordingPath)) {
        this.selectedRecordingPaths.delete(node.recordingPath);
      }
    }
  }

  editSelectedMappings(): void {
    if (this.selectedRecordingPaths.size === 0) {
      this.showSnackbarMessage('Please select at least one recording to proceed');
    } else {
      this.editMappings.emit(this.selectedRecordingPaths);
    }
  }

  clearSelection(): void {
    this.selectedRecordingPaths.clear();
    this.deselectRecordingNodes(this.nestedTreeData);
  }

  private deselectRecordingNodes(nodeArray: FolderNode[]): void {
    nodeArray.forEach(node => {
      if (node.nodeType === FolderNodeTypes.RECORDING) {
        node.isChecked = false;
      } else if (node.children) {
        this.deselectRecordingNodes(node.children);
      }
    });
  }

  showSnackbarMessage(message: string): void {
    this.snackbar.open(message, 'Close', {
      duration: 2500
    });
  }

  refreshFolderTree(): void {
    this.nestedTreeData = JSON.parse(JSON.stringify(this.nestedTreeData));

    this.expandedNodeSet = new Set();
    this.rememberExpandedNodes(this.treeControl.dataNodes, this.expandedNodeSet);

    this.nestedTreeDataSubject.next(this.nestedTreeData);
    this.treeControl.dataNodes = this.nestedTreeData;

    this.expandNodeById(this.treeControl.dataNodes, this.expandedNodeSet);
  }

  rememberExpandedNodes(nodeArray: FolderNode[], expandedNodeSet: Set<string>): void {
    if (nodeArray && nodeArray.length) {
      nodeArray.forEach(node => {
        if (node.children && node.children.length && this.treeControl.isExpanded(node)) {
          expandedNodeSet.add(node.id);
          this.rememberExpandedNodes(node.children, expandedNodeSet);
        }
      });
    }
  }

  expandNodeById(nodeArray: FolderNode[], expandedNodeSet: Set<string>): void {
    if (nodeArray && nodeArray.length) {
      nodeArray.forEach(node => {
        if (node.children && node.children.length && expandedNodeSet.has(node.id)) {
          this.treeControl.expand(node);
          this.expandNodeById(node.children, expandedNodeSet);
        }
      });
    }
  }
}
