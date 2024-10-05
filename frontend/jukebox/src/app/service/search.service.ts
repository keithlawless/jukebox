import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {MediaMeta} from '../entity/MediaMeta';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SearchService {

  constructor(private http: HttpClient) { }

  search(term: string): Observable<MediaMeta[]> {
    return this.http.get<MediaMeta[]>('/api/search/all?searchTerm=' + term);
  }
}
