import { Component, Inject } from '@angular/core';
import { Entregador } from '../../../interfaces/entregador';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-modal-view-entregador',
  templateUrl: './modal-view-entregador.component.html',
  styleUrl: './modal-view-entregador.component.scss'
})
export class ModalViewEntregadorComponent {

  entregadorData: Entregador;

  constructor(public dialogRef: MatDialogRef<ModalViewEntregadorComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    this.entregadorData = data;
  }

  closeModal() {
    this.dialogRef.close();
  }

}
