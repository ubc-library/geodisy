# Developer documentation for Geodisy

## Software Requirements
In order to build the Geodisy software, Java and Maven are required
- Java 8 or greater
- Maven 3.x

## Building the software

1. Create a local clone of the code base:
   ```
   $ git clone https://github.com/ubc-library/geodisy.git
   ```
1. Create a copy of [PrivateStringTemplate.java](../Geodisy/Geodisy/src/main/java/_Strings/PrivateStringTemplate.java) named "PrivateStrings.java" in the same directory where "PrivateStringTemplate.java" is found. Update the values in "PrivateStrings.java" appropriately.
   - Note: There is an open GitHub issue ([#24](https://github.com/ubc-library/geodisy/issues/24)) for documenting the description and purpose for each of these strings.
1. Build the application
   ```
   $ cd geodisy/Geodisy/Geodisy
   $ mvn clean install
   ```
1. After a successful build, a standalone, executable jar file can be found in the "target" directory, named: 
   - `Geodisy-1.0-SNAPSHOT-jar-with-dependencies.jar`

## Running the software
The following command runs the application:
```
$ java -jar target/Geodisy-1.0-SNAPSHOT-jar-with-dependencies.jar
```

   - Note: There is an open GitHub issue ([#25](https://github.com/ubc-library/geodisy/issues/25)) for documenting what happens when running the application and any required configuration.

