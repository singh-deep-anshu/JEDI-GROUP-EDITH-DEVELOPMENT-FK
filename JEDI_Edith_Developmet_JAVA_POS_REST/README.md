FlipFit - Maven + Dropwizard Conversion

This repository was converted to a Maven project and a minimal Dropwizard REST skeleton was added.

Quick start

1. Build (requires Java 17 and Maven):

   mvn -DskipTests clean package

2. Run Dropwizard server with sample config:

   java -jar target/flipfit-rest-0.1.0-SNAPSHOT-shaded.jar server src/main/resources/config.yml

3. Health check / ping:

   curl http://localhost:8080/ping

Run the original CLI app without packaging:

   mvn -Dexec.mainClass=com.flipfit.client.FlipFitApplication exec:java

Notes

- The current codebase is still located under `src/` (legacy layout). The pom.xml temporarily sets `<sourceDirectory>src</sourceDirectory>` to compile the existing code. Move sources to `src/main/java` incrementally and then remove that override.
- DB credentials in `config.yml` are placeholders — replace with real values or use environment variables.
