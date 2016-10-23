# Maven lab
## Task 1
Create project with exec plugin. Main class should work with parameters. Write command examples for maven to run project. Create build profile that will turn on exec plugin. Use property to specify main class.
### Maven command examples:
* mvn exec:java -Dexec.args="arg1 arg2 arg3"
* mvn exec:java

## Task 2
Create project with 2 types of test - ITest and simple test. Configure maven to run build without ITests. Create profile to run build with ITests, only ITests. Use maven properties to configure ITest name convention.

### Maven command examples
* mvn clean install -P integrationTestExec
* mvn clean test -P unitTestExec