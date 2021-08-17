import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.mitmproxy.InterceptedMessage;
import io.appium.mitmproxy.MitmproxyJava;
import io.appium.mitmproxy.MitmproxyServer;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import sun.security.krb5.internal.crypto.Des;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeoutException;

public class MITM_Proxy_Test {

//    @BeforeClass
//    public void setMITMCertificate() throws IOException, TimeoutException, InterruptedException {
//        ProxyUtils proxyUtils = new ProxyUtils();
//        MitmproxyJava mitmproxyJava = proxyUtils.startProxy(8866);
//        proxyUtils.addProxyConfig("192.168.1.7", "8866");
//        proxyUtils.downloadMITMCertificate();
//        proxyUtils.stopProxy(mitmproxyJava);
//        proxyUtils.activateCertificateProfilesAndManagement();
//        proxyUtils.activateCertificateAbout();
//    }

    @BeforeClass
    public void setMITMCertificate() throws IOException, TimeoutException, InterruptedException {
        MockServerUtils proxyUtils = new MockServerUtils();
        proxyUtils.downloadMITMCertificate();
        proxyUtils.activateCertificateProfilesAndManagement();
        proxyUtils.activateCertificateAbout();
    }

//    @AfterClass
//    public void proxyTeardown() throws MalformedURLException {
//        ProxyUtils proxyUtils = new ProxyUtils();
//        proxyUtils.removeCertificate();
//        proxyUtils.removeProxyConfig();
//    }

    @AfterClass
    public void proxyTeardown() throws MalformedURLException {
        MockServerUtils proxyUtils = new MockServerUtils();
        proxyUtils.removeCertificate();
    }

    @Test
    public void nativeTest() throws IOException, InterruptedException, TimeoutException {
        ProxyUtils proxyUtils = new ProxyUtils();
        MitmproxyJava proxy = proxyUtils.returnBody(8866, "http://dummy.restapiexample.com/api/v1/employees", "{koliotest}");
        proxy.start();
        DesiredCapabilities safariCapabilities = new DesiredCapabilities();
        safariCapabilities.setCapability("browserName", "safari");
        safariCapabilities.setCapability("nativeWebTap", "true");
        IOSDriver driver = new IOSDriver<>(new URL("http://127.0.0.1:4841/wd/hub"), safariCapabilities);
        driver.get("http://dummy.restapiexample.com/api/v1/employees");
        String pageSource = driver.getPageSource();
        Assert.assertTrue(pageSource.contains("koliotest"));
        proxy.stop();
        driver.get("http://dummy.restapiexample.com/api/v1/employees");
        pageSource = driver.getPageSource();
        Assert.assertTrue(!pageSource.contains("koliotest"));
        driver.quit();
    }
}
