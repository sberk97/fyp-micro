import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { JWTTokenService } from '../jwt/jwt.token.service';

@Injectable({
  providedIn: 'root',
})
export class AuthorizeGuard implements CanActivate {
  constructor(private router: Router, private jwtService: JWTTokenService) {}

  canActivate(): boolean {
    if (this.jwtService.getUser()) {
      if (this.jwtService.isTokenExpired()) {
        void this.router.navigate(['/login']);
      } else {
        return true;
      }
    }

    return false;
  }
}
