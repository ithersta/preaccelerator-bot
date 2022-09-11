package ru.spbstu.preaccelerator.telegram.resources.modules

import ru.spbstu.preaccelerator.domain.entities.module.ModuleConfigBuilder
import kotlin.time.Duration.Companion.days

fun ModuleConfigBuilder.fifthModule() {
    module(
        "Экономика проекта", 7.days,
        "https://docs.google.com/forms/d/e/1FAIpQLScmxyMXsFBoW5mMGoWsOD3Ckzy2vn-5JpI1QVToK_-hGNhvoA/viewform?usp=sf_link"
    ) {
        lecture(
            "Финансовое моделирование \\(бюджет доходов и расходов\\)",
            ModuleStrings.Speaker.KseniaShip,
            "https://drive.google.com/file/d/1UtnlaHgkHUG02uMjLl4W2KmPkSeGLF05/view?usp=sharing",
            "https://drive.google.com/file/d/1oael5D6wfLAA23wUkbOd0RDhJ72_USV8/view?usp=sharing"
        )
        lecture(
            "Финансовое моделирование \\(бюджет движения денежных средств, финансовые показатели, оценка стоимости\\)",
            ModuleStrings.Speaker.KseniaShip,
            "https://drive.google.com/file/d/1bd_8AlnUxz0dJn-kG4f29VkFINy7g0-W/view?usp=sharing",
            "https://drive.google.com/file/d/1Wd9N7sWnQmU5TWFlj_KNKot7C07j_csn/view?usp=sharing"
        )
        additionalInfo(
            "• [Как построить финансовую модель вашего стартапа за 40 минут](https://vc.ru/finance/137270-kak-postroit-finansovuyu-model-vashego-startapa-za-40-minut)\n" +
                    "• [Как составить финансовую модель: практическое руководство](https://vc.ru/u/682577-oleg-evseenkov/193892-kak-sostavit-finansovuyu-model-prakticheskoe-rukovodstvo)\n" +
                    "• [Рассказ о компании языком цифр](https://vc.ru/finance/186915-rasskaz-o-kompanii-yazykom-cifr)\n" +
                    "• [Пять ошибок в финмодели стартапа: как их заметить и избежать](https://rb.ru/opinion/pyat-oshibok-finmodeli/)\n"
        )
        task(
            "Постройте финансовую модель вашего стартапа и показатели эффективности, используя [Шаблон «Финмодель»](https://docs.google.com/spreadsheets/d/1V5BTZN37LA6Bbfzq6EcbZI_WNnv9KWmd/edit?usp=sharing&ouid=117136603392830305877&rtpof=true&sd=true)",
            "«Финмодель»"
        )
    }
}
