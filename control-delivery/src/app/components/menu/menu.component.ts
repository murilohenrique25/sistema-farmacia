import { Component, HostBinding, HostListener, OnInit } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs/operators';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.scss'
})
export class MenuComponent implements OnInit {
  private submenuState: { [key: string]: boolean } = {};
  isMenuOpen = false;

  @HostBinding('class.menu-open') get menuOpen() {
    return this.isMenuOpen;
  }

  constructor(private route: Router, public authService: AuthService) { }

  ngOnInit() {
    this.route.events.pipe(
      filter(e => e instanceof NavigationEnd)
    ).subscribe(() => this.closeMenu());
  }

  toggleMenu() {
    this.isMenuOpen = !this.isMenuOpen;
  }

  closeMenu() {
    this.isMenuOpen = false;
  }

  @HostListener('window:resize', ['$event'])
  onResize(_event: Event) {
    if (window.innerWidth >= 768) {
      this.isMenuOpen = false;
    }
  }

  toggleSubmenu(menu: string) {
    this.submenuState[menu] = !this.submenuState[menu];
  }

  isSubmenuOpen(menu: string): boolean {
    return !!this.submenuState[menu];
  }

  isSuperAdmin(): boolean {
    return this.authService.getRole() === 'Administrador';
  }

  logout() {
    this.authService.logout();
    this.route.navigate(['login']);
  }
}
