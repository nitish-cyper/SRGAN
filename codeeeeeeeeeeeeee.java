// app-routing.ts
import { Routes } from '@angular/router';
import { SignInComponent } from './auth/sign-in/sign-in.component';
import { SignUpComponent } from './auth/sign-up/sign-up.component';

export const routes: Routes = [
  { path: 'signin', component: SignInComponent },
  { path: 'signup', component: SignUpComponent },
  { path: '', redirectTo: 'signin', pathMatch: 'full' },
  { path: '**', redirectTo: 'signin' }
];

// auth/auth-form/auth-form.component.ts
import { Component, Input, Output, EventEmitter, OnInit, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-auth-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './auth-form.component.html',
  styleUrls: ['./auth-form.component.css']
})
export class AuthFormComponent implements OnInit, OnChanges {
  @Input() formType: 'signin' | 'signup' = 'signin';
  @Output() formSubmitted = new EventEmitter<FormGroup>();

  authForm: FormGroup;
  isSignup: boolean = false;

  constructor(private router: Router) {
    this.authForm = new FormGroup({
      username: new FormControl('', Validators.required),
      password: new FormControl('', [Validators.required, Validators.minLength(6)])
    });
  }

  ngOnInit() {
    this.isSignup = this.formType === 'signup';
    this.updateFormControls();
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['formType']) {
      this.isSignup = this.formType === 'signup';
      this.updateFormControls();
    }
  }

  updateFormControls() {
    if (this.isSignup) {
      if (!this.authForm.get('confirmPassword')) {
        this.authForm.addControl('confirmPassword', new FormControl('', Validators.required));
        this.authForm.setValidators(this.passwordMatchValidator);
      }
    } else {
      if (this.authForm.get('confirmPassword')) {
        this.authForm.removeControl('confirmPassword');
        this.authForm.clearValidators();
      }
    }
    this.authForm.updateValueAndValidity();
  }

  passwordMatchValidator(form: FormGroup) {
    const password = form.get('password')?.value;
    const confirmPassword = form.get('confirmPassword')?.value;
    return password === confirmPassword ? null : { passwordMismatch: true };
  }

  onSubmit() {
    if (this.authForm.valid) {
      this.formSubmitted.emit(this.authForm);
      if (this.isSignup) {
        this.router.navigate(['/signin']);
      }
    }
  }

  navigate() {
    this.router.navigate([this.isSignup ? '/signin' : '/signup']);
  }
}

// auth/auth-form/auth-form.component.html
<div class="container">
  <div class="auth-card">
    <h2 class="title">GROCERY STORE <br> <span>MANAGEMENT SYSTEM</span></h2>
    <div *ngIf="isSignup" class="description">
      Streamline inventory, track orders, manage suppliers, and enhance store operations—all in one place.
    </div>
    <h3 class="subtitle">{{ isSignup ? 'ACCOUNT SIGN UP' : 'ACCOUNT SIGN IN' }}</h3>
    
    <form [formGroup]="authForm" (ngSubmit)="onSubmit()">
      <div class="form-group">
        <label for="username">Username</label>
        <input id="username" formControlName="username" placeholder="Enter username" />
        <div class="error" *ngIf="authForm.get('username')?.invalid && authForm.get('username')?.touched">
          Username is required
        </div>
      </div>

      <div class="form-group">
        <label for="password">Password *</label>
        <input id="password" formControlName="password" type="password" placeholder="Enter password" />
        <div class="error" *ngIf="authForm.get('password')?.invalid && authForm.get('password')?.touched">
          Password must be at least 6 characters
        </div>
      </div>

      <div *ngIf="isSignup" class="form-group">
        <label for="confirmPassword">Confirm Password *</label>
        <input id="confirmPassword" formControlName="confirmPassword" type="password" placeholder="Confirm password" />
        <div class="error" *ngIf="authForm.hasError('passwordMismatch') && authForm.get('confirmPassword')?.touched">
          Passwords do not match
        </div>
      </div>

      <button type="submit" [disabled]="authForm.invalid" class="btn">
        {{ isSignup ? 'Sign Up' : 'Sign In' }}
      </button>
    </form>

    <p class="link-text">
      {{ isSignup ? 'Already have an account?' : "Don't have an account?" }}
      <a (click)="navigate()">{{ isSignup ? 'Sign In' : 'Sign Up' }}</a>
    </p>
  </div>
</div>

// auth/auth-form/auth-form.component.css
.container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: url('/assets/bg.jpg') no-repeat center center;
  background-size: cover;
}

.auth-card {
  background: white;
  padding: 30px;
  border-radius: 10px;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
  width: 400px;
  text-align: center;
}

.title {
  font-size: 22px;
  font-weight: bold;
  margin-bottom: 10px;
}

.title span {
  color: #007bff;
  font-weight: bold;
}

.description {
  font-size: 14px;
  color: #666;
  margin-bottom: 20px;
}

.subtitle {
  font-size: 18px;
  margin-bottom: 20px;
  font-weight: 600;
}

.form-group {
  margin-bottom: 15px;
  text-align: left;
}

label {
  display: block;
  margin-bottom: 5px;
  font-weight: 500;
}

input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 14px;
}

.error {
  color: #dc3545;
  font-size: 12px;
  margin-top: 5px;
}

.btn {
  width: 100%;
  background-color: #007bff;
  color: white;
  padding: 10px;
  border: none;
  border-radius: 5px;
  font-size: 16px;
  cursor: pointer;
  margin-top: 10px;
}

.btn:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

.link-text {
  margin-top: 15px;
  font-size: 14px;
}

.link-text a {
  color: #007bff;
  cursor: pointer;
  text-decoration: underline;
}

// auth/sign-in/sign-in.component.ts
import { Component } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthFormComponent } from '../auth-form/auth-form.component';

@Component({
  selector: 'app-sign-in',
  standalone: true,
  imports: [AuthFormComponent],
  template: '<app-auth-form formType="signin" (formSubmitted)="handleSignIn($event)"></app-auth-form>'
})
export class SignInComponent {
  constructor(private router: Router) {}

  handleSignIn(form: FormGroup) {
    console.log('Sign In Successful', form.value);
    // Here you would typically call an authentication service
    // For demo purposes, just log the values
    alert('Sign In Successful');
  }
}

// auth/sign-up/sign-up.component.ts
import { Component } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthFormComponent } from '../auth-form/auth-form.component';

@Component({
  selector: 'app-sign-up',
  standalone: true,
  imports: [AuthFormComponent],
  template: '<app-auth-form formType="signup" (formSubmitted)="handleSignUp($event)"></app-auth-form>'
})
export class SignUpComponent {
  constructor(private router: Router) {}

  handleSignUp(form: FormGroup) {
    console.log('Sign Up Successful', form.value);
    // Here you would typically call a registration service
    // For demo purposes, just log the values and redirect
    alert('Sign Up Successful');
    this.router.navigate(['/signin']);
  }
}

// main.ts
import { bootstrapApplication } from '@angular/platform-browser';
import { provideRouter } from '@angular/router';
import { AppComponent } from './app/app.component';
import { routes } from './app/app-routing';

bootstrapApplication(AppComponent, {
  providers: [
    provideRouter(routes)
  ]
}).catch(err => console.error(err));

// app.component.ts
import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  template: '<router-outlet></router-outlet>'
})
export class AppComponent {
  title = 'grocery-store-management';
}

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
public class Groups implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long groupId;

    @NotNull
    private String groupName;

    private boolean isPublic;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private Users groupCreator;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupMember> members;
}


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


