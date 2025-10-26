import { Component, OnInit, HostListener, ViewChild, ElementRef } from '@angular/core';
import { Row, ConnectionService } from '../connection/connection.service';

@Component({
  selector: 'app-grid',
  templateUrl: './grid.component.html',
  styleUrls: ['./grid.component.scss']
})
export class GridComponent implements OnInit {

  rows: Row[] = [];
  message = '';

  selectedRowId: number | null = null;
  selectedFieldId: number | null = null;

  @ViewChild('sudokuTable') tableRef!: ElementRef;

  constructor(private connectionService: ConnectionService) { }

  ngOnInit(): void {
    this.connectionService.rows$.subscribe((value: Row[]) => {
      this.rows = value;
    });
  }

  onSelect(rowId: number, fieldId: number): void {
    this.selectedRowId = rowId;
    this.selectedFieldId = fieldId;
  }

  numberClicked(value: number): void {
    this.message = 'pressed: ' + value

    if (this.selectedRowId !== null && this.selectedFieldId !== null) {
      const selectedRow = this.rows.find(r => r.id === this.selectedRowId);
      const selectedField = selectedRow?.fields.find(f => f.id === this.selectedFieldId);
      if (selectedField) {
        selectedField.value = value.toString();
      }
    }
  }

  @HostListener('document:click', ['$event'])
  handleClickOutside(event: Event) {
    if (this.tableRef && !this.tableRef.nativeElement.contains(event.target)) {
      this.selectedRowId = null;
      this.selectedFieldId = null;
    }
  }

  init(): void {
    this.message = 'init clicked';
    this.connectionService.init();
  }

  clear(): void {
    this.message = 'clear clicked';
    this.connectionService.clear();
  }

  solve(): void {
    this.message = 'solve clicked';
    this.connectionService.solve();
  }

  reset(): void {
    this.message = 'reset clicked';
    this.connectionService.reset();
  }

  tdClass(x: number, y: number): string {
    const vert = ['top', 'center', 'center'];
    const hor = ['middle', 'middle', 'right'];
    let style = `board-${x === 9 ? 'bottom' : vert[(x - 1) % 3]}-${y === 1 ? 'left' : hor[(y - 1) % 3]}`;

    if (this.selectedRowId === x && this.selectedFieldId === y) {
      style += ' selected-cell'; // voeg extra class toe
    }

    return style;
  }
}
