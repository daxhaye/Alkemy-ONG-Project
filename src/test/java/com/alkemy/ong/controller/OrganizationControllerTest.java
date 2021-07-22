package com.alkemy.ong.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.annotation.SecurityTestExecutionListeners;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.alkemy.ong.dto.request.OrganizationCreationDto;
import com.alkemy.ong.service.Interface.IOrganization;
import com.alkemy.ong.service.impl.UsersServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;


@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
public class OrganizationControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	   
	@InjectMocks
	OrganizationController controller;
                          
	@MockBean
	ObjectMapper mapper;
	
	@MockBean
	UsersServiceImpl user;
	
	@MockBean
	IOrganization organizationService;
	
	@MockBean
	ProjectionFactory projectionFactory;

	
	@Test
	public void emptyOrganization() throws Exception {
		assertEquals(HttpStatus.NOT_FOUND, controller.getOrganizationById(null).getStatusCode());
	}
	
	@Test
	public void organizationWithOneId() throws Exception {
		MockMultipartFile file = new MockMultipartFile("Hola", "Hola.jpg", MediaType.TEXT_PLAIN_VALUE, "Hello world!".getBytes());
		
		
		OrganizationCreationDto dto = new OrganizationCreationDto();
		dto.setName("david");
		dto.setEmail("david@gmail.com");
		dto.setWelcomeText("hola mundo");
		dto.setImage(file);
		
		assertEquals(200, mockMvc.perform(get("/organization/public").content(mapper.writeValueAsString(dto)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(HttpStatus.CREATED.value())).andReturn().getResponse().getStatus());
		
		
	}
	

	
	
	
	

}