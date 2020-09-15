import {Component, HostListener, OnInit} from '@angular/core';
import {AuthService} from '../vk-services/auth.service';
import {vkOpenAuthDialogURL} from '../const';
import {ActivatedRoute} from '@angular/router';
import {throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {FormControl, FormGroup} from "@angular/forms";
import {CommunitiesService} from "../vk-services/communities.service";
import {VkGroupsResponse} from "../vk-services/insterfaces/vkGroupsResponse";

@Component({
  selector: 'app-admin-component',
  templateUrl: './admin-component.component.html',
  styleUrls: ['./admin-component.component.css']
})
export class AdminComponentComponent implements OnInit {
  vkAuthPath = vkOpenAuthDialogURL;
  searchForm: FormGroup;
  groupsResult: VkGroupsResponse[] = [];


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
      this.communitiesService.searchCommunities(this.searchForm.value).subscribe((res: VkGroupsResponse[]) => {
        console.log(res);
        this.groupsResult = res;
      });
    }
  }
  @HostListener('keyup.enter') onPressEnter() {
    this.search();
  }

  public onCheck(id: number) {

  }
}
