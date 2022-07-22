package org.devalurum.egrp365bot.utils;

import lombok.experimental.UtilityClass;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

@UtilityClass
public final class Constants {

    public final static String EGRP365_URL = "https://egrp365.ru/map/?kadnum=%s";

    public final static Duration pageLoadTimeout = Duration.ofMinutes(1);

    public final static WebDriver dummyDriver = null;

    public final static String CLASS_WITH_COORDINATES = "more-info__info-item-coords";
    public final static String CLASS_WITH_ADDRESS = "address-text";
    public final static String CLASS_SHOW_MORE_INFO = "more-info__show";
    public final static String CLASS_MORE_INFO = "more-info";

    public final static String MAIN_BLOCKS = ".main_info_block-content > .info__info-item";
    public final static String MAIN_HEADER = "info__info-item-header";
    public final static String MAIN_TEXT = "info__info-item-text";

    public final static String MORE_BLOCKS = ".more-info__info > .more-info__info-item";
    public final static String MORE_HEADER = "more-info__info-item-header";
    public final static String MORE_TEXT = "more-info__info-item-text";

}
