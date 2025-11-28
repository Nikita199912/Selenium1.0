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
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void afterEach() {
        driver.quit();
        driver = null;
    }


    @Test
    public void shouldBeFailedIncorrectNameInput() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Gleb"); // Имя на латинице
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79800555555");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.",
                driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText().trim());
        assertTrue(driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).isDisplayed());
    }


    @Test
    public void shouldBeFailedIfInvalidPhoneNumberTooShort() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иванов Иван"); // Валидное имя
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+712345"); // Невалидный номер: слишком короткий
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.",
                driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText().trim());
        assertTrue(driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).isDisplayed());
    }


    @Test
    public void shouldBeFailedIfAgreementNotChecked() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Петров Петр"); // Валидное имя
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79123456789"); // Валидный телефон
        driver.findElement(By.cssSelector("button")).click();
        assertTrue(driver.findElement(By.cssSelector("[data-test-id=agreement].input_invalid")).isDisplayed());
    }

    @Test
    public void shouldBeFailedIfNameIsEmpty() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys(""); // Пустое имя
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79123456789");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        assertEquals("Поле обязательно для заполнения",
                driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText().trim());
        assertTrue(driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).isDisplayed());
    }

    @Test
    public void shouldBeFailedIfPhoneIsEmpty() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Сергеев Сергей"); // Валидное имя
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys(""); // Пустой телефон
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        assertEquals("Поле обязательно для заполнения",
                driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText().trim());
        assertTrue(driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).isDisplayed());
    }


    @Test
    public void shouldBeFailedIfNameHasNumbers() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иван123"); // Имя с цифрами
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79123456789");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.",
                driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText().trim());
        assertTrue(driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).isDisplayed());
    }

    @Test
    public void shouldBeFailedIfPhoneWithoutPlus() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Олег Олегов"); // Валидное имя
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("79123456789"); // Телефон без начального '+'
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.",
                driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText().trim());
        assertTrue(driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).isDisplayed());
    }
}
