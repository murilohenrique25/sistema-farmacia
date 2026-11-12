import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'moeda'
})
export class MoedaPipe implements PipeTransform {

  transform(value: number): string {
    if (value !== null && value !== undefined) {
      return 'R$ ' + value.toFixed(2).replace('.', ',');
    }
    return 'R$ 0,00';  // Caso o valor seja nulo ou indefinido
  }

}
