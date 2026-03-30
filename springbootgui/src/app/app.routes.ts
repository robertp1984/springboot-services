import { Routes } from '@angular/router';
import { ImageMessagesComponent } from './image-messages/image-messages';
import { AboutComponent } from './about/about';

export const routes: Routes = [
    {path: '', redirectTo: 'image-messages', pathMatch: 'full' },
    {path: 'image-messages', component: ImageMessagesComponent },
    {path: 'about', component: AboutComponent}
];
