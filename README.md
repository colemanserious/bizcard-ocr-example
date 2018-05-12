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

