import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-interactions',
  template: `
    <button mat-raised-button color="primary" (click)="next()">Next Question</button>
    <button mat-raised-button color="accent" (click)="hint()">Hint</button>
    <button mat-raised-button color="warn" (click)="answer()">Answer</button>
  `,
  styleUrls: ['./interactions.component.css']
})
export class InteractionsComponent implements OnInit {

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
  }

  next(): void {
    this.http.post<any>(environment.apiUrl+'/presentation/nextquestion', {}).subscribe(
      (data: any) => console.log(data)
    );
  }

  hint(): void {
    this.http.post<any>(environment.apiUrl+'/presentation/hint', {}).subscribe(
      (data: any) => console.log(data)
    );
  }

  answer(): void {
    this.http.post<any>(environment.apiUrl+'/presentation/answer', {}).subscribe(
      (data: any) => console.log(data)
    );
  }

}
