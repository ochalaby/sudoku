import { Component, OnInit } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

import { ConnectionService } from '../connection/connection.service';

@Component({
  selector: 'app-grid',
  templateUrl: './grid.component.html',
  styleUrls: ['./grid.component.scss']
})

export class GridComponent implements OnInit {

  rows = [{}];
  message = '';

  constructor(private connectionService: ConnectionService) { }

  ngOnInit(): void {
    this.connectionService._rows.subscribe(value => {
      this.rows = value;
    });
  }

  init(){
      this.message = 'init clicked';
      this.connectionService.init();
    }

  clear(){
    this.message = 'clear clicked';
    this.connectionService.clear();
  }

  solve(){
    this.message = 'solve clicked';
    this.connectionService.solve(this.rows);
  }

  reset(){
      this.message = 'reset clicked';
      this.connectionService.reset();
    }

  tdClass(x, y): string {
    const vert = ['top', 'center', 'center'];
    const hor = ['middle', 'middle', 'right'];
    var style = `board-${x === 9 ? 'bottom' : vert[(x - 1) % 3]}-${y === 1 ? 'left' : hor[(y - 1) % 3]}`;
    return style;
  }

}
