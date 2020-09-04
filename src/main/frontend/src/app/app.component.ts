import { Component, OnInit } from '@angular/core';
import { SpinnerService } from './services/spinner.service';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { SpinnerComponent } from './components/spinner/spinner.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'wiremock-ui-angular';
  private dialogRef: MatDialogRef<SpinnerComponent>;

  constructor(private spinnerService: SpinnerService, private dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.spinnerService.getSpinnerState().subscribe((spinnerState) => {
      if (spinnerState) {
        this.openDialog();
      } else {
        this.closeDialog();
      }
    });
  }

  openDialog(): void {
    if (!this.dialogRef) {
      this.dialogRef = this.dialog.open(SpinnerComponent, {
        width: '100vw',
        hasBackdrop: true,
        disableClose: true,
        panelClass: 'spinner-panel'
      });
    }
  }

  closeDialog(): void {
    if (this.dialogRef) {
      this.dialogRef.close();
      this.dialogRef = null;
    }
  }
}
