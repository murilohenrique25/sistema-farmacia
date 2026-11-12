import { Component, Inject } from '@angular/core';
import { Entregador } from '../../../interfaces/entregador';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { EntregadorService } from '../../../services/entregador.service';
import { EntregaService } from '../../../services/entrega.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-modal-alterar-entregador',
  templateUrl: './modal-alterar-entregador.component.html',
  styleUrl: './modal-alterar-entregador.component.scss'
})
export class ModalAlterarEntregadorComponent {
 listEntregador: Entregador[] = [];
  idEntregador: number;

  constructor(
    public dialogRef: MatDialogRef<ModalAlterarEntregadorComponent>,
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

  alterarEntrega() {
    if (this.idEntregador == null) {
      this.snackBar.open('Necessário selecionar o entregador', 'Fechar', { duration: 3000 });
      return;
    }
    this.entregaService.alterarEntrega(this.data.idEntrega, this.idEntregador).subscribe({
      next: () => {
        this.closeModal();
      }, error: (erro) => {
        console.error(erro);
      }
    });
  }
}
