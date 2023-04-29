package utils.languages

object StringResourcesRU : BaseStringResources {
    override val languageType = StringResources.Language.RUSSIAN

    override val aboutText = """ 
### Инструкция по использованию приложения

#### Общая информация
Окно состоит из двух частей: слева отображается список диалогов, которые можно обработать, а справа - список уже обработанных, для каждой из частей доступен поиск по имени собеседника/названию беседы или сообщества. У каждого диалога левой части имеется кнопка `Parse` для отдельного парсинга. По нажатию ПКМ по диалогам правой части можно их выбирать и применять действия из верхнего меню

#### Верхнее меню
- `File`
    - **Export** позволяет экспортировать выбранные диалоги в json-файлы, которые будут храниться в папке *parsed_messages* в директории архива. Название json-файла соответствует ID диалога
    - **Import** достает диалоги из json-файлов, если они уже имеются, и отображает их справа (если все диалоги есть в виде json-файлов, для более быстрого парсинга лучше использовать его, а не **ParseAll**)
- `About`
    - Вызов окна с данной справкой:)

#### Начало работы
- Указать путь к архиву, нажав на кнопку `Choose Folder`.
- Спарсить нужные диалоги: либо отдельно по кнопке `Parse`, либо все вместе по кнопке слева `Actions->ParseAll`, либо использовать `Import`
- Применить нужные действия по кнопке справа `Actions` к выбранным диалогам
"""

    override val currentDirectoryPlaceholder = "Пожалуйста, выберите папку VK архива"
    override val parseAll = "Парсинг всего"
    override val downloadAttachments = "Загрузить вложения"
    override val selectTypeOfAttachments = "Выберите тип вложения"
    override val submit = "Подтвердить"
    override val cancel = "Отменить"
    override val chosenFolder = "Выбранная папка"
    override val chooseFolder = "Выбрать папку"
    override val searchAll = "Поиск по всем"
    override val nothingHere = "Здесь пока пусто"
    override val actions = "Действия"
    override val select = "Выбрать"
    override val selectAll = "Выбрать все"
    override val clearAll = "Убрать все"
    override val amountMsg = "Кол-во сообщ"
    override val amountAttchm = "Кол-во влож"
    override val about = "О приложении"
    override val status = "Статус"
    override val file = "Файл"
    override val import = "Импорт"
    override val export = "Экспорт"
    override val language = "Язык"
    override val ruLanguage = "Русский"
    override val enLanguage = "Английский"
    override val parsingDialogs = "Парсинг диалогов"
    override val parsingDialog = "Парсинг диалога"
    override val importDialogs = "Импорт диалогов"
    override val exportDialogs = "Экспорт диалогов"
    override val parse = "Обработать"

    override fun todoTranslate(): String {
        println("Отсутствует русский перевод")
        return "Ошибка"
    }
}
