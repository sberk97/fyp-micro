import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Advert } from 'src/app/models/advert/advert';
import { BackendService } from 'src/app/services/backend/backend.service';
import { JWTTokenService } from 'src/app/services/jwt/jwt.token.service';

@Component({
  templateUrl: './advert-page.component.html',
  styleUrls: ['./advert-page.component.scss'],
})
export class AdvertPageComponent implements OnInit, OnDestroy {
  private subscription: Subscription = new Subscription();
  public advertLoaded!: Promise<boolean>;

  loadingFailed = false;
  failedMsg = '';
  id!: number;
  advert!: Advert;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private backendService: BackendService,
    public jwtTokenService: JWTTokenService
  ) {}

  ngOnInit(): void {
    this.getIdFromPath();

    this.getAdvert();
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  private getIdFromPath(): void {
    this.subscription.add(
      this.route.params.subscribe((params) => {
        this.id = params['id'] as number;
      })
    );
  }

  private getAdvert(): void {
    this.subscription.add(
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
      )
    );
  }
}
