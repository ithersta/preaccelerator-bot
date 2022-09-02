package ru.spbstu.preaccelerator.telegram.resources.modules

import ru.spbstu.preaccelerator.domain.entities.module.ModuleConfigBuilder
import kotlin.time.Duration.Companion.days

fun ModuleConfigBuilder.fourthModule() {
    module("Коммерциализация", 7.days) {
        lecture(
            "Бизнес\\-модель проекта и модели монетизации",
            ModuleStrings.Speaker.AlexanderMalecev,
            "https://drive.google.com/file/d/1sOm6Hc1Xom8AoeajJJI1dBI8SQGOSuU1/view?usp=sharing",
            "https://drive.google.com/file/d/1xXx8KxrHq4-NDCGtynzKdAV6byZumZ5-/view?usp=sharing"
        )
        additionalInfo(
            "• [Бизнес\\-модель А\\. Остервальдера](https://www.coursera.org/lecture/strategichesky-menegment/liektsiia-1-biznies-modiel-a-ostierval-diera-z03O2)\n" +
                    "• [Финансовая механика стартапов \\(Kirsty Nathoo\\)](https://www.youtube.com/watch?v=fGI8ddaP21c)\n" +
                    "• [Девять бизнес\\-моделей и какие метрики хотят видеть инвесторы \\— Ану Харихара](https://www.youtube.com/watch?v=OVBoHfX-pXk)\n" +
                    "• [Лекция Н\\.В\\.Цехомского «Новые бизнес\\-модели»](https://mgimo.ru/about/news/departments/tsekhomsky/)\n" +
                    "• [Книга «WTF?: Гид по бизнес\\-моделям будущего» Автор: О'Рейли Тим](https://www.ozon.ru/product/wtf-gid-po-biznes-modelyam-budushchego-161575991/)\n" +
                    "• [Бизнес\\-модели для стартапа: обзор](https://vc.ru/finance/123807-biznes-modeli-dlya-startapa-obzor)\n" +
                    "• [Какие бизнес\\-модели существуют и как выбрать подходящую твоему стартапу](https://1va.vc/webinars/kakie-biznes-modeli-suschestvuet-i-kak-vybrat-podhodyaschuyu-tvoemu-startapu.html)\n" +
                    "• [Модели монетизации и заработка для стартапов](https://www.youtube.com/watch?v=KONl6xKuK-4)\n" +
                    "• [17 бизнес\\-моделей](https://kontur.ru/articles/5030)\n" +
                    "\n" +
                    "*Полезные книги:*\n" +
                    "•Построение бизнес\\-моделей: Настольная книга стратега и новатора\\. Авторы: Александр Остервальдер \\(Alexander Osterwalder\\) и Ив Пинье \\(Yves Pigneur\\)\n" +
                    "•Бизнес с нуля: Метод Lean Startup для быстрого тестирования идей и выбора бизнес-модели\\. Автор: Эрик Рис (Eric Ries)\n" +
                    "•Четыре шага к озарению: Стратегии создания успешных стартапов\\. Автор: Стив Бланк \\(Steve Blank\\)\n"
        )
        task(
            "1\\. Опишите бизнес\\-модель вашего проекта/продукта, используя [Шаблон “Бизнес\\-модель”](https://docs.google.com/document/d/1SbVXBDkImvBaX3fliv3bE7XZSD7zu-gh1cBbqI8BGtc/edit?usp=sharing)\n" +
                    "2\\. Если вы уже провели проблемные интервью, отметьте гипотезы, которые уже подтвердились\\. Проведите 1\\-2 дополнительных интервью\\.\n" +
                    "3\\. Обновите бизнес\\-модель своего проекта по результатам проверки гипотез\\.\n"
        )
        lecture(
            "Тестирование каналов привлечения и продаж",
            ModuleStrings.Speaker.IrinaShafr,
            "https://drive.google.com/file/d/1ssku7uo6dPk24aRezSaCBiP6cX5NAN4-/view?usp=sharing",
            "https://drive.google.com/file/d/1xHg4z4sI30R9xdlghpKKXh89YySh2diA/view?usp=sharing"
        )
        additionalInfo(
            "• [Руководство по настройке рекламы в Яндекс\\.Директе](https://direct.yandex.ru)\n" +
                    "• [Яндекс\\.Директ простыми словами: как это работает и с чего начать](https://www.youtube.com/watch?v=VEx_dQ2F6VE)\n" +
                    "• [Гугл Адвордс: Настройка рекламы в Google Ads с нуля](https://www.youtube.com/watch?v=vMK_pPcag2Y)\n" +
                    "• [Запуск поисковой рекламы в Google Ads: мастер\\-класс](https://www.youtube.com/watch?v=_-SY_j9ytVM)\n" +
                    "• [О размере и видах гипотез роста\\. Воронка AAARRR](https://growthacademy.ru/voronka_aaarrr)\n" +
                    "• [Что такое источники трафика](https://www.unisender.com/ru/support/about/glossary/chto-takoe-istochniki-trafika/)\n" +
                    "• [Как настроить рекламу в социальных сетях](https://www.youtube.com/watch?v=WbUwPtTvtaY)\n" +
                    "• [Павел Гуров: лекция по SMM \\(продвижение и таргетинг в Instagram, VKontakte, YouTube, FaceBook\\)](https://www.youtube.com/watch?v=f5eEPK4-52g)\n" +
                    "\n" +
                    "*Полезные книги:* \n" +
                    "•М\\. Солодар\\. Воронка продаж в Интернете\\.\n" +
                    "•С\\. Кингснорт\\. Стратегия цифрового маркетинга\\.\n" +
                    "•М\\. Зобнина, Е\\. Васильева\\. Маркетинг и управление продуктом на цифровых рынках: генерация и проверка идей через CustDev, дизайн\\-мышление и расчеты юнит\\-экономики\\.\n" +
                    "•М\\. Стелзнер\\. Контент\\-маркетинг\\. Новые методы привлечения в эпоху Интернета\\.\n" +
                    "•Д\\. Халилов\\. Маркетинг в социальных сетях\\.\n"
        )
        task(
            "1\\. Если у вас есть данные по каналам, привлечению и продажам, то составьте воронку AAARRR по вашему продукту/проекту, используя [Шаблон “Воронка АААRRR”](https://docs.google.com/document/d/1e592nDaYAep0XXGFYgZvTezxYY9SxJL92cyy7Cp9VNk/edit?usp=sharing)\n" +
                    "2\\. Проанализируйте воронку и сформулируйте выводы, поймите нужно ли “чинить” воронку, с помощью каких целевых метрик поймете, что результат улучшился\n" +
                    "3\\. Если у вас нет реальных данных по воронке АААRRR, то смоделируйте ее \\(выдвиньте гипотезы\\)\\.\n" +
                    "4\\. На первом этапе можно выбрать 1 канал, который вы можете быстро проверить и смоделировать воронку только по нему\\.\n" +
                    "5\\. Определите метрики для этого канала \\(цели, источники трафика, метрика для анализа\\)\n" +
                    "6\\. Проведите эксперимент в этом канале \\(например, тестовая реклама/написание писем на e\\-mail/холодные звонки итд\\)\n" +
                    "7\\. Сравните смоделированную воронку \\(гипотезы\\) и реальный результат, внесите корректировки\\.\n" +
                    "8\\. Сформулируйте выводы\n"
        )
        lecture(
            "Маркетинговая стратегия, коммуникации и PR\n",
            ModuleStrings.Speaker.IrinaShafr,
            "https://drive.google.com/file/d/1ndq4MvQrdE4295GO4hdcxoqrl6AKAbL8/view?usp=sharing",
            "https://drive.google.com/file/d/1XSVsMRWn8LwkjTi2vDObI1kCIrqAlLDG/view?usp=sharing"
        )
        additionalInfo(
            "• [Маркетинг для стартапов: руководство от запуска рекламы до первых заявок](https://vc.ru/marketing/108328-marketing-dlya-startapov-rukovodstvo-ot-zapuska-reklamy-do-pervyh-zayavok)\n" +
                    "• [Зачем стартапу маркетинговая стратегия\\. Как IT\\-стартап запустился без анализа и стратегии\\. И к чему это привело](https://vc.ru/marketing/50536-zachem-startapu-marketingovaya-strategiya-kak-it-startap-zapustilsya-bez-analiza-i-strategii-i-k-chemu-eto-privelo)\n" +
                    "• [Продвижение стартапа без бюджета\\. Эффективная маркетинговая стратегия\\. Тренды digital\\-маркетинга\\.](https://www.youtube.com/watch?v=VwaMBvQv2sc)\n" +
                    "• [Маркетинговая стратегия стартапа, Роман Кумар Виас, Игорь Винидиктов](https://www.youtube.com/watch?v=jriIVmTXOJY)\n" +
                    "• [Основы маркетинга стартапа\\. Кейс Qlean](https://www.youtube.com/watch?v=oMjdv5KZg7I)\n" +
                    "• [Конкурентный анализ для стартапов](https://tceh.com/events/competitive_analysis/)\n" +
                    "• [Конкурентный анализ\\. Кто основные конкуренты и как их найти](https://ru.coursera.org/lecture/marketing-innov-produktov/konkurientnyi-analiz-kto-osnovnyie-konkurienty-i-kak-ikh-naiti-XmxJG)\n" +
                    "\n" +
                    "*Полезные книги:* \n" +
                    "•Н\\. Кумар\\. Маркетинг как стратегия\\.\n" +
                    "•М\\. Зобнина, Е\\. Васильева\\. Маркетинг и управление продуктом на цифровых рынках: генерация и проверка идей через CustDev, дизайн\\-мышление и расчеты юнит\\-экономики\\.\n" +
                    "•М\\. Спиридонов\\. Старт\\-ап на миллиард\\. Пошаговое руководство по созданию диджитал \\- бизнеса\\.\n" +
                    "•А\\. Горный\\. Старт\\-ап: как начать с нуля и изменить мир\\.\n" +
                    "•С\\. Бланк\\. Стартап: настольная книга основателя\\.\n"
        )
        task(
            "1\\. Обобщите результаты домашней работы по оценке рынка, анализу конкурентов и тестированию каналов\\.\n" +
                    "2\\. Заполните Marketing Canvas, используя [Шаблон “Marketing Canvas”](https://docs.google.com/document/d/1ZF60xs0K8Kit5y07ibk0GF0YRWh_ZHBwF9y8C4kwOc0/edit?usp=sharing)\\.\n" +
                    "3\\. Создайте карту трафика, оцените необходимые объемы трафика с точки зрения вашей оценки рынка\\.\n"
        )
    }
}