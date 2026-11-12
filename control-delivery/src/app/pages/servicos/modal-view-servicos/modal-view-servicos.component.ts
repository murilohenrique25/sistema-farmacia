import { Component, Inject } from '@angular/core';
import { Servico } from '../../../interfaces/servico';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-modal-view-servicos',
  templateUrl: './modal-view-servicos.component.html',
  styleUrl: './modal-view-servicos.component.scss'
})
export class ModalViewServicosComponent {
  servicoData: Servico;

  constructor(public dialogRef: MatDialogRef<ModalViewServicosComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    this.servicoData = data;
  }

  closeModal() {
    this.dialogRef.close();
  }
}
