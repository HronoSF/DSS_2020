import {Component, OnInit} from '@angular/core';
import {vkAppID} from './const';

declare var VK;

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'vk-admin';

  ngOnInit(): void {
    VK.init({
      apiId: vkAppID,
    });
  }
}
