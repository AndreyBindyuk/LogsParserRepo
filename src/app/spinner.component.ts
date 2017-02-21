import { Component, Input } from '@angular/core';

@Component({
  selector: 'spinner',
  template: '<i *ngIf="visible" class="fa fa-spinner fa-spin fa-{{size}}"></i>'
})
export class SpinnerComponent {
  @Input() visible = true;
  @Input() size = '3x';
}
