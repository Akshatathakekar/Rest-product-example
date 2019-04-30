package com.capgemini.restdemo.controllertest;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.capgemini.restdemo.entity.Product;
import com.capgemini.restdemo.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

	@MockBean
	private ProductService service;

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("POST/product-success")
	public void testAddProduct() throws Exception {

		Product product = new Product(1, "abc", 3, 1);
		Product mockProduct = new Product(1, "abc", 3, 1);

		doReturn(mockProduct).when(service).addProduct(any());

		mockMvc.perform(post("/product").contentType(MediaType.APPLICATION_JSON).content(asJsonString(product)))
				.andExpect(jsonPath("$.id", is(1))).andExpect(jsonPath("$.name", is("abc")))
				.andExpect(jsonPath("$.quantity", is(3)))
				.andExpect(header().string(HttpHeaders.LOCATION, "/product/1"));
	}

	@Test
	@DisplayName("GET/product/1-Found")
	public void testGetProductByIdFound() throws Exception {
		Product mockProduct = new Product(1, "qqq", 44, 1);
		doReturn(Optional.of(mockProduct)).when(service).getProduct(1);

		mockMvc.perform(get("/product/{id}", 1)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))

				// Validate the headers
				.andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
				.andExpect(header().string(HttpHeaders.LOCATION, "/product/1"))

				// Validate the returned fields
				.andExpect(jsonPath("$.id", is(1))).andExpect(jsonPath("$.name", is("qqq")))
				.andExpect(jsonPath("$.quantity", is(44))).andExpect(jsonPath("$.version", is(1)));
	}

	@Test
	@DisplayName("GET/product/1-not found")
	public void testGetProductByIDNotFound() throws Exception {
		doReturn(Optional.empty()).when(service).getProduct(1);

		mockMvc.perform(get("/product/{id}", 3)).andExpect(status().isNotFound());
	}

	static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
