import {MusicResourceLocator} from './MusicResourceLocator';

export class QueueList {
  size: number;
  queue: MusicResourceLocator[];

  constructor() {
    this.size = 0;
    this.queue = [];
  }
}
