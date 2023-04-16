import { Routes } from '@angular/router';
import { NoteListComponent } from './note-list/note-list.component';
import { NoteEditComponent } from './note-edit/note-edit.component';
import { AuthGuard } from '@auth0/auth0-angular';

export const NOTE_ROUTES: Routes = [
  {
    path: 'notes',
    component: NoteListComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'notes/:id',
    component: NoteEditComponent,
    canActivate: [AuthGuard]
  }
];
