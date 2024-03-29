# VK-Archive
Приложение, написанное с использованием фреймворка Compose Multiplatform, которое на основании данных из [Архива Вконтакте](https://vk.com/data_protection?section=rules&scroll_to_archive=1) удобно группирует информацию, позволяет скачивать фотографии и ...

## Полезные ссылки по проекту 
  - [База знаний по проекту](https://miro.com/app/board/uXjVPjK_iOw=/?share_link_id=115273625923)

## Информация о языке и используемых библиотеках
Проект написан на языке программирования Kotlin, с использованием фреймворка Compose Multiplatform. Используется Java 11 версии.

## Инструкция по сборке и запуску
### Сборка проекта:
Для сборки проекта в терминале перейдите в папку проекта ``\vk_archive`` и введите одну из следующих команд

```./gradlew buildAllJars``` - собрать проект сразу для всех платформ (Windows, Linux, MacOS)

```./gradlew buildJarWindows``` - собрать проект для Windows

```./gradlew buildJarLinux``` - собрать проект для Linux

```./gradlew buildJarMacOs``` - собрать проект для MacOS

Эти команды создают .jar файлы для запуска на устройстве.

### Запуск приложения
Для запуска приложения на компьютере должна быть установлена [Java](https://www.java.com/ru/download/manual.jsp). В папке проекта + \build\libs после сборки будет находиться собранный .jar файл, который можно запустить, дважды нажав на него или командой 

```java -jar vk_archive-{version}-{your OS}}.jar```

### Наработки 
(обзор аналогов, формат JSON файлов, краткое описание алгоритмов) - TODO

## Инструкция по использованию
- Выбрать папку архива, нажав на `Choose Folder`.

...