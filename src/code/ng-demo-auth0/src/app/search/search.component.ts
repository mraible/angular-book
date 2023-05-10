import { Component } from '@angular/core';
import { Person, SearchService } from '../shared';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent {
  query!: string;
  searchResults: Person[] = [];

  constructor(private searchService: SearchService, private route: ActivatedRoute) { }

  ngOnInit(): void {
    const params = this.route.snapshot.params;
    if (params['term']) {
      this.query = decodeURIComponent(params['term']);
      this.search();
    }
  }

  search(): void {
    this.searchService.search(this.query).subscribe({
      next: (data: Person[]) => { this.searchResults = data },
      error: (e) => console.log(e)
    });
  }
}
