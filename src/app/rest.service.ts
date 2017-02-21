import { Injectable } from '@angular/core';
import { Http, RequestOptions, Headers } from '@angular/http';
import 'rxjs/add/operator/map';

@Injectable()
export class RestService {
  private _url = 'http://10.119.5.148:8080/LogsParser/';

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

  getLogsByTrackingId(serverIp: String, bankPath: String, trackingId: String) {
    return this._http.get(this._url +
      'bankLogs?serverName=' + serverIp +
      '&bankPath=' + bankPath +
      '&trackingId=' + trackingId)
      .map(result => result.json());
  }

}
