export enum Transmission{
  MANUAL = 'MANUAL',
  AUTOMATIC = 'AUTOMATIC',
  HYBRID = 'HYBRID'
}

export interface Car{
  id?: number,
  name: string,
  transmission: Transmission,
  yearOfManufacture: string,
  pricePerDay: string
}
