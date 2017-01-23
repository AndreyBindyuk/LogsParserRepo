package network.sftp;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Sergey_Chernikov on 1/23/2017.
 */
public class SFTPConnectorTest {
    SFTPConnector sftpConnector;

    @Before
    public void init(){
        sftpConnector = new SFTPConnector();
    }

    @Test
    public void testParseConnectionURL() {
        String connectionURL = "sftp://root_wer:#^12Ugr@test.com";
        String path = "/opt/test/test.txt";
        assertEquals("test.com", sftpConnector.getHost(connectionURL));
        assertEquals("root_wer", sftpConnector.getUser(connectionURL));
        assertEquals("#^12Ugr", sftpConnector.getPassword(connectionURL));

        assertEquals("test.txt", sftpConnector.getFilename(path));
        assertEquals("/opt/test/", sftpConnector.getAbsolutePath(path));

        assertTrue(sftpConnector.validateURL(connectionURL));
        connectionURL = "ftp://root_wer:#^12Ugr@test.com";
        assertFalse(sftpConnector.validateURL(connectionURL));
        connectionURL = "sftp:/root_wer:#^12Ugr@test.com";
        assertFalse(sftpConnector.validateURL(connectionURL));

        connectionURL = "sftp://root_wer:#12Ugrtest.com";
        assertFalse(sftpConnector.validateURL(connectionURL));

    }
}
