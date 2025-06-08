# Дипломный проект java-explore-with-me

Этап 1. Сервис статистики
  * Собирает информацию о количестве обращений пользователей к спискам событий
  * Собирает информацию о количестве запросов к подробной информации о событии
  * На основе этой информации формируется статистика о работе приложения

Этап 2. Основной сервис
  * Работа с событиями
  * Работа с категориями
  * Работа с пользователями
  * Работа с подборкой событий
  * Работа с запросами на участие

Этап 3. Дополнительная функциональность
  * Комментарии к событиям
  * Написать тесты для Postman

> Admin: Комментарии
> > PATCH: /admin/comments/{commentId}
> 
> Тест
> > DELETE: /admin/comments/{commentId}
> 
> Текст
> > GET: /admin/comments/{commentId} - получение комментария администратором
> 
> Текст
> > GET: /admin/comments - поиск множества комментарий администратором
> 
> Текст

> Private: Комментарии
> > POST: /users/{userId}/events/{eventId}/comments
> 
> Создание комментария пользователя с идентификатором userId по событию eventId, на выходе получаем DTO.ResponseComment, на вход передаем DTO.RequestCommentUser
> > PATCH: /users/{userId}/events/{eventId}/comments/{commentId}
> 
> Редактирование комментария пользователя с идентификатором userId по событию eventId, на выходе получаем DTO.ResponseComment, на вход передаем DTO.RequestCommentUser и идентификатор комментария commentId
> > GET: /users/{userId}/events/{eventId}/comments/{commentId}
> 
> Получение комментария пользователя с идентификатором userId по событию eventId, на выходе получаем DTO.ResponseComment, на вход передаем идентификатор комментария commentId
> > GET: /users/{userId}/events/{eventId}/comments
>
> Получение всех комментариев пользователя с идентификатором userId по событию eventId, на выходе получаем DTO.ResponseEventCommentsFull

> Public: Комментарии
>  > GET: events/{eventId}/comments
> 
> Получение всех опубликованных администратором (по признаку accepted) пользовательских комментариев по событию eventId, на выходе получаем DTO.ResponseEventCommentsShort
