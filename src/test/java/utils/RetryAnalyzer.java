package utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
    private int count = 0;
    private final int max;

    public RetryAnalyzer() {
        // Get max retries from system property or default to 1
        this.max = Integer.parseInt(System.getProperty("maxRetry", "1"));
    }

    @Override
    public boolean retry(ITestResult result) {
        if (count < max) {
            count++;
            System.out.println("Retrying test: " + result.getName() + 
                               " | Attempt " + count + " of " + max);
            return true;
        }
        return false;
    }
}
