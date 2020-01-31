package ru.netology;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;


public class CardDeliveryTest {

    int FUTURE_DATE = 7;

    private static String methodDate(int days) {
        Date currentDate = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.DATE, days);

        Date extendedDate = c.getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(extendedDate);
    }

    private static long methodTimestamp(int days) {
        Date currentDate = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.DATE, days);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        Date extendedDate = c.getTime();
        return extendedDate.getTime();
    }

    @Test
    void cardDeliveryTest() {

        open("http://localhost:9999/");

        $("#root > .App_appContainer__3jRx1").waitUntil(Condition.visible, 5000);
        $(".input__control[type=text]").setValue("Москва");

        $(".input__control[type=tel][placeholder='Дата встречи'").sendKeys(Keys.CONTROL, "a");
        $(".input__control[type=tel][placeholder='Дата встречи'").sendKeys(Keys.DELETE);

        $(".input__control[type=tel][placeholder='Дата встречи'").sendKeys(methodDate(FUTURE_DATE));

        $(".input__control[name=name]").sendKeys("Иванов Иван");

        $(".input__control[name=phone]").sendKeys("+79061234567");
        $("span.checkbox__box").click();

        $("span.button__text").click();
        $(".notification[data-test-id=notification]").waitUntil(Condition.visible, 15000);
        $(".notification[data-test-id=notification]").shouldHave(text("Успешно!"));
        $(".notification[data-test-id=notification]").shouldHave(text("Встреча успешно забронирована на " + methodDate(FUTURE_DATE)));
    }

    @Test
    void CardDeliveryWithClicksTest() {
        open("http://localhost:9999/");

        $("#root > .App_appContainer__3jRx1").waitUntil(Condition.visible, 5000);
        $(".input__control[type=text]").setValue("Мос");
        $(".popup").isDisplayed();
        $(byText("Москва")).click();

        $(".input__control[type=tel][placeholder='Дата встречи'").click();
        $(".calendar[role=grid]").isDisplayed();

        long extendedTimestamp = methodTimestamp(FUTURE_DATE);
        String extended = String.valueOf(extendedTimestamp);
        $("tr.calendar__row  td.calendar__day[data-day=\"" + extended + "\"]").click();
        String date = $(".input__control[type=tel]").getText();

        $(".input__control[name=name]").sendKeys("Иванов Иван");

        $(".input__control[name=phone]").sendKeys("+79061234567");
        $("span.checkbox__box").click();

        $("span.button__text").click();
        $(".notification[data-test-id=notification]").waitUntil(Condition.visible, 15000);
        $(".notification[data-test-id=notification]").shouldHave(text("Успешно!"));
        $(".notification[data-test-id=notification]").shouldHave(text("Встреча успешно забронирована на " + date));
    }
}
