import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { BackendService } from 'src/app/services/backend/backend.service';
import { JWTTokenService } from 'src/app/services/jwt/jwt.token.service';

@Component({
  selector: 'app-advert-manage-buttons',
  templateUrl: './advert-manage-buttons.component.html',
  styleUrls: ['./advert-manage-buttons.component.scss'],
})
export class AdvertManageButtonsComponent implements OnInit, OnDestroy {
  private subscription: Subscription = new Subscription();

  @Input() advertUserId!: number;
  @Input() advertId!: number;
  @Input() shouldGoToHomePage = false;

  shouldBeVisible!: boolean;

  constructor(
    private backendService: BackendService,
    private jwtTokenService: JWTTokenService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.shouldBeVisible =
      this.jwtTokenService.getUserId() == this.advertUserId.toString() ||
      this.jwtTokenService.isAdmin();
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  public deleteAdvert(): void {
    this.subscription.add(
      this.backendService.deleteAdvertById(this.advertId).subscribe(() => {
        if (!this.shouldGoToHomePage) {
          window.location.reload();
        } else {
          void this.router.navigate(['/']);
        }
      })
    );
  }

  public editAdvert(): void {
    void this.router.navigate(['/edit-advert/' + this.advertId.toString()]);
  }
}
