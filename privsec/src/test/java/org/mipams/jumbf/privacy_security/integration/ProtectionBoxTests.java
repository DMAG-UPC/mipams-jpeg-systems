package org.mipams.jumbf.privacy_security.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mipams.jumbf.core.entities.BinaryDataBox;
import org.mipams.jumbf.core.entities.DescriptionBox;
import org.mipams.jumbf.core.entities.JsonBox;
import org.mipams.jumbf.core.entities.JumbfBox;
import org.mipams.jumbf.core.entities.XmlBox;
import org.mipams.jumbf.core.util.MipamsException;
import org.mipams.jumbf.privacy_security.entities.ProtectionBox;
import org.mipams.jumbf.privacy_security.entities.ProtectionDescriptionBox;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ProtectionBoxTests extends AbstractIntegrationTests {

    @BeforeAll
    static void initRequest() throws IOException {
        generateFile();
    }

    @AfterAll
    public static void cleanUp() throws IOException {
        fileCleanUp();
    }

    @Test
    void testProtectionBoxAes() throws Exception {

        ProtectionDescriptionBox protectionDescriptionBox = new ProtectionDescriptionBox();
        protectionDescriptionBox.setAes256CbcProtection();
        protectionDescriptionBox.updateBmffHeadersBasedOnBox();

        JumbfBox givenJumbfBox = getProtectionJumbfBoxBasedOnProtectionDescriptionBox(protectionDescriptionBox);
        JumbfBox parsedJumbfBox = generateJumbfFileAndParseBox(List.of(givenJumbfBox)).get(0);

        assertEquals(givenJumbfBox, parsedJumbfBox);
        assertEquals(givenJumbfBox.getBmffBoxes(), parsedJumbfBox.getBmffBoxes());
    }

    @Test
    void testProtectionBoxAesWithIv() throws Exception {
        ProtectionDescriptionBox protectionDescriptionBox = new ProtectionDescriptionBox();
        protectionDescriptionBox.setAes256CbcWithIvProtection();
        byte[] iv = DatatypeConverter.parseHexBinary("D9BBA15016D876F67532FAFB8B851D24");
        protectionDescriptionBox.setIv(iv);
        protectionDescriptionBox.updateBmffHeadersBasedOnBox();

        JumbfBox givenJumbfBox = getProtectionJumbfBoxBasedOnProtectionDescriptionBox(protectionDescriptionBox);
        JumbfBox parsedJumbfBox = generateJumbfFileAndParseBox(List.of(givenJumbfBox)).get(0);

        assertEquals(givenJumbfBox, parsedJumbfBox);
        assertEquals(givenJumbfBox.getBmffBoxes(), parsedJumbfBox.getBmffBoxes());
    }

    @Test
    void testProtectionBoxWithExternalEncryption() throws Exception {
        ProtectionDescriptionBox protectionDescriptionBox = new ProtectionDescriptionBox();
        protectionDescriptionBox.setProtectionMethodAsExternallyReferenced();
        protectionDescriptionBox.setEncLabel("encryption-reference");
        protectionDescriptionBox.updateBmffHeadersBasedOnBox();

        JumbfBox protectionJumbfBox = getProtectionJumbfBoxBasedOnProtectionDescriptionBox(protectionDescriptionBox);

        JsonBox jsonBox = new JsonBox();
        jsonBox.setFileUrl(TEST_FILE_PATH);
        jsonBox.updateBmffHeadersBasedOnBox();

        DescriptionBox dBox = new DescriptionBox();
        dBox.setUuid(jsonBox.getContentTypeUUID());
        dBox.setLabel("encryption-reference");
        dBox.computeAndSetToggleBasedOnFields();
        dBox.updateBmffHeadersBasedOnBox();

        JumbfBox jsonJumbfBox = MockJumbfBox.generateJumbfBox(dBox, jsonBox);

        List<JumbfBox> givenJumbfBoxList = List.of(protectionJumbfBox, jsonJumbfBox);
        List<JumbfBox> parsedJumbfBoxList = generateJumbfFileAndParseBox(givenJumbfBoxList);

        assertEquals(givenJumbfBoxList, parsedJumbfBoxList);
    }

    @Test
    void testProtectionBoxWithAccessRules() throws Exception {
        ProtectionDescriptionBox protectionDescriptionBox = new ProtectionDescriptionBox();
        protectionDescriptionBox.setAes256CbcProtection();
        protectionDescriptionBox.setArLabel("access-rules-reference");
        protectionDescriptionBox.includeAccessRulesInToggle();
        protectionDescriptionBox.updateBmffHeadersBasedOnBox();
        JumbfBox protectionJumbfBox = getProtectionJumbfBoxBasedOnProtectionDescriptionBox(protectionDescriptionBox);

        XmlBox xmlBox = new XmlBox();
        xmlBox.setFileUrl(TEST_FILE_PATH);
        xmlBox.updateBmffHeadersBasedOnBox();

        DescriptionBox dBox = new DescriptionBox();
        dBox.setUuid(xmlBox.getContentTypeUUID());
        dBox.setLabel("access-rules-reference");
        dBox.computeAndSetToggleBasedOnFields();
        dBox.updateBmffHeadersBasedOnBox();

        JumbfBox xmlJumbfBox = MockJumbfBox.generateJumbfBox(dBox, xmlBox);

        List<JumbfBox> givenJumbfBoxList = List.of(protectionJumbfBox, xmlJumbfBox);
        List<JumbfBox> parsedJumbfBoxList = generateJumbfFileAndParseBox(givenJumbfBoxList);

        assertEquals(givenJumbfBoxList, parsedJumbfBoxList);
    }

    JumbfBox getProtectionJumbfBoxBasedOnProtectionDescriptionBox(ProtectionDescriptionBox pdBox)
            throws MipamsException {

        BinaryDataBox binaryDataBox = new BinaryDataBox();
        binaryDataBox.setFileUrl(TEST_FILE_PATH);
        binaryDataBox.updateBmffHeadersBasedOnBox();

        ProtectionBox protectionBox = new ProtectionBox();
        protectionBox.setProtectionDescriptionBox(pdBox);
        protectionBox.setBinaryDataBox(binaryDataBox);

        return MockJumbfBox.generateJumbfBoxWithContent(protectionBox);

    }
}
