# Бэкенд приложения для хранения списка задач
Задачи могут сохраняться в оперативной памяти или в txt-файле.

### Эндпойнты
- Добавление или обновление обычной задачи POST /tasks
```
{
    "id":Integer
    "taskName":String
    "taksDescription":String
    "taskStatus":(NEW, IN_PROGRESS, DONE)
    "startTime":LocalDateTime
    "duration":Duration
}
```
- получение обычной задачи по id GET /tasks/{taskId}
- удаление обычной задами по id DELETE /tasks/{taskId}
- добавление составной задачи POST /epics
```
{
    "taskName":String
    "taskDescription":String
    "taskStatus":(NEW, IN_PROGRESS, DONE)
}
```
- получение списка всех составных задач GET /epics
- получение составной задачи по id GET /epics/{taskId}
- удаление составной задачи по id DELETE /epics/{taskId}
- добавление подзадачи POST /subtasks
- обновление подзадачи по id POST /subtasks/{taskId}
```
{
    "id":Integer
    "taskName":String
    "taskDescription":String
    "taskStatus":(NEW, IN_PROGRESS, DONE)
    "epicId":Integer
}
```
- получение подзадами по id GET /subtasks/{taskId}
- удаление подзадачи по id DELETE /subtasks/{taskId}
- получение истории просмотренных задач GET /history
- получение отсортированного по дате списка задач с указанным временем начала GET /prioritized
