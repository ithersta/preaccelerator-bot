package ru.spbstu.preaccelerator.telegram.resources.modules

import ru.spbstu.preaccelerator.domain.entities.module.ModuleConfigBuilder
import kotlin.time.Duration.Companion.days

fun ModuleConfigBuilder.firstModule() {
    module("Национальная технологическая инициатива\\. Жизненный цикл проекта\\. Команда проекта\\. Технологии", 7.days) {
        lecture(
            "Введение: что такое стартап и зачем он нужен?",
            ModuleStrings.Speaker.AlenaFranc,
            "https://drive.google.com/file/d/1E7eJjIRJj8Ro5urk2DffWje9gJ9iW24s/view?usp=sharing",
            "https://drive.google.com/file/d/17e1Lddos4wVcpjkrUOHT4w2opDWncLiI/view?usp=sharing"
        )
        lecture(
            "Жизненный цикл проекта",
            ModuleStrings.Speaker.AlenaFranc,
            "https://drive.google.com/file/d/1uYKtiaqFS5Ay5sYbFoS-kjYdL-uHkSSZ/view?usp=sharing",
            "https://drive.google.com/file/d/1C0FNZGTwePKNRNWT9Yh26HA_6aLwixHn/view?usp=sharing"
        )
        lecture(
            "Команда проекта",
            ModuleStrings.Speaker.AlenaFranc,
            "https://drive.google.com/file/d/1Gd4BXDR1qtUTDYkUy16YSHsW7BYlJIVF/view?usp=sharing",
            "https://drive.google.com/drive/folders/1rwIBSEApQ3JvXpdo48mIAb3AH9hFmFMY"
        )
        task(
            "1\\. Проанализируйте вашу команду\n" +
                    "2\\. Распределите роли каждого члена команды, укажите сильные стороны и компетенции, определите кого в команде вам не хватает, решите где и как будете искать этого человека\\.\n" +
                    "3\\. Заполните предложенный шаблон [«Команда»](https://docs.google.com/document/d/1eU0Y4ynvUxbjC7pGBvZKmbg8DVx8rHQqAxj5ikhAh1E/edit?usp=sharing\\)\n" +
                    "4\\. При желании, пройдите тест командой [Стили управления по И\\. Адизесу](https://s.adizes.org/alis/?lang=ru)\n"
        )
        lecture(
            "Национальная технологическая инициатива: рынки, сквозные технологии, технологические барьеры и инфраструктура",
            ModuleStrings.Speaker.NikolaiKosv,
            "https://drive.google.com/file/d/12vsBEpiYRElXdkv5dzkxWetgs7bi1isZ/view?usp=sharing",
            "https://drive.google.com/file/d/1cGkWRTE6dcg99MKTiaixUlCoENkZmzKJ/view?usp=sharing"
        )
        additionalInfo(
            "•[Вызов и цель НТИ](https://docs.google.com/presentation/d/1W-zetEnA6PfRRksG384_MRZVc12vg7IY/edit?usp=sharing&ouid=117136603392830305877&rtpof=true&sd=true)\n" +
                    "•[Матрица НТИ 2\\.0](https://drive.google.com/file/d/1nrt2XbG1FW4Jd9l9-gkA1m3ecVgRlvgJ/view?usp=sharing)\n" +
                    "•[Дорожная карта Реализации НТИ в Пермском крае](https://docs.google.com/document/d/1h79Enk6kRGDT7fPsAF74eh7LyYbOPg1r/edit?usp=sharing&ouid=117136603392830305877&rtpof=true&sd=true)\n" +
                    "•[Критерии компаний НТИ](https://drive.google.com/file/d/1Pm1kROlOMfi23oCqzJRgZf1SWpxn6etO/view?usp=sharing)\n"
        )
        task(
            "1\\. Проанализируйте ваш проект и его соответствие одному из рынков НТИ\n" +
                    "2\\. Проанализируйте технологию \\(ии\\) в основе вашего проекта и ее \\(их\\) соответствие сквозным технологиям НТИ\\.\n" +
                    "3\\. Заполните предложенный шаблон [«Чек\\-лист Рынки и сквозные технологии НТИ»](https://docs.google.com/document/d/1DgowXCUOU83ukdoibaYZYHDsa6RruaEQro8-2pYyJFk/edit?usp=sharing)\n"
        )
        lecture(
            "Технологии и технологический аудит",
            ModuleStrings.Speaker.IgorBesucl,
            "https://drive.google.com/file/d/1bhKNTP17Vr6kGI1OuPHZeTMq1vJi4UA_/view?usp=sharingL",
            "https://drive.google.com/file/d/13GCMT2EprebgJw5me-MjANo0Qp4jMVR_/view?usp=sharing"
        )
        task(
            "1\\. Проанализируйте ваш продукт и ответьте на вопрос: для чего он нужен?\n" +
                    "2\\. Сформулируйте основные сценарии использования вашего продукта \\(1\\-3 сценария\\)\n" +
                    "3\\. Выделите все технологии используемые в вашем продукте и разделите на 3 группы: критические, ключевые и все остальные технологии\\. \n" +
                    "4\\. Заполните предложенный шаблон [«Продукт\\-сценарии\\-технологии»](https://docs.google.com/document/d/1UBemwOQYH1-rC_hNUL-FAGSlmKtEBu5dBi2b1bNU1Fw/edit?usp=sharing)\n"
        )
        lecture(
            "Конкурентный анализ технологий",
            ModuleStrings.Speaker.IgorBesucl,
            "https://drive.google.com/file/d/1_fEyP2I_78ioozx_rwliSENwU1UK5qeV/view?usp=sharing",
            "https://drive.google.com/file/d/1F5gY8gkNhxF-lNQCW5ZUNgiNppcO0eoy/view?usp=sharing"
        )
        task(
            "1\\. Проанализируйте ваш проект и проведите самодиагностику готовности вашего продукта: технологическая готовность \\(TRL\\), производственная готовность \\(MRL\\), рыночная готовность \\(CRL\\)\n" +
                    "2\\. Заполните предложенный шаблон [«Готовность продукта TRL\\-MRL\\-CRL»](https://docs.google.com/document/d/19RI4Zu5uHBaa-j1w9o97MxsFNGc85_fU1WdBMs5gjQ8/edit?usp=sharing)\n"
        )
    }
}