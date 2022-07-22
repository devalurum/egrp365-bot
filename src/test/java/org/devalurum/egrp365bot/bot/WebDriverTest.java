package org.devalurum.egrp365bot.bot;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.StringUtils;
import org.devalurum.egrp365bot.model.EgrpEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.devalurum.egrp365bot.utils.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WebDriverTest {

    String cadastralNumber = "38:36:000034:2954";
    String EGRP365_URL = "https://egrp365.org/map/?kadnum=%s";
    Duration pageLoadTimeout = Duration.ofMinutes(1);

    WebDriver driver;

    EgrpEntity expectedEntity;

    WebElement show;

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();

        this.driver = new ChromeDriver();
        this.driver.manage().window().minimize();
        this.driver.manage().timeouts().pageLoadTimeout(pageLoadTimeout);

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
        driver.quit();
    }

    @Test
    void getInfo() {
        driver.get(String.format(EGRP365_URL, cadastralNumber));

        WebDriverWait wait = new WebDriverWait(driver, pageLoadTimeout);

        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState")
                .equals("complete"));

        wait.until(webDriver -> ((JavascriptExecutor) webDriver))
                .executeScript(String.format("document.getElementsByClassName('%s')[0].style.display='inline-block';",
                        CLASS_MORE_INFO));

        show = driver.findElement(By.className(CLASS_SHOW_MORE_INFO));
        wait.until(ExpectedConditions.elementToBeClickable(show));
        show.click();

        Map<String, String> mainInfo = wait.until(ExpectedConditions.
                        visibilityOfAllElementsLocatedBy(By.cssSelector(MAIN_BLOCKS)))
                .stream().collect(Collectors.toMap(elem -> StringUtils.chop(elem.findElement(By.className(MAIN_HEADER))
                        .getText()), elem -> elem.findElement(By.className(MAIN_TEXT))
                        .getText(), (x, y) -> x, LinkedHashMap::new));

        Map<String, String> moreInfo = wait.until(ExpectedConditions.
                        visibilityOfAllElementsLocatedBy(By.cssSelector(MORE_BLOCKS)))
                .stream().collect(Collectors.toMap(elem -> StringUtils.chop(elem.findElement(By.className(MORE_HEADER))
                        .getText()), elem -> elem.findElement(By.className(MORE_TEXT))
                        .getText(), (x, y) -> x, LinkedHashMap::new));

        mainInfo.putAll(moreInfo);

        WebElement addressElem = driver.findElement(By.className(CLASS_WITH_ADDRESS));
        WebElement coordElem = driver.findElement(By.className(CLASS_WITH_COORDINATES));

        String[] coordStr = coordElem.getText().split(",");
        double lat = Double.parseDouble(coordStr[0]);
        double lon = Double.parseDouble(coordStr[1]);

        EgrpEntity actualEntity = EgrpEntity.builder()
                .cadastralNumber(cadastralNumber)
                .address(addressElem.getText())
                .fullInfo(mainInfo)
                .latitude(lat)
                .longitude(lon)
                .build();

        assertEquals(driver.getCurrentUrl(), String.format(EGRP365_URL, cadastralNumber));
        assertEquals(expectedEntity, actualEntity);

    }
}