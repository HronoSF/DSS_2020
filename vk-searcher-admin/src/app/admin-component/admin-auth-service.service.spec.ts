import { TestBed } from '@angular/core/testing';

import { AdminAuthServiceService } from './admin-auth-service.service';

describe('AdminAuthServiceService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: AdminAuthServiceService = TestBed.get(AdminAuthServiceService);
    expect(service).toBeTruthy();
  });
});
