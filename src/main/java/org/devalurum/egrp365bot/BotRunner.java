package org.devalurum.egrp365bot;

import org.devalurum.egrp365bot.bot.Egrp365Bot;
import org.devalurum.egrp365bot.model.EgrpEntity;
import org.devalurum.egrp365bot.webdriver.Browsers;
import org.openqa.selenium.WebDriver;

import javax.annotation.Nonnull;
import java.util.Scanner;

public class BotRunner {

    public static void main(String[] args) {
        //Examples: 38:36:000034:2954

        final String searchCadastralNumber = getCadastralNumber(args);

        WebDriver browser = Browsers.CHROME.setup();

        Egrp365Bot bot = new Egrp365Bot(browser);
        EgrpEntity address = bot.getInfo(searchCadastralNumber);
        System.out.println(address);
        bot.shutdownBot();

    }

    @Nonnull
    private static String getCadastralNumber(String[] args) {
        if (args.length > 0) {
            return args[0];
        }
        final Scanner in = new Scanner(System.in);
        System.out.print("Enter cadastral number: ");
        return in.nextLine();
    }
}
