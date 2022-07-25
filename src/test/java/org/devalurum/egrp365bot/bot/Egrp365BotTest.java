package org.devalurum.egrp365bot.bot;

import org.devalurum.egrp365bot.exception.Egrp365BotException;
import org.devalurum.egrp365bot.model.EgrpEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class Egrp365BotTest {

    String cadastralNumber = "38:36:000034:2954";

    Egrp365Bot bot;
    EgrpEntity expectedEntity;

    @BeforeEach
    void setUp() {

        bot = new Egrp365Bot();

        expectedEntity = EgrpEntity.builder()
                .cadastralNumber(cadastralNumber)
                .address("Иркутская область, г. Иркутск, б-р. Гагарина, д. 20")
                .latitude(52.275227)
                .longitude(104.279988)
                .fullInfo(Map.of("Кадастровый номер", "38:36:000034:2954",
                        "Назначение", "Нежилое здание",
                        "Здание по адресу", "Иркутская область, г. Иркутск, б-р. Гагарина, д. 20",
                        "Общая площадь", "9 132 кв.м.",
                        "Год постройки", "1856",
                        "Статус", "Ранее учтенный",
                        "Координаты", "52.275227, 104.279988"))
                .build();

    }

    @AfterEach
    void closeBrowser() {
        bot.shutdownBot();
    }

    @Test
    void getInfo() {
        // when:
        EgrpEntity actualEntity = bot.getInfo(cadastralNumber);

        // result:
        assertEquals(expectedEntity, actualEntity);
    }

    @Test()
    void with_exception_getInfo() {
        assertThrows(Egrp365BotException.class,
                () -> bot.getInfo("123456789"));
    }
}