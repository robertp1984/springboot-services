import { DatePipe, NgClass } from '@angular/common';
import { Component, input, output } from '@angular/core';
import { ImageMessage } from '../models/image-message.model';

@Component({
  selector: 'app-image-message',
  imports: [DatePipe, NgClass],
  templateUrl: './image-message.html',
  styleUrl: './image-message.css',
})
export class ImageMessageComponent {
  dataItem = input.required<ImageMessage>();
  onDelete = output<ImageMessage>();

  deleteImageMessage(): void {
    this.onDelete.emit(this.dataItem());

  }

  getOriginalFilenameClass(): string {
    switch (this.dataItem().contentType) {
      case "image/png":
        return "bi-filetype-png";
      case "text/plain":
        return "bi-filetype-txt";
      case "image/jpeg":
        return "bi-filetype-jpg";
    }
    return "bi-file-binary";
  }


}
