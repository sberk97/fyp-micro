export class Advert {
  constructor(
    public id: number,
    public user_id: number,
    public username: string,
    public price: number,
    public creation_date: Date,
    public title: string,
    public description: string,
    public contact_details: string
  ) {}
}
