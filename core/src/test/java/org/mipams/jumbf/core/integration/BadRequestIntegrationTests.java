package org.mipams.jumbf.core.integration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BadRequestIntegrationTests {

    @Autowired
    protected MockMvc mockMvc;

    private static String REQUEST_BODY;

    @BeforeAll
    static void initUseCase() {

        StringBuilder badRequest = new StringBuilder();

        badRequest.append("{")
                .append("  \"type\": \"jumb\",")
                .append("  \"description\": {")
                .append("    \"label\": \"JSON Content Type JUMBF box\",")
                .append("    \"id\": 4334431")
                .append("  },")
                .append("  \"content\": {")
                .append("    \"fileUrl\":\"/tmp/test1.jpeg\"")
                .append("  }")
                .append("}");

        REQUEST_BODY = badRequest.toString();
    }

    public String getRequestBody() {
        return REQUEST_BODY;
    }

    @Test
    void generateBoxRequestInSpecificFile() throws Exception {

        String nonExistentFileName = "test32.jumbf";

        mockMvc.perform(post("/core/v1/generateBox?targetFile=" + nonExistentFileName)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getRequestBody())).andExpect(status().isBadRequest());
    }

    @Test
    void parseBoxFromMetadata() throws Exception {
        String nonExistentFileName = "test32.jumbf";

        mockMvc.perform(get("/core/v1/parseMetadata?fileName=" + nonExistentFileName))
                .andExpect(status().isBadRequest());
    }
}
