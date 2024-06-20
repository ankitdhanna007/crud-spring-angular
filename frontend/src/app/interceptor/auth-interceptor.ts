import {HttpEvent, HttpHandlerFn, HttpInterceptorFn, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (
  req: HttpRequest<any>,
  next: HttpHandlerFn
): Observable<HttpEvent<any>> => {

  // Exclude interceptor for login request
  const re = /login/gi;
  if (req.url.search(re) !== -1 ) {
    return next(req);
  }

  const token = localStorage.getItem('access_token');
  if (token) {
    const cloned = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });

    return next(cloned);
  } else {
    return next(req);
  }
};
