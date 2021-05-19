import { Advert } from '../advert/advert';

export class User {
  constructor(
    public id: number,
    public username: string,
    public roles: string,
    public adverts: Advert[]
  ) {}
}
