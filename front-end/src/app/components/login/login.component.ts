import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { BackendService } from 'src/app/services/backend.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  username!: string;
  password!: string;

  loginFailed = false;
  failedMsg = '';

  constructor(private router: Router, private backendService: BackendService, private cookieService: CookieService) { }


  ngOnInit() {
      // navigate to the root if we already have a token set (are logged in)
      if (this.cookieService.check('token')) {
          this.router.navigate(['/']);
      }
  }

  onSubmit() {
      this.backendService.authenticate(this.username, this.password).subscribe(response => {
          // this.cookieService.set('username', response.user, 365, '/');
          this.cookieService.set('jwt', response.jwt, 365, '/');
          this.router.navigate(['/']);
      }, error => {
          this.loginFailed = true;
          if (error.status === 401) {
              this.failedMsg = error.error;
          } else {
              this.failedMsg = 'Login failed!';
          }
      });
  }
}
