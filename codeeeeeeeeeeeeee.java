
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
