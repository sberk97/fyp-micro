import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Advert } from 'src/app/models/advert/advert';
import { BackendService } from 'src/app/services/backend/backend.service';

@Component({
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.scss'],
})
export class HomePageComponent implements OnInit {
  advertList!: Advert[];
  advertListLoaded!: Promise<boolean>;
  searchQuery!: string;

  constructor(private backendService: BackendService, private router: Router) {}

  ngOnInit(): void {
    this.backendService.getLastNAdverts(5).subscribe((response: Advert[]) => {
      this.advertList = response;
      this.advertListLoaded = Promise.resolve(true);
    });
  }

  onSubmit(): void {
    if (this.searchQuery.length > 0) {
      // this.router.navigate(['search/' + Base64.encode(this.searchQuery)]);
    }
  }
}
