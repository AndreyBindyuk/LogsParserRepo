import { Component, Input, Output, EventEmitter } from '@angular/core';
import { RestService } from './rest.service';

@Component({
  selector: 'file-tree',
  templateUrl: 'file-tree.component.html'
})
export class FileTreeComponent {
  @Input() tree;
  @Output() select = new EventEmitter();
  private _banks = [];

  constructor(private _restService: RestService) {
    this._banks = this._restService.getAvailableBanks();
  }

  onClick($event, item) {
    $event.stopPropagation();
    window.scrollTo(0, 0);
    this.select.emit(item);
  }

  onChildClick($event) {
    this.select.emit($event);
  }
}
