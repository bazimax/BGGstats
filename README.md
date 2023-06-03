# BGGstats
App for BoardGameGeek stats

Ru: Приложение собирает статистику по настольным играм с сайта boardgamegeek.com, далее данные выгружаются в таблицу .csv, которую уже можно дополнительно анализировать в сторонних программах

Приложение в процессе разработки:  
- интерфейс - Compose - ГОТОВО - заготовка дизайна (в целом примерно так и будет выглядеть, осталось все подключить)  
- получение данные - базовый список - retrofit? - НЕ ГОТОВО (будет работать через парсинг https://boardgamegeek.com/browse/boardgame/page/1, в целом все понятно, возьму решение из trackNews) - это первый проход, хранящий в себе id игр  
- получение данные - подробный список - retrofit - ГОТОВО (работает через XML_API (SimpleXml) https://boardgamegeek.com/wiki/page/BGG_XML_API) - это второй проход, используя id получая подробные сведения (в целом все работает, осталось расписать полностью все элементы)  
- хранение данных - Room - ГОТОВО - запись, чтение в процессе (все через LiveData)  
- выгрузка в CSV - НЕ ГОТОВО (выгрузка на sd карту)  
-  

<a href="bggStats preview"><img src="https://github.com/bazimax/BGGstats/assets/110244156/6c75033f-c91c-4c01-9fb1-4a00c7234853" align="left" width="200" ></a>
