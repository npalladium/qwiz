import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-creator',
  templateUrl: './creator.component.html',
  styleUrls: ['./creator.component.css'],
})
export class CreatorComponent implements OnInit {
  isLinear = true;

  firstFormGroup: FormGroup;

  secondFormGroup: FormGroup;

  constructor(private _formBuilder: FormBuilder) {}

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
}
