import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { BackendService } from 'src/app/services/backend.service';
import { JWTTokenService } from 'src/app/utils/jwt.token.service';

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
    if (this.cookieService.check('jwt')) {
      void this.router.navigate(['/']);
    }
  }

  onSubmit(): void {
    this.backendService.authenticate(this.username, this.password).subscribe(
      (response) => {
        console.log(response);
        // this.cookieService.set('username', response.user, 365, '/');
        this.cookieService.set('jwt', response.jwt, 1, '/');
        void this.router.navigate(['/']);
        this.jwtTokenService.setToken(response.jwt);
        console.log(this.jwtTokenService.getUser());
        console.log(this.jwtTokenService.getRoles());
        console.log(this.jwtTokenService.getExpiryTime());
        console.log(this.jwtTokenService.isTokenExpired());
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
