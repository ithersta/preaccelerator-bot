package ru.spbstu.preaccelerator.telegram.resources.modules

import ru.spbstu.preaccelerator.domain.entities.module.*

object ModuleStrings {
    object Speaker {
        const val AlenaFranc = "Алена Францкевич, со\\-основатель тьюторского центра «Выбор будущего»"
        const val NikolaiKosv =
            "Николай Косвинцев, руководитель направления «Экосистема НТИ» Агентства инвестиционного развития Пермского края"
        const val IgorBesucl = "Игорь Безукладников, со\\-основатель технологических стартапов"
        const val AnastasiaRyl =
            "Анастасия Рылова, со\\-основатель тьюторского центра «Выбор будущего», трекер, практик дизайн\\-мышления "
        const val AnastasiaOrz =
            "Анастасия Орзаева, маркетинговый стратег, продакт\\-менеджер, специалист в коммуникациях"
        const val IrinaShafr = "Ирина Шафранская, маркетолог, доцент НИУ ВШЭ\\-Пермь"
        const val AlexanderMalecev = "Александр Мальцев, руководитель Центра прикладной экономики НИУ ВШЭ\\-Пермь"
        const val DaryaZern = "Дарья Зернина, Заместитель Генерального директора ООО «Компания «ИнКорс»"
        const val SemyonSazh = " Семён Сажин, Product Adviser в американском edtech стартапе careerist\\.com\n"
        const val KseniaShip =
            "Ксения Шипулина, Руководитель направления привлечения инвесторов Агентство инвестиционного развития Пермского края"
        const val RustamBag = "Рустам Багизов, Тренер по публичным выступлениям, речи, работе в кадре;\n" +
                "Cпикер Деловой Среды СБЕРА; Шеф\\-редактор РБК\\-Пермь"
    }

    object Error {
        const val MalformedHomeworkUrl = "Отправьте ссылку на задание"
        const val HomeworkWasAlreadyAdded = "Ваша команда уже выполнила это задание"
    }

    const val SendHomework = "Пришлите ссылку на выполненное задание в чат"
    const val HomeworkNotSentAlert = "Ваша команда ещё не отправила это задание"

    private val Congrats = """
        |Мы вас поздравляем, вы прошли всю акселерационную программу, осталось совсем чуть\-чуть…
        |Отрепетируйте ваш питч и до встречи на демо\-дне\!
    """.trimMargin()

    fun welcomeModule(module: Module) = """
        |*Модуль ${module.number.value}*
        |*Название*: ${module.name}
    """.trimMargin()

    fun module(number: Module.Number) = "Модуль ${number.value}"

    fun goodbyeModule(moduleConfig: ModuleConfig, number: Module.Number): String {
        val nextModule = Module.Number(number.value + 1).takeIf { moduleConfig.modules.containsKey(it) }
        return """
        |Вы закончили изучать материалы Модуля ${number.value}.
        |${
            if (nextModule != null) {
                "Пройдите короткий тест и приступайте к изучению Модуля ${nextModule.value}!"
            } else {
                Congrats
            }
        }
    """.trimMargin()
    }

    fun doTest(number: Module.Number) = "Пройдите короткий тест Модуля ${number.value}"

    fun lectureMessage(lecture: Lecture) = """
        |*Тема лекции*: ${lecture.name}
        |
        |*Спикер*: ${lecture.speaker}
    """.trimMargin()

    fun additionalInfoMessage(additionalInfo: AdditionalInfo) = """
        |*Дополнительная информация*:
        |
        |${additionalInfo.text}
    """.trimMargin()

    fun taskMessage(task: Task) = """
        |Пришло время выполнить задание №${task.number.value}
        |
        |${task.description}
    """.trimMargin()

    fun doneTaskMessage(task: Task, url: String) = """
        |✅ Ваша команда отправила ответ на задание №${task.number.value}:
        |$url
    """.trimMargin()
}
