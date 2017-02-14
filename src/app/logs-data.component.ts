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
  currentTrackingId: String;
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

  selectPath(event) {
    this.currentPath = event.path;
    if (document.getElementById('trackingId')) {
      document.getElementById('trackingId').focus();
    }
  }

  getLoggedMessagesList(form) {
    this.logResultList = [];
    this.isLoading = true;
    this.currentTrackingId = form.trackingId;
    if (this.currentPath.includes('.')) {
      this._restService.getLogsFromFileByTrackingId(this.currentServers, this.currentPath, this.currentTrackingId)
        .subscribe(result => {
          this.logResultList = this.parseResult(result);
          this.isLoading = false;
          console.log('1 Log Result List', this.logResultList);
        });
    } else {
      this._restService.getLogsForBankByTrackingId(this.currentServers, this.currentPath, this.currentTrackingId)
        .subscribe(result => {
          console.log('2 Log Result List', result);
          this.logResultList = this.parseResult(result);
          this.isLoading = false;
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
