package com.capgemini.restdemo.service;

import java.util.List;
import java.util.Optional;

import com.capgemini.restdemo.entity.Product;

public interface ProductService {

	public Product addProduct(Product product);
	public Optional<Product> getProduct(int id);
	public boolean deleteProduct(int id);
	public List<Product> getAllProducts();
}
