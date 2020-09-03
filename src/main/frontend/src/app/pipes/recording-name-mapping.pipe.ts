import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'recordingNameMapping'
})
export class RecordingNameMappingPipe implements PipeTransform {

  transform(value: string, ...args: unknown[]): string {
    if (!value) {
      return '';
    }

    let replacedValue = value;
    if (replacedValue.indexOf(':::') !== -1) {
      replacedValue = replacedValue.replace(':::', ' > ');
    }

    while (replacedValue.indexOf('::') !== -1) {
      replacedValue = replacedValue.replace('::', ' > ');
    }
    return replacedValue;
  }

}
