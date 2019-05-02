package ru.funbox.links;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.funbox.links.config.RedisTestConfiguration;
import ru.funbox.links.web.RestResponse;
import ru.funbox.links.web.rest.VisitedLinksResource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@WebMvcTest(VisitedLinksResource.class)
@ContextHierarchy({
        @ContextConfiguration(classes = RedisTestConfiguration.class)
})
@ComponentScan("ru.funbox.links.service")
public class VisitedLinksRestTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void restTest() throws Exception {

        final ObjectMapper om = new ObjectMapper();
        final RestResponse restResponse = new RestResponse("Required Long parameter 'from' is not present");

        // Выводится ошибка отсутсвия параметра from
        this.mockMvc.perform(
                get("/visited_domains"))
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(MockMvcResultMatchers.content().json(om.writeValueAsString(restResponse)));

        restResponse.setStatus("Required Long parameter 'to' is not present");

        // Выводится ошибка отсутсвия параметра to
        this.mockMvc.perform(
                get("/visited_domains?from=1"))
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(MockMvcResultMatchers.content().json(om.writeValueAsString(restResponse)));

        restResponse.setStatus("ok");

        // Возвращается status : ok
        MvcResult mvcResult = this.mockMvc.perform(
                get("/visited_domains?from=1&to=9556765888342"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(om.writeValueAsString(restResponse)))
                .andReturn();

        // Содержит массив доменов
        final JsonNode bodyJn = om.readTree(mvcResult.getResponse().getContentAsString());
        Assert.assertNotNull(bodyJn.get("domains"));
        Assert.assertTrue(bodyJn.get("domains").isArray());
    }
}
