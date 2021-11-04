import { Component } from '@angular/core';
import { OktaAuthStateService } from '@okta/okta-angular';
import { OktaAuth } from '@okta/okta-auth-js';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'Notes';
  isCollapsed = true;

  constructor(public oktaAuth: OktaAuth, public authService: OktaAuthStateService) {
  }
}
