Проектная работа: "СЕРВИС ДЛЯ РАБОТЫ С ЗАМЕТКАМИ"

Сервис позволяет хранить данные отдельно для какждой учётной записи

Функционал:
- Создать чётную запись
- Войти в свою учетную запись с помощью логина
- Создать/Обновить/Удалить/Просматривать все свои заметки

В рамках тестирования на UI реализована одна техническая учётная запись.
Полный функционал доступен через API

Инструкция по настройке проекта:
1. Клонировать проект в локальный репозиторий
2. В корневом каталоге проекта вызвать консоль, и запустить сборку командой "docker compose up" (должен быть установлен докер)
3. Фронт - http://localhost:3000/
4. Бэкенд - http://localhost:8080/

Список эндпоинтов описан в сваггере проекта. https://github.com/KuznetsovMK/user-note-service/blob/master/src/main/resources/swagger/client-user-note.1.0.0.yaml
