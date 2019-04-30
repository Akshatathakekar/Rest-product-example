package com.capgemini.restdemo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.restdemo.dao.ProductDao;
import com.capgemini.restdemo.entity.Product;
import com.capgemini.restdemo.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDao dao;

	@Override
	public Product addProduct(Product product) {

		dao.save(product);

		return product;
	}

	@Override
	public Optional<Product> getProduct(int id) {

		return 	dao.findById(id);
	}

	@Override
	public List<Product> getAllProducts() {
		return dao.findAll();
	}

	@Override
	public void deleteProduct(int id) {
		 dao.deleteById(id);
	}

	
}
