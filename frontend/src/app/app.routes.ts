import { Routes } from '@angular/router';
import {LoginComponent} from "./components/core/login/login.component";
import {ListCarsComponent} from "./components/cars/list-cars/list-cars.component";
import {SaveCarComponent} from "./components/cars/save-car/save-car.component";
import {authGuard} from "./guards/auth/auth.guard";
import {noAuthGuard} from "./guards/noAuth/no-auth.guard";
import {adminGuard} from "./guards/admin/admin.guard";
import {editorGuard} from "./guards/editor/editor.guard";

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent, canActivate: [noAuthGuard] },
  { path: 'cars', children:[
      { path: '', component: ListCarsComponent},
      { path: 'add', component: SaveCarComponent, canActivate: [adminGuard]},
      { path: 'edit/:id', component: SaveCarComponent, canActivate: [editorGuard]}
    ], canActivate: [authGuard] }
];
