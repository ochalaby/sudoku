import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { HttpClient } from '@angular/common/http';

const ROWS = [
  {id: 1, fields: [
    {id: 1, value: ''},
    {id: 2, value: '9'},
    {id: 3, value: ''},
    {id: 4, value: ''},
    {id: 5, value: ''},
    {id: 6, value: ''},
    {id: 7, value: ''},
    {id: 8, value: ''},
    {id: 9, value: ''}
  ]},
  {id: 2, fields: [
    {id: 1, value: ''},
    {id: 2, value: ''},
    {id: 3, value: ''},
    {id: 4, value: '5'},
    {id: 5, value: ''},
    {id: 6, value: ''},
    {id: 7, value: '4'},
    {id: 8, value: ''},
    {id: 9, value: '8'}
  ]},
  {id: 3, fields: [
    {id: 1, value: ''},
    {id: 2, value: '4'},
    {id: 3, value: ''},
    {id: 4, value: '6'},
    {id: 5, value: '9'},
    {id: 6, value: ''},
    {id: 7, value: ''},
    {id: 8, value: '2'},
    {id: 9, value: '7'}
  ]},
  {id: 4, fields: [
    {id: 1, value: '1'},
    {id: 2, value: '6'},
    {id: 3, value: '5'},
    {id: 4, value: '2'},
    {id: 5, value: ''},
    {id: 6, value: ''},
    {id: 7, value: ''},
    {id: 8, value: '7'},
    {id: 9, value: ''}
  ]},
  {id: 5, fields: [
    {id: 1, value: ''},
    {id: 2, value: ''},
    {id: 3, value: '8'},
    {id: 4, value: ''},
    {id: 5, value: ''},
    {id: 6, value: '6'},
    {id: 7, value: ''},
    {id: 8, value: ''},
    {id: 9, value: ''}
  ]},
  {id: 6, fields: [
    {id: 1, value: ''},
    {id: 2, value: ''},
    {id: 3, value: ''},
    {id: 4, value: ''},
    {id: 5, value: ''},
    {id: 6, value: ''},
    {id: 7, value: '8'},
    {id: 8, value: '1'},
    {id: 9, value: ''}
  ]},
  {id: 7, fields: [
    {id: 1, value: ''},
    {id: 2, value: ''},
    {id: 3, value: ''},
    {id: 4, value: ''},
    {id: 5, value: '3'},
    {id: 6, value: '7'},
    {id: 7, value: ''},
    {id: 8, value: ''},
    {id: 9, value: ''}
  ]},
  {id: 8, fields: [
    {id: 1, value: ''},
    {id: 2, value: ''},
    {id: 3, value: '2'},
    {id: 4, value: ''},
    {id: 5, value: ''},
    {id: 6, value: ''},
    {id: 7, value: ''},
    {id: 8, value: ''},
    {id: 9, value: ''}
  ]},
  {id: 9, fields: [
    {id: 1, value: '6'},
    {id: 2, value: ''},
    {id: 3, value: ''},
    {id: 4, value: '8'},
    {id: 5, value: ''},
    {id: 6, value: ''},
    {id: 7, value: '3'},
    {id: 8, value: ''},
    {id: 9, value: ''}
  ]}
];


@Injectable({
  providedIn: 'root',
})
export class ConnectionService {

    _rows = new BehaviorSubject(JSON.parse(JSON.stringify(ROWS)));
    rootURL = 'http://localhost:8080/api';
    _previous = [];

  constructor(private http: HttpClient) { }
  //constructor() { }

  init(){
    this._rows.next(JSON.parse(JSON.stringify(ROWS)));
  }

  clear(){
    let solvedRows = [];
    for (let i = 1; i <= 9; i++) {
      let fields = [];
      for (let j = 1; j <= 9; j++) {
        fields.push({id: j, value: ''});
      }
      solvedRows.push({id: i, fields: fields});
    }

    this._rows.next(solvedRows);
  }

  reset(){
    if (this._previous.length > 0){
      this._rows.next(this._previous);
    }
  }

  getSolution(inputRows): Observable<any> {
      return this.http.post(this.rootURL + '/solve', inputRows);
    }

    solve(inputRows) {
       if (inputRows.length > 0){
            this._previous = inputRows;
                    this.getSolution(inputRows)
                      .subscribe(
                        (response) => {
                          console.log('response received');
                          console.log(response);
                          this._rows.next(response);
                        });
       }
      }

}
