package org.devalurum.egrp365bot.webdriver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;

import static org.devalurum.egrp365bot.utils.Constants.dummyDriver;
import static org.devalurum.egrp365bot.utils.Constants.pageLoadTimeout;

public enum Browsers {

    CHROME {
        public WebDriver setup() {
            WebDriverManager.chromedriver().setup();

            WebDriver driver = new ChromeDriver();
            setOptions(driver);
            return driver;
        }
    },
    IE {
        public WebDriver setup() {
            WebDriverManager.iedriver().setup();

            WebDriver driver = new InternetExplorerDriver();
            setOptions(driver);
            return driver;
        }
    },
    FIREFOX {
        public WebDriver setup() {
            WebDriverManager.firefoxdriver().setup();

            WebDriver driver = new FirefoxDriver();
            setOptions(driver);
            return driver;
        }
    },
    SAFARI {
        public WebDriver setup() {
            WebDriverManager.safaridriver().setup();

            WebDriver driver = new SafariDriver();
            setOptions(driver);
            return driver;
        }
    };

    public WebDriver setup(){
        return dummyDriver;
    }

    private static void setOptions(WebDriver driver) {
        driver.manage().window().minimize();
        driver.manage().timeouts().pageLoadTimeout(pageLoadTimeout);
    }

}
