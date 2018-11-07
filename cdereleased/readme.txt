This is Maven springboot command line utility to generate XML CDE Released dump with CDE XML Download format.
Excel download part is not implemented.
In order to package, the project expects private Maven repository in "repo" directory, which is not included in GIT.

To package:
>mvn clean package
It creates springboot cdereleased-1.0.1.jar which includes all dependencies.

Environment required:
db_credential
db_user
db_url
db_driver=oracle.jdbc.OracleDriver

Format of db_url:
jdbc:oracle:thin:@<url>:<port>:<OID>

To run using Maven from project directory:
>mvn spring-boot:run -Drun.arguments="xml"

To run using jar provide a path to jar file. 

Example:
> java -Xmx4096m -jar target/cdereleased-1.0.1.jar xml



