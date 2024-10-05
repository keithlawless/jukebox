import {Injectable, OnDestroy} from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, timer, Subject } from 'rxjs';
import { switchMap, share, retry, takeUntil } from 'rxjs/operators';
import {MetaList} from '../entity/MetaList';
import {MusicResourceLocator} from '../entity/MusicResourceLocator';

@Injectable({
  providedIn: 'root'
})
export class SongQueueService implements OnDestroy {

  private songQueue$: Observable<MetaList>;
  private stopPolling = new Subject();

  constructor(private http: HttpClient) {
    this.songQueue$ = timer(1, 5000).pipe(
      switchMap(() => http.get<MetaList>('/api/queue/list')),
      retry(),
      share(),
      takeUntil(this.stopPolling)
    );
  }

  list(): Observable<MetaList> {
    return this.songQueue$;
  }

  ngOnDestroy(): void {
    this.stopPolling.next();
  }

  queue(mrl: string): Observable<any> {
    const locator = new MusicResourceLocator();
    locator.mrl = decodeURIComponent(mrl);
    return this.http.post<MusicResourceLocator>('/api/queue/add', locator);
  }

  queuemany(songList: string[]): Observable<any> {
    const mrlList: string[] = [];
    for (const song of songList) {
      mrlList.push(decodeURIComponent(song));
    }
    return this.http.post<string[]>('/api/queue/addmany', mrlList);
  }

  empty(): Observable<any> {
    return this.http.post('/api/queue/empty', {});
  }

}
