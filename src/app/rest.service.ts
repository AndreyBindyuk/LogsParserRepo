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

  getLogsForBankByTrackingId(serverIp: String, bankPath: String, trackingId: String) {
    return this._http.get(this._url +
      'bankLogs?serverName=' + serverIp +
      '&bankPath=' + bankPath +
      '&trackingId=' + trackingId)
      .map(result => result.json());
  }

  getLogsFromFileByTrackingId(serverIp: String, filePath: String, trackingId: String) {
    return this._http.get(this._url +
      'bankComponentLogs?serverName=' + serverIp +
      '&filePath=' + filePath +
      '&trackingId=' + trackingId)
      .map(result => result.json());
  }

}
