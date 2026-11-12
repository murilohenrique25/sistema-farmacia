import { Component, Inject } from '@angular/core';
import { Usuario } from '../../../interfaces/usuario';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-modal-view-user',
  templateUrl: './modal-view-user.component.html',
  styleUrl: './modal-view-user.component.scss'
})
export class ModalViewUserComponent {

  userData: Usuario;

  constructor(public dialogRef: MatDialogRef<ModalViewUserComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    this.userData = data;
  }

  closeModal() { this.dialogRef.close(); }

}
