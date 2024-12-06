# Testing

## Repository is not detected in classpath:
```kt
@SpringBootTest(
    classes = [OrderService::class, OrderMapper::class, OrderRepository::class]
)
class Test
```

# Code
- Package the app https://docs.spring.io/spring-boot/gradle-plugin/packaging-oci-image.html#build-image.customization
- Add mysql to test
- Add testcontainers
- Add liquibase