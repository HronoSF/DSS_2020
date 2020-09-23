import { TestBed } from '@angular/core/testing';

import { CommunitiesService } from './communities.service';

describe('CommunitiesService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: CommunitiesService = TestBed.get(CommunitiesService);
    expect(service).toBeTruthy();
  });
});
