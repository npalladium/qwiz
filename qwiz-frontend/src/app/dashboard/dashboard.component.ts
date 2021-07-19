import { Component, OnInit } from '@angular/core';
import { SseService } from 'src/app/sse.service';``

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  constructor(private sseService: SseService) {}

  ngOnInit(): void {
    this.sseService
      .getServerSentEvent("http://localhost:8080/stream-pounces")
      .subscribe((data: any) => console.log(data));
  }

}
