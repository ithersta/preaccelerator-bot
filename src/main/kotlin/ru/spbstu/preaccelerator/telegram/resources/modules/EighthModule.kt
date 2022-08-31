package ru.spbstu.preaccelerator.telegram.resources.modules

import ru.spbstu.preaccelerator.domain.entities.module.ModuleConfigBuilder
import kotlin.time.Duration.Companion.days

fun ModuleConfigBuilder.eighthModule() {
    module("Оформление и презентация проекта", 7.days) {
        lecture(
            "Структура и составляющие презентации для разных видов аудитории",
            ModuleStrings.Speaker.AnastasiaRyl,
            "https://drive.google.com/file/d/1pg4HhilFbQrp3-sc0q56CkdjPN77NwRh/view?usp=sharing",
            "https://drive.google.com/file/d/1U6s0JTYgDrUIBFh_QI1IK5pWB0dmKqUj/view?usp=sharing"
        )
        lecture(
            "Технология самопрезентации",
            ModuleStrings.Speaker.RustamBag,
        "https://drive.google.com/file/d/1pW9zTsovUwNrAkuh6W-9rmMWfAOZZ6Mb/view?usp=sharing",
            "https://drive.google.com/file/d/1w4CCOcB8LrcmitpTQsjelVEVW2tldqea/view?usp=sharing"
        )
        additionalInfo(
             "Как сделать крутой питч: пять ингредиентов https://www.iidf.ru/media/articles/lifehacks/kak-sdelat-krutoy-pitch/\n" +
                     "Что говорить про конкурентов в питч-деке — советы стартапу (https://rb.ru/story/analysis-competitors/)\n" +
                     "Как сделать питч-дек, чтобы инвестор сказал «да» https://vc.ru/life/267649-kak-sdelat-pitch-dek-chtoby-investor-skazal-da\n" +
                     "Как подготовить идеальный питч для выступления перед клиентами, инвесторами и партнерами https://vc.ru/sber500/239362-kak-podgotovit-idealnyy-pitch-dlya-vystupleniya-pered-klientami-investorami-i-partnerami\n" +
                     "Как продать свою идею за 30 секунд https://vc.ru/finance/128052-kak-prodat-svoyu-ideyu-za-30-sekund\n" +
                     "Правила стартапов. Изнурительный декатлон https://vc.ru/u/147882-viktor-zakharchenko/147804-pravila-startapov-iznuritelnyy-dekatlon\n" +
                     "Любовь с первого питча: как сразу понравиться инвестору https://rb.ru/opinion/lyubov-s-pervogo-pitcha/\n"
        )
        task(
            "Используя шаблон «Питч проекта», оформите презентацию вашего стартапа для демо-дня акселератора.\n" +
                    "Отрепетируйте несколько раз вашу речь для выступления.\n"
        )
    }
}