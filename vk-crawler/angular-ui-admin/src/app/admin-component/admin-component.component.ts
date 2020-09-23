import {Component, HostListener, OnInit} from '@angular/core';
import {AuthService} from '../vk-services/auth.service';
import {vkOpenAuthDialogURL} from '../const';
import {ActivatedRoute} from '@angular/router';
import {forkJoin, throwError, zip} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {FormControl, FormGroup} from "@angular/forms";
import {CommunitiesService} from "../vk-services/communities.service";
import {VkGroupsResponse} from "../vk-services/insterfaces/vkGroupsResponse";
import {VkUsersResponse} from "../vk-services/insterfaces/vkUsersResponse";

@Component({
  selector: 'app-admin-component',
  templateUrl: './admin-component.component.html',
  styleUrls: ['./admin-component.component.css']
})
export class AdminComponentComponent implements OnInit {
  public searchForm: FormGroup;
  public groupsResult: VkGroupsResponse[] = [];
  public choosenGroups: VkGroupsResponse[] = [];
  public usersResult: VkUsersResponse[] = [];

  private vkAuthPath = vkOpenAuthDialogURL;

  constructor(private authService: AuthService, private activatedRoute: ActivatedRoute, private communitiesService: CommunitiesService) {
    this.searchForm = new FormGroup({
      query: new FormControl('')
    });
  }

  ngOnInit() {
    const vkCode = this.activatedRoute.snapshot.queryParams.code;
    if (vkCode && vkCode !== '') {
      this.authService.getAccessToken(vkCode).pipe(catchError(error => {
        return throwError(error);
      })).subscribe(vkAccessResponse => console.log(vkAccessResponse));
    } else {
      window.location.href = this.vkAuthPath;
    }
  }

  public search(): void {
    if (this.searchForm.value.query !== '') {
      forkJoin(
        [this.communitiesService.searchCommunities(this.searchForm.value),this.communitiesService.searchUsers(this.searchForm.value)])
      .subscribe(([groups, users]: [VkGroupsResponse[], VkUsersResponse[]]) => {
        console.log(groups, users);
        this.groupsResult = groups.filter(item => item.is_closed === 0);
        this.usersResult = users;
      });
    }
  }
  @HostListener('keyup.enter') onPressEnter() {
    this.search();
  }

  public onCheck(id: number) {
    this.choosenGroups.push(this.groupsResult.find(item => item.id === +id));
    console.log(this.choosenGroups);
  }
}
