# VK-Archive
Приложение, написанное с использованием фреймворка Compose Multiplatform, которое на основании данных из [Архива Вконтакте](https://vk.com/data_protection?section=rules&scroll_to_archive=1) удобно группирует информацию, позволяет скачивать фотографии и ...

## Полезные ссылки по проекту 
  - [База знаний по проекту](https://miro.com/app/board/uXjVPjK_iOw=/?share_link_id=115273625923)

## Информация о языке и используемых библиотеках
Проект написан на языке программирования Kotlin, с использованием фреймворка Compose Multiplatform. Используется Java 11 версии.

## Инструкция по сборке и запуску
### Сборка проекта:
Для сборки проекта в терминале перейдите в папку проекта ``\vk_archive`` и введите одну из следующих команд

```./gradlew buildJarWindows``` - собрать проект для Windows

```./gradlew buildJarLinux``` - собрать проект для Linux

```./gradlew buildJarMacOs``` - собрать проект для MacOS

Эти команды создают .jar файлы для запуска на устройстве.

Нужно вводить ту команду, на платформе которой вы работаете в данный момент. То есть, например, для генерации jar файла, работающем на Linux, нужно ввести команду ```./gradlew buildJarLinux``` непосредственно на Linux платформе.

### Запуск приложения
Для запуска приложения на компьютере должна быть установлена [Java](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) **не ниже 11 версии**. В папке проекта + \build\libs после сборки будет находиться собранный .jar файл, который можно запустить, дважды нажав на него или командой 

```java -jar vk_archive-{version}-{your OS}}.jar```

### Создание exe файла
Для создания exe файла приложения можно воспользоваться любым онлайн конвертером Jar to Exe, например, [genuinecoder.com](https://genuinecoder.com/online-converter/jar-to-exe/).
На сайте нужно выбрать jar файл, Header type оставить *GUI*, выбрать иконку для него в формате *ico*, и указать версию Java, которую вы загрузили, в поле *Minimum JRE version* (например, 11.0.0 или 17.0.0). 

Также можно воспользоваться программой [Launch4j](https://launch4j.sourceforge.net/).
 - В поле *Output file* указываем расположение и название желаемого Exe файла (расширение вручную дописываем как .exe).
 - В поле *Jar* указываем расположение Jar файла, из которого мы хотим получить Exe.
 - В поле *Icon* можно указать расположение желаемой иконки для приложения в формате *ico*.
 - Во вкладке *JRE* в поле *Min JRE version* нужно указать версию Java, которую вы загрузили (например, 11.0.0 или 17.0.0)
 - Рядом с данным поле нужно поставить галочку напротив *JDK required*
 - Далее нужно нажать на *Build Wrapper* (шестеренка вверху приложения), в выпавшем окне указать место сохранения конфига, и произойдет генерация Exe файла.

### Наработки 
(обзор аналогов, формат JSON файлов, краткое описание алгоритмов) - TODO

## Инструкция по использованию
- Выбрать папку архива, нажав на `Choose Folder`.

...