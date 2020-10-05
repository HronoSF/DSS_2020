import { Component, OnInit } from '@angular/core';
import {VkGroupsResponse} from '../../../vk-services/insterfaces/vkGroupsResponse';
import {VkUsersResponse} from '../../../vk-services/insterfaces/vkUsersResponse';
import {CommunitiesService} from '../../../vk-services/communities.service';
import {CrawlerJobStatusDTO} from '../../../proto-gen/crawler_pb';

@Component({
  selector: 'app-confirmation',
  templateUrl: './confirmation.component.html',
  styleUrls: ['./confirmation.component.css']
})
export class ConfirmationComponent implements OnInit {

  groups: VkGroupsResponse[];
  users: VkUsersResponse[];

  step = 1;
  result: CrawlerJobStatusDTO.AsObject;
  constructor(
    private communitiesService: CommunitiesService
  ) { }

  ngOnInit() {
    this.groups = this.communitiesService.chosenGroups;
    this.users = this.communitiesService.chosenUsers;
  }

  onConfirm() {
    this.communitiesService.startCrawling().subscribe(res => {
      console.log(res);
      this.result = res;
      this.step = 2;
    });
  }

}
