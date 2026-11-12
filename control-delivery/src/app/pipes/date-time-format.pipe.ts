import { Injectable, Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'dateTimeFormat'
})

@Injectable({ // Permite injeção do pipe
  providedIn: 'root',
})
export class DateTimeFormatPipe implements PipeTransform {

  transform(value: Date | string | null): string {
    if (!value) return '';

    const date = typeof value === 'string' ? new Date(value) : value;

    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0'); // Janeiro é 0
    const year = date.getFullYear();

    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');

    return `${day}/${month}/${year} ${hours}:${minutes}:${seconds}`;
  }

}
