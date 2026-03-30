import { Injectable } from '@angular/core';
import { ImageMessage } from '../models/image-message.model';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class MqReceiverService {
    baseUrl = "https://localhost:8082/api/v1/imageMessages";

    constructor(private http: HttpClient) {
    }

    public getImageMessages() : Observable<ImageMessage[]> {
      return this.http.get<ImageMessage[]>(this.baseUrl);
    }

    public deleteImageMessage(id: string) : Observable<void> {
      const url = `${this.baseUrl}/${id}`;
      return this.http.delete<void>(url);
    }
}

