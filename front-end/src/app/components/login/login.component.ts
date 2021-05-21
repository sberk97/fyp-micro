import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { LoginResponse } from 'src/app/models/login-response/login-response';
import { BackendService } from 'src/app/services/backend/backend.service';
import { JWTTokenService } from 'src/app/services/jwt/jwt.token.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit, OnDestroy {
  private subscription: Subscription = new Subscription();

  username!: string;
  password!: string;

  loginFailed = false;
  failedMsg = '';

  constructor(
    private router: Router,
    private backendService: BackendService,
    private jwtTokenService: JWTTokenService
  ) {}

  ngOnInit(): void {
    // navigate to the root if we already have a token set (are logged in)
    if (this.jwtTokenService.isLoggedIn()) {
      void this.router.navigate(['/']);
    }
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  public onSubmit(): void {
    this.subscription.add(
      this.backendService.authenticate(this.username, this.password).subscribe(
        (response: LoginResponse) => {
          this.jwtTokenService.setToken(response.jwt);
          void this.router.navigate(['/']);
        },
        (error: HttpErrorResponse) => {
          this.loginFailed = true;
          if (error.status === 401) {
            this.failedMsg = error.error as string;
          } else {
            this.failedMsg = 'Login failed!';
          }
        }
      )
    );
  }
}
