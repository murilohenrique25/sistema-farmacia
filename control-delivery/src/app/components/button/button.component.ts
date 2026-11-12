import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-button',
  templateUrl: './button.component.html',
  styleUrls: ['./button.component.scss']
})
export class ButtonComponent {

  @Input() textButton: string = '';
  @Input() size: 'sm' | 'md' | 'lg' | 'full' = 'md';
  @Input() disabled: boolean = false;

  @Output() click = new EventEmitter<void>();

  onClick() {
    if (!this.disabled) {
      this.click.emit();
    }
  }
}
