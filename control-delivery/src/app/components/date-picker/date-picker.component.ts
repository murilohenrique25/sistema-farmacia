import { Component, EventEmitter, Output, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-date-picker',
  templateUrl: './date-picker.component.html',
  styleUrls: ['./date-picker.component.scss'],
})
export class DatePickerComponent implements OnInit {
  @Output() dateRangeSelected = new EventEmitter<{ start: Date | null; end: Date | null }>();

  range = new FormGroup({
    start: new FormControl<Date | null>(null),
    end: new FormControl<Date | null>(null),
  });

  ngOnInit() {
    // Escuta mudanças no formulário para emitir os valores
    this.range.valueChanges.subscribe(() => {
      this.onDateChange();
    });
  }

  onDateChange() {
    const start = this.range.value.start ?? null;
    const end = this.range.value.end ?? null;

    this.dateRangeSelected.emit({ start, end });
  }
}
