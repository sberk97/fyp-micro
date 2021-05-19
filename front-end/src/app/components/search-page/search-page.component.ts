import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import * as Base64 from 'js-base64';
import { Advert } from 'src/app/models/advert/advert';
import { BackendService } from 'src/app/services/backend/backend.service';

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
  query!: string;

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.query = Base64.decode(params['query']);
      if (this.query) {
        this.backendService.getAdvertByTiitle(this.query).subscribe((data) => {
          this.advertList = data;
          this.advertListLoaded = Promise.resolve(true);
        });
      } else {
        void this.router.navigate(['not-found']);
      }
    });
  }
}
