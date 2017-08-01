Meta:

Narrative:
As a User, given a csv, I can produce a json file of valid csv records

Scenario: successful processing

Given the application has started
When a csv file which contains at least one correct line is added to the input directory
Then the output directory has at least one file in it