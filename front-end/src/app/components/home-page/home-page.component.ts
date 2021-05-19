import { Component, OnInit } from '@angular/core';
import { Advert } from 'src/app/models/advert/advert';
import { BackendService } from 'src/app/services/backend/backend.service';

@Component({
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.scss'],
})
export class HomePageComponent implements OnInit {
  advertList!: Advert[];
  advertListLoaded!: Promise<boolean>;

  constructor(private backendService: BackendService) {}

  ngOnInit(): void {
    this.backendService.getLastNAdverts(5).subscribe((response: Advert[]) => {
      this.advertList = response;
      this.advertListLoaded = Promise.resolve(true);
    });
  }
}
