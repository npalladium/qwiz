import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'answers'
})
export class AnswersPipe implements PipeTransform {

  transform(value: any): string {
    return `${value.channel}'s ${value.user} ${value.type}d ${value.message}.`;
    // return "test";
  }

}
