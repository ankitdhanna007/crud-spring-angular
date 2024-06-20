import {Component} from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {Router, RouterOutlet} from "@angular/router";
import {UserService} from "../../../services/user/user.service";
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {HttpFailedResponse} from "../../../models/http-failed-response";
import {LoginResponse} from "../../../models/login-response";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    RouterOutlet,
    ReactiveFormsModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatButtonModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  hide: boolean = true;
  error_message: string = '';

  form: FormGroup = this.fb.group({
    username: ['', [Validators.required]],
    password: ['', Validators.required],
  });

  constructor(private fb: FormBuilder, private userService: UserService, private router: Router) {}

  login(): void{
    this.userService.login(this.form.value).subscribe({
      next: (response: LoginResponse) => {

        if(response.token && response.role){
          localStorage.setItem('access_token', response.token);
          localStorage.setItem('role', response.role);
          localStorage.setItem('username', response.username);
        }

        this.error_message = '';
        this.router.navigate(['/cars']);
      },
      error: errorResponse => {
        let error = errorResponse.error as HttpFailedResponse;
        this.error_message = error.description;
      },
    });
  }

}
