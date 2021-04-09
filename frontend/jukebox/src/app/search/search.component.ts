import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {fromEvent} from 'rxjs';
import {debounceTime, distinctUntilChanged, filter, map} from 'rxjs/operators';
import {SearchService} from '../service/search.service';
import {MediaMeta} from '../entity/MediaMeta';
import {SongQueueService} from '../service/song-queue.service';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})
export class SearchComponent implements OnInit {

  isSearching: boolean;
  searchResults: MediaMeta[];
  @ViewChild('searchTermInput', { static: true }) searchTermInput: ElementRef | undefined;

  constructor(private searchService: SearchService, private songQueueService: SongQueueService) {
    this.isSearching = false;
    this.searchResults = [];
  }

  ngOnInit(): void {
    if (this.searchTermInput !== undefined) {
      fromEvent(this.searchTermInput.nativeElement, 'keyup').pipe(
        // get value
        map((event: any) => {
          return event.target.value;
        })
        // if character length greater then 2
        , filter(res => res.length > 2)

        // Time in milliseconds between key events
        , debounceTime(1000)

        // If previous query is diffent from current
        , distinctUntilChanged()

        // subscription for response
      ).subscribe((text: string) => {

        this.isSearching = true;

        this.searchService.search(text).subscribe((res) => {
          console.log('res', res);
          this.isSearching = false;
          this.searchResults = res;
        }, (err) => {
          this.isSearching = false;
          console.log('error', err);
        });
      });
    }
  }

  queueSong(mrl: string): void {
    this.songQueueService.queue(mrl)
      .subscribe( ret => {
      });
  }

  imageURL(mrl: string): string {
    mrl = mrl.replace( /&/gi, '%26');
    return '/api/image/fetch?mrl=' + mrl;
  }
}
