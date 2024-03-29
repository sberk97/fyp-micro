import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Advert } from 'src/app/models/advert/advert';
import { User } from 'src/app/models/user/user';
import { BackendService } from 'src/app/services/backend/backend.service';
import { JWTTokenService } from 'src/app/services/jwt/jwt.token.service';

@Component({
  templateUrl: './user-page.component.html',
  styleUrls: ['./user-page.component.scss'],
})
export class UserPageComponent implements OnInit, OnDestroy {
  private subscription: Subscription = new Subscription();

  userLoaded!: Promise<boolean>;
  userAdvertsLoaded!: Promise<boolean>;

  userLoadingFailed = false;
  userFailedMsg = '';
  advertsLoadingFailed = false;
  advertsFailedMsg = '';
  id!: number;
  user!: User;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private backendService: BackendService,
    public jwtTokenService: JWTTokenService
  ) {}

  ngOnInit(): void {
    this.getIdFromPath();

    this.getUser();
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

  private getUser(): void {
    this.subscription.add(
      this.backendService.getUser(this.id).subscribe(
        (response: User) => {
          this.user = response;
          this.userLoaded = Promise.resolve(true);
          this.getUserAdverts();
        },
        (error: HttpErrorResponse) => {
          this.userLoadingFailed = true;
          if (error.status === 404) {
            // redirect to 404 page
            void this.router.navigate(['not-found']);
          } else {
            this.userFailedMsg = 'Fetching user failed';
          }
        }
      )
    );
  }

  private getUserAdverts(): void {
    this.subscription.add(
      this.backendService.getUserAdverts(this.user.id).subscribe(
        (response: Advert[]) => {
          this.user.adverts = response;
          this.userAdvertsLoaded = Promise.resolve(true);
        },
        (error: HttpErrorResponse) => {
          this.advertsLoadingFailed = true;
          if (error.status === 404) {
            this.advertsFailedMsg = 'This user does not have any adverts';
          } else {
            this.advertsFailedMsg = 'Fetching adverts failed';
          }
        }
      )
    );
  }

  public deleteUser(): void {
    this.subscription.add(
      this.backendService.deleteUserById(this.user.id).subscribe(() => {
        this.subscription.add(
          this.backendService
            .deleteAdvertsByUserId(this.user.id)
            .subscribe(() => {
              void this.router.navigate(['/']);
            })
        );
      })
    );
  }
}
