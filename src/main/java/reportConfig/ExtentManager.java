package reportConfig;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import commons.GlobalConstants;

//File nay add vao de cau hinh cho report Extend5
public class ExtentManager {
    public static final ExtentReports extentReports = new ExtentReports();

    public synchronized static ExtentReports createExtentReports() {
        ExtentSparkReporter reporter = new ExtentSparkReporter(GlobalConstants.getGlobalConstants().getProjectPath() + "/extentReport/HTMLReportVersion5.html");
        reporter.config().setReportName("NopCommerce HTML Report");
        reporter.config().setDocumentTitle("NopCommerce HTML Report");
        reporter.config().setTimelineEnabled(true);
        reporter.config().setEncoding("utf-8");
        reporter.config().setTheme(Theme.DARK);

        extentReports.attachReporter(reporter);
        extentReports.setSystemInfo("Company", "Automation FC");
        extentReports.setSystemInfo("Project", "Nop Commerce");
        extentReports.setSystemInfo("Team", "Nocommcer Automation Testing Team");
        extentReports.setSystemInfo("JDK version", GlobalConstants.getGlobalConstants().getJavaVersion());
        return extentReports;
    }

}
