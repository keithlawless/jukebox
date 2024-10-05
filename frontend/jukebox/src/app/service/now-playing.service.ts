import {Injectable, OnDestroy} from '@angular/core';
import {Observable, Subject, timer} from 'rxjs';
import {MediaMeta} from '../entity/MediaMeta';
import { HttpClient } from '@angular/common/http';
import {retry, share, switchMap, takeUntil} from 'rxjs/operators';
import {MusicResourceLocator} from '../entity/MusicResourceLocator';

@Injectable({
  providedIn: 'root'
})
export class NowPlayingService implements OnDestroy {

  private nowPlaying$: Observable<MediaMeta>;
  private stopPolling = new Subject();

  constructor(private http: HttpClient) {
    this.nowPlaying$ = timer(1, 2000).pipe(
      switchMap(() => http.get<MediaMeta>('/api/queue/playing')),
      retry(),
      share(),
      takeUntil(this.stopPolling)
    );
  }

  fetch(): Observable<MediaMeta> {
    return this.nowPlaying$;
  }

  pause(): Observable<any> {
    return this.http.get('/api/media/pause');
  }

  resume(): Observable<any> {
    return this.http.get('/api/media/resume');
  }

  stop(): Observable<any> {
    return this.http.get('/api/media/stop');
  }

  next(): Observable<MusicResourceLocator> {
    return this.http.get<MusicResourceLocator>('/api/media/next');
  }

  ngOnDestroy(): void {
    this.stopPolling.next();
  }
}
