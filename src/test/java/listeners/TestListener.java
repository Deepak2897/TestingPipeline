package listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import base.BaseClass;
import reports.ExtentManager;
import utils.ScreenshotUtil;

import com.aventstack.extentreports.Status;

public class TestListener implements ITestListener {

	@Override
    public void onStart(ITestContext context) {
        System.out.println("[TestListener] onStart: " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("[TestListener] onFinish: " + context.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("[TestListener] onTestStart: " + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("[TestListener] onTestSuccess: " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("[TestListener] onTestFailure: " + result.getName());
        // optionally call screenshot util but guarded to avoid NPEs
        try {
            ScreenshotUtil.takeScreenshotStatic(result.getName());
        } catch (Exception e) {
            System.out.println("[TestListener] screenshot failed: " + e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("[TestListener] onTestSkipped: " + result.getName());
    }
}