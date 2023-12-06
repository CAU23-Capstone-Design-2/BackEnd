package com.cau.vostom.dev.component;

import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

@Component
public class Crawl {
    private WebDriver driver;

    private String url = "https://www.youtube.com/results?search_query=%EB%8D%B0%EC%9D%B4%EC%8B%9D%EC%8A%A4";

    public void crawl() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\CAU\\Desktop\\BackEnd\\chrome-win64\\chromedriver.exe");
        driver = new org.openqa.selenium.chrome.ChromeDriver();
        driver.get(url);
        System.out.println(driver.getTitle());
        driver.quit();
    }
}
