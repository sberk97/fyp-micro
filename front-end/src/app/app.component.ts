import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { JWTTokenService } from './services/jwt/jwt.token.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent {
  constructor(
    private router: Router,
    public jwtTokenService: JWTTokenService
  ) {}

  public loggedIn(): boolean {
    return this.jwtTokenService.isLoggedIn();
  }

  public logOut(): void {
    this.jwtTokenService.removeToken();
    void this.router.navigate(['/']);
  }
}
