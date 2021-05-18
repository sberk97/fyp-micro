import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoginResponse } from '../../models/login-response/login-response';
import { Advert } from 'src/app/models/advert/advert';

@Injectable({
  providedIn: 'root',
})
export class BackendService {
  constructor(private http: HttpClient) {}

  // modify this so it points to your API
  public endpoint = 'http://localhost:9092/api/';

  public register(username: string, password: string): Observable<string> {
    const registerPath = 'register';
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    const options = { headers };
    const body = { username: username, password: password };
    return this.http.post<string>(this.endpoint + registerPath, body, options);
  }

  public authenticate(
    username: string,
    password: string
  ): Observable<LoginResponse> {
    const authenticatePath = 'authenticate';
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    const options = { headers };
    const body = { username: username, password: password };
    return this.http.post<LoginResponse>(
      this.endpoint + authenticatePath,
      body,
      options
    );
  }

  public getAdvert(id: number): Observable<Advert> {
    const registerPath = 'advert-service/adverts/';
    return this.http.get<Advert>(this.endpoint + registerPath + id.toString());
  }
}
