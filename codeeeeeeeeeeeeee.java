Angular Standalone Grocery Management (main.ts)

import { bootstrapApplication } from '@angular/platform-browser';
import { provideAnimations } from '@angular/platform-browser/animations';
import { AppComponent } from './app/app.component';

bootstrapApplication(AppComponent, {
  providers: [
    provideAnimations()
  ]
}).catch(err => console.error(err));


import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

// Material Imports
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatDividerModule } from '@angular/material/divider';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatBadgeModule } from '@angular/material/badge';
import { MatMenuModule } from '@angular/material/menu';
import { MatCardModule } from '@angular/material/card';

interface NavItem {
  icon: string;
  title: string;
  active: boolean;
}

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatSidenavModule,
    MatToolbarModule,
    MatListModule,
    MatIconModule,
    MatButtonModule,
    MatDividerModule,
    MatFormFieldModule,
    MatInputModule,
    MatBadgeModule,
    MatMenuModule,
    MatCardModule
  ],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'grocery-management-system';
  isExpanded = true;

  navItems: NavItem[] = [
    { icon: 'dashboard', title: 'Dashboard', active: true },
    { icon: 'inventory_2', title: 'Stocks Management', active: false },
    { icon: 'local_shipping', title: 'Shipment Tracking', active: false },
    { icon: 'analytics', title: 'Reports & Analytics', active: false },
    { icon: 'people', title: 'Customer Management', active: false },
    { icon: 'menu_book', title: 'Food Safety Blogs', active: false },
    { icon: 'settings', title: 'Settings', active: false },
    { icon: 'account_circle', title: 'My Account', active: false },
    { icon: 'help', title: 'Help & Support', active: false }
  ];

  toggleSidebar() {
    this.isExpanded = !this.isExpanded;
  }

  setActiveItem(index: number) {
    this.navItems.forEach((item, i) => {
      item.active = i === index;
    });
  }
}



app.component.html

<mat-sidenav-container class="sidenav-container">
  <!-- Sidebar -->
  <mat-sidenav #sidenav mode="side" [opened]="true" [ngClass]="{'expanded': isExpanded}" class="mat-elevation-z8">
    <div class="sidebar-header">
      <div class="logo-container">
        <div class="logo">
          <mat-icon>store</mat-icon>
        </div>
        <div class="logo-text" *ngIf="isExpanded">
          <div class="title">Grocery Store</div>
          <div class="subtitle">Management System</div>
        </div>
      </div>
    </div>

    <mat-divider></mat-divider>

    <!-- Navigation Menu -->
    <mat-nav-list>
      <a mat-list-item *ngFor="let item of navItems; let i = index" 
         [ngClass]="{'active-link': item.active}"
         (click)="setActiveItem(i)">
        <mat-icon matListItemIcon>{{item.icon}}</mat-icon>
        <span matListItemTitle *ngIf="isExpanded">{{item.title}}</span>
      </a>
    </mat-nav-list>

    <!-- Toggle Button -->
    <div class="toggle-container">
      <button mat-mini-fab color="primary" (click)="toggleSidebar()">
        <mat-icon>{{isExpanded ? 'chevron_left' : 'chevron_right'}}</mat-icon>
      </button>
    </div>
  </mat-sidenav>

  <!-- Main Content -->
  <mat-sidenav-content>
    <!-- Header Toolbar -->
    <mat-toolbar color="primary" class="mat-elevation-z4">
      <span *ngIf="isExpanded">Hello John Doe!</span>
      <span class="toolbar-spacer"></span>
      
      <!-- Search box -->
      <div class="search-container">
        <mat-form-field appearance="outline">
          <mat-icon matPrefix>search</mat-icon>
          <input matInput placeholder="Search...">
        </mat-form-field>
      </div>
      
      <!-- Notifications -->
      <button mat-icon-button>
        <mat-icon [matBadge]="'2'" matBadgeColor="accent">notifications</mat-icon>
      </button>
      
      <!-- User Avatar -->
      <button mat-icon-button [matMenuTriggerFor]="userMenu">
        <div class="user-avatar">JD</div>
      </button>
      <mat-menu #userMenu="matMenu">
        <button mat-menu-item>
          <mat-icon>person</mat-icon>
          <span>Profile</span>
        </button>
        <button mat-menu-item>
          <mat-icon>exit_to_app</mat-icon>
          <span>Logout</span>
        </button>
      </mat-menu>
    </mat-toolbar>

    <!-- Main Content Area -->
    <div class="content-container">
      <div class="dashboard-card mat-elevation-z2">
        <h2>Dashboard Overview</h2>
        <p>Welcome to your grocery store management dashboard.</p>
      </div>
    </div>
  </mat-sidenav-content>
</mat-sidenav-container>


  Angular Standalone Grocery Management (app.component.scss)

.sidenav-container {
  height: 100vh;
}

mat-sidenav {
  width: 250px;
  background-color: #fff;
  transition: width 0.3s ease;
  overflow-x: hidden;
  
  &.expanded {
    width: 250px;
  }
  
  &:not(.expanded) {
    width: 70px;
    
    .logo-container {
      justify-content: center;
    }
    
    .mat-list-item {
      padding: 0 8px;
    }
  }
}

.sidebar-header {
  height: 64px;
  display: flex;
  align-items: center;
  padding: 0 16px;
}

.logo-container {
  display: flex;
  align-items: center;
}

.logo {
  background-color: #3f51b5;
  color: white;
  width: 40px;
  height: 40px;
  border-radius: 4px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.logo-text {
  margin-left: 12px;
  
  .title {
    font-weight: 500;
    font-size: 14px;
  }
  
  .subtitle {
    font-size: 11px;
    color: rgba(0, 0, 0, 0.6);
  }
}

.toggle-container {
  position: absolute;
  bottom: 16px;
  left: 50%;
  transform: translateX(-50%);
}

.active-link {
  background-color: rgba(63, 81, 181, 0.1);
  color: #3f51b5;
  
  mat-icon {
    color: #3f51b5;
  }
}

.toolbar-spacer {
  flex: 1 1 auto;
}

.search-container {
  margin-right: 16px;
  width: 300px;
  
  .mat-form-field {
    width: 100%;
    font-size: 14px;
    
    ::ng-deep .mat-form-field-wrapper {
      padding-bottom: 0;
    }
    
    ::ng-deep .mat-form-field-outline {
      background-color: rgba(255, 255, 255, 0.2);
      border-radius: 4px;
    }
    
    ::ng-deep .mat-form-field-infix {
      padding: 0.5em 0;
    }
  }
  
  input {
    color: white;
    
    &::placeholder {
      color: rgba(255, 255, 255, 0.7);
    }
  }
  
  mat-icon {
    color: rgba(255, 255, 255, 0.7);
  }
}

.user-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background-color: #c5cae9;
  color: #3f51b5;
  display: flex;
  justify-content: center;
  align-items: center;
  font-weight: 500;
  font-size: 14px;
}

.content-container {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: calc(100vh - 64px);
}

.dashboard-card {
  background-color: white;
  padding: 24px;
  border-radius: 4px;
  
  h2 {
    margin-top: 0;
    font-size: 18px;
    font-weight: 500;
    color: rgba(0, 0, 0, 0.87);
    margin-bottom: 16px;
  }
  
  p {
    color: rgba(0, 0, 0, 0.6);
  }
}


ng add @angular/material



  




import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
public class GroupMember implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Groups group;

    private boolean isAdmin;
}

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface GroupRepository extends JpaRepository<Groups, Long> {

    @Query("SELECT g FROM Groups g WHERE g.groupCreator.userId = :userId")
    List<Groups> findGroupsByCreator(Long userId);

    @Query("SELECT g FROM Groups g JOIN g.members gm WHERE gm.user.userId = :userId")
    List<Groups> findGroupsByMember(Long userId);

    @Query("SELECT g FROM Groups g WHERE LOWER(g.groupName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Groups> searchGroupsByName(String name);

    @Transactional
    void deleteByGroupCreator_UserIdAndGroupId(Long userId, Long groupId);
}


import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    void deleteByUser_UserIdAndGroup_GroupId(Long userId, Long groupId);
}


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupMemberRepository groupMemberRepository;

    @Transactional
    public Groups createGroup(Long creatorId, String groupName, boolean isPublic) {
        Users creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Groups group = new Groups();
        group.setGroupCreator(creator);
        group.setGroupName(groupName);
        group.setPublic(isPublic);
        group = groupRepository.save(group);

        // Add creator as a default member
        GroupMember member = new GroupMember();
        member.setUser(creator);
        member.setGroup(group);
        member.setAdmin(true);
        groupMemberRepository.save(member);

        return group;
    }

    public List<Groups> getGroupsByCreator(Long userId) {
        return groupRepository.findGroupsByCreator(userId);
    }

    public List<Groups> getGroupsByMember(Long userId) {
        return groupRepository.findGroupsByMember(userId);
    }

    public List<Groups> searchGroups(String groupName) {
        return groupRepository.searchGroupsByName(groupName);
    }

    @Transactional
    public void deleteGroup(Long userId, Long groupId) {
        groupRepository.deleteByGroupCreator_UserIdAndGroupId(userId, groupId);
    }

    @Transactional
    public void addUserToGroup(Long groupId, Long userId, boolean isAdmin) {
        Groups group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        GroupMember member = new GroupMember();
        member.setUser(user);
        member.setGroup(group);
        member.setAdmin(isAdmin);
        groupMemberRepository.save(member);
    }

    @Transactional
    public void removeUserFromGroup(Long groupId, Long userId) {
        groupMemberRepository.deleteByUser_UserIdAndGroup_GroupId(userId, groupId);
    }
}

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping("/create")
    public ResponseEntity<Groups> createGroup(@RequestParam Long creatorId,
                                              @RequestParam String groupName,
                                              @RequestParam boolean isPublic) {
        Groups group = groupService.createGroup(creatorId, groupName, isPublic);
        return ResponseEntity.ok(group);
    }

    @GetMapping("/by-creator/{userId}")
    public ResponseEntity<List<Groups>> getGroupsByCreator(@PathVariable Long userId) {
        return ResponseEntity.ok(groupService.getGroupsByCreator(userId));
    }

    @GetMapping("/by-member/{userId}")
    public ResponseEntity<List<Groups>> getGroupsByMember(@PathVariable Long userId) {
        return ResponseEntity.ok(groupService.getGroupsByMember(userId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Groups>> searchGroups(@RequestParam String name) {
        return ResponseEntity.ok(groupService.searchGroups(name));
    }

    @DeleteMapping("/delete/{userId}/{groupId}")
    public ResponseEntity<String> deleteGroup(@PathVariable Long userId, @PathVariable Long groupId) {
        groupService.deleteGroup(userId, groupId);
        return ResponseEntity.ok("Group deleted successfully");
    }

    @PostMapping("/{groupId}/add-user/{userId}")
    public ResponseEntity<String> addUserToGroup(@PathVariable Long groupId,
                                                 @PathVariable Long userId,
                                                 @RequestParam boolean isAdmin) {
        groupService.addUserToGroup(groupId, userId, isAdmin);
        return ResponseEntity.ok("User added to group");
    }

    @DeleteMapping("/{groupId}/remove-user/{userId}")
    public ResponseEntity<String> removeUserFromGroup(@PathVariable Long groupId,
                                                      @PathVariable Long userId) {
        groupService.removeUserFromGroup(groupId, userId);
        return ResponseEntity.ok("User removed from group");
    }
}


