# ATM Modified
This project is a modified version of the original tool [AppTestMigrator](https://ieeexplore.ieee.org/document/9270392).
ATM is tool to migrate test cases of an android application to another application with similar functionalities. 
On of important components of test migration (test reuse) process is semantic matching of events between source to target applications.
We reversed engineered the tool to understand how it functions in fine grain levels.
Then we disabled the semantic matching part carefully without disrupting other parts and made the necessary modifications to make ATM rely on external service for semantic matching. 
We did so to make is suitable for one of our recent experiments.

The project is not ready for public usage.

