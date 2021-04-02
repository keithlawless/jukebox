import { Component, OnInit } from '@angular/core';
import {FolderService} from '../service/folder.service';
import {Folder} from '../entity/Folder';
import {SongQueueService} from '../service/song-queue.service';
import {Breadcrumb} from '../entity/Breadcrumb';

@Component({
  selector: 'app-add-songs',
  templateUrl: './add-songs.component.html',
  styleUrls: ['./add-songs.component.scss']
})
export class AddSongsComponent implements OnInit {

  public folder: Folder;
  public origFolderMrl: string;
  public breadcrumbs: Breadcrumb[];

  constructor(private folderService: FolderService, private songQueueService: SongQueueService) {
    this.folder = new Folder();
    this.origFolderMrl = '';
    this.breadcrumbs = [];
    this.folderService.list()
      .subscribe( res => {
        this.folder = res;
        this.origFolderMrl = this.folder.mrl;
        const home = new Breadcrumb('Home', this.folder.mrl);
        this.breadcrumbs.push(home);
      });
  }

  ngOnInit(): void {
  }

  refreshFolder(entryPoint: string): void {
    this.folderService.list(entryPoint)
      .subscribe( res => {
        this.folder = res;
      });
  }

  displayFolder(mrl: string): string {
    let lastItem = decodeURIComponent(mrl);
    lastItem = lastItem.substring(0, lastItem.length - 1);
    lastItem = lastItem.substring(lastItem.lastIndexOf('/') + 1);
    lastItem = this.replaceAll(lastItem, '%20', ' ');
    lastItem = this.replaceAll(lastItem, '%E2%80%99', '\'');
    lastItem = this.replaceAll(lastItem, '%C2%BF', '?');
    lastItem = this.replaceAll(lastItem, '%26', '&');
    return lastItem;
  }

  displaySong(mrl: string): string {
    let lastItem = decodeURIComponent(mrl);
    lastItem = lastItem.substring(lastItem.lastIndexOf('/') + 1);
    lastItem = this.replaceAll(lastItem, '%20', ' ');
    lastItem = this.replaceAll(lastItem, '%E2%80%99', '\'');
    lastItem = this.replaceAll(lastItem, '%C2%BF', '?');
    lastItem = this.replaceAll(lastItem, '%26', '&');
    return lastItem;
  }

  private replaceAll(s: string, search: string, replace: string): string {
    return s.split(search).join(replace);
  }

  showUpFolder(): boolean {
    // If we are displaying the root/original folder, then don't show an "up" link.
    if (this.folder.mrl === this.origFolderMrl) {
      return false;
    }
    return true;
  }

  upFolder(): void {
    let upFolder = this.folder.mrl.substring(0, this.folder.mrl.length - 1);
    upFolder = upFolder.substring(0, (upFolder.lastIndexOf('/') + 1));
    this.breadcrumbs.pop();
    this.refreshFolder(encodeURIComponent(upFolder));
  }

  downFolder(entryPoint: string): void {
    const breadcrumb = new Breadcrumb(this.displayFolder(entryPoint), entryPoint);
    this.breadcrumbs.push(breadcrumb);
    this.refreshFolder(entryPoint);
  }

  jumpFolder(entryPoint: string): void {
    // "Back Up" the breadcrumbs as necessary.
    let lastItem: Breadcrumb | undefined = this.breadcrumbs[this.breadcrumbs.length - 1];
    while (lastItem !== undefined && lastItem.url !== entryPoint) {
      this.breadcrumbs.pop();
      lastItem = this.breadcrumbs[this.breadcrumbs.length - 1];
    }
    this.refreshFolder(entryPoint);
  }

  queueSong(mrl: string): void {
    this.songQueueService.queue(mrl)
      .subscribe( ret => {
      });
  }

  queueAll(): void {
    this.songQueueService.queuemany(this.folder.files)
      .subscribe(ret => {
      });
  }
}
