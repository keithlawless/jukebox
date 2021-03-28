import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {MetaList} from '../entity/MetaList';

@Injectable({
  providedIn: 'root'
})
export class FolderService {

  constructor(private http: HttpClient) { }

  list(entryPoint: string = ''): Observable<any> {
    if (entryPoint === '') {
      return this.http.get<MetaList>('/api/folder/list');
    }
    else {
      return this.http.get<MetaList>('/api/folder/list?entryPoint=' + entryPoint);
    }
  }
}
