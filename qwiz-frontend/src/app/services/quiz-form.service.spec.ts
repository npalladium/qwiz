import { TestBed } from '@angular/core/testing';

import { QuizFormService } from './quiz-form.service';

describe('QuizFormService', () => {
  let service: QuizFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(QuizFormService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
