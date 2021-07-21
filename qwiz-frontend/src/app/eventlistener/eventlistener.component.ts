import { Component, OnInit, NgZone } from '@angular/core';
import {Subject, Observable, Observer }  from 'rxjs';



@Component({
  selector: 'app-eventlistener',
  template: `
    <ul>
        <li *ngFor="let s of someStrings">
           a string: {{ s | answers }}
        </li>
    </ul>
    `,
  styleUrls: ['./eventlistener.component.css']
})
export class EventlistenerComponent implements OnInit {
  someStrings:any[] = [];
  subscription: any;
  observable: any;

  constructor(private zone: NgZone) {}

  ngOnInit(): void{
    this.observable = Observable.create((observer: Observer<any>) => {

      const eventSource = new EventSource('http://localhost:8080/stream-pounces', {withCredentials: true});
      eventSource.addEventListener('pounce', (message: any) => observer.next(JSON.parse(message.data)));

    });


    this.subscription = this.observable.subscribe({
      next: (guid: string) => {
        console.log(guid);
        this.zone.run(() => this.someStrings.push(guid));
      },
      error:(err: any) => console.error('something wrong occurred: ' + err)
    });
  }
}
