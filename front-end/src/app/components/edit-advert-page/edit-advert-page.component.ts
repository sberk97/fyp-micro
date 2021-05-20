import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Advert } from 'src/app/models/advert/advert';
import { BackendService } from 'src/app/services/backend/backend.service';
import { JWTTokenService } from 'src/app/services/jwt/jwt.token.service';

@Component({
  templateUrl: './edit-advert-page.component.html',
  styleUrls: ['./edit-advert-page.component.scss'],
})
export class EditAdvertPageComponent implements OnInit, OnDestroy {
  private routeSub!: Subscription;
  advertId!: number;
  failedMsg = '';
  operationFailed = false;
  advert!: Advert;
  advertLoaded!: Promise<boolean>;
  formFailedMsg = '';
  formFailed = false;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private backendService: BackendService,
    public jwtTokenService: JWTTokenService
  ) {}

  ngOnInit(): void {
    this.routeSub = this.route.params.subscribe((params) => {
      this.advertId = params['id'] as number;
    });

    if (!this.jwtTokenService.isTokenExpired()) {
      this.backendService.getAdvert(this.advertId).subscribe(
        (response: Advert) => {
          this.advert = response;
          this.determineOwner();
        },
        (error: HttpErrorResponse) => {
          this.operationFailed = true;
          if (error.status === 404) {
            // redirect to 404 page
            void this.router.navigate(['not-found']);
          } else {
            this.failedMsg = 'Fetching advert failed';
          }
        }
      );
    } else {
      void this.router.navigate(['/login']);
    }
  }

  ngOnDestroy(): void {
    this.routeSub.unsubscribe();
  }

  determineOwner(): void {
    if (this.jwtTokenService.getUserId() == this.advert.user_id.toString()) {
      this.advertLoaded = Promise.resolve(true);
    } else {
      this.operationFailed = true;
      this.failedMsg = 'You are not the owner of this advert';
    }
  }

  onSubmit(): void {
    const body = {
      title: this.advert.title,
      description: this.advert.description,
      contact_details: this.advert.contact_details,
      price: this.advert.price,
    };
    this.backendService.editAdvert(body, this.advertId).subscribe(
      (response: number) => {
        void this.router.navigate(['/advert/' + response.toString()]);
      },
      (error: HttpErrorResponse) => {
        this.formFailed = true;
        if (error.status === 400) {
          this.formFailedMsg = error.error as string;
        } else if (error.status === 401) {
          void this.router.navigate(['/login']);
        } else {
          this.formFailedMsg = 'Editing advert failed!';
        }
      }
    );
  }
}
