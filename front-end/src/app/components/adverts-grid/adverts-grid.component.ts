import { Component, Input } from '@angular/core';
import { Advert } from 'src/app/models/advert/advert';

@Component({
  selector: 'app-adverts-grid',
  templateUrl: './adverts-grid.component.html',
  styleUrls: ['./adverts-grid.component.scss'],
})
export class AdvertsGridComponent {
  @Input() advertList!: Advert[];
  private priceOrder = false;
  private titleOrder = false;

  public sortPrice(): void {
    this.priceOrder = !this.priceOrder;
    this.advertList.sort((a, b) => {
      return this.sort(a.price, b.price, this.priceOrder);
    });
  }

  public sortTitle(): void {
    this.titleOrder = !this.titleOrder;
    this.advertList.sort((a, b) => {
      return this.sort(a.title, b.title, this.titleOrder);
    });
  }

  private sort(a: any, b: any, ascending: boolean): number {
    if (a < b) {
      if (ascending) {
        return -1;
      } else {
        return 1;
      }
    } else if (a > b) {
      if (ascending) {
        return 1;
      } else {
        return -1;
      }
    }
    return 0;
  }
}
