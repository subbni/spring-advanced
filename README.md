# SPRING ADVANCED

## LV.5 코드 개선 - Dto 생성 방식 개선
### 문제 인식 및 정의
모든 Response Dto가 생성자를 통해 직접 생성되고 있습니다.

이로 인해 Dto 필드가 변경될 때마다 Service 레이어에서도 수정이 필요한 문제가 발생합니다.

아래는 기존 프로젝트의 `TodoResponse` 클래스입니다.
```java
@Getter
public class TodoResponse {

    private final Long id;
    private final String title;
    private final String contents;
    private final String weather;
    private final UserResponse user;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public TodoResponse(Long id, String title, String contents, String weather, UserResponse user, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.weather = weather;
        this.user = user;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
```
- 문제점
  - Dto의 필드에 변경이 생기면 Dto 클래스와 함께 Service 레이어 코드도 수정해주어야 한다. 유지보수 비용이 증가한다.
  - 생성자에 모든 필드를 전달해야한다. 코드의 가독성이 떨어진다.
### 해결 방안
- 목표
  - Dto 객체 생성을 한 곳에서만 책임지게 한다.
  - Service 레이어에서 최소한의 데이터만 전달하도록 한다.
  - 코드의 가독성과 유지보수성을 향상한다.

이를 위해 정적 팩토리 메서드 패턴을 적용합니다.
```java
@Getter
public class TodoResponse {

    private final Long id;
    private final String title;
    private final String contents;
    private final String weather;
    private final UserResponse user;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public TodoResponse(Long id, String title, String contents, String weather, UserResponse user, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.weather = weather;
        this.user = user;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static TodoResponse from(Todo todo) {
        return new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getContents(),
                todo.getWeather(),
                UserResponse.from(todo.getUser()),
                todo.getCreatedAt(),
                todo.getModifiedAt()
        );
    }
}
```

### 해결 완료
- 기존 Service 레이어 코드
```java
    public TodoResponse getTodo(long todoId) {
        Todo todo = todoRepository.findByIdWithUser(todoId)
                .orElseThrow(() -> new InvalidRequestException("Todo not found"));

        User user = todo.getUser();

        return new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getContents(),
                todo.getWeather(),
                new UserResponse(user.getId(), user.getEmail()),
                todo.getCreatedAt(),
                todo.getModifiedAt()
        );
    }
```
- 개선된 Service 레이어 코드
```java
    public TodoResponse getTodo(long todoId) {
        Todo todo = todoRepository.findByIdWithUser(todoId)
                .orElseThrow(() -> new InvalidRequestException("Todo not found"));

				return TodoResponse.from(todo);
    }
```
- 개선된 점
  1. 서비스 레이어의 유지보수성 향성
     - 새로운 엔티티의 정보가 Dto 필드로 요구되지 않는 한 Dto 필드가 변경될 때 Service 레이어의 수정 없이 Dto 내부 코드만 수정하면 됩니다.
  2. 코드의 가독성 향상
     - Service 레이어에서 Dto 생성 코드 라인 수가 줄어들어 Service 로직에만 집중할  수 있습니다.
    
## Lv.6 테스트 커버리지
<img width="650" alt="Screenshot 2025-02-27 at 13 07 45" src="https://github.com/user-attachments/assets/4d152e88-dc20-44b3-aec8-a2037687f51a" />
