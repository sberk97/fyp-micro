import { Injectable } from '@angular/core';
import jwt_decode from 'jwt-decode';

@Injectable({
  providedIn: 'root',
})
export class JWTTokenService {
  jwtToken!: string;
  decodedToken!: { [key: string]: string };

  public setToken(token: string): void {
    if (token) {
      this.jwtToken = token;
    }
  }

  public removeToken(): void {
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
