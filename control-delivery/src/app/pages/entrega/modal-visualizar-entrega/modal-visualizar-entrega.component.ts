import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Entrega } from '../../../interfaces/entrega';


@Component({
  selector: 'app-modal-visualizar-entrega',
  templateUrl: './modal-visualizar-entrega.component.html',
  styleUrl: './modal-visualizar-entrega.component.scss'
})
export class ModalVisualizarEntregaComponent {

  selectedTab = 'Entrega';

  entregaData: Entrega;

  constructor(public dialogRef: MatDialogRef<ModalVisualizarEntregaComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any) {
    this.entregaData = data;
  }

  onTabChange(event: any) {
    this.selectedTab = event.tab.textLabel;
  }

  fechar() {
    this.dialogRef.close();
  }

}
