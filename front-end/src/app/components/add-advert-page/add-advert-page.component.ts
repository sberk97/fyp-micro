import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { BackendService } from 'src/app/services/backend/backend.service';

@Component({
  templateUrl: './add-advert-page.component.html',
  styleUrls: ['./add-advert-page.component.scss'],
})
export class AddAdvertPageComponent {
  title!: string;
  price!: number;
  description!: string;
  contactDetails!: string;

  postFailed = false;
  failedMsg = '';

  constructor(private router: Router, private backendService: BackendService) {}

  onSubmit(): void {
    const body = {
      title: this.title,
      description: this.description,
      contact_details: this.contactDetails,
      price: this.price,
    };
    this.backendService.addAdvert(body).subscribe(
      (response: number) => {
        void this.router.navigate(['/advert/' + response.toString()]);
      },
      (error: HttpErrorResponse) => {
        this.postFailed = true;
        if (error.status === 400) {
          this.failedMsg = error.error as string;
        } else {
          this.failedMsg = 'Adding advert failed!';
        }
      }
    );
  }
}
