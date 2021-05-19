import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { BackendService } from 'src/app/services/backend/backend.service';
import { JWTTokenService } from 'src/app/services/jwt/jwt.token.service';

@Component({
  selector: 'app-advert-manage-buttons',
  templateUrl: './advert-manage-buttons.component.html',
  styleUrls: ['./advert-manage-buttons.component.scss'],
})
export class AdvertManageButtonsComponent implements OnInit {
  @Input() advertUserId!: number;
  @Input() advertId!: number;
  @Input() shouldGoToHomePage = false;

  shouldBeVisible!: boolean;

  constructor(
    public backendService: BackendService,
    public jwtTokenService: JWTTokenService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.shouldBeVisible =
      this.jwtTokenService.getUserId() == this.advertUserId.toString() ||
      this.jwtTokenService.getRoles() == 'ROLE_ADMIN';
  }

  deleteAdvert(): void {
    this.backendService.deleteAdvertById(this.advertId).subscribe(() => {
      if (!this.shouldGoToHomePage) {
        window.location.reload();
      } else {
        void this.router.navigate(['/']);
      }
    });
  }

  editAdvert(): void {}
}
