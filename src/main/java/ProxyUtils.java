import io.appium.java_client.MobileBy;
import io.appium.java_client.ios.IOSDriver;
import io.appium.mitmproxy.InterceptedMessage;
import io.appium.mitmproxy.MitmproxyJava;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeoutException;

public class ProxyUtils {


    public MitmproxyJava startProxy(int port) throws IOException, TimeoutException {
        List<InterceptedMessage> messages = new ArrayList<>();
        List<String> extraMitmProxyParams = Arrays.asList("--listen-host", "192.168.1.7");
        MitmproxyJava proxy = new MitmproxyJava("/usr/local/bin/mitmdump", (InterceptedMessage m) -> {
            System.out.println("intercepted message for " + m.getRequest().toString());
            messages.add(m);
            return m;
        }, port, null);
        proxy.start();
        return proxy;
    }

    public void stopProxy(MitmproxyJava proxy) throws InterruptedException {
        proxy.stop();
    }

    public void downloadMITMCertificate() throws MalformedURLException {
        Map<String, Object> args = new HashMap<>();
        args.put("direction", "up");
        DesiredCapabilities safariCapabilities = new DesiredCapabilities();
        safariCapabilities.setCapability("browserName", "safari");
        safariCapabilities.setCapability("nativeWebTap", "true");
        IOSDriver driver = new IOSDriver<>(new URL("http://127.0.0.1:4841/wd/hub"), safariCapabilities);
        driver.get("http://mitm.it");
        WebElement downloadCertButton = driver.findElement(By.xpath("//h5[contains(text(),'iOS')]/parent::div/a[contains(@href,'/cert/pem')]"));
        driver.executeScript("mobile: swipe", args);
        while (!downloadCertButton.isDisplayed()) {
            driver.executeScript("mobile: swipe", args);
        }
        downloadCertButton.click();
        driver.context("NATIVE_APP");
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement allowCertDownloadButton = driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeStaticText[`label == \"Allow\"`]"));
        if (allowCertDownloadButton.isDisplayed()) {
            allowCertDownloadButton.click();
        }
        WebElement profileDownloadedPopup = driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeScrollView[$name contains 'Profile Downloaded'$]"));
        if (profileDownloadedPopup.isDisplayed()) {
            WebElement closeProfileDownloadedPopupButton = driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeButton[`label == \"Close\"`]"));
            closeProfileDownloadedPopupButton.click();
        }
    }

    public void activateCertificateProfilesAndManagement() throws MalformedURLException, InterruptedException {
        Map<String, Object> args = new HashMap<>();
        args.put("direction", "up");
        DesiredCapabilities capabilitiesDevice = new DesiredCapabilities();
        capabilitiesDevice.setCapability("bundleId", "com.apple.Preferences");
        IOSDriver driver = new IOSDriver<>(new URL("http://127.0.0.1:4841/wd/hub"), capabilitiesDevice);
        WebElement generalSettings = driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeCell[`label == \"General\"`]"));
        while (!generalSettings.isDisplayed()) {
            driver.executeScript("mobile: swipe", args);
        }
        generalSettings.click();
        WebElement profilesAndManagement = driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeCell[`label == \"Profiles & Device Management\"`]"));
        while (!profilesAndManagement.isDisplayed()) {
            driver.executeScript("mobile: swipe", args);
        }
        profilesAndManagement.click();
        WebElement mitmProxyCell = driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeCell[`label == \"mitmproxy\"`]"));
        mitmProxyCell.click();
        WebElement mitmProxyCertInstallButton = driver.findElement(MobileBy.id("Install"));
        mitmProxyCertInstallButton.click();
        WebElement mitmProxyCertInstallButton2 = driver.findElement(MobileBy.id("Install"));
        mitmProxyCertInstallButton2.click();
        WebElement mitmCertConfirmInstall = driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeScrollView/**/XCUIElementTypeButton[`label == \"Install\"`]"));
        mitmCertConfirmInstall.click();
        Thread.sleep(1000);
        WebElement certInstallDone = driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeButton[`label == \"Done\"`]"));
        certInstallDone.click();
    }

    public void activateCertificateAbout() throws MalformedURLException {
        Map<String, Object> args = new HashMap<>();
        args.put("direction", "up");
        DesiredCapabilities capabilitiesDevice = new DesiredCapabilities();
        capabilitiesDevice.setCapability("bundleId", "com.apple.Preferences");
        IOSDriver driver = new IOSDriver<>(new URL("http://127.0.0.1:4841/wd/hub"), capabilitiesDevice);
        WebElement generalSettings = driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeCell[`label == \"General\"`]"));
        while (!generalSettings.isDisplayed()) {
            driver.executeScript("mobile: swipe", args);
        }
        generalSettings.click();
        WebElement aboutSettingsButton = driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeCell[`label == \"About\"`]"));
        aboutSettingsButton.click();
        WebElement certificateSettings = driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeCell[`label == \"Certificate Trust Settings\"`]"));
        while (!certificateSettings.isDisplayed()) {
            driver.executeScript("mobile: swipe", args);
        }
        certificateSettings.click();
        WebElement mitmProxyCertToggle = driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeSwitch[`label == \"mitmproxy\"`]"));
        if (mitmProxyCertToggle.isEnabled()) {
            driver.quit();
        } else {
            mitmProxyCertToggle.click();
            driver.quit();
        }
    }

    public void removeCertificate() throws MalformedURLException {
        Map<String, Object> args = new HashMap<>();
        args.put("direction", "up");
        DesiredCapabilities capabilitiesDevice = new DesiredCapabilities();
        capabilitiesDevice.setCapability("bundleId", "com.apple.Preferences");
        IOSDriver driver = new IOSDriver<>(new URL("http://127.0.0.1:4841/wd/hub"), capabilitiesDevice);
        WebElement generalSettings = driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeCell[`label == \"General\"`]"));
        while (!generalSettings.isDisplayed()) {
            driver.executeScript("mobile: swipe", args);
        }
        generalSettings.click();
        WebElement profilesAndManagement = driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeCell[`label == \"Profiles & Device Management\"`]"));
        while (!profilesAndManagement.isDisplayed()) {
            driver.executeScript("mobile: swipe", args);
        }
        profilesAndManagement.click();
        WebElement mitmProxyCell = driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeCell[`label == \"mitmproxy\"`]"));
        mitmProxyCell.click();
        WebElement removeCertButton = driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeStaticText[`label == \"Remove Profile\"`]"));
        removeCertButton.click();
        WebElement confirmRemoveCertButton = driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeButton[`label == \"Remove\"`]"));
        confirmRemoveCertButton.click();
    }

    public MitmproxyJava returnBody(int port, String urlEndpoint, String body) {
        List<InterceptedMessage> messages = new ArrayList<>();
        MitmproxyJava proxy = new MitmproxyJava("/usr/local/bin/mitmdump", (InterceptedMessage m) -> {
            System.out.println("intercepted message for " + m.getRequest().toString());
            if (m.getRequest().getUrl().endsWith(urlEndpoint)) {
                m.getResponse().setBody(body.getBytes(StandardCharsets.UTF_8));
            }
            messages.add(m);
            return m;
        }, port, null);
        return proxy;
    }

    public void addProxyConfig(String ipAddress, String port) throws MalformedURLException, InterruptedException {
        Map<String, Object> args = new HashMap<>();
        args.put("direction", "up");
        DesiredCapabilities capabilitiesDevice = new DesiredCapabilities();
        capabilitiesDevice.setCapability("bundleId", "com.apple.Preferences");
        IOSDriver driver = new IOSDriver<>(new URL("http://127.0.0.1:4841/wd/hub"), capabilitiesDevice);
        WebElement wifiSectionButton = driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeCell[`label == \"Wi-Fi\"`]"));
        wifiSectionButton.click();
        WebElement connectedWifiMoreInfoButton = driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeCell[$type=='XCUIElementTypeImage' AND name=='checkmark'$]/XCUIElementTypeButton"));
        connectedWifiMoreInfoButton.click();
        WebElement proxyConfigButton = driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeCell[$name=='Configure Proxy'$]"));
        while (!proxyConfigButton.isDisplayed()) {
            driver.executeScript("mobile: swipe", args);
        }
        proxyConfigButton.click();
        WebElement manualConfigButton = driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeStaticText[`label == \"Manual\"`]"));
        manualConfigButton.click();
        WebElement serverField = driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeTextField[`label == \"Server\"`]"));
        serverField.click();
        Thread.sleep(500);
        serverField.sendKeys(ipAddress);
        WebElement portField = driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeTextField[`label == \"Port\"`]"));
        portField.click();
        Thread.sleep(500);
        portField.sendKeys(port);
        WebElement saveButton = driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeButton[`label == \"Save\"`]"));
        saveButton.click();
        driver.quit();
    }

    public void removeProxyConfig() throws MalformedURLException {
        Map<String, Object> args = new HashMap<>();
        args.put("direction", "up");
        DesiredCapabilities capabilitiesDevice = new DesiredCapabilities();
        capabilitiesDevice.setCapability("bundleId", "com.apple.Preferences");
        IOSDriver driver = new IOSDriver<>(new URL("http://127.0.0.1:4841/wd/hub"), capabilitiesDevice);
        WebElement wifiSectionButton = driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeCell[`label == \"Wi-Fi\"`]"));
        wifiSectionButton.click();
        WebElement connectedWifiMoreInfoButton = driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeCell[$type=='XCUIElementTypeImage' AND name=='checkmark'$]/XCUIElementTypeButton"));
        connectedWifiMoreInfoButton.click();
        WebElement proxyConfigButton = driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeCell[$name=='Configure Proxy'$]"));
        while (!proxyConfigButton.isDisplayed()) {
            driver.executeScript("mobile: swipe", args);
        }
        proxyConfigButton.click();
        WebElement offButton = driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeStaticText[`label == \"Off\"`]"));
        offButton.click();
        WebElement saveButton = driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeButton[`label == \"Save\"`]"));
        saveButton.click();
        driver.quit();
    }

    private boolean checkMITMCertificateInstalled() throws MalformedURLException {
        Map<String, Object> args = new HashMap<>();
        args.put("direction", "up");
        DesiredCapabilities capabilitiesDevice = new DesiredCapabilities();
        capabilitiesDevice.setCapability("bundleId", "com.apple.Preferences");
        IOSDriver driver = new IOSDriver<>(new URL("http://127.0.0.1:4841/wd/hub"), capabilitiesDevice);
        WebElement generalSettings = driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeCell[`label == \"General\"`]"));
        while (!generalSettings.isDisplayed()) {
            driver.executeScript("mobile: swipe", args);
        }
        generalSettings.click();
        WebElement aboutSettingsButton = driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeCell[`label == \"About\"`]"));
        aboutSettingsButton.click();
        WebElement certificateSettings = driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeCell[`label == \"Certificate Trust Settings\"`]"));
        while (!certificateSettings.isDisplayed()) {
            driver.executeScript("mobile: swipe", args);
        }
        certificateSettings.click();
        if (!driver.findElements(MobileBy.iOSClassChain("**/XCUIElementTypeCell[`label == \"mitmproxy\"`]")).isEmpty()) {
            WebElement mitmProxyCertToggle = driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeSwitch[`label == \"mitmproxy\"`]"));
            if (mitmProxyCertToggle.isEnabled()) {
                driver.quit();
                return true;
            } else {
                mitmProxyCertToggle.click();
                driver.quit();
                return true;
            }
        } else {
            driver.quit();
            return false;
        }
    }
}
