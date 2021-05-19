import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import * as Base64 from 'js-base64';
import { Advert } from 'src/app/models/advert/advert';
import { BackendService } from 'src/app/services/backend/backend.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  templateUrl: './search-page.component.html',
  styleUrls: ['./search-page.component.scss'],
})
export class SearchPageComponent implements OnInit {
  constructor(
    private backendService: BackendService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  advertList!: Advert[];
  advertListLoaded!: Promise<boolean>;
  searchQuery = '';
  searchFailed = false;
  failedMsg!: string;

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.searchQuery = Base64.decode(params['query']);
      this.searchFailed = false;
      this.advertList = [];
      this.advertListLoaded = Promise.resolve(false);
      if (this.searchQuery) {
        this.backendService.getAdvertByTiitle(this.searchQuery).subscribe(
          (data) => {
            this.advertList = data;
            this.advertListLoaded = Promise.resolve(true);
          },
          (error: HttpErrorResponse) => {
            this.searchFailed = true;
            if (error.status === 404) {
              this.failedMsg = 'No results for your search';
            } else {
              this.failedMsg = 'Could not fetch results';
            }
          }
        );
      } else {
        void this.router.navigate(['not-found']);
      }
    });
  }
}
