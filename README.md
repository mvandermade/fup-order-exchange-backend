# made-funicular-postzegel
Demonstration project on how items can be picked only once using optimistic locking

# Getting started
Use an environment to build your application.
```bash
./gradlew bootRun
```
(on windows use gradlew.bat)

Edit the files and then run it again.

See if anything breaks using tests
```bash
./gradlew test
```

## Tips
- You can access the h2 console http://localhost:8080/h2-console, user=user password=admin
- You can access the swagger ui at http://localhost:8080/swagger-ui/index.html

## Programming choices
- Chose to work with DTO instead of entity passing between services to prevent entitygraph misses.

Use in application.properties to persits a file which you can link to in most editors
```
spring.datasource.url=jdbc:h2:file:~/test
```

## Testing
The annotation dirties context is in place to ensure a clean database each run.
This might be possible to solve when tests clean up after themselves.
I tried using @Transaction on the test class but this gave odd results between test fun's.
-> Use SpringBootTestWithCleanup

# Controlleradvice
Controlleradvice is disabled for exceptions under the profile `development`. The advices are annotated with @Hidden for Springdoc

# Upgrading
When using dependabot:
Use gradle htmlDependencyReport
Or re-run the dependency submission action in debug mode:
https://github.com/gradle/actions/blob/main/docs/dependency-submission.md

Or manually update using `dependencyUpdates`
