# Testing

## Repository is not detected in classpath:
```kt
@SpringBootTest(
    classes = [OrderService::class, OrderMapper::class, OrderRepository::class]
)
class Test
```