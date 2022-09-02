package ru.spbstu.preaccelerator.telegram.resources.modules

import ru.spbstu.preaccelerator.domain.entities.module.AdditionalInfo
import ru.spbstu.preaccelerator.domain.entities.module.Lecture
import ru.spbstu.preaccelerator.domain.entities.module.Module
import ru.spbstu.preaccelerator.domain.entities.module.Task
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
                "Cпикер Деловой Среды СБЕРА; Шеф-редактор РБК\\-Пермь"

    }

    val ModuleNumberToFinalTestUrl = mapOf<Int, String>(
        1 to "https://docs.google.com/forms/d/e/1FAIpQLScRzZ51ctdots21Bu6GgaBrasioxQtiRdkFxr633515Nii6xA/viewform?usp=sf_link",
        2 to "https://docs.google.com/forms/d/e/1FAIpQLSdmePLdpDhublloE7rn0K1hwuqwf4uMTwBBfi2XWehrOBwqfQ/viewform?usp=sf_link",
        3 to "https://docs.google.com/forms/d/e/1FAIpQLSeXkoD0ClGzamFBaJXyQWvYg7LKZuop2_1BL_L30jZ9mDOkfg/viewform?usp=sf_link",
        4 to "https://docs.google.com/forms/d/e/1FAIpQLSdimYv8sKlNjPZB7ZqNGZ2qJfDAuXocEFHfChAdDzQbCUoCaw/viewform?usp=sf_link",
        5 to "https://docs.google.com/forms/d/e/1FAIpQLScmxyMXsFBoW5mMGoWsOD3Ckzy2vn-5JpI1QVToK_-hGNhvoA/viewform?usp=sf_link",
        6 to "https://docs.google.com/forms/d/e/1FAIpQLScL-gqj7sasamzgQ1lKPo1VXC2rwuRle0TlyNTgk8SE6BuVFg/viewform?usp=sf_link",
        7 to "https://docs.google.com/forms/d/e/1FAIpQLScltP7Wly_DFocPfPkoJG4utBcgFfAiqqwbFumh0kN4FJKKtg/viewform?usp=sf_link",
        8 to "https://docs.google.com/forms/d/e/1FAIpQLSfw4SQ5WBqe2f8WcifHZGTNm181xVIq9U98kdurX-t_B1uv3g/viewform?usp=sf_link"
    )

    const val TaskNumberWord = "Пришло время выполнить задание №"
    const val NameOfLectureWord = "*Тема лекции*: "
    const val WatchLecture = "Перейти к просмотру лекции"
    const val ShowPresentation = "Посмотреть презентацию"
    const val AddInfoWord = "*Дополнительная информация*:\n "
    const val SpeakerWord = "*Спикер*: "
    const val NextPart = "Продолжить обучение"
    const val DoTest = "Пройти тест"

    fun WelcomeModule(module: Module) = "Модуль ${module.number.value+1}\n" +
            "Название: *${module.name}*"

    fun NextModule(number: Module.Number) = "Модуль ${number.value + 2}"
    fun GoodByeModule(number: Module.Number) = "Вы закончили изучать материалы Модуля ${number.value+1}\\. \n" +
            "Пройдите  короткий тест и приступайте к изучению Модуля ${number.value + 2}\\! \n"

    fun GetFinalTestUrl(number: Module.Number) = ModuleNumberToFinalTestUrl[number.value+1]

    fun LectureString(lect: Lecture) = NameOfLectureWord + lect.name + "\n" + SpeakerWord + lect.speaker
    fun AdditionalInfoString(addInf: AdditionalInfo) = AddInfoWord + addInf.text

    fun TaskString(task: Task) = TaskNumberWord + (task.number.value + 1) + "\n\n" + task.description


}


