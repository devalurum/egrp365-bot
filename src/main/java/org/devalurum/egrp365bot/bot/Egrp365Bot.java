package org.devalurum.egrp365bot.bot;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.StringUtils;
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
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

        this.listenerForClosingAfterShutdown();
    }

    public Egrp365Bot(WebDriver driver) {
        this.driver = driver;

        this.listenerForClosingAfterShutdown();
    }


    public EgrpEntity getInfo(@Nonnull String cadastralNumber) {

        try {
            driver.get(String.format(EGRP365_URL, cadastralNumber));

            WebDriverWait wait = new WebDriverWait(driver, pageLoadTimeout);

            // waiting for loading all page
            wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                    .executeScript("return document.readyState")
                    .equals("complete"));

            WebElement divMore = wait.until(ExpectedConditions.presenceOfElementLocated(By.className(CLASS_MORE_INFO)));
            String classMore = divMore.getAttribute("class");

            wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                    .executeScript(String.format("return document.getElementsByClassName('%s')[0].style.display='inline-block'",
                            classMore))
                    .equals("inline-block"));


            if (driver.findElements(By.className(CLASS_SHOW_MORE_INFO)).isEmpty()) {
                throw new Egrp365BotException(String.format("Object with number:'%s' not found",
                        cadastralNumber));
            }

            WebElement show = driver.findElement(By.className(CLASS_SHOW_MORE_INFO));
            wait.until(ExpectedConditions.elementToBeClickable(show));
            show.click();

            Map<String, String> info = setFullInfo(wait);

            WebElement addressElem = driver.findElement(By.className(CLASS_WITH_ADDRESS));
            WebElement coordElem = driver.findElement(By.className(CLASS_WITH_COORDINATES));

            String[] coordStr = coordElem.getText().split(",");
            double lat = Double.parseDouble(coordStr[0]);
            double lon = Double.parseDouble(coordStr[1]);

            return EgrpEntity.builder()
                    .cadastralNumber(cadastralNumber)
                    .address(addressElem.getText())
                    .fullInfo(info)
                    .latitude(lat)
                    .longitude(lon)
                    .build();

        } catch (Exception e) {
            shutdownBot();
            throw new Egrp365BotException("Page parsing error.", e);
        }
    }

    private Map<String, String> setFullInfo(WebDriverWait wait) {

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

        return mainInfo;
    }

    private void listenerForClosingAfterShutdown() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdownBot));
    }

    public void shutdownBot() {
        driver.quit();
    }
}
