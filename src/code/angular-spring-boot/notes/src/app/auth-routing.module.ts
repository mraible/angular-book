import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { AuthHttpInterceptor, AuthModule } from '@auth0/auth0-angular';

const config = {
  domain: 'dev-06bzs1cu.us.auth0.com',
  clientId: 'y3RlJzTl68eZOQyGqA0yGiJla7fyaZ88',
  authorizationParams: {
    audience: 'https://dev-06bzs1cu.us.auth0.com/api/v2/',
    redirect_uri: window.location.origin + '/home'
  },
  httpInterceptor: {
    allowedList: ['http://localhost:8080/api/*']
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
