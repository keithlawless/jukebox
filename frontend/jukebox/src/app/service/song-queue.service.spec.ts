import { TestBed } from '@angular/core/testing';

import { SongQueueService } from './song-queue.service';

describe('SongQueueServiceService', () => {
  let service: SongQueueService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SongQueueService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
