package ca.noae.TestSuite.Connections;

import ca.noae.Connections.EmailServerFinder;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class EmailServerFinderTest {

    @Test
    public void testCheckValidEmail() throws Exception {
        String email = "john.doe@gmail.com";
        String[] servers = EmailServerFinder.check(email);
        assertNotNull(servers[0]);
        assertNotNull(servers[1]);
        assertNotNull(servers[2]);
    }

    @Test
    public void testCheckInvalidEmail() {
        String email = "invalid-email";
        assertThrows(IllegalArgumentException.class, () -> {
            EmailServerFinder.check(email);
        });
    }

    @Test
    public void testProbePorts() {
        String[] possibleHosts = {"mail.migadu.com", "smtp.migadu.com"};
        String[] ports = {"25", "465", "587"};
        String result = EmailServerFinder.probePorts(possibleHosts, ports);
        assertNotNull(result);
    }

    @Test
    public void testProbeCapabilitiesSMTP() {
        String[] possibleHosts = {"smtp-mail.outlook.com"};
        String[] ports = {"25", "465", "587" };
        String protocol = "SMTP";
        String result = EmailServerFinder.probeCapabilities(possibleHosts, ports, protocol);
        System.out.println(result);
        assertNotNull(result);
    }

    @Test
    public void testProbeCapabilitiesIMAP() {
        String[] possibleHosts = {"imap-mail.outlook.com"};
        String[] ports = {"143", "993"};
        String protocol = "IMAP";
        String result = EmailServerFinder.probeCapabilities(possibleHosts, ports, protocol);
        System.out.println(result);
        assertNotNull(result);
    }

    @Test
    public void testProbeCapabilitiesPOP3() {
        String[] possibleHosts = {"pop-mail.outlook.com"};
        String[] ports = {"110", "995"};
        String protocol = "POP3";
        String result = EmailServerFinder.probeCapabilities(possibleHosts, ports, protocol);
        System.out.println(result);
        assertNotNull(result);
    }
}