package ru.spbstu.preaccelerator.telegram.resources.modules

import ru.spbstu.preaccelerator.domain.entities.module.ModuleConfigBuilder
import kotlin.time.Duration.Companion.days

fun ModuleConfigBuilder.thirdModule() {
    module(
        "Анализ окружения", 7.days,
        "https://docs.google.com/forms/d/e/1FAIpQLSeXkoD0ClGzamFBaJXyQWvYg7LKZuop2_1BL_L30jZ9mDOkfg/viewform?usp=sf_link"
    ) {
        lecture(
            "Анализ рынка",
            ModuleStrings.Speaker.IrinaShafr,
            "https://drive.google.com/file/d/10VdEbgQhO4kPukgr3YxmqZJw3M_4xBox/view?usp=sharing",
            "https://drive.google.com/file/d/17EKYh9orV7SLQQEMJvFeOyDSWbWWoOpF/view?usp=sharing"
        )
        additionalInfo(
            "• [Оценить рынок стартапа за полчаса \\- метод TAM\\-SAM\\-SOM](https://vc.ru/finance/159115-ocenit-rynok-startapa-za-polchasa-metod-tam-sam-som)\n" +
                    "• [Оценка объёма рынка\\. Кому надо и зачем?](https://vc.ru/marketing/63539-ocenka-obema-rynka-komu-nado-i-zachem)\n" +
                    "• [Анализ рынка: обзор лучших практик](http://powerbranding.ru/rynok/plan-analiza/)\n" +
                    "• [Как бесплатно провести исследование рынка в 2020 году: 35 авторитетных источников](https://vc.ru/services/141040-kak-besplatno-provesti-issledovanie-rynka-v-2020-godu-35-avtoritetnyh-istochnikov)\n" +
                    "• [Как узнать за один вечер почти все о целевом рынке, не потратив ни рубля](https://zen.yandex.ru/media/id/5ad3807fdd2484cb62707d89/kak-uznat-za-odin-vecher-pochti-vse-o-celevom-rynke-ne-potrativ-ni-rublia-5b07eda555876b13d0c42fa6?utm_source=serp)"
        )
        task(
            "1\\. Найдите экспертные оценки емкости рынка, на который вы планируете выходить\\.\n" +
                    "2\\. Найдите данные о выручке нескольких игроков рынка, на который планируете выходить\\.\n" +
                    "3\\. Оценить динамику рынка, на который вы планируете выходить, на основе поисковых запросов\\.\n" +
                    "4\\. Проведите оценку рынка, на который вы планируете выходить, «снизу» и «сверху», используя [Шаблон «Анализ рынка»](https://docs.google.com/document/d/1gFon8p--zeNi1uzfo2yxYSu29mXxFkd4FG7tSwV0Az8/edit?usp=sharing)\n" +
                    "5\\. Сделайте сопоставление результатов\n",
            "«Анализ рынка»"
        )
        lecture(
            "Анализ конкурентов",
            ModuleStrings.Speaker.IrinaShafr,
            "https://drive.google.com/file/d/1BYPgMyM2eX9YvuYErJpubd_dAICCg55V/view?usp=sharing",
            "https://drive.google.com/file/d/16Wp57AIrj3NjJEuPT1GeujGoQe9C4Xqn/view?usp=sharing"
        )
        additionalInfo(
            "• [Анализ конкурентов на практике: 10 шагов](http://powerbranding.ru/competition/analiz-konkurentov-primer/)\n" +
                    "• [Анализ конкурентов на практике: 10 шагов \\+ шаблон](https://vc.ru/marketing/181297-analiz-konkurentov-na-praktike-10-shagov-shablon)\n"
        )
        task(
            "1\\. Составьте список прямых и косвенных конкурентов своего продукта/ проекта / бизнеса\\.\n" +
                    "2\\. Заполните таблицу конкурентного анализа для четырех ближайших конкурентов, используя [Шаблон «Конкуренты»](https://docs.google.com/document/d/1KZmgWYX1eDTQP_X9wKV7GvLkcTCAjGsjI5WtlpxEecg/edit?usp=sharing)\n" +
                    "3\\. Обоснуйте устойчивость и уникальность вашего конкурентного преимущества\\. Как оно связано с потребностями / выгодами / проблемами ваших клиентов\\.\n" +
                    "4\\. Используйте как минимум 2 сервиса для проведения конкурентного анализа\\.\n",
            "«Конкуренты»"
        )
    }
}
