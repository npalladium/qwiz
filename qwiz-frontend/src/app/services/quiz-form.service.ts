import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { retry, catchError } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { Quiz } from '../shared/quiz';

@Injectable({
  providedIn: 'root'
})
export class QuizFormService {

  apiUrl = environment.apiUrl;

  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };

  addQuiz(quiz: Quiz): Observable<Quiz> {
    return this.http.post<Quiz>(this.apiUrl+'/quizzes', JSON.stringify(quiz), this.httpOptions)
    .pipe(
      retry(1), catchError(this.handleError)
    );
  }

  handleError(error: any) {
    let msg;
    if(error.error && error.error instanceof ErrorEvent) {
      msg = error.error.message;
    } else {
      msg = `Code: ${error.status}\nMessage: ${error.message}`;
    }
    window.alert(msg);
    return throwError(msg);
  }

  constructor(private http: HttpClient) { }

}
