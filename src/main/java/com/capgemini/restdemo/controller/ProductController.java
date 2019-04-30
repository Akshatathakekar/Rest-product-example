package com.capgemini.restdemo.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.restdemo.entity.Product;
import com.capgemini.restdemo.service.ProductService;

@RestController("/")
public class ProductController {

	private org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager
			.getLogger(ProductController.class);

	@Autowired
	private ProductService service;

	@PostMapping("/product")
	public ResponseEntity<Product> addProduct(@RequestBody Product product) {
		// logger.info("creating new product with
		// name:{},quantity:{}",product.getName(),product.getQuantity());

		try {
			return ResponseEntity.created(new URI("/product/" + product.getId()))
					.eTag(Integer.toString(product.getVersion())).body(product);
		} catch (URISyntaxException e) {

			return new ResponseEntity<Product>(service.addProduct(product), HttpStatus.OK);
		}

	}

	@GetMapping("/product/{id}")
	public ResponseEntity<?> getProduct(@PathVariable Integer id) {

		return service.getProduct(id).map(product -> {
			try {
				return ResponseEntity.ok().eTag(Integer.toString(product.getVersion()))
						.location(new URI("/product/" + product.getId())).body(product);
			} catch (URISyntaxException e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		}).orElse(ResponseEntity.notFound().build());

	}


//	@GetMapping("/products")
//	public Iterable<Product> getAllProducts()
//	{
//		return service.getAllProducts();
//	}
	
	
	public ResponseEntity<Product> deleteProduct(@PathVariable Integer id)
	{
		  Optional<Product> existProduct=service.getProduct(id);
		  
		
		return existProduct.map(p->

		if(service.deleteProduct(p.getId()))
		{
			return ResponseEntity.ok().build();
		}
		else
		{
			  return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

				);
		
	}

}
