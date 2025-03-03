

// File: main.ts
import { bootstrapApplication } from '@angular/platform-browser';
import { provideAnimations } from '@angular/platform-browser/animations';
import { AppComponent } from './app/app.component';

bootstrapApplication(AppComponent, {
  providers: [
    provideAnimations()
  ]
}).catch(err => console.error(err));

// File: app/app.component.ts
import { Component, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatSidenavModule, MatSidenav } from '@angular/material/sidenav';

import { HeaderComponent } from './components/header/header.component';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { MainContentComponent } from './components/main-content/main-content.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    MatSidenavModule,
    HeaderComponent,
    SidebarComponent,
    MainContentComponent
  ],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  @ViewChild('sidenav') sidenav!: MatSidenav;
  isSidenavExpanded = true;

  toggleSidenav() {
    this.isSidenavExpanded = !this.isSidenavExpanded;
    // We're not actually toggling the sidenav open/close state
    // because we want it to stay open but just resize
  }
}
// File: app/app.component.html
<div class="app-container">
  <mat-sidenav-container class="sidenav-container">
    <mat-sidenav #sidenav [mode]="'side'" [opened]="true" class="sidenav" [ngClass]="{'sidenav-expanded': isSidenavExpanded, 'sidenav-collapsed': !isSidenavExpanded}">
      <app-sidebar [isExpanded]="isSidenavExpanded"></app-sidebar>
    </mat-sidenav>
    <mat-sidenav-content class="sidenav-content">
      <app-header (toggleSidenavEvent)="toggleSidenav()"></app-header>
      <app-main-content></app-main-content>
    </mat-sidenav-content>
  </mat-sidenav-container>
</div>

// File: app/app.component.scss
.app-container {
  display: flex;
  flex-direction: column;
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  overflow: hidden;
}

.sidenav-container {
  flex: 1;
  overflow: hidden;
}

.sidenav {
  transition: width 0.3s ease;
  background-color: #1a4b8e;
  overflow: hidden;
}

.sidenav-expanded {
  width: 240px;
}

.sidenav-collapsed {
  width: 64px;
}

.sidenav-content {
  display: flex;
  flex-direction: column;
  height: 100%;
  overflow: hidden;
}

// File: app/components/header/header.component.ts
import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    CommonModule,
    MatToolbarModule,
    MatIconModule,
    MatButtonModule
  ],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {
  @Output() toggleSidenavEvent = new EventEmitter<void>();

  toggleSidenav() {
    this.toggleSidenavEvent.emit();
  }
}

// File: app/components/header/header.component.html
<mat-toolbar class="header">
  <button mat-icon-button (click)="toggleSidenav()">
    <mat-icon>menu</mat-icon>
  </button>
  <div class="search-container">
    <input type="text" placeholder="Search..." class="search-input">
    <button mat-icon-button class="search-button">
      <mat-icon>search</mat-icon>
    </button>
  </div>
  <span class="spacer"></span>
  <button mat-icon-button>
    <mat-icon>notifications</mat-icon>
  </button>
  <button mat-icon-button class="user-avatar">
    JD
  </button>
</mat-toolbar>

// File: app/components/header/header.component.scss
.header {
  background-color: white;
  color: #333;
  border-bottom: 1px solid #e0e0e0;
  padding: 0 16px;
  height: 64px;
  display: flex;
  align-items: center;
}

.search-container {
  display: flex;
  align-items: center;
  background-color: #f5f5f5;
  border-radius: 24px;
  padding: 0 8px;
  margin-left: 16px;
  width: 400px;
  height: 40px;
}

.search-input {
  border: none;
  background: transparent;
  outline: none;
  padding: 8px 12px;
  width: 100%;
  font-size: 14px;
}

.search-button {
  color: #666;
}

.spacer {
  flex: 1 1 auto;
}

.user-avatar {
  background-color: #e0e0ff;
  color: #1a4b8e;
  border-radius: 50%;
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  margin-left: 12px;
}

// File: app/components/sidebar/sidebar.component.ts
import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';

interface SidebarItem {
  icon: string;
  text: string;
  route: string;
  isActive?: boolean;
}

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [
    CommonModule,
    MatIconModule,
    MatTooltipModule
  ],
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent {
  @Input() isExpanded = true;

  sidebarItems: SidebarItem[] = [
    { icon: 'dashboard', text: 'Dashboard', route: '/dashboard', isActive: true },
    { icon: 'inventory_2', text: 'Stocks Management', route: '/stocks' },
    { icon: 'local_shipping', text: 'Shipment Tracking', route: '/shipments' },
    { icon: 'assessment', text: 'Reports & Analytics', route: '/reports' },
    { icon: 'people', text: 'Customer Management', route: '/customers' },
    { icon: 'article', text: 'Food Safety Blogs', route: '/blogs' },
    { icon: 'settings', text: 'Settings', route: '/settings' },
    { icon: 'account_circle', text: 'My Account', route: '/account' },
    { icon: 'help', text: 'Help & Support', route: '/help' }
  ];
}

// File: app/components/sidebar/sidebar.component.html
<div class="sidebar" [class.expanded]="isExpanded" [class.collapsed]="!isExpanded">
  <div class="logo-container">
    <img *ngIf="isExpanded" src="assets/logo-full.png" alt="Grocery Store Management System" class="logo-full">
    <img *ngIf="!isExpanded" src="assets/logo-icon.png" alt="GSMS" class="logo-icon">
  </div>
  
  <div class="sidebar-items">
    <div 
      *ngFor="let item of sidebarItems" 
      class="sidebar-item" 
      [class.active]="item.isActive"
      [matTooltip]="!isExpanded ? item.text : ''"
      matTooltipPosition="right">
      <mat-icon>{{ item.icon }}</mat-icon>
      <span *ngIf="isExpanded" class="item-text">{{ item.text }}</span>
    </div>
  </div>
</div>

// File: app/components/sidebar/sidebar.component.scss
.sidebar {
  height: 100%;
  color: white;
  display: flex;
  flex-direction: column;
  transition: width 0.3s ease;
  overflow: hidden;
}

.expanded {
  width: 240px;
}

.collapsed {
  width: 64px;
}

.logo-container {
  padding: 16px;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 64px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.logo-full {
  height: 40px;
  width: auto;
}

.logo-icon {
  height: 32px;
  width: 32px;
}

.sidebar-items {
  display: flex;
  flex-direction: column;
  padding: 16px 0;
}

.sidebar-item {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  cursor: pointer;
  transition: background-color 0.2s;
  border-radius: 0 28px 28px 0;
  margin-right: 16px;
  white-space: nowrap;
  
  &:hover {
    background-color: rgba(255, 255, 255, 0.1);
  }
  
  &.active {
    background-color: rgba(255, 255, 255, 0.2);
  }
  
  mat-icon {
    margin-right: 16px;
  }
  
  .item-text {
    white-space: nowrap;
  }
}

.collapsed .sidebar-item {
  justify-content: center;
  padding: 12px 0;
  
  mat-icon {
    margin-right: 0;
  }
}

// File: app/components/main-content/main-content.component.ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-main-content',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './main-content.component.html',
  styleUrls: ['./main-content.component.scss']
})
export class MainContentComponent {
  // This will be empty for now as per requirements
}

// File: app/components/main-content/main-content.component.html
<div class="main-content">
  <!-- This will be kept blank for now as per requirements -->
</div>

// File: app/components/main-content/main-content.component.scss
.main-content {
  padding: 20px;
  height: calc(100% - 64px);
  background-color: #f8f9fa;
  overflow-y: auto;
}

// File: styles.scss (Global Styles)
@import '@angular/material/prebuilt-themes/indigo-pink.css';

body {
  margin: 0;
  font-family: Roboto, "Helvetica Neue", sans-serif;
}

html, body {
  height: 100%;
}

* {
  box-sizing: border-box;
}
