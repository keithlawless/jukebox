import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule} from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ActionBarComponent } from './action-bar/action-bar.component';
import { NowPlayingComponent } from './now-playing/now-playing.component';
import { SongQueueComponent } from './song-queue/song-queue.component';
import { HomeComponent } from './home/home.component';
import { AddSongsComponent } from './add-songs/add-songs.component';
import { FooterComponent } from './footer/footer.component';
import { PlayStreamComponent } from './play-stream/play-stream.component';
import { SearchComponent } from './search/search.component';

@NgModule({
  declarations: [
    AppComponent,
    ActionBarComponent,
    NowPlayingComponent,
    SongQueueComponent,
    HomeComponent,
    AddSongsComponent,
    FooterComponent,
    PlayStreamComponent,
    SearchComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
