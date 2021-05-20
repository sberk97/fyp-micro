import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { JWTTokenService } from '../jwt/jwt.token.service';

@Injectable({
  providedIn: 'root',
})
export class AuthorizeGuard implements CanActivate {
  constructor(private router: Router, private jwtService: JWTTokenService) {}

  canActivate(): boolean {
    if (!this.jwtService.isTokenExpired()) {
      return true;
    }
    void this.router.navigate(['login']);
    return false;
  }
}
