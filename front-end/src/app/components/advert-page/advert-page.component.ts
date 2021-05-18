import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Advert } from 'src/app/models/advert/advert';
import { BackendService } from 'src/app/services/backend/backend.service';

@Component({
  templateUrl: './advert-page.component.html',
  styleUrls: ['./advert-page.component.scss'],
})
export class AdvertPageComponent implements OnInit, OnDestroy {
  private routeSub!: Subscription;
  advertLoaded!: Promise<boolean>;

  loadingFailed = false;
  failedMsg = '';
  id!: number;
  advert!: Advert;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private backendService: BackendService
  ) {}

  ngOnInit(): void {
    this.routeSub = this.route.params.subscribe((params) => {
      this.id = params['id'] as number;
    });

    this.backendService.getAdvert(this.id).subscribe(
      (response: Advert) => {
        this.advert = response;
        this.advertLoaded = Promise.resolve(true);
      },
      (error: HttpErrorResponse) => {
        this.loadingFailed = true;
        if (error.status === 404) {
          // redirect to 404 page
          void this.router.navigate(['not-found']);
        } else {
          this.failedMsg = 'Fetching advert failed';
        }
      }
    );
  }

  ngOnDestroy(): void {
    this.routeSub.unsubscribe();
  }
}
