import { Routes } from '@angular/router';
import { NoteListComponent } from './note-list/note-list.component';
import { NoteEditComponent } from './note-edit/note-edit.component';
import { OktaAuthGuard } from '@okta/okta-angular';

export const NOTE_ROUTES: Routes = [
  {
    path: 'notes',
    component: NoteListComponent,
    canActivate: [OktaAuthGuard]
  },
  {
    path: 'notes/:id',
    component: NoteEditComponent,
    canActivate: [OktaAuthGuard]
  }
];
