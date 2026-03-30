import { Component, OnInit  } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { ImageMessage } from '../models/image-message.model';
import { MqReceiverService } from '../services/mq-receiver-service';
import { ImageMessageComponent } from '../image-message/image-message';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-image-messages',
  imports: [CommonModule, ImageMessageComponent],
  templateUrl: './image-messages.html',
  styleUrl: './image-messages.css',
})
export class ImageMessagesComponent implements OnInit {
  title: string = "Image Messages";
  dataItemListObservable: Observable<ImageMessage[]> = new Observable<ImageMessage[]>();

  constructor(private mqReceiverService: MqReceiverService) {
  }

  ngOnInit() : void {
    console.log("INIT");
    this.getImageMessages();
  }

  private getImageMessages() : void {
    this.dataItemListObservable = this.mqReceiverService.getImageMessages();
  }

  onDeleteImageMessage(imageMessage: ImageMessage) : void {
    this.mqReceiverService.deleteImageMessage(imageMessage.id).subscribe({
      next: () => {
        console.log("Deleted image message with id " + imageMessage.id);
        this.getImageMessages();
      },
      error: () => {
        console.error("Error deleting image message with id " + imageMessage.id);
      }
    });

  }
}

