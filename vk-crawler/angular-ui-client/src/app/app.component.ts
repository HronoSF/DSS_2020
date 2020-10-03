import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup} from '@angular/forms';
import {SearchService} from './app.service';
import {ObjectToRelation, SearchResponse, WallPost} from './proto-gen/search_pb';
import {catchError} from 'rxjs/operators';
import {throwError} from 'rxjs';
import {Edge, Node} from '@swimlane/ngx-graph';

declare var VK;

interface GraphData {
  edges: Edge[];
  graphNodes: Node[];
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  title = 'Search';
  searchForm: FormGroup;
  response: SearchResponse.AsObject;
  graphsData: GraphData[] = [];

  page: number;
  size: number;

  constructor(private searchService: SearchService) {
    this.searchForm = new FormGroup({
      texttosearch: new FormControl('')
    });
  }

  ngOnInit() {
    VK.init({
      apiId: '7591763'
    });
  }

  search() {
    this.searchService.search(this.searchForm.value.texttosearch)
      .pipe(catchError(error => {
          return throwError(error);
        }
        )
      ).subscribe((result) => {
      console.log(result);
      this.response = result;
      this.graphsData = this.getGraph();
      console.log(this.graphsData);
    });
  }

  getGraph(): GraphData[] {
    const graphData: GraphData[] = [];
    this.response.contentList.forEach((wallPost: WallPost.AsObject) => {
      const links: Edge[] = [];
      const nodes: Node[] = [{id: 'author', label: 'author'}];
      wallPost.relationmapList.forEach((relation: ObjectToRelation.AsObject, index) => {
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

      graphData.push({edges: links, graphNodes: nodes});
    });
    return graphData;
  }

}
