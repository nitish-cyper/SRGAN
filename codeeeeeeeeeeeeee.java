
User Authentication Service

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { map, catchError } from 'rxjs/operators';

export interface User {
  username: string;
  password: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private usersFile = 'assets/users.json';

  constructor(private http: HttpClient) {}

  // Validate user credentials against stored users
  validateUser(username: string, password: string): Observable<boolean> {
    return this.http.get<User[]>(this.usersFile).pipe(
      map(users => {
        const user = users.find(u => 
          u.username === username && u.password === password
        );
        return !!user;
      }),
      catchError(() => of(false))
    );
  }

  // Check if username already exists during signup
  checkUsernameExists(username: string): Observable<boolean> {
    return this.http.get<User[]>(this.usersFile).pipe(
      map(users => users.some(u => u.username === username)),
      catchError(() => of(false))
    );
  }

  // Add new user to the JSON file
  registerUser(username: string, password: string): Observable<boolean> {
    return this.http.get<User[]>(this.usersFile).pipe(
      map(users => {
        // Check if username already exists
        if (users.some(u => u.username === username)) {
          return false;
        }

        // Add new user
        users.push({ username, password });
        
        // In a real app, you'd use a backend API to save the user
        // This is a simplified example
        localStorage.setItem('users', JSON.stringify(users));
        return true;
      }),
      catchError(() => of(false))
    );
  }
}


Updated Sign In Component

import { Component } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthFormComponent } from '../auth-form/auth-form.component';
import { AuthService } from './auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-sign-in',
  standalone: true,
  imports: [AuthFormComponent, CommonModule],
  template: `
    <app-auth-form 
      formType="signin" 
      (formSubmitted)="handleSignIn($event)"
      [errorMessage]="errorMessage">
    </app-auth-form>
  `
})
export class SignInComponent {
  errorMessage: string = '';

  constructor(
    private router: Router, 
    private authService: AuthService
  ) {}

  handleSignIn(form: FormGroup) {
    const { username, password } = form.value;
    
    this.authService.validateUser(username, password).subscribe(
      isValid => {
        if (isValid) {
          // Successful login
          console.log('Sign In Successful');
          this.router.navigate(['/dashboard']); // Redirect to dashboard
        } else {
          // Invalid credentials
          this.errorMessage = 'Invalid username or password';
        }
      }
    );
  }
}


Updated Sign Up Component

import { Component } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthFormComponent } from '../auth-form/auth-form.component';
import { AuthService } from './auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-sign-up',
  standalone: true,
  imports: [AuthFormComponent, CommonModule],
  template: `
    <app-auth-form 
      formType="signup" 
      (formSubmitted)="handleSignUp($event)"
      [errorMessage]="errorMessage">
    </app-auth-form>
  `
})
export class SignUpComponent {
  errorMessage: string = '';

  constructor(
    private router: Router, 
    private authService: AuthService
  ) {}

  handleSignUp(form: FormGroup) {
    const { username, password, confirmPassword } = form.value;
    
    // Additional validation
    if (password !== confirmPassword) {
      this.errorMessage = 'Passwords do not match';
      return;
    }

    this.authService.registerUser(username, password).subscribe(
      success => {
        if (success) {
          console.log('Sign Up Successful');
          this.router.navigate(['/signin']);
        } else {
          this.errorMessage = 'Username already exists';
        }
      }
    );
  }
}


Updated Auth Form Component

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
  @Input() errorMessage: string = '';
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
    }
  }

  navigate() {
    this.router.navigate([this.isSignup ? '/signin' : '/signup']);
  }
}


AuthForm.html

  <div class="container">
  <div class="auth-card">
    <h2 class="title">GROCERY STORE <br> <span>MANAGEMENT SYSTEM</span></h2>
    <div *ngIf="isSignup" class="description">
      Streamline inventory, track orders, manage suppliers, and enhance store operations—all in one place.
    </div>
    <h3 class="subtitle">{{ isSignup ? 'ACCOUNT SIGN UP' : 'ACCOUNT SIGN IN' }}</h3>
    
    <!-- Global Error Message -->
    <div *ngIf="errorMessage" class="global-error">
      {{ errorMessage }}
    </div>
    
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


      Updated Auth Form CSS

.global-error {
  background-color: #f8d7da;
  color: #721c24;
  padding: 10px;
  border-radius: 5px;
  margin-bottom: 15px;
  text-align: center;
}

[
  {
    "username": "admin",
    "password": "password123"
  },
  {
    "username": "user",
    "password": "user123"
  }
]

