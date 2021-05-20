import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoginResponse } from '../../models/login-response/login-response';
import { Advert } from 'src/app/models/advert/advert';
import { User } from 'src/app/models/user/user';

@Injectable({
  providedIn: 'root',
})
export class BackendService {
  constructor(private http: HttpClient) {}

  // modify this so it points to your API
  public endpoint = 'http://localhost:9092/api/';

  public register(username: string, password: string): Observable<string> {
    const path = 'register';
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    const options = { headers };
    const body = { username: username, password: password };
    return this.http.post<string>(this.endpoint + path, body, options);
  }

  public authenticate(
    username: string,
    password: string
  ): Observable<LoginResponse> {
    const path = 'authenticate';
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    const options = { headers };
    const body = { username: username, password: password };
    return this.http.post<LoginResponse>(this.endpoint + path, body, options);
  }

  public getAdvert(id: number): Observable<Advert> {
    const path = 'advert-service/adverts/';
    return this.http.get<Advert>(this.endpoint + path + id.toString());
  }

  public getLastNAdverts(
    numberOfAdvertsRequested: number
  ): Observable<Advert[]> {
    const path = 'advert-service/adverts-latest?last=';
    return this.http.get<Advert[]>(
      this.endpoint + path + numberOfAdvertsRequested.toString()
    );
  }

  public getAdvertByTiitle(title: string): Observable<Advert[]> {
    const path = 'advert-service/adverts?title=';
    return this.http.get<Advert[]>(this.endpoint + path + title);
  }

  public getUser(id: number): Observable<User> {
    const path = 'users/';
    return this.http.get<User>(this.endpoint + path + id.toString());
  }

  public getUserAdverts(id: number): Observable<Advert[]> {
    const path = 'advert-service/adverts/users/';
    return this.http.get<Advert[]>(this.endpoint + path + id.toString());
  }

  public deleteAdvertById(id: number): Observable<number> {
    const path = 'advert-service/adverts/';
    return this.http.delete<number>(this.endpoint + path + id.toString());
  }

  public deleteUserById(id: number): Observable<number> {
    const path = 'users/';
    return this.http.delete<number>(this.endpoint + path + id.toString());
  }

  public deleteAdvertsByUserId(id: number): Observable<void> {
    const path = 'advert-service/adverts/users/';
    return this.http.delete<void>(this.endpoint + path + id.toString());
  }

  public addAdvert(body: unknown): Observable<number> {
    const path = 'advert-service/adverts';
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    const options = { headers };
    return this.http.post<number>(this.endpoint + path, body, options);
  }

  public editAdvert(body: unknown, id: number): Observable<number> {
    const path = 'advert-service/adverts/';
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    const options = { headers };
    return this.http.put<number>(
      this.endpoint + path + id.toString(),
      body,
      options
    );
  }
}
