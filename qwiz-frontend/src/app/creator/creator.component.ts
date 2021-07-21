import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { QuizFormService } from '../services/quiz-form.service';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-creator',
  templateUrl: './creator.component.html',
  styleUrls: ['./creator.component.css'],
})
export class CreatorComponent implements OnInit {
  isLinear = true;

  firstFormGroup: FormGroup;

  secondFormGroup: FormGroup;

  constructor(private _formBuilder: FormBuilder, private _service: QuizFormService) {}

  ngOnInit() {
    this.firstFormGroup = this._formBuilder.group({
      quiz: ['', Validators.required],
    });
    this.secondFormGroup = this._formBuilder.group({
        questions: this._formBuilder.array([])
    });
  }

    get questions() {
      return this.secondFormGroup.controls["questions"] as FormArray;
    }

    addQuestion() {
      const questionForm = this._formBuilder.group({
        que: ['', Validators.required],
        hint: [''],
        ans: ['', Validators.required],
      });
      this.questions.push(questionForm);
    }

    deleteQuestion(questionIndex: number) {
      this.questions.removeAt(questionIndex);
    }
  processQuestions(question: any) {
    let q = {
      que: question.que.split(',').map((s: string) => s.trim()).map(Number),
      hint: question.hint.split(',').map((s: string) => s.trim()).map(Number),
      ans: question.ans.split(',').map((s: string) => s.trim()).map(Number)
    }
    return q;
  }
  submitQuizDetails() {
    let quizDetails = {
      name: this.firstFormGroup.controls['quiz'].value,
      questions: this.secondFormGroup.controls['questions'].value.map(this.processQuestions)
    }
    console.log(quizDetails);
    this._service.addQuiz(quizDetails).subscribe((data: any) => console.log(data));
  }
}
