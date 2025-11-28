package ru.netology.selenium; // Убедитесь, что ваш пакет совпадает

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DebitcardapplicationNegative {

    private WebDriver driver;

    @BeforeAll
    public static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void beforeEach() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless"); // Закомментируйте эту строку, если хотите видеть браузер
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999"); // Адрес вашего тестового приложения
    }

    @AfterEach
    void afterEach() {
        driver.quit();
        driver = null;
    }

    // ТЕСТ 1: Невалидное имя (латиница)
    // Ошибка на строке 51/54 - ожидание с заглавными "И" и "Ф"
    @Test
    public void shouldBeFailedIncorrectNameInput() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Gleb"); // Имя на латинице
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79800555555");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();

        // ИСПРАВЛЕНИЕ: Ожидаемое сообщение ТОЧНО совпадает с фактическим (с заглавными "И" и "Ф")
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.",
                driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText().trim());
        assertTrue(driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).isDisplayed());
    }

    // ТЕСТ 2: Невалидный номер телефона (слишком короткий)
    // Строка 80 (была 67), где произошла ошибка
    @Test
    public void shouldBeFailedIfInvalidPhoneNumberTooShort() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иванов Иван"); // Валидное имя
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+712345"); // Невалидный номер: слишком короткий
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();

        // ИСПРАВЛЕНИЕ: Ожидаемое сообщение теперь ТОЧНО совпадает с фактическим
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.",
                driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText().trim());
        assertTrue(driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).isDisplayed());
    }

    // ТЕСТ 3: Неотмеченное согласие
    @Test
    public void shouldBeFailedIfAgreementNotChecked() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Петров Петр"); // Валидное имя
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79123456789"); // Валидный телефон
        // driver.findElement(By.cssSelector("[data-test-id='agreement']")).click(); // НЕ КЛИКАЕМ на чекбокс
        driver.findElement(By.cssSelector("button")).click();

        // Проверяем, что элемент согласия помечен как невалидный.
        // Судя по скриншотам, сообщение об ошибке для согласия не отображается отдельно.
        // Просто проверяем, что сам элемент [data-test-id=agreement] получает класс .input_invalid и виден.
        assertTrue(driver.findElement(By.cssSelector("[data-test-id=agreement].input_invalid")).isDisplayed());
        // Если появится конкретный текст ошибки для чекбокса, его можно добавить здесь:
        // assertEquals("Необходимо дать согласие на обработку персональных данных", driver.findElement(By.cssSelector("[data-test-id=agreement].input_invalid .checkbox__text")).getText().trim());
    }

    // ТЕСТ 4: Пустое поле имени
    // Строка 97 (была 97), где произошла ошибка
    @Test
    public void shouldBeFailedIfNameIsEmpty() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys(""); // Пустое имя
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79123456789");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();

        // ИСПРАВЛЕНИЕ: Ожидаемое сообщение теперь ТОЧНО совпадает с фактическим
        assertEquals("Поле обязательно для заполнения",
                driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText().trim());
        assertTrue(driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).isDisplayed());
    }

    // ТЕСТ 5: Пустое поле телефона
    @Test
    public void shouldBeFailedIfPhoneIsEmpty() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Сергеев Сергей"); // Валидное имя
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys(""); // Пустой телефон
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();

        // ИСПРАВЛЕНИЕ: Ожидаемое сообщение теперь ТОЧНО совпадает с фактическим (как и для короткого телефона)
        assertEquals("Поле обязательно для заполнения",
                driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText().trim());
        assertTrue(driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).isDisplayed());
    }

    // ТЕСТ 6: Имя с цифрами
    @Test
    public void shouldBeFailedIfNameHasNumbers() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иван123"); // Имя с цифрами
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79123456789");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();

        // Ожидаемое сообщение
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.",
                driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText().trim());
        assertTrue(driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).isDisplayed());
    }

    // ТЕСТ 7: Телефон без плюса
    @Test
    public void shouldBeFailedIfPhoneWithoutPlus() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Олег Олегов"); // Валидное имя
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("79123456789"); // Телефон без начального '+'
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();

        // ИСПРАВЛЕНИЕ: Ожидаемое сообщение теперь ТОЧНО совпадает с фактическим (как и для короткого телефона)
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.",
                driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText().trim());
        assertTrue(driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).isDisplayed());
    }
}
