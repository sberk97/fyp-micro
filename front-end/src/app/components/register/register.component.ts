import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { BackendService } from 'src/app/services/backend/backend.service';
import { JWTTokenService } from 'src/app/services/jwt/jwt.token.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent implements OnInit, OnDestroy {
  private subscription: Subscription = new Subscription();

  username!: string;
  passwordFirst!: string;
  passwordSecond!: string;

  registerFailed = false;
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
    if (this.passwordFirst != this.passwordSecond) {
      this.registerFailed = true;
      this.failedMsg = "Passwords doesn't match!";
    } else {
      this.subscription.add(
        this.backendService
          .register(this.username, this.passwordSecond)
          .subscribe(
            // eslint-disable-next-line @typescript-eslint/no-empty-function
            () => {},
            (error: HttpErrorResponse) => {
              if (error.status === 200) {
                void this.router.navigate(['/login']);
              }
              this.registerFailed = true;
              if (error.status === 400 || error.status === 409) {
                this.failedMsg = error.error as string;
              } else {
                this.failedMsg = 'Registration failed!';
              }
            }
          )
      );
    }
  }
}
