package org.devalurum.egrp365bot.bot;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.devalurum.egrp365bot.exception.Egrp365BotException;
import org.devalurum.egrp365bot.model.EgrpEntity;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.devalurum.egrp365bot.utils.Constants.*;


public class Egrp365Bot {

    private final static Duration pageLoadTimeout = Duration.ofMinutes(1);

    static {
        WebDriverManager.chromedriver().setup();
    }

    private final WebDriver driver;

    public Egrp365Bot() {
        this.driver = new ChromeDriver();
        this.driver.manage().window().minimize();
        this.driver.manage().timeouts().pageLoadTimeout(pageLoadTimeout);

        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdownBot));
    }

    public EgrpEntity getInfo(@Nonnull String cadastralNumber) {
        try {
            driver.get(String.format(EGRP365_URL, cadastralNumber));

            WebDriverWait wait = new WebDriverWait(driver, pageLoadTimeout);

            // waiting for loading all page
            wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                    .executeScript("return document.readyState")
                    .equals("complete"));

            if (driver.findElements(By.className(CLASS_SHOW_MORE_INFO)).isEmpty()) {
                throw new Egrp365BotException(String.format("Object with number:'%s' not found",
                        cadastralNumber));
            }

            WebElement show = driver.findElement(By.className(CLASS_SHOW_MORE_INFO));
            wait.until(ExpectedConditions.elementToBeClickable(show));
            show.click();

            Map<String, String> info = new LinkedHashMap<>();

            setFullInfo(wait, info);

            WebElement addressElem = driver.findElement(By.className(CLASS_WITH_ADDRESS));
            WebElement coordElem = driver.findElement(By.className(CLASS_WITH_COORDINATES));

            String[] coordStr = coordElem.getText().split(",");
            double lat = Double.parseDouble(coordStr[0]);
            double lon = Double.parseDouble(coordStr[1]);

            String address = new String(addressElem
                    .getText()
                    .getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8)
                    .intern();

            return EgrpEntity.builder()
                    .cadastralNumber(cadastralNumber)
                    .address(address)
                    .fullInfo(info)
                    .latitude(lat)
                    .longitude(lon)
                    .build();

        } catch (Exception e) {
            shutdownBot();
            throw new Egrp365BotException("Page parsing error.", e);
        }
    }

    private void setFullInfo(WebDriverWait wait, Map<String, String> map) {

        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(MAIN_BLOCKS)))
                .forEach(elementMain -> {
                    String key = new String(elementMain.findElement(By.className(MAIN_HEADER))
                            .getText()
                            .getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8)
                            .intern();

                    String value = new String(elementMain.findElement(By.className(MAIN_TEXT))
                            .getText()
                            .getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8)
                            .intern();
                    map.put(key, value);
                });

        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(MORE_BLOCKS)))
                .forEach(elementMore -> {
                    String key = new String(elementMore.findElement(By.className(MORE_HEADER))
                            .getText()
                            .getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8)
                            .intern();
                    String value = new String(elementMore.findElement(By.className(MORE_TEXT))
                            .getText()
                            .getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8)
                            .intern();

                    map.put(key, value);
                });
    }

    public void shutdownBot() {
        driver.close();
        driver.quit();
    }
}
