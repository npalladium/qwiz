import { Component, OnInit, NgZone } from '@angular/core';
import {Subject, Observable, Observer }  from 'rxjs';



@Component({
  selector: 'app-eventlistener',
  // templateUrl: './eventlistener.component.html',
  template: `<h1>My second Angular 2 App</h1>
    <ul>
        <li *ngFor="let s of someStrings">
           a string: {{ s }}
        </li>
    </ul>
    `,
  styleUrls: ['./eventlistener.component.css']
})
export class EventlistenerComponent implements OnInit {
  someStrings:string[] = [];
  subscription: any;

  constructor(private zone: NgZone) {}

  ngOnInit(): void{
    const observable = Observable.create((observer: Observer<any>) => {

      const eventSource = new EventSource('http://localhost:8080/stream-pounces', {withCredentials: true});
      eventSource.onmessage = x => observer.next(x.data);
      eventSource.onerror = x => observer.error(x);
      eventSource.addEventListener('pounce', (message: any) => this.someStrings.push(JSON.parse(message.data).message));

    });


    this.subscription = observable.subscribe({
      next: (guid: string) => {
        console.log(guid);
        this.zone.run(() => this.someStrings.push(guid));
      },
      error:(err: any) => console.error('something wrong occurred: ' + err)
    });
  }
}
