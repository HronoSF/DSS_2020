import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup} from '@angular/forms';
import {SearchService} from '../app.service';
import {IdSearchResponseDTO, ObjectToRelation, TextSearchResponseDTO, WallPost} from '../proto-gen/search_pb';
import {catchError, switchMap} from 'rxjs/operators';
import {throwError, zip} from 'rxjs';
import {Edge, Node} from '@swimlane/ngx-graph';
import {ActivatedRoute} from '@angular/router';
import {AuthService} from '../auth.service';
import {vkOpenAuthDialogURL} from '../const';
import {VkGroupsResponse, VkSearchService, VkUsersResponse} from '../vk-search.service';

interface GraphData {
  edges: Edge[];
  graphNodes: Node[];
}

@Component({
  selector: 'app-root',
  templateUrl: './client.component.html',
  styleUrls: ['./client.component.css']
})
export class ClientComponent implements OnInit {
  title = 'Search';
  searchForm: FormGroup;
  textResponse: any;
  idResponse: any;
  graphsData: {relation: string, objects: string[]}[];

  groupIds: string[] = [];
  userIds: string[] = [];

  groupsMap: {[key: string]: VkGroupsResponse} = {};
  usersMap: {[key: string]: VkUsersResponse} = {};

  showTextResponse = false;
  showIdResponse = false;

  page = 1;
  size = 20;

  vkOpenAuthURL = vkOpenAuthDialogURL;



  constructor(
    private searchService: SearchService,
    private activatedRoute: ActivatedRoute,
    private authService: AuthService,
    private vkSearchService: VkSearchService) {
    this.searchForm = new FormGroup({
      texttosearch: new FormControl(''),
      searchWithId: new FormControl('')
    });
  }

  ngOnInit() {
    const vkCode = this.activatedRoute.snapshot.queryParams.code;
    console.log(vkCode);
    if (vkCode && vkCode !== '') {
      this.authService.getAccessToken(vkCode).pipe(catchError(error => {
        return throwError(error);
      })).subscribe(vkAccessResponse => console.log(vkAccessResponse));
    } else {
      window.location.href = this.vkOpenAuthURL;
    }
  }

  search(event: string) {
    if (event === 'next') {
      this.page += 1;
    }
    if (event === 'prev' && this.page > 1) {
      this.page -= 1;
    }
    if (this.searchForm.value.searchWithId) {
      this.searchWithId();
    } else {
      this.searchWithText(this.page, this.size);
    }
  }

  searchWithText(page: number, size: number) {
    this.searchService.searchWithText(this.searchForm.value.texttosearch, page, size)
      .pipe(
        switchMap((result) => {
          console.log(result);
          this.textResponse = result;
          this.textResponse.contentList.forEach(resp => {
            resp.relationmapList = this.getGraphs(resp.relationmapList);
          });
          console.log(this.textResponse);
          this.getUsersAndGroupsIds(result.contentList);
          return zip(this.vkSearchService.getGroups(this.groupIds.join(',')), this.vkSearchService.getUsers(this.userIds.join(',')));
        }  ),
        catchError(error => {
          return throwError(error);
        }
        )
      ).subscribe(([groupsMap, usersMap]) => {
        this.groupsMap = groupsMap;
        this.usersMap = usersMap;
        this.idResponse = null;
        this.showTextResponse = true;
        this.showIdResponse = false;
    });
  }

  searchWithId() {
    const id = '/id' + this.searchForm.value.texttosearch;

    this.searchService.searchWithId(id)
      .pipe(
        switchMap(result => {
          console.log(result);
          this.userIds = [];
          this.groupIds = [];
          this.idResponse = result;
          console.log(result);
          this.idResponse.relationmapList = this.getGraphs(this.idResponse.relationmapList);
          if (result.fromid.toString().includes('-')) {
            this.groupIds.push(result.fromid.toString().replace('-', ''));
          } else {
            this.userIds.push(result.fromid.toString());
          }
          return zip(this.vkSearchService.getGroups(this.groupIds.join(',')), this.vkSearchService.getUsers(this.userIds.join(',')));
        }),
        catchError(error => {
          return throwError(error);
        }
        )
      ).subscribe(([groupsMap, usersMap]) => {
        console.log(groupsMap, usersMap);
        this.groupsMap = groupsMap;
        this.usersMap = usersMap;
        this.textResponse = null;
        this.showTextResponse = false;
        this.showIdResponse = true;
    });
  }

  getGraphFormIdResponse(response: IdSearchResponseDTO.AsObject): GraphData[] {
    const graphData: GraphData[] = [];
    graphData.push(this.getEdgesAndNodes(response.relationmapList));
    return graphData;
  }

  getGraphsFromTextResponse(response: TextSearchResponseDTO.AsObject): GraphData[] {
    const graphData: GraphData[] = [];

    response.contentList.forEach((wallPost: WallPost.AsObject) => {
      graphData.push(this.getEdgesAndNodes(wallPost.relationmapList));
    });
    return graphData;
  }

  getGraphs(response: ObjectToRelation.AsObject[]): {relation: string, objects: string[]}[] {
    const graph: {[key: string]: {relation: string, objects: string[]}} = {};
    response.forEach(rel => {
      if (!graph[rel.relation]) {
        graph[rel.relation] = {relation: rel.relation, objects: [rel.object]};
      } else {
        graph[rel.relation].objects.push(rel.object);
      }
    });
    return Object.values(graph);

  }

  getEdgesAndNodes(item: ObjectToRelation.AsObject[]) {
    const links: Edge[] = [];
    const nodes: Node[] = [{id: 'author', label: 'author'}];
    item.forEach((relation: ObjectToRelation.AsObject, index) => {
      links.push({
        id: `link${index}`,
        source: 'author',
        target: relation.object,
        label: relation.relation
      });

      nodes.push({
        id: relation.object,
        label: relation.object
      });
    });

    return {edges: links, graphNodes: nodes};
  }


  getUsersAndGroupsIds(result: WallPost.AsObject[]) {
    const groupsIds = [];
    const usersIds = [];
    result.forEach((post: WallPost.AsObject) => {
      if (post.fromid.toString().includes('-')) {
        groupsIds.push(post.fromid.toString().replace('-', ''));
      } else {
        usersIds.push(post.fromid.toString());
      }
    });
    this.groupIds = Array.from(new Set(groupsIds));
    this.userIds = Array.from(new Set(usersIds));
  }

  getPhoto(fromid) {
    if (fromid.toString().includes('-')) {
      return this.groupsMap[fromid.toString().replace('-', '')].photo_50;
    }
    return this.usersMap[fromid.toString()].photo;
  }

  getName(fromid) {
    if (fromid.toString().includes('-')) {
      return this.groupsMap[fromid.toString().replace('-', '')].name;
    }
    return this.usersMap[fromid.toString()].first_name + ' ' + this.usersMap[fromid.toString()].last_name;
  }
}
