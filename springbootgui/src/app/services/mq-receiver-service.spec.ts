import { TestBed } from '@angular/core/testing';

import { MqReceiverService } from './mq-receiver-service';

describe('MqReceiverService', () => {
  let service: MqReceiverService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MqReceiverService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
