Each student or group of students will perform diverse forms of testing and generate several deliverables on an application of their choice (either built specifically for this project, a repurposed app or even someone else’s code). The topic, business idea, specifications, type of application (web, mobile, standalone), programming language(s), DBMSs, test tools, continuous integration tools, and IDEs used will be decided by the students. However, the application must include the following:

A frontend
A backend the frontend connects to via an API
A database
A connection to an external, public API (could be something simple like a weather widget)
Task list:


1. Review.
One member of the group will write an SRS (Software Requirements Specification) for the application in the format chosen by the students (it can be a series of user stories).
The remaining members will conduct a formal review and generate a review document.
Deliverables:
The SRS (pdf file)
The review report (pdf file), including the roles assigned to each group member


2. Risk assessment.
 - Deliverables (both in a single pdf file):
 - Risk table(s). Either one that includes the follow-ups to the risks throughout the project’s development or several tables at different stages of  development (in that case, at least an initial table, one created mid-development, and a final one must be included)
 - Risk matrices. Several illustrating the state of the risks at several phases of the project


3. Black-box test design. Black-box techniques will help identify test cases.
 - Deliverables (all in a single pdf file):
 - Equivalence partitioning analysis
 - Boundary value analysis
 - Decision table(s) (only if relevant)
 - State transition diagram(s) and/or state transition table(s) (only if relevant)


4. Static Testing Tools and White-box test design. Use the static testing tool(s) of your choice (besides linters and IDE extensions) and document their contribution.
- Include coverage calculation
- Deliverables (all in a single pdf file):
- Static testing tool(s) reports/screenshots and brief explanation of results
- Code coverage report and brief explanation of how it helped design unit tests


5. Unit testing and integration testing. Design and implement:
- Unit tests        
- Integration tests in code (unless you decide to mock all dependencies)
- Pay attention to the following:

- Comprehensive assertions and test cases are expected
- The test cases identified in the black-box design phase must be present in the unit tests
- Use parameterised tests/data providers
Deliverables:
- Source code of the application
- Source code of the unit tests and integration tests


6. Continuous testing. Create an online code repository and a continuous integration job or pipeline that checks potential integration errors.
The CI job must automate the execution of the unit tests and integration tests in code
Deliverables:
CI job script (xml/yaml file or similar)
Output of the integration project          
(if the CI tool does not generate a file, copy the output and paste it on a text file)


7. API testing. Use the tool of your choice (e.g., Postman, Thunder Client, Insomnia...) to design and implement test scripts on your backend’s API.
Test all your endpoints for different elements (e.g., HTTP status codes, execution time, JSON content...)
Positive and negative testing is expected
Deliverables:
Collection and environment in JSON format, or a similar output


8. End-to-end UI testing. Design and implement a series of end-to-end tests using a UI automation tool (e.g., Selenium WebDriver, Cypress, Playwright, Espresso)
The tests must be in code. Scripting tools like Selenium IDE are forbidden
Deliverables:
Source code of the end-to-end tests


9. Stress performance testing. Implement load, stress, and spike testing on the application with a stress performance testing application (e.g., Apache JMeter).
Deliverables:
Reports or screenshots documenting the tests.


10. Usability testing. Design, but do not conduct, a usability test.
Deliverables:
A pdf file with the list of preference and performance measures and a script with relevant scenarios and tasks