import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomeComponent} from './home/home.component';
import {AddSongsComponent} from './add-songs/add-songs.component';
import {PlayStreamComponent} from './play-stream/play-stream.component';
import {SearchComponent} from './search/search.component';

const routes: Routes = [
  {path: '', pathMatch: 'full', redirectTo: 'home'},
  {path: 'home', component: HomeComponent},
  {path: 'add-songs', component: AddSongsComponent},
  {path: 'play-stream', component: PlayStreamComponent},
  {path: 'search', component: SearchComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
