# olx.ua crawler

- [start Kotlin maven](https://kotlinlang.org/docs/reference/using-maven.html)
- add **Jsoup**
- https://replace.org.ua/topic/8348/


#### Комбинации Selector
|Selector | Описание |
|---------|----------| 
|el#id	|Элементы с ID, например div#logo|
|el.class	|Элементы с классом, например div.masthead|
|el[attr]	|Элементы с атрибутом, например a[href] например **a[href].highlight**|
|ancestor child	|(Родительский элемент - наследованный элемент) Подэлементы родительского элемента, например . ***.body p*** ищет элемент **p** везде под блоком с классом "body"
|parent > child	|Прямые элементы наследники родительского элемента, например div.content > p найти элементы p которые являются прямыми наследниками div имеющие class ='content'; и body > * найти прямые подэлементы тега body|
|siblingA + siblingB	|Найти элементы братья B сразу перед элементом A, например div.head + div|
|siblingA ~ siblingX	|Найти элементы братья X перед элементом A, например h1 ~ p|
|el, el, el	|Группа с разными Selector, ищет элементы подходящие к одному из Selector; например div.masthead, div.logo|
###### Pseudo selectors
|Selector	  |Описание      |
|------------:|:------------:|
|:lt(n)|	Поиск элементов с родственным индексом (местоположение в дереве DOM связь с родтельским элементом) меньше n; например td:lt(3)|
|:gt(n) |	Поиск элементов с родственным индексом больше n, например div p:gt(2)
|:eq(n)	|Поиск элементов с родственным индексом равным n; e.g. form input:eq(1)
|:has(seletor)	|Поиск элементов содержащих элементы совпадающие с selector; например div:has(p)
|:not(selector)	|Поиск элементов несовпадающих с Selector; например div:not(.logo)
|:contains(text)	|Поиск элементов содержащих данный текст. Поиск не отличая заглавные или строчные буквы, например p:contains(jsoup)
|:containsOwn(text)	|Поиск элементов которые напрямую содержат данный текст
|:matches(regex)	|Поиск элементов где текст не совпадает с определенным обычным выражением; например div:matches((?i)login)
|:matchesOwn(regex)	|Поиск элементов где текст совпадает с определенным обычным выражением.

Примечение: Способ индекса pseudo начинается с 0, первый элемент имеет индекс 0, второй элемент имеет индекс 1

