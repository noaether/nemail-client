package ca.noae.TestSuite.Connections;

import ca.noae.Connections.EmailServerFinder;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class EmailServerFinderTest {

    /**
     * Tests the {@link EmailServerFinder#check(String)} method.
     *
     * @throws Exception if the email is invalid
     */
    @Test
    public void testCheckValidEmail() throws Exception {
        String email = "john.doe@gmail.com";
        String[] servers = EmailServerFinder.check(email);
        assertNotNull(servers[0]);
        assertNotNull(servers[1]);
        assertNotNull(servers[2]);
    }

    /**
     * Tests the {@link EmailServerFinder#check(String)} method.
     */
    @Test
    public void testCheckInvalidEmail() {
        String email = "invalid-email";
        assertThrows(IllegalArgumentException.class, () -> {
            EmailServerFinder.check(email);
        });
    }

    /**
     * Tests the {@link EmailServerFinder#probePorts(String[], String[])} method
     * with a valid email.
     *
     * This test will fail if the email server is down, or if it's unreachable.
     */
    @Test
    public void testProbePorts() {
        String[] possibleHosts = { "mail.migadu.com", "smtp.migadu.com" };
        String[] ports = { "25", "465", "587" };
        String result = EmailServerFinder.probePorts(possibleHosts, ports);
        assertNotNull(result);
    }

    /**
     * Tests the {@link EmailServerFinder#probeCapabilities(String[], String[], String)}
     * method with SMTP.
     */
    @Test
    public void testProbeCapabilitiesSMTP() {
        String[] possibleHosts = { "smtp-mail.outlook.com" };
        String[] ports = { "25", "465", "587" };
        String protocol = "SMTP";
        String result = EmailServerFinder.probeCapabilities(possibleHosts, ports, protocol);
        System.out.println(result);
        assertNotNull(result);
    }

    /**
     * Tests the {@link EmailServerFinder#probeCapabilities(String[], String[], String)}
     * method with IMAP.
     */
    @Test
    public void testProbeCapabilitiesIMAP() {
        String[] possibleHosts = { "imap-mail.outlook.com" };
        String[] ports = { "143", "993" };
        String protocol = "IMAP";
        String result = EmailServerFinder.probeCapabilities(possibleHosts, ports, protocol);
        System.out.println(result);
        assertNotNull(result);
    }

    /**
     * Tests the {@link EmailServerFinder#probeCapabilities(String[], String[], String)}
     * method with POP3.
     */
    @Test
    public void testProbeCapabilitiesPOP3() {
        String[] possibleHosts = { "pop-mail.outlook.com" };
        String[] ports = { "110", "995" };
        String protocol = "POP3";
        String result = EmailServerFinder.probeCapabilities(possibleHosts, ports, protocol);
        System.out.println(result);
        assertNotNull(result);
    }
}
