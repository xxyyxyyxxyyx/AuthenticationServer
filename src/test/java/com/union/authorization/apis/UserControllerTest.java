package com.union.authorization.apis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.union.authorization.model.User;
import com.union.authorization.service.UserService;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class, secure = false)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @Test
    public void testCreateUser() throws Exception {
        User mockUser = new User();
        mockUser.setStaffCode((long) 19);
        mockUser.setUsername("mock");
        mockUser.setPassword("password");
        mockUser.setDepartment("mock");
        mockUser.setRole("ROLE_MOCK");
        String inputAsString = this.mapToJson(mockUser);
        String uri = "/api/users";
        Mockito.when(userService.create(Mockito.any(User.class))).thenReturn(mockUser);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(uri)
                .accept(MediaType.APPLICATION_JSON).content(inputAsString)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String outputAsString = response.getContentAsString();
        JSONObject outputAsJson = new JSONObject(outputAsString);
//        assertEquals(mockUser.getStaffCode(), Long.parseLong(String.valueOf(outputAsJson.get("staffCode"))));
        assertThat(mockUser.getDepartment()).isEqualTo(outputAsJson.get("department"));
        assertEquals(HttpStatus.OK.value(), response.getStatus());

    }

    private String mapToJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }
}