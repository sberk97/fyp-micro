import { Component } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import { Router, ActivatedRoute, NavigationEnd } from '@angular/router';
import { BackendService } from './services/backend/backend.service';
import { JWTTokenService } from './services/jwt/jwt.token.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent {
  title = 'Homepage - Freelancer hire';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private backendService: BackendService,
    public cookieService: CookieService,
    public jwtTokenService: JWTTokenService
  ) {
    // listen to every routing event and redirect the route to login if the user is not logged in (or trying to sign up)
    // this.router.events.subscribe((event) => {
    //   if (
    //     event instanceof NavigationEnd &&
    //     !this.loggedIn() &&
    //     event.url !== '/register'
    //   ) {
    //     void this.router.navigate(['/login']);
    //   }
    // });
  }

  loggedIn(): boolean {
    return (
      this.cookieService.check('jwt') &&
      this.cookieService.get('jwt').length > 0 &&
      !this.jwtTokenService.isTokenExpired()
    );
  }

  logOut(): void {
    this.jwtTokenService.removeToken();
    void this.router.navigate(['/']);
  }
}
