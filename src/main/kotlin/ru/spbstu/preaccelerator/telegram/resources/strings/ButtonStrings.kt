package ru.spbstu.preaccelerator.telegram.resources.strings

object ButtonStrings {
    const val ChooseStep = "Выберите модуль"

    object Start {
        const val SendContact = "Отправить мой номер телефона"
    }

    object Models {
        const val Model1 =
            "Национальная технологическая инициатива. Жизненный цикл проекта. Команда проекта. Технологии."
        const val Model2 = "Определение ключевой деятельности компании"
        const val Model3 = "Анализ окружения"
        const val Model4 = "Коммерциализация"
        const val Model5 = "Экономика проекта"
        const val Model6 = "Юридические основы управление проектами"
        const val Model7 = "Инвестиции"
        const val Model8 = "Оформление и презентация проекта"
    }

    val listOfModels = mapOf<String, Int>(
        Models.Model1 to 0,
        Models.Model2 to 1,
        Models.Model3 to 2,
        Models.Model4 to 3,
        Models.Model5 to 4,
        Models.Model6 to 5,
        Models.Model7 to 6,
        Models.Model8 to 7
    )


    object Module {
        const val WatchLecture = "Запись лекции"
        const val ShowPresentation = "Презентация"
        const val NextPart = "Продолжить обучение"
        const val DoTest = "Пройти тест"
        const val HomeworkAlreadyDone = "Уже отправлено другим участником"
        const val SendHomework = "Отправить домашнее задание"
    }

    const val Cancel = "Отменить"
}
