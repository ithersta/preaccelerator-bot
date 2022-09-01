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

    //TODO: map
    val listOfModels = mapOf<Int, String>(
        1 to Models.Model1,
        2 to Models.Model2,
        3 to Models.Model3,
        4 to Models.Model4,
        5 to Models.Model5,
        6 to Models.Model6,
        7 to Models.Model7,
        8 to Models.Model8
    )

}
