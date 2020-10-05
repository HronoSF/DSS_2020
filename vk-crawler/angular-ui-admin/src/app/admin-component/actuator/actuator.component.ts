import {Component, OnDestroy, OnInit} from '@angular/core';
import {ThemePalette} from '@angular/material';
import {CommunitiesService} from '../../vk-services/communities.service';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-actuator',
  templateUrl: './actuator.component.html',
  styleUrls: ['./actuator.component.css']
})
export class ActuatorComponent implements OnInit, OnDestroy {

  color: ThemePalette = 'primary';
  bufferValue = 75;

  statusesSub: Subscription;

  constructor(public communitiesService: CommunitiesService) { }

  ngOnInit() {
    this.statusesSub = this.communitiesService.updateCrawlerStatus().subscribe();
  }

  ngOnDestroy() {
    this.statusesSub.unsubscribe();
  }

}
