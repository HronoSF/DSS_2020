import { Component, OnInit } from '@angular/core';
import {VkGroupsResponse} from '../../../vk-services/insterfaces/vkGroupsResponse';
import {VkUsersResponse} from '../../../vk-services/insterfaces/vkUsersResponse';
import {CommunitiesService} from '../../../vk-services/communities.service';

@Component({
  selector: 'app-confirmation',
  templateUrl: './confirmation.component.html',
  styleUrls: ['./confirmation.component.css']
})
export class ConfirmationComponent implements OnInit {

  groups: VkGroupsResponse[];
  users: VkUsersResponse[];

  constructor(
    private communitiesService: CommunitiesService
  ) { }

  ngOnInit() {
    this.groups = this.communitiesService.chosenGroups;
    this.users = this.communitiesService.chosenUsers;
  }

}
