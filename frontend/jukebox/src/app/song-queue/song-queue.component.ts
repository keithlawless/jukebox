import { Component, OnInit } from '@angular/core';
import {SongQueueService} from '../service/song-queue.service';
import {MetaList} from '../entity/MetaList';

@Component({
  selector: 'app-song-queue',
  templateUrl: './song-queue.component.html',
  styleUrls: ['./song-queue.component.scss']
})
export class SongQueueComponent implements OnInit {

  public metaList: MetaList;

  constructor(private songQueueService: SongQueueService ) {
    this.metaList = new MetaList();
  }

  ngOnInit(): void {
    this.songQueueService.list()
      .subscribe( res => {
        this.metaList = res;
      });
  }

  imageURL(mrl: string): string {
    mrl = mrl.replace( /&/gi, '%26');
    return '/api/image/fetch?mrl=' + mrl;
  }
}
