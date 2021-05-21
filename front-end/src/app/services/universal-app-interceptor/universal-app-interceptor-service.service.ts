/* eslint-disable @typescript-eslint/no-explicit-any */
import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { JWTTokenService } from '../jwt/jwt.token.service';
@Injectable()
export class UniversalAppInterceptor implements HttpInterceptor {
  constructor(private jwtTokenService: JWTTokenService) {}

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    const token = this.jwtTokenService.getToken();
    if (!this.jwtTokenService.isTokenExpired()) {
      req = req.clone({
        url: req.url,
        setHeaders: {
          Authorization: `Bearer ${token}`,
        },
      });
    } else {
      this.jwtTokenService.removeToken();
    }

    return next.handle(req);
  }
}
