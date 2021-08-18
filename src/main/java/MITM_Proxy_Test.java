import com.google.common.base.Charsets;
import io.appium.java_client.ios.IOSDriver;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.mockserver.configuration.ConfigurationProperties;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.matchers.Times;
import org.mockserver.model.HttpForward;
import org.mockserver.verify.Verification;
import org.mockserver.verify.VerificationTimes;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeoutException;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class MITM_Proxy_Test {

    static ClientAndServer mockServer;
    static ClientAndServer proxy;

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
       // MockServerUtils proxyUtils = new MockServerUtils();
       // proxyUtils.downloadMITMCertificate();
//        proxyUtils.activateCertificateProfilesAndManagement();
//        proxyUtils.activateCertificateAbout();
//        proxyUtils.addProxyConfig("192.168.1.7", "8866");
        //String mockServerCA = loadFileFromLocation("/org/mockserver/socket/CertificateAuthorityCertificate.pem");
        ConfigurationProperties.certificateAuthorityCertificate("/home/kolio/CertificateAuthorityCertificate.pem");
        proxy = ClientAndServer.startClientAndServer(8866);
        System.setProperty("http.proxyHost", "192.168.1.7");
        System.setProperty("http.proxyPort", String.valueOf(proxy.getLocalPort()));
    }

//    @AfterClass
//    public void proxyTeardown() throws MalformedURLException {
//        ProxyUtils proxyUtils = new ProxyUtils();
//        proxyUtils.removeCertificate();
//        proxyUtils.removeProxyConfig();
//    }

//    @AfterClass(alwaysRun = true)
//    public void proxyTeardown() throws MalformedURLException {
//        MockServerUtils proxyUtils = new MockServerUtils();
//        proxyUtils.removeCertificate();
//        proxyUtils.removeProxyConfig();
//        proxy.stop();
//    }

    @Test
    public void nativeTest() throws IOException, InterruptedException {
        proxy
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/api/v1/employees"),
                        Times.exactly(1)
                )
                .respond(
                        response()
                        .withBody("Kolio ma nigga")
                        .withStatusCode(200)
                );
        proxy
                .when(
                        request()
                                .withPath("https://abv.bg")
                ).forward(
                HttpForward.forward().withHost("https://abv.bg")
        );
        DesiredCapabilities safariCapabilities = new DesiredCapabilities();
        safariCapabilities.setCapability("browserName", "safari");
        safariCapabilities.setCapability("nativeWebTap", "true");
        IOSDriver driver = new IOSDriver<>(new URL("http://127.0.0.1:4841/wd/hub"), safariCapabilities);
        Thread.sleep(15000);
        driver.get("http://dummy.restapiexample.com/api/v1/employees");
        String pageSource = driver.getPageSource();
        Assert.assertTrue(pageSource.contains("Foshizzle"));
        driver.get("http://dummy.restapiexample.com/api/v1/employees");
        pageSource = driver.getPageSource();
        Assert.assertTrue(!pageSource.contains("koliotest"));
        driver.quit();
    }

    public String loadFileFromLocation(String location) throws IOException {
        location = location.trim().replaceAll("\\\\", "/");

        Path path;
        if (location.toLowerCase().startsWith("file:")) {
            path = Paths.get(URI.create(location));
        } else {
            path = Paths.get(location);
        }

        if (Files.exists(path)) {
            // org.apache.commons.io.FileUtils
            return FileUtils.readFileToString(path.toFile(), "UTF-8");
        } else {
            return loadFileFromClasspath(location);
        }
    }

    private String loadFileFromClasspath(String location) {
        try {
            InputStream inputStream = this.getClass().getResourceAsStream(location);
            try {
                if (inputStream == null) {
                    inputStream = this.getClass().getClassLoader().getResourceAsStream(location);
                }

                if (inputStream == null) {
                    inputStream = ClassLoader.getSystemResourceAsStream(location);
                }

                if (inputStream != null) {
                    try {
                        // org.apache.commons.io.IOUtils
                        return IOUtils.toString(inputStream, Charsets.UTF_8);
                    } catch (IOException e) {
                        throw new RuntimeException("Could not read " + location + " from the classpath", e);
                    }
                }

                throw new RuntimeException("Could not find " + location + " on the classpath");
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        } catch (IOException ioe) {
            throw new RuntimeException("Exception closing input stream for " + location, ioe);
        }
    }
}
