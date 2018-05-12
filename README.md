# Business Card OCR Component

This component that parses the results of the optical character recognition (OCR) component in order to extract the name, phone number, and email address from the processed business card image.

It is provided as a Java jar for integration into the OCR processing pipeline.

## Scope of component:
Given a string that contains the text retrieved from a business card, returns the *name*, *phone number*, and *email address* within a ContactInfo object.
 
* name: (string) full name
* phone number: (string) only digits
* email address: (string) 

Any item not available in the provided text will be returned as null.

## Build instructions
 
`mvn package`

## Test instructions

`mvn test` 

## Technical Notes

The version of en-ner-person.bin included in src/main/resources was pulled from http://opennlp.sourceforge.net/models-1.5/ on 5/12/2018.

Per http://opennlp.apache.org/models.html
> The models on Sourceforge for 1.5.0 ...  are fully compatible with Apache OpenNLP 1.8.4.

