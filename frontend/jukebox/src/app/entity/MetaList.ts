import {MediaMeta} from './MediaMeta';

export class MetaList {
  size: number;
  queue: MediaMeta[];

  constructor() {
    this.size = 0;
    this.queue = [];
  }
}
