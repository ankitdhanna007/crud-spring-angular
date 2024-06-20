import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {UserService} from "../../services/user/user.service";

export const editorGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const userService = inject(UserService);

  if (['ADMIN', 'EDITOR'].includes(userService.getRole())) {
    return true;
  }

  router.navigate(['/']);
  return false;};
