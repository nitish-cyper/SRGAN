import { Component, ElementRef, HostListener, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, MatIconModule],
  template: `
    <header class="header">
      <div class="search-container">
        <input type="text" placeholder="Search..." class="search-bar" />
        <mat-icon class="search-icon">search</mat-icon>
      </div>
      <div class="icons">
        <mat-icon>notifications</mat-icon>
        <div class="profile-container" #profileMenu>
          <mat-icon (click)="toggleDropdown()">account_circle</mat-icon>
          <div *ngIf="dropdownOpen" class="dropdown-menu">
            <div class="dropdown-item" (click)="onLogout()">
              <mat-icon>logout</mat-icon>
              <span>Logout</span>
            </div>
          </div>
        </div>
      </div>
    </header>
  `,
  styles: [`
    .header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      height: 60px;
      padding: 0 20px;
      background: #c5cae9;
      border-bottom: 2px solid #e0e0e0;
      box-shadow: 0px 2px 4px rgba(0, 0, 0, 0.1);
      position: relative;
    }
    .search-container {
      flex: 1;
      display: flex;
      align-items: center;
      margin-left: 250px; /* Leave space for side nav */
      position: relative;
      max-width: 320px;
    }
    .search-bar {
      width: 100%;
      padding: 10px 40px 10px 15px; /* Space for icon */
      font-size: 14px;
      border: 1px solid #ccc;
      border-radius: 40px;
      outline: none;
      transition: all 0.3s ease-in-out;
    }
    .search-bar:focus {
      border-color: #6200ea;
      box-shadow: 0 0 5px rgba(98, 0, 234, 0.5);
    }
    .search-icon {
      position: absolute;
      right: 10px;
      color: #555;
      cursor: pointer;
      font-size: 22px;
    }
    .icons {
      display: flex;
      gap: 20px;
      align-items: center;
      cursor: pointer;
      position: relative;
    }
    .icons mat-icon {
      font-size: 26px;
      color: #555;
      transition: color 0.3s ease-in-out;
    }
    .icons mat-icon:hover {
      color: #6200ea;
    }
    .profile-container {
      position: relative;
    }
    .dropdown-menu {
      position: absolute;
      top: 100%;
      right: 0;
      background-color: white;
      border-radius: 8px;
      box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
      min-width: 200px;
      z-index: 1000;
      margin-top: 10px;
    }
    .dropdown-item {
      display: flex;
      align-items: center;
      padding: 10px 15px;
      cursor: pointer;
      transition: background-color 0.2s;
    }
    .dropdown-item:hover {
      background-color: #f0f0f0;
    }
    .dropdown-item mat-icon {
      margin-right: 10px;
      font-size: 20px;
    }
  `]
})
export class HeaderComponent {
  dropdownOpen = false;
  @ViewChild('profileMenu') profileMenu: ElementRef;

  constructor(private router: Router) {}

  toggleDropdown() {
    this.dropdownOpen = !this.dropdownOpen;
  }

  onLogout() {
    // Navigate to login page
    this.router.navigate(['/login']);
  }

  @HostListener('document:click', ['$event'])
  clickOutside(event: Event) {
    // Close dropdown if clicked outside
    if (this.profileMenu && 
        !this.profileMenu.nativeElement.contains(event.target)) {
      this.dropdownOpen = false;
    }
  }
}



import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { LayoutComponent } from './layout/layout.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { StockManagementComponent } from './stock-management/stock-management.component';
import { ShipmentTrackingComponent } from './shipment-tracking/shipment-tracking.component';
import { BlogsComponent } from './blogs/blogs.component';

export const routes: Routes = [
  { 
    path: 'login', 
    component: LoginComponent 
  },
  { 
    path: '', 
    component: LayoutComponent,
    children: [
      { path: 'dashboard', component: DashboardComponent },
      { path: 'stock-management', component: StockManagementComponent },
      { path: 'shipment-tracking', component: ShipmentTrackingComponent },
      { path: 'blogs', component: BlogsComponent },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  },
  { path: '**', redirectTo: 'login' }
];             

        import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { SideNavComponent } from '../side-nav/side-nav.component';
import { HeaderComponent } from '../header/header.component';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [
    CommonModule, 
    RouterOutlet, 
    SideNavComponent, 
    HeaderComponent
  ],
  template: `
    <div class="app-container">
      <app-side-nav></app-side-nav>
      <div class="main-content">
        <app-header></app-header>
        <div class="router-outlet-container">
          <router-outlet></router-outlet>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .app-container {
      display: flex;
      height: 100vh;
      overflow: hidden;
    }
    .main-content {
      flex-grow: 1;
      display: flex;
      flex-direction: column;
      overflow: hidden;
    }
    .router-outlet-container {
      flex-grow: 1;
      overflow-y: auto;
      padding: 20px;
      background-color: #f4f5f7;
    }
  `]
})
export class LayoutComponent {}



        import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { SideNavComponent } from '../side-nav/side-nav.component';
import { HeaderComponent } from '../header/header.component';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [
    CommonModule, 
    RouterOutlet, 
    SideNavComponent, 
    HeaderComponent
  ],
  template: `
    <div class="app-container">
      <app-side-nav class="sidebar"></app-side-nav>
      <div class="main-content">
        <app-header class="header"></app-header>
        <div class="content-area">
          <router-outlet></router-outlet>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .app-container {
      display: flex;
      height: 100vh;
      overflow: hidden;
    }
    .sidebar {
      width: 250px;
      flex-shrink: 0;
    }
    .main-content {
      display: flex;
      flex-direction: column;
      flex-grow: 1;
      overflow: hidden;
    }
    .header {
      height: 60px;
      flex-shrink: 0;
    }
    .content-area {
      flex-grow: 1;
      overflow-y: auto;
      padding: 20px;
      background-color: #f4f5f7;
    }
  `]
})
export class LayoutComponent {}
