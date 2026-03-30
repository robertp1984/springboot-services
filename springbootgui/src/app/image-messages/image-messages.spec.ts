import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ImageMessagesComponent } from './image-messages';

describe('MqImageMessages', () => {
  let component: ImageMessagesComponent;
  let fixture: ComponentFixture<ImageMessagesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ImageMessagesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ImageMessagesComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
