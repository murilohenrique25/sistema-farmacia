import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router } from '@angular/router';
import { AuthService } from './auth.service';

@Injectable({
    providedIn: 'root'
})
export class PermissaoGuard implements CanActivate {

    constructor(private authService: AuthService, private router: Router) { }

    canActivate(route: ActivatedRouteSnapshot): boolean {
        const permissaoNecessaria: string = route.data['permissao'];

        // ROLE_ADMIN passa em qualquer rota
        if (this.authService.getRole() === 'Administrador') return true;

        if (!permissaoNecessaria || this.authService.temPermissao(permissaoNecessaria)) {
            return true;
        }

        this.router.navigate(['/home']);
        return false;
    }
}
