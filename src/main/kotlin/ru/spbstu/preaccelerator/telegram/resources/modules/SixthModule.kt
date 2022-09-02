package ru.spbstu.preaccelerator.telegram.resources.modules

import ru.spbstu.preaccelerator.domain.entities.module.ModuleConfigBuilder
import kotlin.time.Duration.Companion.days

fun ModuleConfigBuilder.sixthModule() {
    module("Юридические основы\\. Управление проектами\\.", 7.days) {
        lecture(
            "Как защитить интеллектуальную собственность?",
            ModuleStrings.Speaker.DaryaZern,
            "https://drive.google.com/file/d/1BcsawEwy8593BMAzgP-YskzvZshDAzyg/view?usp=sharing",
            "https://drive.google.com/file/d/1IFX5wGSIltZ6xp1_OyZ0i1K2f5tKHUdw/view?usp=sharing"
        )
        additionalInfo(
            "• [От блокчейна до суда: десять способов защитить интеллектуальную собственность](https://vc.ru/digitalip/251650-ot-blokcheyna-do-suda-desyat-sposobov-zashchitit-intellektualnuyu-sobstvennost)\n" +
                    "• [Как бесплатно защитить свою интеллектуальную собственность и не «попасть» на деньги](https://vc.ru/legal/238761-kak-besplatno-zashchitit-svoyu-intellektualnuyu-sobstvennost-i-ne-popast-na-dengi)\n" +
                    "• [Как стартапу правильно защитить свои разработки от копирования\\. Что нужно \\- товарный знак, патент, депонирование?](https://vc.ru/legal/282582-kak-startapu-pravilno-zashchitit-svoi-razrabotki-ot-kopirovaniya-chto-nuzhno-tovarnyy-znak-patent-deponirovanie)\n" +
                    "• [Как я выбирал юрисдикцию для штаб\\-квартиры](https://vc.ru/legal/107820-kak-ya-vybiral-yurisdikciyu-dlya-shtab-kvartiry)\n" +
                    "• [Умное патентование: как бизнесу избежать ошибок в сфере интеллектуального права](https://rb.ru/opinion/umnoe-patentovanie/)\n" +
                    "• [Распределение долей между основателями стартапа](https://vc.ru/tribuna/70055-raspredelenie-doley-mezhdu-osnovatelyami-startapa)\n" +
                    "• [Как распределить доли в стартапе](https://vc.ru/finance/159894-kak-raspredelit-doli-v-startape)\n" +
                    "• [Топ\\-5 фактов про общее собрание участников ООО, о которых не знают стартапы и инвесторы](https://vc.ru/legal/189761-top-5-faktov-pro-obshchee-sobranie-uchastnikov-ooo-o-kotoryh-ne-znayut-startapy-i-investory)\n" +
                    "• [Юридический аудит \\(due diligence\\) стартапа \\- от А до Я](https://vc.ru/legal/84524-yuridicheskiy-audit-due-diligence-startapa-ot-a-do-ya)\n" +
                    "• [7 юридических советов стартапам: договор, форма деятельности, конкуренция](https://vc.ru/flood/9701-7-law-advice)\n"
        )
        task(
            "1\\. Выберите юрисдикцию и организационно\\-правовую форму вашей компании\n" +
                    "2\\. Определитесь как вы будете распределять доли в компании\n" +
                    "3\\. Будете ли вы предусматривать опционы для сотрудников? В каком размере?\n" +
                    "4\\. Каким образов будете защищать свою интеллектуальную собственность и в какой юрисдикции? Оцените примерно расходы, которые вы понесете для защиты интеллектуальной собственности\\.\n"
        )
        lecture(
            "Теория стратегического менеджмента и управление проектами",
            ModuleStrings.Speaker.SemyonSazh,
            "https://drive.google.com/file/d/1b5XlBYjmSHT7WZRx62L9Icyo6l32gShQ/view?usp=sharing",
            "https://drive.google.com/file/d/1VEldmIYRu7w3c-oVGl-cVPmQLGOIvWo-/view?usp=sharing"
        )
        lecture(
            "Инструменты управления проектами и командами",
            ModuleStrings.Speaker.SemyonSazh,
            "https://drive.google.com/file/d/1-fe6ckeFvUhpo828dzTTHCuQLQ5kgCGe/view?usp=sharing",
            "https://drive.google.com/file/d/14tFCt8HWYymsaOFP4fUBj8YvfuZPM8WT/view?usp=sharing"
        )
        additionalInfo(
            "• [Обзор методов agile](https://vc.ru/u/117584-dmitriy-gukov/135749-obzor-metodov-agile)\n" +
                    "• [Не только Agile: 10 книг про управление проектами](https://vc.ru/flood/47288-ne-tolko-agile-10-knig-pro-upravlenie-proektami)\n" +
                    "• [Kanban/Agile/Scrum/Lean \\- гибкие методологии разработки](https://vc.ru/u/752307-karina-gorbunova/218436-kanban-agile-scrum-lean-gibkie-metodologii-razrabotki)\n" +
                    "• [Что такое OKR и почему все о них говорят](https://vc.ru/hr/103991-chto-takoe-okr-i-pochemu-vse-o-nih-govoryat)\n" +
                    "• [Как ставить цели по OKR: 8 советов новичку для хорошего старта](https://vc.ru/hr/217769-kak-stavit-celi-po-okr-8-sovetov-novichku-dlya-horoshego-starta)\n" +
                    "• [3 фатальные ошибки при внедрении OKR](https://vc.ru/hr/157742-3-fatalnye-oshibki-pri-vnedrenii-okr)\n" +
                    "• [OKR: как ставить цели, чтобы их достигать](https://vc.ru/life/121513-okr-kak-stavit-celi-chtoby-ih-dostigat)\n" +
                    "• [Why the secret to success is setting the right goals, Джон Дор](https://youtu.be/L4N1q4RNi9I)\n" +
                    "• [Чек\\-лист с полезными материалами по созданию OKR от компании Carrot quest](https://drive.google.com/file/d/1q17mymhqcvAh0BHrHbNlHdhjoP-NU1u7/view?usp=sharing)\n" +
                    "• [Шаблон «Трекер OKR»](https://docs.google.com/spreadsheets/d/132RCwqW3t8MWG1l6MS1b4zqiaZhjJoYS/edit?usp=sharing&ouid=117136603392830305877&rtpof=true&sd=true)\n"
        )
    }
}