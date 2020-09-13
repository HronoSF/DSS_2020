import { Injectable, Inject, InjectionToken } from '@angular/core';
import { StorageService } from 'ngx-webstorage-service';

export const SESSION_STORAGE_SERVICE =
  new InjectionToken<StorageService>('SESSION_STORAGE_SERVICE');

@Injectable({
  providedIn: 'root'
})
export class SessionService {

  constructor(@Inject(SESSION_STORAGE_SERVICE) private storage: StorageService) { }

  private saveSessionID(val) {
    this.storage.set('id', val);
  }
  private saveSessionStatus(val) {
    this.storage.set('isLoggedIn', val);
  }
  public getSessionID() {
    return this.storage.get('id');
  }
  public getSessionStatus() {
    return this.storage.get('isLoggedIn');
  }
  public createSession(id, status) {
    this.saveSessionID(id);
    this.saveSessionStatus(status);
  }
  public destroySession() {
    this.storage.clear();
  }

}
