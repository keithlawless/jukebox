import { Component, OnInit } from '@angular/core';
import {MediaMeta} from '../entity/MediaMeta';
import {NowPlayingService} from '../service/now-playing.service';
import {MusicResourceLocator} from '../entity/MusicResourceLocator';
import {SongQueueService} from '../service/song-queue.service';

@Component({
  selector: 'app-now-playing',
  templateUrl: './now-playing.component.html',
  styleUrls: ['./now-playing.component.scss']
})
export class NowPlayingComponent implements OnInit {

  public nowPlaying: MediaMeta;
  public depth: number;

  constructor(private nowPlayingService: NowPlayingService, private songQueueService: SongQueueService) {
    this.nowPlaying = new MediaMeta();
    this.depth = 0;
  }

  ngOnInit(): void {
    this.nowPlayingService.fetch()
      .subscribe( res => {
        this.nowPlaying = res;
      });

    this.songQueueService.list()
      .subscribe( res => {
        this.depth = res.size;
      });
  }

  imageURL(mrl: string): string {
    mrl = mrl.replace(/&/gi, '%26');
    return '/api/image/fetch?mrl=' + mrl;
  }

  isPlaying(): boolean {
    if (this.nowPlaying.playState === 'PLAYING') {
      return true;
    }
    return false;
  }

  isPaused(): boolean {
    if (this.nowPlaying.playState === 'PAUSED') {
      return true;
    }
    return false;
  }

  isStopped(): boolean {
    if (this.nowPlaying.playState === 'STOPPED') {
      return true;
    }
    return false;
  }

  resume(): void {
    this.nowPlayingService.resume()
      .subscribe( ret => {
        // No action needed.
      });
  }

  pause(): void {
    this.nowPlayingService.pause()
      .subscribe( ret => {
        // No action needed.
      });
  }

  stop(): void {
    this.nowPlayingService.stop()
      .subscribe( ret => {
        // No action needed.
      });
  }

  next(): void {
    this.nowPlayingService.next()
      .subscribe( ret => {
        // No action needed.
      });
  }

  isRadio(): boolean {
    return !this.nowPlaying.mrl.startsWith('file');
  }

  formatDuration(): string {
    return this.formatTime(this.nowPlaying.duration);
  }

  formatElapsed(): string {
    return this.formatTime(this.nowPlaying.elapsedTime);
  }

  formatTime(time: number): string {
    let seconds = time / 1000;
    const minutes = Math.floor(seconds / 60 );
    seconds = Math.floor(seconds % 60);
    return minutes + ':' + zeroPad(seconds, 2);
  }

  formatProgress(): string {
    const progress = this.nowPlaying.elapsedTime / this.nowPlaying.duration * 100;
    return 'width: ' + zeroPad(progress, 2) + '%';
  }
}

const zeroPad = (num: number, places: number) => String(num).padStart(places, '0');
