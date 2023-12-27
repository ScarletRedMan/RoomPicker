# RoomPicker

## Build
### Windows
```shell
gradlew.bat build
```

### Linux
```shell
./gradlew build
```

## Run
After build, we can see done jar files for usage at locations `./app/build/libs/app-0.0.1-SNAPSHOT.jar` 
and `./control-panel/build/libs/control-panel-0.0.1-SNAPSHOT.jar`.

For run application we need start `app` first, then if we want we can start `control-panel` next.

For start application we need have Java 17.

### app (main application)
Start application:
```shell
java -jar app-0.0.1-SNAPSHOT.jar
```

Start with debug mode using test data(not for production):
```shell
java -jar -Dspring.profiles.active=test app-0.0.1-SNAPSHOT.jar
```

#### Environments
| Name                 | Description            | Default value |
|----------------------|------------------------|---------------|
| ROOM_PICKER_USER     | Default admin username | admin         |
| ROOM_PICKER_PASSWORD | Default admin password | admin         |

### control-panel (optional)
Copy the folder with frontend to working directory. Working directory need have below structure:
```text
working-dir/
    * frontend/
    * control-panel-0.0.1-SNAPSHOT.jar
```

Start application:
```shell
java -jar control-panel-0.0.1-SNAPSHOT.jar
```

#### Environments
| Name            | Description             | Default value          |
|-----------------|-------------------------|------------------------|
| ROOM_PICKER_URL | URL to main application | http://localhost:8080/ | 

## Authorisation
For use api methods from main application you need add http headers with username (`RoomPicker-User`) 
and password (`Room_Picker-Password`).

Example:
```http request
GET http://localhost:8080/nodes/hub/buckets/test-3/users
RoomPicker-User: admin
RoomPicker-Password: admin
```