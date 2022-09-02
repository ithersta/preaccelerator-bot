package ru.spbstu.preaccelerator.telegram.resources.modules

import ru.spbstu.preaccelerator.domain.entities.module.*
import org.koin.core.component.inject
import java.util.*

object ModuleStrings {
    object Speaker {
        val AlenaFranc = "Алена Францкевич, со\\-основатель тьюторского центра «Выбор будущего»"
        val NikolaiKosv =
            "Николай Косвинцев, руководитель направления «Экосистема НТИ» Агентства инвестиционного развития Пермского края"
        val IgorBesucl = "Игорь Безукладников, со\\-основатель технологических стартапов"
        val AnastasiaRyl =
            "Анастасия Рылова, со\\-основатель тьюторского центра «Выбор будущего», трекер, практик дизайн\\-мышления "
        val AnastasiaOrz = "Анастасия Орзаева, маркетинговый стратег, продакт\\-менеджер, специалист в коммуникациях"
        val IrinaShafr = "Ирина Шафранская, маркетолог, доцент НИУ ВШЭ\\-Пермь"
        val AlexanderMalecev = "Александр Мальцев, руководитель Центра прикладной экономики НИУ ВШЭ\\-Пермь"
        val DaryaZern = "Дарья Зернина, Заместитель Генерального директора ООО «Компания «ИнКорс»"
        val SemyonSazh = " Семён Сажин, Product Adviser в американском edtech стартапе careerist\\.com\n"
        val KseniaShip =
            "Ксения Шипулина, Руководитель направления привлечения инвесторов Агентство инвестиционного развития Пермского края"
        val RustamBag = "Рустам Багизов, Тренер по публичным выступлениям, речи, работе в кадре;\n" +
                "Cпикер Деловой Среды СБЕРА; Шеф\\-редактор РБК\\-Пермь"

    }

    const val TaskNumberWord = "Пришло время выполнить задание №"
    const val NameOfLectureWord = "*Тема лекции*: "
    const val WatchLecture = "Запись лекции"
    const val ShowPresentation = "Презентация"
    const val AddInfoWord = "*Дополнительная информация*:\n"
    const val SpeakerWord = "*Спикер*: "
    const val NextPart = "Продолжить обучение"
    const val DoTest = "Пройти тест"
    const val Congrats =
        "Мы вас поздравляем, вы прошли всю акселерационную программу, осталось совсем чуть\\-чуть… \n" +
                "Отрепетируйте ваш питч и до встречи на демо\\-дне\\!\n"

    fun WelcomeModule(module: Module) = "*Модуль ${module.number.value + 1}*\n" +
            "*Название*: ${module.name}"

    fun NextModule(number: Module.Number) = "Модуль ${number.value + 2}"
    fun GoodByeModule(moduleConfig: ModuleConfig, number: Module.Number) = "Вы закончили изучать материалы Модуля ${number.value + 1}\\. \n" +
            if (number.value != moduleConfig.modules.lastIndex) {
                "Пройдите  короткий тест и приступайте к изучению Модуля ${number.value + 2}\\! \n"
            } else {
                Congrats
            }

    fun LectureString(lect: Lecture) = NameOfLectureWord + lect.name + "\n\n" + SpeakerWord + lect.speaker
    fun AdditionalInfoString(addInf: AdditionalInfo) = AddInfoWord + "\n" + addInf.text

    fun TaskString(task: Task) = TaskNumberWord + (task.number.value + 1) + "\n\n" + task.description


}


