package ru.spbstu.preaccelerator.telegram.resources.modules

import ru.spbstu.preaccelerator.domain.entities.module.ModuleConfigBuilder
import kotlin.time.Duration.Companion.days

fun ModuleConfigBuilder.seventhModule() {
    module("Инвестиции", 7.days) {
        lecture(
            "Инструменты привлечения государственного финансирования",
            ModuleStrings.Speaker.KseniaShip,
            "https://drive.google.com/file/d/1xNiiT0APYNLoxrtaqJe_wA7aVfW786-r/view?usp=sharing",
            "https://drive.google.com/file/d/1y9vqb1JwWnU7fnfkA0CNujYsKrIY0g9W/view?usp=sharing"
        )
        lecture(
            "Инструменты привлечения частного финансирования",
            ModuleStrings.Speaker.AlexanderMalecev,
            "https://drive.google.com/file/d/1U8mBAw-PST34B5eK2kG4mR715VH8VoKf/view?usp=sharing",
            "https://drive.google.com/file/d/1te405YfSiGQNXRgWRuaRO6H4NXO0w6qT/view?usp=sharing"
        )
        additionalInfo(
            "«[Как получить инвестиции — книга Морейниса, глава 10»](https://vc.ru/flood/7977-moreynis-part10)\n" +
                    "Телеграм-канал «Стартап дня Горного Александра» @startupoftheday\n" +
                    "[«Как стартапу привлечь инвестиции на начальном этапе развития: советы главного инкубатора Кремниевой долины»](https://www.forbes.ru/karera-i-svoy-biznes/394395-kak-startapu-privlech-investicii-na-nachalnom-etape-razvitiya-sovety)\n" +
                    "«Венчурное финансирование: стоимость компаний и корпоративное управление»/n" +
                    "[Ч.1](https://gaap.ru/articles/chast_1_venchurnoe_finansirovanie_otsenka_stoimosti_kompaniy_i_korporativnoe_upravlenie/)\n" +
                    "[Ч.2](https://gaap.ru/articles/chast_2_venchurnoe_finansirovanie_stoimost_kompaniy_i_korporativnoe_upravlenie/)\n" +
                    "[Как привлекать инвестиции //(Марк Андриессен, Рон Конвей, Паркер Конрад//)](https://www.youtube.com/watch?v=-2LVfjrM6jw)\n" +
                    "[Юридические и финансовые вопросы привлечения инвестиций //(Кирсти Нату, Кэролайн Леви//)](https://www.youtube.com/watch?v=EHzvmyMJEK4)\n" +
                    "[расшифровка ч.1](http://megamozg.ru/company/friifond/blog/22252/)\n" +
                    "[Ч.2](http://megamozg.ru/company/friifond/blog/22448/)\n" +
                    "[Переговоры с инвестором //(Тайлер Босмени//)](https://www.youtube.com/watch?v=SHAh6WKBgiE) [расшифровка](https://habr.com/ru/company/friifond/blog/297744/)\n" +
                    "[Юридическая мехника стартапа — Кэролин Леви, Джон Леви и Джейсон Квон](https://www.youtube.com/watch?v=6nWgXQ5wxSg)\n" +
                    "[Основы привлечения финансов — Джеф Ральстон](https://www.youtube.com/watch?v=XLJ0YownzlU)\n" +
                    "[Как проводить встречи с инвесторами и убеждать их вложить деньги в вас — Арон Харрис](https://www.youtube.com/watch?v=Rp1O7ComU7A)\n" +
                    "[Куда идти стартапам в США](https://rb.ru/countries/usa/)\n" +
                    "[Письмо инвестору: 9 способов с первых строк потерять шанс на финансирование](https://rb.ru/opinion/investment-proposal-letter/)\n" +
                    "[60 полезных сайтов для поиска инвестиций в стартап](https://vc.ru/finance/128687-60-poleznyh-saytov-dlya-poiska-investiciy-v-startap)\n" +
                    "\n" +
                    "*Список литературы*\n" +
                    "Легкий венчур. Практическое руководство для начинающих ангелов и будущих единорогов. Автор:Игорь Рябенький\n" +
                    "Привлечение инвестиций в стартап. Как договориться с инвестором об условиях финансирования.  Брэд Фелд и Джейсон Мендельсон \n" +
                    "«Инвестиционная оценка» Асват Дамодаран\n"
             )
        task(
            "Исходя из стадии развития вашего стартапа, определите какие инструменты привлечения финансирования вам подходят.\n" +
                    "Создайте список источников, из которых возможно достать контакты бизнес-ангелов и фондов.\n" +
                    "Подготовьте список целевых фондов/бизнес-ангелов/грантодателей для вашего стартапа.\n"
        )
    }
}