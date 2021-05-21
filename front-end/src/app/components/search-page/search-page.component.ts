import { Router } from '@angular/router';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import * as Base64 from 'js-base64';
import { Advert } from 'src/app/models/advert/advert';
import { BackendService } from 'src/app/services/backend/backend.service';
import { HttpErrorResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';

@Component({
  templateUrl: './search-page.component.html',
  styleUrls: ['./search-page.component.scss'],
})
export class SearchPageComponent implements OnInit, OnDestroy {
  private subscription: Subscription = new Subscription();

  advertList!: Advert[];
  advertListLoaded!: Promise<boolean>;
  searchQuery = '';
  searchFailed = false;
  failedMsg!: string;

  constructor(
    private backendService: BackendService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.searchQuery = Base64.decode(params['query']);
      this.setVariablesToDefault();
      if (this.searchQuery) {
        this.getAdvertByTitle();
      } else {
        void this.router.navigate(['not-found']);
      }
    });
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  private getAdvertByTitle(): void {
    this.subscription.add(
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
      )
    );
  }

  private setVariablesToDefault(): void {
    this.searchFailed = false;
    this.advertList = [];
    this.advertListLoaded = Promise.resolve(false);
  }
}
