import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { BackendService } from 'src/app/services/backend/backend.service';
import { JWTTokenService } from 'src/app/services/jwt/jwt.token.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  username!: string;
  password!: string;

  loginFailed = false;
  failedMsg = '';

  constructor(
    private router: Router,
    private backendService: BackendService,
    private cookieService: CookieService,
    private jwtTokenService: JWTTokenService
  ) {}

  ngOnInit(): void {
    // navigate to the root if we already have a token set (are logged in)
    if (
      this.cookieService.check('jwt') &&
      !this.jwtTokenService.isTokenExpired()
    ) {
      void this.router.navigate(['/']);
    }
  }

  onSubmit(): void {
    this.backendService.authenticate(this.username, this.password).subscribe(
      (response) => {
        this.jwtTokenService.setToken(response.jwt);

        this.cookieService.set(
          'username',
          this.jwtTokenService.getUser(),
          1,
          '/'
        );
        this.cookieService.set(
          'roles',
          this.jwtTokenService.getRoles(),
          1,
          '/'
        );
        this.cookieService.set('jwt', response.jwt, 1, '/');
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
    );
  }
}
