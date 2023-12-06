package com.cau.vostom.dev.service;

import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

@Service
public class CrawllerService {
    private WebDriver driver;

    private String base_url = "https://www.youtube.com/results?search_query=";

    public void crawl(String keyword) {
        // 현재 시스템이 윈도우인지 리눅스인지 확인
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            System.setProperty("webdriver.chrome.driver", "C:\\Users\\CAU\\Desktop\\BackEnd\\chrome-win64\\chromedriver.exe");
        } else {
            System.setProperty("webdriver.chrome.driver", "/home/snark/dev/jiwoo/chromedriver");
        }
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\CAU\\Desktop\\BackEnd\\chrome-win64\\chromedriver.exe");
        driver = new org.openqa.selenium.chrome.ChromeDriver();
        driver.get(base_url + keyword);
        System.out.println(driver.getTitle());
        driver.quit();
    }
}
