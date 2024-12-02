# made-funicular-postzegel
Demonstration project on how items can be picked only once using optimistic locking

Use in application.properties to persits a file:
```
spring.datasource.url=jdbc:h2:file:~/test
```

# Tips
- You can access the h2 console using the disk based one.

# TODO
- Add exception catcher for REST endpoint
- Package the app https://docs.spring.io/spring-boot/gradle-plugin/packaging-oci-image.html#build-image.customization
