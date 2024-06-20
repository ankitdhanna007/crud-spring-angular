import {Component} from '@angular/core';
import {MatTableModule} from "@angular/material/table";
import {CarService} from "../../../services/car/car-service";
import {Car} from "../../../models/car";
import {MatDialog} from "@angular/material/dialog";
import {MatIconModule} from "@angular/material/icon";
import {Router} from "@angular/router";
import {MatDividerModule} from "@angular/material/divider";
import {MatButtonModule} from "@angular/material/button";
import {DeleteConfirmComponent} from "./delete-confirm/delete-confirm.component";
import {HttpFailedResponse} from "../../../models/http-failed-response";
import {UserService} from "../../../services/user/user.service";
import {CommonModule} from "@angular/common";

@Component({
  standalone: true,
  imports: [MatIconModule, MatTableModule, MatButtonModule, MatDividerModule, CommonModule
  ],
  templateUrl: './list-cars.component.html',
  styleUrl: './list-cars.component.css'
})
export class ListCarsComponent {

  displayedColumns: string[] = ['id', 'name', 'transmission', 'year', 'price_per_day', 'actions'];
  carData: Car[] = [];
  error_message: string = '';
  user_role: string;

  constructor(private carService: CarService, private userService: UserService, private dialog: MatDialog, private router: Router) {
    this.user_role = userService.getRole();   //Role can also be provided by resolves through the routes

  }

  ngOnInit(){
    this.listCars();
  }

  navigateToEditCar(car: Car): void{
    this.router.navigate([`/cars/edit/${car.id}`]);
  }

  navigateToAdd(): void{
    this.router.navigate([`cars/add`]);
  }

  openDeleteConfirmPopUp(car: Car) {

    const dialogRef = this.dialog.open(DeleteConfirmComponent, {
      data: {name: car.name},
      width: '500px',
    });

    dialogRef.afterClosed().subscribe(result => {
      if(result == true){
        this.carService.delete(car).subscribe({
          next: (response: Car) => {
            this.listCars();
          },
          error: errorResponse => {
            let error = errorResponse.error as HttpFailedResponse;
            this.error_message = error.description;
          },
        });
      }
    });
  }

  private listCars() {
    this.carService.list().subscribe({
      next: (cars: Car[]) => {
        this.carData = cars;
      },
      error: errorResponse => {
        let error = errorResponse.error as HttpFailedResponse;
        this.error_message = error.description;
      },
    });
  }


}
