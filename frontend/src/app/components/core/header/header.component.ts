import { Component } from '@angular/core';
import {Router, RouterLink, RouterOutlet} from "@angular/router";
import {CommonModule} from "@angular/common";
import {MatButtonModule} from "@angular/material/button";
import {UserService} from "../../../services/user/user.service";

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    RouterOutlet,
    RouterLink,
    MatButtonModule,
    CommonModule
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {

  constructor(public router: Router, private userService: UserService) {}

  signOut(): void{
    this.userService.logOut();
  }
}
