import { Injectable } from '@angular/core';
import { Http, RequestOptions, Headers } from '@angular/http';
import 'rxjs/add/operator/map';

@Injectable()
export class RestService {
  private _url = 'http://epkzkarw0410:8883/';

  constructor(private _http: Http) {
  }

  getAvailableServers() {
    return this._http.get(this._url + 'serverNames').map(result => result.json());
  }

  getAvailableBanks() {
    return ['BBY', 'NBF', 'ENBD', 'WARBA'];
  }

  getAvailableFiles(serverIp: String) {
    return this._http.get(this._url + 'directories?serverName=' + serverIp).map(result => result.json());
  }

  getLogsForBankByFlowId(serverIp: String, bankPath: String, flowId: String) {
    return this._http.get(this._url +
      'nieaiLogsByFolder?serverName=' + serverIp +
      '&serverPath=' + bankPath +
      '&trackindId=' + flowId)
      .map(result => result.json());
  }

  getLogsFromFileByFlowId(serverIp: String, filePath: String, flowId: String) {
    let body = {
      serverName: serverIp,
      logPath: filePath,
      trackingId: flowId
    };
    let headers = new Headers({ 'Content-Type': 'application/json' });
    return this._http.post(this._url + 'nieaiLogs', JSON.stringify(body), new RequestOptions({ headers: headers })).map(result => result.json());
  }

}
