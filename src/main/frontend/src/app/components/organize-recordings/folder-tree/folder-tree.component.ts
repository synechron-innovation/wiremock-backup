import { Component, OnInit, Input, OnChanges, SimpleChanges, EventEmitter, Output } from '@angular/core';
import { ArrayDataSource } from '@angular/cdk/collections';
import { FlatTreeControl, NestedTreeControl } from '@angular/cdk/tree';

import { Recording } from 'src/app/model/Recording';
import { FolderNode, FlatFolderNode } from 'src/app/model/FolderNode';

@Component({
  selector: 'app-folder-tree',
  templateUrl: './folder-tree.component.html',
  styleUrls: ['./folder-tree.component.scss']
})
export class FolderTreeComponent implements OnInit, OnChanges {
  @Input() recordings: Recording[];
  @Output() selectRecording = new EventEmitter<string>();

  nestedTreeData: FolderNode[];
  // flatTreeData: FlatFolderNode[];

  // treeControl: FlatTreeControl<FlatFolderNode>;
  treeControl: NestedTreeControl<FolderNode>;

  dataSource: ArrayDataSource<any>;

  constructor() { }

  ngOnInit(): void {
    this.nestedTreeData = [];
    this.treeControl = new NestedTreeControl<FolderNode>((node: FolderNode) => node.children);
    // this.dataSource = new ArrayDataSource(this.flatTreeData);
  }

  ngOnChanges(changes: SimpleChanges): void {
    console.log('changes: ', changes);
    if (changes.recordings && changes.recordings.previousValue !== changes.recordings.currentValue) {
      this.generateNestedTree();
      console.log('nested tree: ', this.nestedTreeData);
      // this.generateTreeView();
      this.dataSource = new ArrayDataSource(this.nestedTreeData);
    }
  }

  generateNestedTree(): void {
    console.log('json structure: ', this.recordings);
    this.nestedTreeData = [];
    this.recordings.forEach((recording: Recording) => {
      const [folderMapping, recordingName] = recording.name.split(':::');
      let nodeArray = this.nestedTreeData;
      folderMapping.split('::').forEach(folder => {
        let folderNode = this.getMatchingNode(nodeArray, folder);
        if (!folderNode) {
          folderNode = {
            name: folder,
            children: []
          };
          nodeArray.push(folderNode);
        }
        nodeArray = folderNode.children;
      });

      nodeArray.push({
        name: recordingName,
        recordingPath: recording.name,
        children: null
      });
    });
  }

  getMatchingNode(nodeArray: FolderNode[], nodeName: string): FolderNode {
    return nodeArray.find((node: FolderNode) => node.name === nodeName);
  }

  hasChild = (_: number, node: FolderNode) => !!node.children && node.children.length > 0;

  onRecordingClick(node: FolderNode): void {
    this.selectRecording.emit(node.recordingPath);
  }

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
