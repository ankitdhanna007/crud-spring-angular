import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Car} from "../../models/car";
import {url} from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class CarService {

  carsUrl: string = `${url}/cars`;

  constructor(private http: HttpClient) { }

  list(): Observable<Car[]> {
    return this.http
      .get<Car[]>(this.carsUrl);
  }

  add(newCar: Car): Observable<Car> {
    return this.http.post<Car>(this.carsUrl, newCar);
  }

  get(id:string): Observable<Car> {
    return this.http
      .get<Car>(`${this.carsUrl}/${id}`)
  }

  put(editedCar: Car): Observable<Car> {
    return this.http.put<Car>(`${this.carsUrl}/${editedCar.id}`, editedCar);
  }

  delete(deletedCar: Car): Observable<Car> {
    return this.http.delete<Car>(`${this.carsUrl}/${deletedCar.id}`);
  }

}
