import { Injectable } from '@angular/core';
import jwt_decode from 'jwt-decode';
import { CookieService } from 'ngx-cookie-service';

@Injectable({
  providedIn: 'root',
})
export class JWTTokenService {
  constructor(private cookieService: CookieService) {}

  jwtToken!: string;
  decodedToken!: { [key: string]: string };

  public setToken(jwt: string): void {
    if (jwt) {
      this.cookieService.set('jwt', jwt, 1, '/');
      this.jwtToken = jwt;
      this.cookieService.set('userId', this.getUserId(), 1, '/');
      this.cookieService.set('username', this.getUser(), 1, '/');
      this.cookieService.set('roles', this.getRoles(), 1, '/');
    }
  }

  public removeToken(): void {
    this.cookieService.delete('jwt', '/');
    this.cookieService.delete('userId', '/');
    this.cookieService.delete('username', '/');
    this.cookieService.delete('roles', '/');
    this.jwtToken = '';
  }

  public decodeToken(): void {
    if (this.jwtToken) {
      this.decodedToken = jwt_decode(this.jwtToken);
    }
  }

  public getDecodeToken(): string {
    return jwt_decode(this.jwtToken);
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
      return false;
    }
  }
}
