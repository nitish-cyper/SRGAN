import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SignInComponent } from './sign-in/sign-in.component';
import { SignUpComponent } from './sign-up/sign-up.component';

const routes: Routes = [
  { path: 'signin', component: SignInComponent },
  { path: 'signup', component: SignUpComponent },
  { path: '**', redirectTo: 'signin' }  // Default route
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
Step 2: Create a Reusable Form Component
This component will be used for both Sign Up and Sign In.

📄 auth-form.component.ts
typescript
Copy
Edit
import { Component, Input, Output, EventEmitter } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-auth-form',
  templateUrl: './auth-form.component.html',
  styleUrls: ['./auth-form.component.css']
})
export class AuthFormComponent {
  @Input() isSignup = false;
  @Output() formSubmitted = new EventEmitter<FormGroup>();

  authForm: FormGroup;

  constructor(private router: Router) {
    this.authForm = new FormGroup({
      username: new FormControl('', Validators.required),
      password: new FormControl('', [Validators.required, Validators.minLength(6)])
    });

    if (this.isSignup) {
      this.authForm.addControl('confirmPassword', new FormControl('', Validators.required));
      this.authForm.setValidators(this.passwordMatchValidator);
    }
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
Step 3: Create the Template for the Reusable Form
📄 auth-form.component.html
html
Copy
Edit
<div class="container">
  <div class="auth-card">
    <h2 class="title">GROCERY STORE <br> <span>MANAGEMENT SYSTEM</span></h2>
    <p *ngIf="isSignup" class="description">
      Streamline inventory, track orders, manage suppliers, and enhance store operations—all in one place.
    </p>
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
Step 4: Create Sign In and Sign Up Components
📄 sign-in.component.ts
typescript
Copy
Edit
import { Component } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'app-sign-in',
  template: '<app-auth-form [isSignup]="false" (formSubmitted)="handleSignIn($event)"></app-auth-form>'
})
export class SignInComponent {
  handleSignIn(form: FormGroup) {
    console.log('Sign In Successful', form.value);
  }
}
📄 sign-up.component.ts
typescript
Copy
Edit
import { Component } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'app-sign-up',
  template: '<app-auth-form [isSignup]="true" (formSubmitted)="handleSignUp($event)"></app-auth-form>'
})
export class SignUpComponent {
  handleSignUp(form: FormGroup) {
    console.log('Sign Up Successful', form.value);
  }
}
Step 5: Styling (CSS)
📄 auth-form.component.css
css
Copy
Edit
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

input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 14px;
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



import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {
    
    @NotBlank(message = "Content cannot be empty")
    private String content;

    private String mediaUrl;
}


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private Long id;
    private String content;
    private String mediaUrl;
    private int likes;
    private int shares;
    private String authorUsername;
}

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByAuthorUserId(Long userId);


}


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // Create a new post
    public PostResponse createPost(Long userId, PostRequest request) {
        Users author = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User does not exist"));

        Post newPost = new Post();
        newPost.setAuthor(author);
        newPost.setContent(request.getContent());
        newPost.setMediaUrl(request.getMediaUrl());

        return convertToResponse(postRepository.save(newPost));
    }

    // Edit an existing post (only by the author)
    @Transactional
    public PostResponse modifyPost(Long userId, Long postId, PostRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        if (!post.getAuthor().getUserId().equals(userId)) {
            throw new SecurityException("Unauthorized action");
        }

        post.setContent(request.getContent());
        post.setMediaUrl(request.getMediaUrl());

        return convertToResponse(postRepository.save(post));
    }

    // Retrieve all posts by a specific user
    public List<PostResponse> fetchUserPosts(Long userId) {
        return postRepository.findAllByAuthorUserId(userId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Remove a post (only by the author)
    public void removePost(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post does not exist"));

        if (!post.getAuthor().getUserId().equals(userId)) {
            throw new SecurityException("Unauthorized action");
        }

        postRepository.delete(post);
    }

    // Convert Post entity to DTO
    private PostResponse convertToResponse(Post post) {
        return new PostResponse(
                post.getId(),
                post.getContent(),
                post.getMediaUrl(),
                post.getLikes(),
                post.getShares(),
                post.getAuthor().getUsername()
        );
    }
}


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // Endpoint to create a post
    @PostMapping("/{userId}")
    public ResponseEntity<PostResponse> createPost(
            @PathVariable Long userId,
            @Valid @RequestBody PostRequest request) {
        return ResponseEntity.ok(postService.createPost(userId, request));
    }

    // Endpoint to update an existing post
    @PutMapping("/{userId}/{postId}")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable Long userId,
            @PathVariable Long postId,
            @Valid @RequestBody PostRequest request) {
        return ResponseEntity.ok(postService.modifyPost(userId, postId, request));
    }

    // Endpoint to get all posts by a specific user
    @GetMapping("/{userId}")
    public ResponseEntity<List<PostResponse>> getPostsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(postService.fetchUserPosts(userId));
    }

    // Endpoint to delete a post
    @DeleteMapping("/{userId}/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long userId, @PathVariable Long postId) {
        postService.removePost(userId, postId);
        return ResponseEntity.ok("Post deleted successfully");
    }
}


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<String> handleUnauthorizedAccess(SecurityException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }
}


-------------------------------------------------------------------------------------


1. Follow/Unfollow a User
We'll use the Friend entity for following and unfollowing.

Friend Repository
java
Copy
Edit
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    boolean existsByFollowerIdAndFollowingId(Users follower, Users following);
    void deleteByFollowerIdAndFollowingId(Users follower, Users following);
}
Friend Service
java
Copy
Edit
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    @Transactional
    public void followUser(Long followerId, Long followingId) {
        Users follower = getUserById(followerId);
        Users following = getUserById(followingId);

        if (!friendRepository.existsByFollowerIdAndFollowingId(follower, following)) {
            Friend friend = new Friend();
            friend.setFollowerId(follower);
            friend.setFollowingId(following);
            friendRepository.save(friend);
        }
    }

    @Transactional
    public void unfollowUser(Long followerId, Long followingId) {
        Users follower = getUserById(followerId);
        Users following = getUserById(followingId);

        friendRepository.deleteByFollowerIdAndFollowingId(follower, following);
    }

    private Users getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
Friend Controller
java
Copy
Edit
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @PostMapping("/{followerId}/follow/{followingId}")
    public ResponseEntity<String> followUser(@PathVariable Long followerId, @PathVariable Long followingId) {
        friendService.followUser(followerId, followingId);
        return ResponseEntity.ok("Followed successfully!");
    }

    @PostMapping("/{followerId}/unfollow/{followingId}")
    public ResponseEntity<String> unfollowUser(@PathVariable Long followerId, @PathVariable Long followingId) {
        friendService.unfollowUser(followerId, followingId);
        return ResponseEntity.ok("Unfollowed successfully!");
    }
}
2. Like/Unlike a Post
We'll use the Post entity and manage likes as an integer.

Post Repository
java
Copy
Edit
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
Like Service
java
Copy
Edit
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public void likePost(Long userId, Long postId) {
        Post post = getPostById(postId);
        post.setLikes(post.getLikes() + 1);
        postRepository.save(post);
    }

    @Transactional
    public void unlikePost(Long userId, Long postId) {
        Post post = getPostById(postId);
        if (post.getLikes() > 0) {
            post.setLikes(post.getLikes() - 1);
            postRepository.save(post);
        }
    }

    private Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
    }
}
Like Controller
java
Copy
Edit
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/{userId}/like/{postId}")
    public ResponseEntity<String> likePost(@PathVariable Long userId, @PathVariable Long postId) {
        postService.likePost(userId, postId);
        return ResponseEntity.ok("Post liked!");
    }

    @PostMapping("/{userId}/unlike/{postId}")
    public ResponseEntity<String> unlikePost(@PathVariable Long userId, @PathVariable Long postId) {
        postService.unlikePost(userId, postId);
        return ResponseEntity.ok("Post unliked!");
    }
}
3. Comment on a Post
We'll use the Comments entity to handle user comments.

Comment Repository
java
Copy
Edit
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comments, Long> {
}
Comment Service
java
Copy
Edit
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public void addComment(Long userId, Long postId, String content) {
        Users user = getUserById(userId);
        Post post = getPostById(postId);

        Comments comment = new Comments();
        comment.setUser(user);
        comment.setPost(post);
        comment.setContent(content);

        commentRepository.save(comment);
    }

    private Users getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
    }
}
Comment Controller
java
Copy
Edit
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{userId}/post/{postId}")
    public ResponseEntity<String> addComment(@PathVariable Long userId, @PathVariable Long postId, @RequestParam String content) {
        commentService.addComment(userId, postId, content);
        return ResponseEntity.ok("Comment added!");
    }
}
4. Share a Post
We'll create a new post when a user shares an existing post.

Post Sharing Service
java
Copy
Edit
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostSharingService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public void sharePost(Long userId, Long postId) {
        Users user = getUserById(userId);
        Post originalPost = getPostById(postId);

        Post sharedPost = new Post();
        sharedPost.setUser(user);
        sharedPost.setDescription("Shared Post: " + originalPost.getDescription());
        sharedPost.setMediaUrl(originalPost.getMediaUrl());
        sharedPost.setShared(true);
        sharedPost.setOriginalPost(originalPost);

        postRepository.save(sharedPost);
    }

    private Users getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
    }
}
Post Sharing Controller
java
Copy
Edit
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/share")
@RequiredArgsConstructor
public class PostSharingController {

    private final PostSharingService postSharingService;

    @PostMapping("/{userId}/share/{postId}")
    public ResponseEntity<String> sharePost(@PathVariable Long userId, @PathVariable Long postId) {
        postSharingService.sharePost(userId, postId);
        return ResponseEntity.ok("Post shared!");
    }
}


1. Report Repository
java
Copy
Edit
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByPost(Post post);
}
2. Report Service
java
Copy
Edit
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public void reportPost(Long userId, Long postId, String description) {
        Users user = getUserById(userId);
        Post post = getPostById(postId);

        Report report = new Report();
        report.setUser(user);
        report.setPost(post);
        report.setDescription(description);
        report.setStatus(Status.PENDING);

        reportRepository.save(report);
    }

    private Users getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
    }
}
3. Report Controller
java
Copy
Edit
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/{userId}/report/{postId}")
    public ResponseEntity<String> reportPost(
            @PathVariable Long userId, 
            @PathVariable Long postId, 
            @RequestParam String description) {
        reportService.reportPost(userId, postId, description);
        return ResponseEntity.ok("Post reported successfully!");
    }
}


