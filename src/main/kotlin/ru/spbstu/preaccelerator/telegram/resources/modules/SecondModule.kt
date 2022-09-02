package ru.spbstu.preaccelerator.telegram.resources.modules

import ru.spbstu.preaccelerator.domain.entities.module.ModuleConfigBuilder
import kotlin.time.Duration.Companion.days

fun ModuleConfigBuilder.secondModule() {
    module("Определение ключевой деятельности компании", 7.days) {
        lecture(
            "Идея \\- Проблема \\- Решение",
            ModuleStrings.Speaker.AnastasiaRyl,
            "https://drive.google.com/file/d/1MME7LtJ22Gj5_uLoLFVHWi-aqbk9TMMf/view?usp=sharing",
            "https://drive.google.com/file/d/1D_i31Z6jwEAWp4lFZvOqjYdmwQiHDtZI/view?usp=sharing"
        )
        task(
            "1\\. Проанализируйте ваш продукт/проект\n" +
                    "2\\. Сформулируйте кратко описание вашего проекта/продукта, так чтобы было понятно неспециалисту в вашей отрасли\\. Желательно, чтобы описание вашего продукта было сформулировано на языке вашей “целевой аудитории”\n" +
                    "3\\. Заполните предложенный шаблон [«Описание идеи»](https://docs.google.com/document/d/1forKFZYth9qN0MNLVpXg3dHGtTDNaQQqm9COClY_1Vw/edit?usp=sharing)\n"
        )
        lecture(
            "Целевая аудитория и сегментация",
            ModuleStrings.Speaker.AnastasiaOrz,
            "https://drive.google.com/file/d/1488EbK94FAnEa8Q2xEq1Q6nno45y1M1w/view?usp=sharing",
            "https://drive.google.com/file/d/19GloXpaj_7BAnYcoFk4IeGZxLTpCFXni/view?usp=sharing"
        )
        additionalInfo(
            "• [Как составить портрет целевой аудитории и внедрить его в работу: готовый пример с job story](https://vc.ru/marketing/227648-kak-sostavit-portret-celevoy-auditorii-i-vnedrit-ego-v-rabotu-gotovyy-primer-s-job-story)\n" +
                    "• [Целевая аудитория в промышленном маркетинге и где её искать в Интернете?](https://vc.ru/marketing/209401-celevaya-auditoriya-v-promyshlennom-marketinge-i-gde-ee-iskat-v-internete)\n" +
                    "• [Как составить портрет клиента \\(целевой аудитории\\): инструкция с примерами](https://vc.ru/marketing/156147-kak-sostavit-portret-klienta-celevoy-auditorii-instrukciya-s-primerami)\n"
        )
        lecture(
            "Разработка ценностного предложения",
            ModuleStrings.Speaker.AnastasiaOrz,
            "https://drive.google.com/file/d/1fueTQvK9ObtBuzaEXkERH6lRTl8ZeUZW/view?usp=sharing",
            "https://drive.google.com/drive/folders/1rwIBSEApQ3JvXpdo48mIAb3AH9hFmFMY"
        )
        additionalInfo(
            "• [Как создать ценностное предложение в b2b и привлечь покупателей\\. Принципы, методы, инструменты](https://vc.ru/marketing/178634-kak-sozdat-cennostnoe-predlozhenie-v-b2b-i-privlech-pokupateley-principy-metody-instrumenty)\n" +
                    "• [Как выявить потребности целевой аудитории и не придумать «коней в вакууме»](https://vc.ru/marketing/137965-kak-vyyavit-potrebnosti-celevoy-auditorii-i-ne-pridumat-koney-v-vakuume)\n"
        )
        task(
            "1\\. Проанализируйте свой продукт/проект и подумайте кто может быть целевой аудиторией? Какие потребности у этих людей? На каком рынке вы будете работать? B2B \\(вы будете продавать компаниям?\\) или B2C \\(вы будете продавать конечным потребителям физическим лицам?\\)\n" +
                    "2\\. Выделите 1\\-3 сегмента\n" +
                    "3\\. По каждому сегменту составьте Портрет потребителя и сформулируйте ценностное предложение\n" +
                    "4\\. Заполните один из предложенных шаблонов [«Портрет целевой аудитории»](https://docs.google.com/document/d/1vg7bHvyKxfjJjDN1Q1QbdAEXPQVcsZ9o/edit?usp=sharing&ouid=117136603392830305877&rtpof=true&sd=true)\n"
        )
        lecture(
            "Исследование потребителей и MVP",
            ModuleStrings.Speaker.AnastasiaRyl,
            "https://drive.google.com/file/d/1DRC9o22zj8YzIKkItMew61QkrAh3Txm_/view?usp=sharing",
            "https://drive.google.com/file/d/1XUmL42JJq-hC0RiQWInnLn64n2CdE9-4/view?usp=sharinghttps://drive.google.com/file/d/1XUmL42JJq-hC0RiQWInnLn64n2CdE9-4/view?usp=sharing"
        )
        additionalInfo(
            "• [Как проверять гипотезы с помощью проблемных интервью: пошаговая инструкция](https://vc.ru/marketing/156573-kak-proveryat-gipotezy-s-pomoshchyu-problemnyh-intervyu-poshagovaya-instrukciya)\n" +
                    "• [CustDev: способ узнать истинные потребности аудитории](https://vc.ru/u/431111-denis-smirnov/147643-custdev-sposob-uznat-istinnye-potrebnosti-auditorii)\n" +
                    "• [Секрет успешных стартапов\\. Minimum Viable Product: что это, и как его создают](https://vc.ru/marketing/139040-sekret-uspeshnyh-startapov-minimum-viable-product-chto-eto-i-kak-ego-sozdayut)\n" +
                    "• [Что такое MVP? Зачем он нужен и как его правильно создать?](https://spark.ru/startup/platpick/blog/55354/chto-takoe-mvp-zachem-on-nuzhen-i-kak-ego-pravilno-sozdat)\n" +
                    "• [StartTrack: проект Healbe \\- история успеха и опыт создателей](https://www.youtube.com/watch?v=6m92yJoaAgw&feature=emb_logo)\n" +
                    "• [Promobot: как российские роботы покорили целый мир](https://www.youtube.com/watch?v=jVuW06R7KS0&feature=emb_logo)\n" +
                    "• [Видео\\-ролик с MVP от Dropbox](https://www.youtube.com/watch?v=7QmCUDHpNzE&feature=emb_logo)"
        )
        task(
           "1\\. Все, что вы написали в Задании 2 \\(используя шаблон “Портрет целевой аудитории”\\) \\-\\ это ваши гипотезы\n" +
                   "2\\. Напишите к ним список потенциальных вопросов интервью для проверки этих гипотез\n" +
                   "3\\. Проведите 3\\-\\5 проблемных интервью\n" +
                   "4\\. Выпишите все главные мысли, факты и инсайты от потенциальных пользователей после интервью\n" +
                   "5\\. Сделайте вывод подтвердились ли ваши гипотезы после интервью?\n" +
                   "6\\. Если подтвердились не все гипотезы, переформулируйте/уточните результаты вашего предыдущего задания \\(Портрет целевой аудитории и ценностное предложение\\)\n" +
                   "7\\. Для проведения интервью и выводах после него можете использовать Шаблон [“Проблемное интервью и кластеризация результатов”](https://docs.google.com/document/d/1TSoSTwIO56MmB2kRH5o-JYh2p_ElEM3N/edit?usp=sharing&ouid=117136603392830305877&rtpof=true&sd=true)\n"
        )
        task(
            "1\\. Проанализируйте данные проведенных проблемных интервью\n" +
                    "2\\. Решите, что является обязательным и желательным в функционале вашего продукта\n" +
                    "3\\. Определитесь какой минимальный функционал необходим\n" +
                    "4\\. Определитесь будете ли вы использовать существующие сервисы/готовые решения для вашего MVP\n" +
                    "5\\. Решите какой тип MVP больше всего подходит для вашего продукта\n" +
                    "6\\. Вы можете использовать шаблон [\\“MVP продукта\\”](https://docs.google.com/document/d/1Buk73Q5Sni-0INC1WltR1feZS7MiqWjp/edit?usp=sharing&ouid=117136603392830305877&rtpof=true&sd=true)\n"
        )
    }
}