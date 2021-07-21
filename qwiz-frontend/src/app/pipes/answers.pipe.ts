import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'answers'
})
export class AnswersPipe implements PipeTransform {

  transform(value: any): string {
    if (value == null) {
      return "No has attempted anything yet!"
    }
    return `${value.channel}'s ${value.user} ${value.type}d ${value.message}.`;
  }

}
