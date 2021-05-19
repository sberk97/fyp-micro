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

  private priceOrder = false;
  private titleOrder = false;

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

  onSubmit(): void {
    if (this.searchQuery.length > 0) {
      void this.router.navigate(['search/' + Base64.encode(this.searchQuery)]);
    }
  }

  sortPrice(): void {
    this.priceOrder = !this.priceOrder;
    this.advertList.sort((a, b) => {
      return this.sort(a.price, b.price, this.priceOrder);
    });
  }

  sortTitle(): void {
    this.titleOrder = !this.titleOrder;
    this.advertList.sort((a, b) => {
      return this.sort(a.title, b.title, this.titleOrder);
    });
  }

  sort(a: any, b: any, ascending: boolean): number {
    if (a < b) {
      if (ascending) {
        return -1;
      } else {
        return 1;
      }
    } else if (a > b) {
      if (ascending) {
        return 1;
      } else {
        return -1;
      }
    }
    return 0;
  }
}
