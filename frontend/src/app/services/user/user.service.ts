import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {LoginRequest} from "../../models/login-request";
import {url} from "../../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {LoginResponse} from "../../models/login-response";
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient, private router: Router) { }

  login(loginParams: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${url}/auth/login`, loginParams);
  }

  isLoggedIn(): boolean{
    return localStorage.getItem('access_token') != null;
  }

  getRole(): string{
    return localStorage.getItem('role') as string;
  }

  logOut(): void{
    delete localStorage['access_token'];
    this.router.navigate(['/login']);
  }

}
