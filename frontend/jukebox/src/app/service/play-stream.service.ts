import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {MusicResourceLocator} from '../entity/MusicResourceLocator';
import {RadioStations} from '../entity/RadioStations';

@Injectable({
  providedIn: 'root'
})
export class PlayStreamService {

  constructor(private http: HttpClient) { }

  play(mrl: string): Observable<any> {
    const locator = new MusicResourceLocator();
    locator.mrl = mrl;
    return this.http.post<MusicResourceLocator>('/api/radio/play', locator);
  }

  list(): Observable<RadioStations> {
    return this.http.get<RadioStations>('/api/radio/list');
  }

  stop(): Observable<any> {
    return this.http.get('/api/radio/stop');
  }

}
