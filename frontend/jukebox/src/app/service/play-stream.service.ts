import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {MusicResourceLocator} from '../entity/MusicResourceLocator';

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

}
