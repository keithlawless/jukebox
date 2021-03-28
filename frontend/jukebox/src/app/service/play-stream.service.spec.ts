import { TestBed } from '@angular/core/testing';

import { PlayStreamService } from './play-stream.service';

describe('PlayStreamService', () => {
  let service: PlayStreamService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PlayStreamService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
