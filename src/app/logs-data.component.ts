import { Component } from '@angular/core';
import { RestService } from './rest.service';
import { Parser } from './parser';

export class LogResult {
  fileName: String;
  messages: String[];
  messageType: String;

  constructor(fileName: String, messages: String[], messageType: String) {
    this.fileName = fileName;
    this.messages = messages;
    this.messageType = messageType;
  }
}

@Component({
  selector: 'logs-data',
  templateUrl: 'logs-data.component.html',
})

export class LogsDataComponent {
  isLoading = false;
  servers = [];
  filesPath = [];
  fileTree;
  currentServers: String;
  currentPath: String;
  currentFlowId: String;
  messageType = 'All';
  logResultList: LogResult[] = [];

  constructor(private _restService: RestService, private _parser: Parser) {
    this._restService.getAvailableServers()
      .subscribe(result => this.servers = result);
  }

  selectServer(serverIp: String) {
    this.filesPath = [];
    if (serverIp) {
      this._restService.getAvailableFiles(serverIp)
        .subscribe(result => {
          this.currentServers = serverIp;
          this.filesPath = result;
          this.fileTree = this._parser.parseFileTree(this.filesPath);
        });
    }
  }

  selectLogFile(file) {
    this.currentPath = file.path;
  }

  getLoggedMessagesList(form) {
    this.logResultList = [];
    this.isLoading = true;
    this.currentFlowId = form.flowId;
    if (this.currentPath.includes('.')) {
      this._restService.getLogsFromFileByFlowId(this.currentServers, this.currentPath, this.currentFlowId)
        .subscribe(result => {
          console.log('result1', result);
          this.logResultList = this._parser.prettyXml(result);
          this.isLoading = false;
        });
    } else {
      this._restService.getLogsForBankByFlowId(this.currentServers, this.currentPath, this.currentFlowId)
        .subscribe(result => {
          this.logResultList = this.parseResult(result);
          this.isLoading = false;
          console.log('Log Result List', this.logResultList);
        });
    }
  }

  private parseResult(result) {
    let resultList = [];
    for (let filePath in result) {
      let fileName = filePath.split('\\').pop();
      console.log(result[filePath]);
      let messages = this._parser.prettyXml(result[filePath]);
      resultList.push(new LogResult(fileName, messages, this.messageType));
    }
    return resultList;
  }
}
