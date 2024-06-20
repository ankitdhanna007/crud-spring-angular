import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogModule} from "@angular/material/dialog";
import {MatButtonModule} from "@angular/material/button";

@Component({
  selector: 'app-delete-confirm',
  standalone: true,
  imports: [
    MatButtonModule,
    MatDialogModule,
  ],
  templateUrl: './delete-confirm.component.html',
  styleUrl: './delete-confirm.component.css'
})
export class DeleteConfirmComponent {
  constructor(@Inject(MAT_DIALOG_DATA) public data: {name: string}) { }
}
