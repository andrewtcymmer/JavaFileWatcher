Meta:

Narrative:
As a User, I can produce a csv file containing validation errors

Scenario: at least one error in the input file

Given the application has started
When a csv file which contains at least one error is added to the input directory
Then the error directory has at least one file in it