import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import * as Base64 from 'js-base64';

@Component({
  selector: 'app-search-bar',
  templateUrl: './search-bar.component.html',
  styleUrls: ['./search-bar.component.scss'],
})
export class SearchBarComponent {
  @Input() searchQuery!: string;

  constructor(private router: Router) {}

  public onSubmit(): void {
    if (this.searchQuery.length > 0) {
      void this.router.navigate(['search/' + Base64.encode(this.searchQuery)]);
    }
  }
}
