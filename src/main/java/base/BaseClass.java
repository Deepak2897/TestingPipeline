package base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseClass {

    // Thread local for Parallel Execution
    public static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    public static Properties config = new Properties();

    // per-thread temp chrome profile path (for cleanup)
    private static ThreadLocal<Path> tempProfilePath = new ThreadLocal<>();

    // Load config.properties once (from classpath - CI friendly; fallback to src/test/resources)
    static {
        try (InputStream in = BaseClass.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in == null) {
                // fallback to file path for developer convenience
                String fallback = "src/test/resources/config.properties";
                try (InputStream fin = Files.newInputStream(Path.of(fallback))) {
                    config.load(fin);
                    System.out.println("[WARN] Loaded config.properties from " + fallback);
                }
            } else {
                config.load(in);
            }

            // Ensure drivers quit if JVM shuts down (important for CI)
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    WebDriver wd = driver.get();
                    if (wd != null) {
                        try { wd.quit(); } catch (Exception ignored) {}
                        driver.remove();
                    }
                    cleanupProfileDir();
                } catch (Exception ignored) {}
            }));

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Could not load config.properties", e);
        }
    }

    public static WebDriver getDriver() {
        WebDriver wd = driver.get();
        if (wd == null) throw new IllegalStateException("WebDriver has not been initialized. Call launchBrowser() first.");
        return wd;
    }

    // Utility: read system property > env var > config.properties
    private String getConfig(String key, String defaultVal) {
        String sys = System.getProperty(key);
        if (sys != null && !sys.isEmpty()) return sys;

        String env = System.getenv(key);
        if (env != null && !env.isEmpty()) return env;

        return config.getProperty(key, defaultVal);
    }

    // Lazily evaluate default timeout (so config is already loaded)
    private long getDefaultTimeout() {
        return Long.parseLong(getConfig("explicitTimeoutSeconds", "15"));
    }

    public void launchBrowser() {
        String browser = getConfig("browser", "chrome");
        String url = getConfig("baseUrl", config.getProperty("baseUrl", "https://adactinhotelapp.com/"));
        String runMode = getConfig("runMode", "local");
        boolean headless = Boolean.parseBoolean(getConfig("headless", "false"));
        long implicitWait = Long.parseLong(getConfig("implicitTimeoutSeconds", "10"));
        String gridUrl = getConfig("gridUrl", config.getProperty("gridUrl", "http://localhost:4444/wd/hub"));

        if (browser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();

            Map<String, Object> prefs = new HashMap<>();
            prefs.put("credentials_enable_service", false);
            prefs.put("profile.password_manager_enabled", false);
            options.setExperimentalOption("prefs", prefs);

            // Stability / CI-safe args
            options.addArguments("--disable-save-password-bubble");
            options.addArguments("--disable-notifications");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
            options.addArguments("--disable-extensions");
            options.addArguments("--remote-allow-origins=*");

            // Extra flags
            options.addArguments("--no-first-run");
            options.addArguments("--disable-background-networking");
            options.addArguments("--disable-default-apps");
            options.addArguments("--disable-translate");
            options.addArguments("--disable-sync");
            options.addArguments("--disable-background-timer-throttling");
            options.addArguments("--disable-renderer-backgrounding");
            options.addArguments("--remote-debugging-port=0");

            // === Unique user-data-dir per run (avoid "profile in use" errors) ===
            try {
                Path profileDir = Files.createTempDirectory("chrome-profile-" + UUID.randomUUID());
                options.addArguments("--user-data-dir=" + profileDir.toAbsolutePath().toString());
                tempProfilePath.set(profileDir);
                System.out.println("[DEBUG] Using temp Chrome profile: " + profileDir);
            } catch (Exception e) {
                System.err.println("[WARN] Could not create temp chrome profile dir: " + e.getMessage());
            }

            // Headless (can be forced via -Dheadless=true, or via env CI)
            boolean ciHeadless = headless || System.getenv("CI") != null;
            if (ciHeadless) {
                options.addArguments("--headless=new"); // modern headless
                System.out.println("[DEBUG] Headless enabled");
            }

            // Remote vs Local
            if (runMode.equalsIgnoreCase("remote")) {
                try {
                    driver.set(new RemoteWebDriver(new URL(gridUrl), options));
                } catch (Exception e) {
                    throw new RuntimeException("Could not connect to Selenium Grid: " + e.getMessage(), e);
                }
            } else {
                driver.set(new ChromeDriver(options));
            }

        } else if (browser.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            // You can add FirefoxOptions similarly if required
            driver.set(new FirefoxDriver());
        } else {
            throw new RuntimeException("Browser not supported: " + browser);
        }

        // Implicit wait & window
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        try {
            getDriver().manage().window().maximize();
        } catch (Exception ignored) {}
        getDriver().get(url);
    }

    public void closeBrowser() {
        WebDriver wd = driver.get();
        if (wd != null) {
            try {
                wd.quit();
            } catch (Exception ignored) {}
            driver.remove();
        }
        cleanupProfileDir();
    }

    private static void cleanupProfileDir() {
        Path path = tempProfilePath.get();
        if (path != null) {
            try {
                if (Files.exists(path)) {
                    Files.walk(path)
                         .sorted(Comparator.reverseOrder())
                         .forEach(p -> {
                             try { Files.deleteIfExists(p); } catch (Exception ignored) {}
                         });
                }
            } catch (Exception ignored) {}
            tempProfilePath.remove();
        }
    }

    // Click element with wait + JS fallback
    public void clickElement(WebElement element) {
        waitForClickable(element);
        try {
            element.click();
        } catch (Exception e) {
            try {
                ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", element);
            } catch (Exception ex) {
                throw new RuntimeException("Unable to click element: " + ex.getMessage(), ex);
            }
        }
    }

    // Enter data with wait
    public void enterData(WebElement element, String text) {
        waitForVisible(element);
        try {
            element.clear();
        } catch (Exception ignored) {}
        element.sendKeys(text);
    }

    // Wait until element is visible
    public WebElement waitForVisible(WebElement element) {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(getDefaultTimeout()))
                .until(ExpectedConditions.visibilityOf(element));
    }

    // Wait until element is clickable
    public WebElement waitForClickable(WebElement element) {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(getDefaultTimeout()))
                .until(ExpectedConditions.elementToBeClickable(element));
    }

    // Dropdown select (keeps same method name as Sauce Demo / your pages)
    public void selectByVisibilityText(WebElement element, String text) {
        waitForVisible(element);
        new Select(element).selectByVisibleText(text);
    }

    // Take Screenshot (Save inside target/ so CI can collect artifacts)
    public String takeScreenshot(String fileName) {
        File srcFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
        String artifactsDir = System.getProperty("artifactsDir", getConfig("artifactsDir", "target/screenshots"));
        String path = artifactsDir + "/" + fileName + "_" + System.currentTimeMillis() + ".png";

        try {
            Files.createDirectories(Path.of(artifactsDir));
            Files.copy(srcFile.toPath(), Path.of(path), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    // Title
    public String getPageTitle() {
        return getDriver().getTitle();
    }

    // Scroll
    public void scrollPage(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", element);
    }

    // Scroll Top
    public void scrollToTop() {
        ((JavascriptExecutor) getDriver()).executeScript("window.scrollTo(0, 0)");
    }
}
