import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';

export interface Field {
  id: number;
  value: string;
}

export interface Row {
  id: number;
  fields: Field[];
}

export interface SudokuDto {
  rows: Row[];
}

const ROWS: Row[] = [
  {id: 1, fields: [
    {id: 1, value: ''}, {id: 2, value: '9'}, {id: 3, value: ''},
    {id: 4, value: ''}, {id: 5, value: ''},  {id: 6, value: ''},
    {id: 7, value: ''}, {id: 8, value: ''},  {id: 9, value: ''}
  ]},
  {id: 2, fields: [
    {id: 1, value: ''}, {id: 2, value: ''}, {id: 3, value: ''},
    {id: 4, value: '5'}, {id: 5, value: ''}, {id: 6, value: ''},
    {id: 7, value: '4'}, {id: 8, value: ''}, {id: 9, value: '8'}
  ]},
  {id: 3, fields: [
    {id: 1, value: ''}, {id: 2, value: '4'}, {id: 3, value: ''},
    {id: 4, value: '6'}, {id: 5, value: '9'}, {id: 6, value: ''},
    {id: 7, value: ''}, {id: 8, value: '2'}, {id: 9, value: '7'}
  ]},
  {id: 4, fields: [
    {id: 1, value: '1'}, {id: 2, value: '6'}, {id: 3, value: '5'},
    {id: 4, value: '2'}, {id: 5, value: ''}, {id: 6, value: ''},
    {id: 7, value: ''}, {id: 8, value: '7'}, {id: 9, value: ''}
  ]},
  {id: 5, fields: [
    {id: 1, value: ''}, {id: 2, value: ''}, {id: 3, value: '8'},
    {id: 4, value: ''}, {id: 5, value: ''}, {id: 6, value: '6'},
    {id: 7, value: ''}, {id: 8, value: ''}, {id: 9, value: ''}
  ]},
  {id: 6, fields: [
    {id: 1, value: ''}, {id: 2, value: ''}, {id: 3, value: ''},
    {id: 4, value: ''}, {id: 5, value: ''}, {id: 6, value: ''},
    {id: 7, value: '8'}, {id: 8, value: '1'}, {id: 9, value: ''}
  ]},
  {id: 7, fields: [
    {id: 1, value: ''}, {id: 2, value: ''}, {id: 3, value: ''},
    {id: 4, value: ''}, {id: 5, value: '3'}, {id: 6, value: '7'},
    {id: 7, value: ''}, {id: 8, value: ''}, {id: 9, value: ''}
  ]},
  {id: 8, fields: [
    {id: 1, value: ''}, {id: 2, value: ''}, {id: 3, value: '2'},
    {id: 4, value: ''}, {id: 5, value: ''}, {id: 6, value: ''},
    {id: 7, value: ''}, {id: 8, value: ''}, {id: 9, value: ''}
  ]},
  {id: 9, fields: [
    {id: 1, value: '6'}, {id: 2, value: ''}, {id: 3, value: ''},
    {id: 4, value: '8'}, {id: 5, value: ''}, {id: 6, value: ''},
    {id: 7, value: '3'}, {id: 8, value: ''}, {id: 9, value: ''}
  ]}
];


@Injectable({
  providedIn: 'root',
})
export class ConnectionService {

  private _rows = new BehaviorSubject<Row[]>(JSON.parse(JSON.stringify(ROWS)));
  private _previous: Row[][] = [];
  rootURL = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  get rows$(): Observable<Row[]> {
    return this._rows.asObservable();
  }

  init(): void {
    this._rows.next(JSON.parse(JSON.stringify(ROWS)));
  }

  clear(): void {
    const solvedRows: Row[] = [];
    for (let i = 1; i <= 9; i++) {
      const fields: Field[] = [];
      for (let j = 1; j <= 9; j++) {
        fields.push({ id: j, value: '' });
      }
      solvedRows.push({ id: i, fields });
    }
    this._rows.next(solvedRows);
  }

  reset(): void {
    if (this._previous.length > 0) {
      this._rows.next(this._previous[this._previous.length - 1]);
    }
  }

  getSolution(sudoku: SudokuDto): Observable<SudokuDto> {
    return this.http.post<SudokuDto>(`${this.rootURL}/solve`, sudoku);
  }

  solve(): void {
    const currentRows = this._rows.value; // haalt de huidige sudoku op

    if (currentRows.length > 0) {
      this._previous.push(JSON.parse(JSON.stringify(currentRows))); // kopie

      const sudokuDto: SudokuDto = { rows: currentRows };

      // vraag backend om oplossing
      this.getSolution(sudokuDto).subscribe({
        next: (response: SudokuDto) => {
          console.log('Sudoku opgelost:', response);
          this._rows.next(response.rows);
        },
        error: (err) => {
          console.error('Fout bij solve-aanroep:', err);
        }
      });
    }
  }

}
