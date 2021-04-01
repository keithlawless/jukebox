export class MediaMeta {
  mrl: string;
  artist: string;
  album: string;
  title: string;
  duration: number;
  elapsedTime: number;
  playState: string;

  constructor() {
    this.mrl = '';
    this.artist = '';
    this.album = '';
    this.title = '';
    this.duration = 0;
    this.elapsedTime = 0;
    this.playState = '';
  }
}
