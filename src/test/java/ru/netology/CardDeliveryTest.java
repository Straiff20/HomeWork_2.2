package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class CardDeliveryTest {
    static class Form {
        private WebDriver driver;

        @Test
        void cardDeliveryTest() throws InterruptedException {
            Date currentDate = new Date();

            Calendar c = Calendar.getInstance();
            c.setTime(currentDate);
            c.add(Calendar.DATE, 3);

            Date currentDatePlusThree = c.getTime();
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

            open("http://localhost:9999/");

            $("#root > .App_appContainer__3jRx1").waitUntil(Condition.visible, 5000);
            $(".input__control[type=text]").setValue("Москва");

            $(".input__control[type=tel][placeholder='Дата встречи'").sendKeys(Keys.CONTROL, "a");
            $(".input__control[type=tel][placeholder='Дата встречи'").sendKeys(Keys.DELETE);
            Thread.sleep(1000);

            $(".input__control[type=tel][placeholder='Дата встречи'").sendKeys(dateFormat.format(currentDatePlusThree));

            $(".input__control[name=name]").sendKeys("Иванов Иван");

            $(".input__control[name=phone]").sendKeys("+79061234567");
            $("span.checkbox__box").click();
            Thread.sleep(2000);

            $("span.button__text").click();
            $(".notification[data-test-id=notification]").waitUntil(Condition.visible, 15000);
            $(".notification[data-test-id=notification]").shouldHave(text("Успешно!"));
            $(".notification[data-test-id=notification]").shouldHave(text("Встреча успешно забронирована на " + dateFormat.format(currentDatePlusThree)));
        }

        @Test
        void CardDeliveryWithClicksTest() throws InterruptedException {
            open("http://localhost:9999/");

            $("#root > .App_appContainer__3jRx1").waitUntil(Condition.visible, 5000);
            $(".input__control[type=text]").setValue("Мос");
            $(".popup").isDisplayed();
            $(byText("Москва")).click();

            $(".input__control[type=tel][placeholder='Дата встречи'").click();
            Thread.sleep(1000);
            $(".calendar[role=grid]").isDisplayed();

            $(".calendar__arrow[data-step='1']").click();
            $(".calendar__arrow[data-step='1']").click();
            $(".calendar__arrow[data-step='1']").click();
            Thread.sleep(1000);
            $("table.calendar__layout").shouldHave(text("10")).click();
            String data = $(".input__control[type=tel][placeholder='Дата встречи'").getText();
            Thread.sleep(3000);

            $(".input__control[name=name]").sendKeys("Иванов Иван");

            $(".input__control[name=phone]").sendKeys("+79061234567");
            $("span.checkbox__box").click();
            Thread.sleep(2000);

            $("span.button__text").click();
            $(".notification[data-test-id=notification]").waitUntil(Condition.visible, 15000);
            $(".notification[data-test-id=notification]").shouldHave(text("Успешно!"));
            $(".notification[data-test-id=notification]").shouldHave(text(data));
        }
    }
}
