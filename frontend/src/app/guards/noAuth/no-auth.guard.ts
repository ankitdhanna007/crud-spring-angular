import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {UserService} from "../../services/user/user.service";

export const noAuthGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const userService = inject(UserService);

  if (!userService.isLoggedIn()) {
    return true;
  }
  router.navigate(['/cars']);
  return false;
};
