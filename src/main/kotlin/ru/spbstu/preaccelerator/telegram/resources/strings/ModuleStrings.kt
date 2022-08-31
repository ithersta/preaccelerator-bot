package ru.spbstu.preaccelerator.telegram.resources.strings

import ru.spbstu.preaccelerator.telegram.modules.entities.ModulePart
import ru.spbstu.preaccelerator.telegram.modules.entities.Task
import java.util.*

object ModuleStrings {
    object Speaker {
        val AlenaFranc = "Алена Францкевич, со-основатель тьюторского центра «Выбор будущего»"
        val NikolaiKosv =
            "Николай Косвинцев, руководитель направления «Экосистема НТИ» Агентства инвестиционного развития Пермского края"
        val IgorBesucl = "Игорь Безукладников, со-основатель технологических стартапов"
        val AnastasiaRyl =
            "Анастасия Рылова, со-основатель тьюторского центра «Выбор будущего», трекер, практик дизайн-мышления "
        val AnastasiaOrz = "Анастасия Орзаева, маркетинговый стратег, продакт-менеджер, специалист в коммуникациях"
        val IrinaShafr = "Ирина Шафранская, маркетолог, доцент НИУ ВШЭ-Пермь"
        val AlexanderMalecev = "Александр Мальцев, руководитель Центра прикладной экономики НИУ ВШЭ-Пермь"
    }

    const val SpeakerWord = "Спикер:"
    const val DownloadPresentation = "Скачать презентацию к видео"

    object FirstModule {
        val name = "Национальная технологическая инициатива. Жизненный цикл проекта. Команда проекта. Технологии"
        val parts = listOf<ModulePart>(
            ModulePart(
                "Введение: что такое стартап и зачем он нужен?",
                Speaker.AlenaFranc,
                "https://drive.google.com/drive/folders/1wrP-jrHpuZSV3CcwyfrLNFYCRISBf-D7",
                "https://drive.google.com/drive/folders/1wrP-jrHpuZSV3CcwyfrLNFYCRISBf-D7",
                Optional.empty(),
                Optional.empty()
            ),
            ModulePart(
                "Жизненный цикл проекта",
                Speaker.AlenaFranc,
                "https://drive.google.com/drive/folders/1iIuAevEgQUbU6a5-wsQt-IJla5sJHSW5",
                "https://drive.google.com/drive/folders/1iIuAevEgQUbU6a5-wsQt-IJla5sJHSW5",
                Optional.empty(),
                Optional.empty()
            ),
            ModulePart(
                "Команда проекта",
                Speaker.AlenaFranc,
                "https://drive.google.com/drive/folders/1rwIBSEApQ3JvXpdo48mIAb3AH9hFmFMY",
                "https://drive.google.com/drive/folders/1rwIBSEApQ3JvXpdo48mIAb3AH9hFmFMY",
                Optional.empty(),
                Optional.of(
                    Task(
                        1,
                        "1. Проанализируйте вашу команду\n" +
                                "2. Распределите роли каждого члена команды, укажите сильные стороны и компетенции, определите кого в команде вам не хватает, решите где и как будете искать этого человека.\n" +
                                "3. Заполните предложенный шаблон «Команда» (активная ссылка)\n" +
                                "4. При желании, пройдите тест командой Стили управления по И. Адизесу (активная ссылка)\n"
                    )
                )
            ),
            ModulePart(
                "Национальная технологическая инициатива: рынки, сквозные технологии, технологические барьеры и инфраструктура",
                Speaker.NikolaiKosv,
                "https://drive.google.com/drive/folders/1H9mbtf7vqmnpBF8gstC6g-kcSjPk1ebl",
                "https://drive.google.com/drive/folders/1H9jETR2Bem7D7hJLDQi2EBCxnKpKF6fq",
                Optional.of(
                    "Вызов и цель НТИ\n" +
                            "Матрица НТИ 2.0\n" +
                            "Дорожная карта Реализации НТИ в Пермском крае\n" +
                            "Критерии компаний НТИ\n"
                ),//TODO:добавить ссылки
                Optional.of(
                    Task(
                        2,
                        "1. Проанализируйте ваш проект и его соответствие одному из рынков НТИ\n" +
                                "2. Проанализируйте технологию (ии) в основе вашего проекта и ее (их) соответствие сквозным технологиям НТИ.\n" +
                                "3. Заполните предложенный шаблон «Чек-лист Рынки и сквозные технологии НТИ» (активная ссылка)\n"
                    )
                )
            ),
            ModulePart(
                "Технологии и технологический аудит",
                Speaker.IgorBesucl,
                "drive.google.com/drive/folders/1W0518DyGY0SuOgmKHeYeVTfgL6BeiCFL",
                "https://drive.google.com/drive/folders/1W0518DyGY0SuOgmKHeYeVTfgL6BeiCFL",
                Optional.empty(),
                Optional.of(
                    Task(
                        3,
                        "1. Проанализируйте ваш продукт и ответьте на вопрос: для чего он нужен?\n" +
                                "2. Сформулируйте основные сценарии использования вашего продукта (1-3 сценария)\n" +
                                "3. Выделите все технологии используемые в вашем продукте и разделите на 3 группы: критические, ключевые и все остальные технологии. \n" +
                                "Заполните предложенный шаблон «Продукт-сценарии-технологии» (активная ссылка)\n"
                    )
                )
            ),
            ModulePart(
                "Конкурентный анализ технологий",
                Speaker.IgorBesucl,
                "https://drive.google.com/drive/folders/1c2mztXPGfIFwpPab8wqQoTjxW4tnAHjn",
                "https://drive.google.com/drive/folders/1c2mztXPGfIFwpPab8wqQoTjxW4tnAHjn",
                Optional.empty(),
                Optional.of(
                    Task(
                        4,
                        "1. Проанализируйте ваш проект и проведите самодиагностику готовности вашего продукта: технологическая готовность (TRL), производственная готовность (MRL), рыночная готовность (CRL)\n" +
                                "Заполните предложенный шаблон «Готовность продукта TRL-MRL-CRL» (активная ссылка)\n"
                    )
                )
            )
        )
    }
}