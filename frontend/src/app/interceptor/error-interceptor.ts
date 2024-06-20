import {HttpErrorResponse, HttpInterceptorFn} from '@angular/common/http';
import {catchError, throwError} from "rxjs";
import {UserService} from "../services/user/user.service";
import {inject} from "@angular/core";
import {HttpFailedResponse} from "../models/http-failed-response";

export const errorInterceptor: HttpInterceptorFn = (req, next) => {

  const userService = inject(UserService);

  return next(req).pipe(
    catchError((err: any) => {

      if (err instanceof HttpErrorResponse) {
        if (err.error) {
          let error: HttpFailedResponse = err.error;
          if (error.status == '403' && error.description == 'The JWT token has expired') {
            // todo -> This jwt string check is inefficient. Need to create error codes in the backend and use them here
            // trigger the re-authentication flow
            userService.logOut();
          }
        }
      }
      // Re-throw the error to propagate it further
      return throwError(() => err);
    }));

};
