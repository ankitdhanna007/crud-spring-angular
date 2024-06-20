import { Component } from '@angular/core';
import {MatIconModule} from "@angular/material/icon";
import {MatTableModule} from "@angular/material/table";
import {MatButtonModule} from "@angular/material/button";
import {MatDividerModule} from "@angular/material/divider";
import {CarService} from "../../../services/car/car-service";
import {MatDialog, MatDialogModule} from "@angular/material/dialog";
import {ActivatedRoute, Router} from "@angular/router";
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {Car, Transmission} from "../../../models/car";
import {MatSelectModule} from "@angular/material/select";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {CommonModule} from "@angular/common";
import {Observable} from "rxjs";
import {HttpFailedResponse} from "../../../models/http-failed-response";

@Component({
  standalone: true,
  imports: [
    MatIconModule,
    MatTableModule,
    MatButtonModule,
    MatDividerModule,
    ReactiveFormsModule,
    FormsModule,
    MatDialogModule,
    MatSelectModule,
    MatFormFieldModule,
    MatInputModule,
    CommonModule
  ],
  templateUrl: './save-car.component.html',
  styleUrl: './save-car.component.css'
})
export class SaveCarComponent {

  error_message: string = '';
  transmissionTypes: string[] = Object.values(Transmission);
  carForm: FormGroup;
  carId: string;
  canEdit: boolean = false;
  dataLoaded: boolean = false;
  years:string[] = [];

  constructor(private fb: FormBuilder, private carService: CarService, private route: ActivatedRoute, private dialog: MatDialog, private router: Router) {

    this.carForm = this.fb.group({
      id: [null],
      name: ['', Validators.required],
      transmission: [Transmission.MANUAL],
      yearOfManufacture: ['', Validators.required],
      pricePerDay: ['', Validators.required],
    });

    this.carId = this.route.snapshot.paramMap.get('id') as string;
    this.canEdit =  this.carId != null;
  }


  ngOnInit(){
    if(this.canEdit && this.carId != null){
      this.carService.get(this.carId).subscribe({
        next: (newCar: Car) => {
          this.dataLoaded = true;
          this.carForm.patchValue(newCar);
        },
        error: errorResponse => {
          let error = errorResponse.error as HttpFailedResponse;
          this.error_message = error.description;
        },
      });
    }
  }

  saveCar(): void{

    let saveObservable: Observable<Car>;

    if(this.canEdit && (this.carForm.value as Car).id != null){
      saveObservable = this.carService.put(this.carForm.value);
    } else {
      saveObservable = this.carService.add(this.carForm.value);
    }

    saveObservable.subscribe({
      next: (newCar: Car) => {
        this.router.navigate(['/cars']);
      },
      error: errorResponse => {
        let error = errorResponse.error as HttpFailedResponse;
        this.error_message = error.description;
      },
    });
  }

}
