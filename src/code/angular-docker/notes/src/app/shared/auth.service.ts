import { Injectable } from '@angular/core';
import { Location } from '@angular/common';
import { BehaviorSubject, Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { User } from './user';
import { map } from 'rxjs/operators';

const headers = new HttpHeaders().set('Accept', 'application/json');

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  $authenticationState = new BehaviorSubject<boolean>(false);

  constructor(private http: HttpClient, private location: Location) {
  }

  getUser(): Observable<User> {
    return this.http.get<User>(`${environment.apiUrl}/user`, {headers}).pipe( // <1>
      map((response: User) => {
        this.$authenticationState.next(true);
        return response;
      })
    );
  }

  isAuthenticated(): Promise<boolean> {
    return this.getUser().toPromise().then((user: User) => {
      return user !== undefined;
    }).catch(() => {
      return false;
    })
  }

  login(): void { // <2>
    location.href = `${location.origin}${this.location.prepareExternalUrl('oauth2/authorization/okta')}`;
  }

  logout(): void { // <3>
    const redirectUri = `${location.origin}${this.location.prepareExternalUrl('/')}`;

    this.http.post(`${environment.apiUrl}/api/logout`, {}).subscribe((response: any) => {
      location.href = response.logoutUrl + '?id_token_hint=' + response.idToken
        + '&post_logout_redirect_uri=' + redirectUri;
    });
  }
}
