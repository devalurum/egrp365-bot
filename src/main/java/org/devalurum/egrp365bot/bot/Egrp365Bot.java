package org.devalurum.egrp365bot.bot;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.devalurum.egrp365bot.model.EgrpEntity;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.devalurum.egrp365bot.utils.Constants.*;

public class Egrp365Bot {

    private final static Duration secondsToWait = Duration.ofSeconds(5);

    static {
        WebDriverManager.chromedriver().setup();
    }

    private final WebDriver driver;

    public Egrp365Bot() {
        this.driver = new ChromeDriver();
        this.driver.manage().window().minimize();
    }

    public EgrpEntity getInfo(String cadastralNumber) {
        driver.get(String.format(EGRP365_URL, cadastralNumber));

        WebDriverWait wait = new WebDriverWait(driver, secondsToWait);

        // waiting for loading all page
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState")
                .equals("complete"));

        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(MAIN_BLOCKS)));
        List<WebElement> elementsMain = driver.findElements(By.cssSelector(MAIN_BLOCKS));

        WebElement show = driver.findElement(By.className(CLASS_SHOW_MORE_INFO));
        wait.until(ExpectedConditions.elementToBeClickable(show));
        show.click();

        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(MORE_BLOCKS)));
        List<WebElement> elementsMore = driver.findElements(By.cssSelector(MORE_BLOCKS));

        Map<String, String> info = new LinkedHashMap<>();

        elementsMain.forEach(elem -> {
            String key = new String(elem.findElement(By.className(MAIN_HEADER))
                    .getText()
                    .getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8)
                    .intern();

            String value = new String(elem.findElement(By.className(MAIN_TEXT))
                    .getText()
                    .getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8)
                    .intern();
            info.put(key, value);
        });

        elementsMore.forEach(elem -> {
            String key = new String(elem.findElement(By.className(MORE_HEADER))
                    .getText()
                    .getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8)
                    .intern();
            String value = new String(elem.findElement(By.className(MORE_TEXT))
                    .getText()
                    .getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8)
                    .intern();

            info.put(key, value);
        });

        WebElement coordElem = driver.findElement(By.className(CLASS_WITH_COORDINATES));
        WebElement addressElem = driver.findElement(By.className(CLASS_WITH_ADDRESS));

        String address = new String(addressElem
                .getText()
                .getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8)
                .intern();

        String[] coordStr = coordElem.getText().split(",");
        double lat = Double.parseDouble(coordStr[0]);
        double lon = Double.parseDouble(coordStr[1]);

        return EgrpEntity.builder()
                .cadastralNumber(cadastralNumber)
                .address(address)
                .latitude(lat)
                .longitude(lon)
                .fullInfo(info)
                .build();
    }

    public void closeBrowser() {
        driver.close();
        driver.quit();
    }
}
