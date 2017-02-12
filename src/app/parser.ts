import { Injectable } from '@angular/core';

declare var vkbeautify: any;
declare var Prism: any;

@Injectable()
export class Parser {

  prettyXml(messages: any[]) {
    let plainMessageList = [];
    messages.forEach(msgArray => {
      msgArray.forEach(message => {
        plainMessageList.push(message.trim());
      });
    });
    return plainMessageList.map(message => Prism.highlight(vkbeautify.xml(message, '   '), Prism.languages['markup']));
  }

  parseFileTree(filesPath: String[]) {
    let tree = {
      children: {}
    };
    let parsedList = filesPath.map(path => {
      return path.split('/');
    });
    this.buildFileTree(parsedList, tree);
    this.convertItem(tree);
    return tree.children;
  }

  private buildFileTree(pathList, tree) {
    pathList.forEach(parsedItem => {
      let path = '';
      parsedItem.reduce((parent, current, index) => {
        (index !== parsedItem.length - 1) ? (path = path + (current) + '\\') : (path = path + (current));
        ;
        parent.children[current] = parent.children[current] ||
          this.itemConstructor(path, current, index !== parsedItem.length - 1);
        return parent.children[current];
      }, tree);
    });
  }

  private itemConstructor(path, name, isDirectory) {
    let item: any = { path, name };
    if (isDirectory) {
      item.isExpanded = false;
      item.children = {};
    }
    return item;
  }

  private convertItem(item) {
    let childrenList = [];
    for (let child in item.children) {
      if (item.children[child].children) {
        this.convertItem(item.children[child]);
      }
      childrenList.push(item.children[child]);
    }
    item.children = childrenList;
  }
}
