import { Injectable } from '@angular/core';
import jwt_decode from 'jwt-decode';
import { CookieService } from 'ngx-cookie-service';

@Injectable({
  providedIn: 'root',
})
export class JWTTokenService {
  constructor(private cookieService: CookieService) {}

  decodedToken!: { [key: string]: string };

  public setToken(jwt: string): void {
    if (jwt) {
      this.cookieService.set('jwt', jwt, 1, '/');
    }
  }

  public removeToken(): void {
    this.cookieService.delete('jwt', '/');
    this.cookieService.delete('userId', '/');
    this.cookieService.delete('username', '/');
    this.cookieService.delete('roles', '/');
    this.decodedToken = {};
  }

  public getToken(): string {
    return this.cookieService.get('jwt');
  }

  private decodeToken(): void {
    const jwtToken = this.getToken();
    if (jwtToken) {
      this.decodedToken = jwt_decode(jwtToken);
    }
  }

  public getDecodeToken(): string {
    return jwt_decode(this.getToken());
  }

  public getUserId(): string {
    this.decodeToken();
    return this.decodedToken ? this.decodedToken.userId : '';
  }

  public getUser(): string {
    this.decodeToken();
    return this.decodedToken ? this.decodedToken.sub : '';
  }

  public getRoles(): string {
    this.decodeToken();
    return this.decodedToken ? this.decodedToken.roles : '';
  }

  public getExpiryTime(): number | string {
    this.decodeToken();
    return this.decodedToken ? this.decodedToken.exp : -1;
  }

  public isTokenExpired(): boolean {
    const expiryTime: number = this.getExpiryTime() as number;
    if (expiryTime) {
      return 1000 * expiryTime - new Date().getTime() < 5000;
    } else {
      return true;
    }
  }

  public isLoggedIn(): boolean {
    return this.cookieService.check('jwt') && !this.isTokenExpired();
  }

  public isAdmin(): boolean {
    return this.isLoggedIn() && this.getRoles() == 'ROLE_ADMIN';
  }
}
