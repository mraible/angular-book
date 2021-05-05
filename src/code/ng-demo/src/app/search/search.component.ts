import { Component, OnInit } from '@angular/core';
import { Person, SearchService } from '../shared';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {
  query!: string;
  searchResults: Array<Person> = [];

  constructor(private searchService: SearchService) { }

  ngOnInit(): void { }

  search(): void {
    this.searchService.search(this.query).subscribe(
      (data: any) => { this.searchResults = data; },
      error => console.log(error)
    );
  }
}
