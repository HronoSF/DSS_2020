import {ChangeDetectorRef, Component, HostListener, OnInit} from '@angular/core';
import {AuthService} from '../vk-services/auth.service';
import {vkOpenAuthDialogURL} from '../const';
import {ActivatedRoute} from '@angular/router';
import {forkJoin, throwError, zip} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {FormControl, FormGroup} from '@angular/forms';
import {CommunitiesService} from '../vk-services/communities.service';
import {VkGroupsResponse} from '../vk-services/insterfaces/vkGroupsResponse';
import {VkUsersResponse} from '../vk-services/insterfaces/vkUsersResponse';
import {VkCommonResponse} from '../vk-services/insterfaces/VkCommonResponse';
import {PageEvent} from '@angular/material/paginator';
import {MatDialog} from '@angular/material/dialog';
import {ConfirmationComponent} from './confiramtion/confirmation/confirmation.component';
import {MatCheckboxChange} from '@angular/material/checkbox';

@Component({
  selector: 'app-admin-component',
  templateUrl: './admin-component.component.html',
  styleUrls: ['./admin-component.component.css']
})
export class AdminComponentComponent implements OnInit {
  public searchForm: FormGroup;
  public groupsResult: VkGroupsResponse[] = [];
  public usersResult: VkUsersResponse[] = [];
  public groupsResultCount: number;
  public usersResultCount: number;
  public groupsOffset = 0;
  public usersOffset = 0;
  public readonly count = 20;

  constructor(
    public dialog: MatDialog,
    public communitiesService: CommunitiesService,

    private cd: ChangeDetectorRef,
    private authService: AuthService,
    private activatedRoute: ActivatedRoute,
  ) {
    this.searchForm = new FormGroup({
      query: new FormControl('')
    });
  }

  private vkAuthPath = vkOpenAuthDialogURL;

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
        [this.communitiesService.searchCommunities(this.searchForm.value, this.groupsOffset, this.count ),
          this.communitiesService.searchUsers(this.searchForm.value, this.usersOffset, this.count )])
      .subscribe(([groups, users]: [VkCommonResponse<VkGroupsResponse>, VkCommonResponse<VkUsersResponse>]) => {
        console.log(groups, users);
        this.groupsResult = groups.response.items.filter(item => item.is_closed === 0);
        this.groupsResultCount = groups.response.count;
        this.usersResult = users.response.items;
        this.usersResultCount = users.response.count;
      });
    }
  }
  @HostListener('keyup.enter') onPressEnter() {
    this.search();
  }

  public onCheckGroup(event: MatCheckboxChange, id: number) {
    this.communitiesService.chooseGroups(id, this.groupsResult, event.checked);
  }

  public onCheckUser(event: MatCheckboxChange, id: number) {
    this.communitiesService.chooseUsers(id, this.usersResult, event.checked);
  }

  public onGroupsPageChange(event: PageEvent) {
    this.groupsOffset = event.pageIndex * this.count;
    console.log(this.groupsOffset);
    this.communitiesService.searchCommunities(this.searchForm.value, this.groupsOffset, this.count).subscribe(response => {
      this.groupsResult = response.response.items.filter(item => item.is_closed === 0);
    });
  }

  public onUsersPageChange(event: PageEvent) {
    this.usersOffset = event.pageIndex * this.count;
    this.communitiesService.searchUsers(this.searchForm.value, this.usersOffset, this.count).subscribe(response => {
      this.usersResult = response.response.items;
    });
  }

  public openConfirmationDialog() {
      const dialogRef = this.dialog.open(ConfirmationComponent, {
        minWidth: '70%',
      });

      dialogRef.afterClosed().subscribe(result => {
        if (result) {
          // this.communitiesService.startCrawling().subscribe(res => console.log(res));
          this.communitiesService.testGrpc().subscribe(res => console.log(res));
        }
        console.log(`Dialog result: ${result}`);
      });
  }

  public isUserChecked(id: number): boolean {
    return !!this.communitiesService.chosenUsers.find(user => user.id === id);
  }

  public isGroupChecked(id: number): boolean {
    return !!this.communitiesService.chosenGroups.find(group => group.id === id);
  }
}
