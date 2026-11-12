import { Component, Inject } from '@angular/core';
import { EntregadorService } from '../../../services/entregador.service';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Entregador } from '../../../interfaces/entregador';
import { MatSnackBar } from '@angular/material/snack-bar';
import { EntregaService } from '../../../services/entrega.service';

@Component({
  selector: 'app-modal-atribuir-entregador',
  templateUrl: './modal-atribuir-entregador.component.html',
  styleUrl: './modal-atribuir-entregador.component.scss'
})
export class ModalAtribuirEntregadorComponent {

  listEntregador: Entregador[] = [];
  idEntregador: number;

  constructor(
    public dialogRef: MatDialogRef<ModalAtribuirEntregadorComponent>,
    private entregadorService: EntregadorService,
    private entregaService: EntregaService,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private snackBar: MatSnackBar
  ) {
  }

  ngOnInit() {
    this.entregadorService.carregarEntregadores().subscribe({
      next: (res: any) => {
        this.listEntregador = res.body;
      },
      error: (error) => {
        console.error(error);
      }
    });
  }


  closeModal() { this.dialogRef.close(); }

  atribuirEntrega() {
    if (this.idEntregador == null) {
      this.snackBar.open('Necessário selecionar o entregador', 'Fechar', { duration: 3000 });
      return;
    }
    this.entregaService.atribuirEntrega(this.data.idEntrega, { "id": this.idEntregador }).subscribe({
      next: () => {
        this.closeModal();
      }, error: (erro) => {
        console.error(erro);
      }
    });
  }
}
