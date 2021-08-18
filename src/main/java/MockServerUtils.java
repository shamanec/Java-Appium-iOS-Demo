import io.appium.java_client.MobileBy;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MockServerUtils {

    public void downloadMITMCertificate() throws MalformedURLException {
        Map<String, Object> args = new HashMap<>();
        args.put("direction", "up");
        DesiredCapabilities safariCapabilities = new DesiredCapabilities();
        safariCapabilities.setCapability("browserName", "safari");
        safariCapabilities.setCapability("nativeWebTap", "true");
        IOSDriver driver = new IOSDriver<>(new URL("http://127.0.0.1:4841/wd/hub"), safariCapabilities);
        driver.get("https://drive.google.com/uc?id=10bPNc7JiHVCp_O4Pt4ZPBlMtYX0oM2pf&export=download");
        driver.context("NATIVE_APP");
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
        WebElement mitmProxyCell = driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeCell[`label == \"www.mockserver.com\"`]"));
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
        WebElement mitmProxyCertToggle = driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeSwitch[`label == \"www.mockserver.com\"`]"));
        mitmProxyCertToggle.click();
        WebElement continueButton = driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeButton[`label == \"Continue\"`]"));
        continueButton.click();
        driver.quit();
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
        WebElement mitmProxyCell = driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeCell[`label == \"www.mockserver.com\"`]"));
        mitmProxyCell.click();
        WebElement removeCertButton = driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeStaticText[`label == \"Remove Profile\"`]"));
        removeCertButton.click();
        WebElement confirmRemoveCertButton = driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeButton[`label == \"Remove\"`]"));
        confirmRemoveCertButton.click();
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
}
