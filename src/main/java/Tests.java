import io.appium.java_client.MobileBy;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSStartScreenRecordingOptions;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class Tests {

    IOSDriver driver;

    @Test
    public void nativeTest() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("bundleId", "com.apple.Preferences");
        driver = new IOSDriver<>(new URL("http://127.0.0.1:4841/wd/hub"), capabilities);

        WebElement mobileDataButton = driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeCell[`label == \"Mobile Data\"`]"));
        for (int i = 0; i <= 2; i++) {
            mobileDataButton.click();
            driver.navigate().back();
        }
    }

    @Test
    public void nativeTestWithVideo() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("bundleId", "com.apple.Preferences");
        driver = new IOSDriver<>(new URL("http://127.0.0.1:4841/wd/hub"), capabilities);
        driver.startRecordingScreen(new IOSStartScreenRecordingOptions().withFps(25));

        WebElement mobileDataButton = driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeCell[`label == \"Mobile Data\"`]"));
        for (int i = 0; i <= 2; i++) {
            mobileDataButton.click();
            driver.navigate().back();
        }

        saveVideo(driver, "nativeTest");
    }

    @Test
    public void nativeImageTest() throws IOException {
        //Get the file
        File refImgFile = Paths.get("src/main/resources/cellular-button.png").toFile();
        //Get the reference image as Base64 string
        String image = Base64.getEncoder().encodeToString(Files.readAllBytes(refImgFile.toPath()));

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("bundleId", "com.apple.Preferences");
        driver = new IOSDriver<>(new URL("http://127.0.0.1:4841/wd/hub"), capabilities);
        WebDriverWait wait = new WebDriverWait(driver, 10);
        for (int i = 0; i <= 2; i++) {
            wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.image(image))).click();
            driver.navigate().back();
        }
    }

    @Test
    public void safariTest() throws MalformedURLException, InterruptedException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", "safari");
        driver = new IOSDriver<>(new URL("http://127.0.0.1:4841/wd/hub"), capabilities);
        driver.get("http://saucelabs.com/test/guinea-pig");

        WebElement div = driver.findElement(By.id("i_am_an_id"));
        Assert.assertEquals("I am a div", div.getText());
        driver.findElement(By.id("comments")).sendKeys("Comment");
        Thread.sleep(5000);
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }

    private void saveVideo(IOSDriver driver, String videoName) {
        byte[] decode = Base64.getDecoder().decode(driver.stopRecordingScreen());
        try {
            FileUtils.writeByteArrayToFile(new File("src/main/resources/" + videoName + ".mp4"), decode);
        } catch (IOException e) {
            System.out.println("Something went wrong when saving the video file");
        }
    }
}
