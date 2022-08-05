package ru.netology.domain;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Nested;
import org.openqa.selenium.Keys;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.time.Duration;

import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryTest {
    public String shouldChooseDate(int minDaysToAdd) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String nearestDate = LocalDate.now().plusDays(minDaysToAdd).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        return nearestDate;
    }

    @Test
    void shouldGoBySuccessPath() {
        int minDaysToAdd = 3;
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
        $x("//input[@placeholder='Город']").setValue("Санкт-Петербург");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id=date] input").setValue(shouldChooseDate(minDaysToAdd));
        $("[data-test-id='name'] input").setValue("Алекс Окслейд-Чемберлен");
        $("[data-test-id='phone'] input").setValue("+79516827144");
        $(withText("Я соглашаюсь с условиями обработки и")).click();
        $x("//*[text()=\"Забронировать\"]").click();
        $("[data-test-id=notification]")
                .shouldBe(Condition.text("Успешно! Встреча успешно забронирована на " + shouldChooseDate(minDaysToAdd)), Duration.ofSeconds(15));
    }

    @Nested
    @DisplayName("City validation")
    class cityValidation {
        @Test
        void shouldChooseCityNotFromTheList() {
            int minDaysToAdd = 3;
            Configuration.holdBrowserOpen = true;
            open("http://localhost:9999");
            $x("//input[@placeholder='Город']").setValue("Гатчина");
            $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
            $("[data-test-id=date] input").setValue(shouldChooseDate(minDaysToAdd));
            $("[data-test-id='name'] input").setValue("Алекс Окслейд-Чемберлен");
            $("[data-test-id='phone'] input").setValue("+79516827144");
            $(withText("Я соглашаюсь с условиями обработки и")).click();
            $x("//*[text()=\"Забронировать\"]").click();
            $("[data-test-id=city] .input__sub")
                    .shouldBe(Condition.text("Доставка в выбранный город недоступна "));
        }

        @Test
        void shouldSetCityLatinLetters() {
            int minDaysToAdd = 3;
            Configuration.holdBrowserOpen = true;
            open("http://localhost:9999");
            $x("//input[@placeholder='Город']").setValue("Saint-Petersburg");
            $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
            $("[data-test-id=date] input").setValue(shouldChooseDate(minDaysToAdd));
            $("[data-test-id='name'] input").setValue("Алекс Окслейд-Чемберлен");
            $("[data-test-id='phone'] input").setValue("+79516827144");
            $(withText("Я соглашаюсь с условиями обработки и")).click();
            $x("//*[text()=\"Забронировать\"]").click();
            $("[data-test-id=city] .input__sub")
                    .shouldBe(Condition.text("Доставка в выбранный город недоступна "));
        }

        @Test
        void shouldSetNumbersInCityField() {
            int minDaysToAdd = 3;
            Configuration.holdBrowserOpen = true;
            open("http://localhost:9999");
            $x("//input[@placeholder='Город']").setValue("121212");
            $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
            $("[data-test-id=date] input").setValue(shouldChooseDate(minDaysToAdd));
            $("[data-test-id='name'] input").setValue("Алекс Окслейд-Чемберлен");
            $("[data-test-id='phone'] input").setValue("+79516827144");
            $(withText("Я соглашаюсь с условиями обработки и")).click();
            $x("//*[text()=\"Забронировать\"]").click();
            $("[data-test-id=city] .input__sub")
                    .shouldBe(Condition.text("Доставка в выбранный город недоступна "));
        }

        @Test
        void shouldSetNoCity() {
            int minDaysToAdd = 3;
            Configuration.holdBrowserOpen = true;
            open("http://localhost:9999");
            $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
            $("[data-test-id=date] input").setValue(shouldChooseDate(minDaysToAdd));
            $("[data-test-id='name'] input").setValue("Алекс Окслейд-Чемберлен");
            $("[data-test-id='phone'] input").setValue("+79516827144");
            $(withText("Я соглашаюсь с условиями обработки и")).click();
            $x("//*[text()=\"Забронировать\"]").click();
            $("[data-test-id=city] .input__sub")
                    .shouldBe(Condition.text("Поле обязательно для заполнения "));
        }
    }

    @Nested
    @DisplayName("Date validation")
    class dateValidation {
        @Test
        void shouldChooseDateInPast() {
            int minDaysToAdd = -3;
            Configuration.holdBrowserOpen = true;
            open("http://localhost:9999");
            $x("//input[@placeholder='Город']").setValue("Санкт-Петербург");
            $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
            $("[data-test-id=date] input").setValue(shouldChooseDate(minDaysToAdd));
            $("[data-test-id='name'] input").setValue("Алекс Окслейд-Чемберлен");
            $("[data-test-id='phone'] input").setValue("+79516827144");
            $(withText("Я соглашаюсь с условиями обработки и")).click();
            $x("//*[text()=\"Забронировать\"]").click();
            $("[data-test-id=date] .input__sub")
                    .shouldBe(Condition.text("Заказ на выбранную дату невозможен"));
        }

        @Test
        void shouldChooseDateLessThanThreeDays() {
            int minDaysToAdd = 2;
            Configuration.holdBrowserOpen = true;
            open("http://localhost:9999");
            $x("//input[@placeholder='Город']").setValue("Санкт-Петербург");
            $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
            $("[data-test-id=date] input").setValue(shouldChooseDate(minDaysToAdd));
            $("[data-test-id='name'] input").setValue("Алекс Окслейд-Чемберлен");
            $("[data-test-id='phone'] input").setValue("+79516827144");
            $(withText("Я соглашаюсь с условиями обработки и")).click();
            $x("//*[text()=\"Забронировать\"]").click();
            $("[data-test-id=date] .input__sub")
                    .shouldBe(Condition.text("Заказ на выбранную дату невозможен"));
        }

        @Test
        void shouldChooseNoDate() {
            Configuration.holdBrowserOpen = true;
            open("http://localhost:9999");
            $x("//input[@placeholder='Город']").setValue("Санкт-Петербург");
            $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
            $("[data-test-id='name'] input").setValue("Алекс Окслейд-Чемберлен");
            $("[data-test-id='phone'] input").setValue("+79516827144");
            $(withText("Я соглашаюсь с условиями обработки и")).click();
            $x("//*[text()=\"Забронировать\"]").click();
            $("[data-test-id=date] .input__sub")
                    .shouldBe(Condition.text("Неверно введена дата"));
        }
    }

    @Nested
    @DisplayName("Name validation")
    class nameValidation {
        @Test
        void shouldSetLatinName() {
            int minDaysToAdd = 3;
            Configuration.holdBrowserOpen = true;
            open("http://localhost:9999");
            $x("//input[@placeholder='Город']").setValue("Санкт-Петербург");
            $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
            $("[data-test-id=date] input").setValue(shouldChooseDate(minDaysToAdd));
            $("[data-test-id='name'] input").setValue("Аlex Oxlade-Chamberlain");
            $("[data-test-id='phone'] input").setValue("+79516827144");
            $(withText("Я соглашаюсь с условиями обработки и")).click();
            $x("//*[text()=\"Забронировать\"]").click();
            $("[data-test-id=name] .input__sub")
                    .shouldBe(Condition.text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
        }

        @Test
        void shouldSetNumbersInName() {
            int minDaysToAdd = 3;
            Configuration.holdBrowserOpen = true;
            open("http://localhost:9999");
            $x("//input[@placeholder='Город']").setValue("Санкт-Петербург");
            $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
            $("[data-test-id=date] input").setValue(shouldChooseDate(minDaysToAdd));
            $("[data-test-id='name'] input").setValue("13245766612");
            $("[data-test-id='phone'] input").setValue("+79516827144");
            $(withText("Я соглашаюсь с условиями обработки и")).click();
            $x("//*[text()=\"Забронировать\"]").click();
            $("[data-test-id=name] .input__sub")
                    .shouldBe(Condition.text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
        }

        @Test
        void shouldSetOneLetterInName() {
            int minDaysToAdd = 3;
            Configuration.holdBrowserOpen = true;
            open("http://localhost:9999");
            $x("//input[@placeholder='Город']").setValue("Санкт-Петербург");
            $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
            $("[data-test-id=date] input").setValue(shouldChooseDate(minDaysToAdd));
            $("[data-test-id='name'] input").setValue("Ы");
            $("[data-test-id='phone'] input").setValue("+79516827144");
            $(withText("Я соглашаюсь с условиями обработки и")).click();
            $x("//*[text()=\"Забронировать\"]").click();
            $("[data-test-id=notification]")
                    .shouldBe(Condition.text("Успешно! Встреча успешно забронирована на " + shouldChooseDate(minDaysToAdd)), Duration.ofSeconds(15));
        }

        @Test
        void shouldSetNoName() {
            int minDaysToAdd = 3;
            Configuration.holdBrowserOpen = true;
            open("http://localhost:9999");
            $x("//input[@placeholder='Город']").setValue("Санкт-Петербург");
            $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
            $("[data-test-id=date] input").setValue(shouldChooseDate(minDaysToAdd));
            $("[data-test-id='phone'] input").setValue("+79516827144");
            $(withText("Я соглашаюсь с условиями обработки и")).click();
            $x("//*[text()=\"Забронировать\"]").click();
            $("[data-test-id=name] .input__sub")
                    .shouldBe(Condition.text("Поле обязательно для заполнения"));
        }
    }

    @Nested
    @DisplayName("Phone Validation")
    class phoneValidation {
        @Test
        void shouldSetPhoneWithWhitespaces() {
            int minDaysToAdd = 3;
            Configuration.holdBrowserOpen = true;
            open("http://localhost:9999");
            $x("//input[@placeholder='Город']").setValue("Санкт-Петербург");
            $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
            $("[data-test-id=date] input").setValue(shouldChooseDate(minDaysToAdd));
            $("[data-test-id='name'] input").setValue("Алекс Окслейд-Чемберлен");
            $("[data-test-id='phone'] input").setValue("+7 951 682 71 44");
            $(withText("Я соглашаюсь с условиями обработки и")).click();
            $x("//*[text()=\"Забронировать\"]").click();
            $("[data-test-id=phone] .input__sub")
                    .shouldBe(Condition.text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
        }

        @Test
        void shouldSetShortPhoneNumber() {
            int minDaysToAdd = 3;
            Configuration.holdBrowserOpen = true;
            open("http://localhost:9999");
            $x("//input[@placeholder='Город']").setValue("Санкт-Петербург");
            $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
            $("[data-test-id=date] input").setValue(shouldChooseDate(minDaysToAdd));
            $("[data-test-id='name'] input").setValue("Алекс Окслейд-Чемберлен");
            $("[data-test-id='phone'] input").setValue("6827144");
            $(withText("Я соглашаюсь с условиями обработки и")).click();
            $x("//*[text()=\"Забронировать\"]").click();
            $("[data-test-id=phone] .input__sub")
                    .shouldBe(Condition.text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
        }

        @Test
        void shouldSetLongPhoneNumber() {
            int minDaysToAdd = 3;
            Configuration.holdBrowserOpen = true;
            open("http://localhost:9999");
            $x("//input[@placeholder='Город']").setValue("Санкт-Петербург");
            $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
            $("[data-test-id=date] input").setValue(shouldChooseDate(minDaysToAdd));
            $("[data-test-id='name'] input").setValue("Алекс Окслейд-Чемберлен");
            $("[data-test-id='phone'] input").setValue("682714468271446827144");
            $(withText("Я соглашаюсь с условиями обработки и")).click();
            $x("//*[text()=\"Забронировать\"]").click();
            $("[data-test-id=phone] .input__sub")
                    .shouldBe(Condition.text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
        }

        @Test
        void shouldSetNoPhone() {
            int minDaysToAdd = 3;
            Configuration.holdBrowserOpen = true;
            open("http://localhost:9999");
            $x("//input[@placeholder='Город']").setValue("Санкт-Петербург");
            $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
            $("[data-test-id=date] input").setValue(shouldChooseDate(minDaysToAdd));
            $("[data-test-id='name'] input").setValue("Алекс Окслейд-Чемберлен");
            $(withText("Я соглашаюсь с условиями обработки и")).click();
            $x("//*[text()=\"Забронировать\"]").click();
            $("[data-test-id=phone] .input__sub")
                    .shouldBe(Condition.text("Поле обязательно для заполнения"));
        }
    }

    @Nested
    @DisplayName("Checkbox")
    class checkboxValidation {
        @Test
        void shouldNoCheckbox() {
            int minDaysToAdd = 3;
            Configuration.holdBrowserOpen = true;
            open("http://localhost:9999");
            $x("//input[@placeholder='Город']").setValue("Санкт-Петербург");
            $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
            $("[data-test-id=date] input").setValue(shouldChooseDate(minDaysToAdd));
            $("[data-test-id='name'] input").setValue("Алекс Окслейд-Чемберлен");
            $("[data-test-id='phone'] input").setValue("+79516827144");
            $x("//*[text()=\"Забронировать\"]").click();
            $("[data-test-id=agreement].input_invalid .checkbox__text")
                    .shouldBe(Condition.text("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
        }
    }

    @Nested
    @DisplayName("Second task")
    class secondTask {
        @Test
        void shouldChooseCityByFirstLetters() {
            int minDaysToAdd = 3;
            Configuration.holdBrowserOpen = true;
            open("http://localhost:9999");
            $x("//input[@placeholder='Город']").setValue("Са");
            $(byText("Санкт-Петербург")).click();
            $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
            $("[data-test-id=date] input").setValue(shouldChooseDate(minDaysToAdd));
            $("[data-test-id='name'] input").setValue("Алекс Окслейд-Чемберлен");
            $("[data-test-id='phone'] input").setValue("+79516827144");
            $(withText("Я соглашаюсь с условиями обработки и")).click();
            $x("//*[text()=\"Забронировать\"]").click();
            $("[data-test-id=notification]")
                    .shouldBe(Condition.text("Успешно! Встреча успешно забронирована на " + shouldChooseDate(minDaysToAdd)), Duration.ofSeconds(15));
        }

        @Test
        void shouldOrderDeliveryOverTheWeek() {
            int minDaysToAdd = 7;
            Configuration.holdBrowserOpen = true;
            open("http://localhost:9999");
            $x("//input[@placeholder='Город']").setValue("Санкт-Петербург");
            $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
            $("[data-test-id=date] input").setValue(shouldChooseDate(minDaysToAdd));
            $("[data-test-id='name'] input").setValue("Алекс Окслейд-Чемберлен");
            $("[data-test-id='phone'] input").setValue("+79516827144");
            $(withText("Я соглашаюсь с условиями обработки и")).click();
            $x("//*[text()=\"Забронировать\"]").click();
            $("[data-test-id=notification]")
                    .shouldBe(Condition.text("Успешно! Встреча успешно забронирована на " + shouldChooseDate(minDaysToAdd)), Duration.ofSeconds(15));
        }
    }
}
