package listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import base.BaseClass;
import reports.ExtentManager;
import com.aventstack.extentreports.Status;

public class TestListener extends BaseClass implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        extent = ExtentManager.getExtentReports();
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }

    @Override
    public void onTestStart(ITestResult result) {
        test = extent.createTest(result.getMethod().getMethodName());
        test.log(Status.INFO, "Test Started: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.log(Status.PASS, "Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String path = takeScreenshot(result.getMethod().getMethodName());
        try { test.addScreenCaptureFromPath(path); } catch (Exception e) { e.printStackTrace(); }
        test.log(Status.FAIL, result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        test.log(Status.SKIP, "Test Skipped");
    }
}
