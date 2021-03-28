import { Component, OnInit } from '@angular/core';
import {NowPlayingService} from '../service/now-playing.service';
import {PlayStreamService} from '../service/play-stream.service';

@Component({
  selector: 'app-play-stream',
  templateUrl: './play-stream.component.html',
  styleUrls: ['./play-stream.component.scss']
})
export class PlayStreamComponent implements OnInit {

  constructor(private nowPlayingService: NowPlayingService, private playStreamService: PlayStreamService) { }

  ngOnInit(): void {
  }

  play(url: string): void {
    this.playStreamService.play(url)
      .subscribe( ret => {
        // No action needed.
      });
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
}
