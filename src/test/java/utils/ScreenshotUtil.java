package utils;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenshotUtil {

    // call this from tests where you have driver
    public static String takeScreenshot(WebDriver driver, String name) {
        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String dest = "target/screenshots/" + name + "_" + System.currentTimeMillis() + ".png";
            File destF = new File(dest);
            destF.getParentFile().mkdirs();
            org.apache.commons.io.FileUtils.copyFile(src, destF);
            return dest;
        } catch (IOException | RuntimeException e) {
            System.out.println("[ScreenshotUtil] failed: " + e.getMessage());
            return null;
        }
    }

    // safe static wrapper used by listener (won't do anything if you don't pass driver)
    public static String takeScreenshotStatic(String name) {
        try {
            // try to fetch driver from your BaseClass if available
            Class<?> base = Class.forName("base.BaseClass");
            java.lang.reflect.Method m = base.getMethod("getDriver");
            Object drv = m.invoke(null);
            if (drv instanceof WebDriver) {
                return takeScreenshot((WebDriver) drv, name);
            }
        } catch (Exception e) {
            // ignore: safe fallback
        }
        return null;

}
}
