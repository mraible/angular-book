import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { AuthHttpInterceptor, AuthModule } from '@auth0/auth0-angular';
import { environment } from '../environments/environment';

const config = {
  domain: 'dev-06bzs1cu.us.auth0.com',
  clientId: '0ayARxMTmoYRKteiY92YDOwjV9rfz5iK',
  authorizationParams: {
    audience: 'https://dev-06bzs1cu.us.auth0.com/api/v2/',
    redirect_uri: window.location.origin + '/home'
  },
  httpInterceptor: {
    allowedList: [`${environment.apiUrl}/*`]
  },
};

const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  {
    path: 'home',
    component: HomeComponent
  }
];

@NgModule({
  declarations: [
    HomeComponent
  ],
  imports: [
    CommonModule,
    HttpClientModule,
    AuthModule.forRoot(config),
    RouterModule.forRoot(routes)
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthHttpInterceptor, multi: true }
  ],
  exports: [RouterModule]
})
export class AuthRoutingModule { }
