/*
 * Copyright 2012 - 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.whj.study.solr.service.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.whj.study.solr.service.ProductService;

/**
 * @author Christoph Strobl
 */
@RestController
@Component
@Scope("prototype")
public class ProductController {

	private ProductService productService;

	@RequestMapping("/product/{id}")
	public String search(Model model, @PathVariable("id") String id, HttpServletRequest request) {
		model.addAttribute("product", productService.findById(id));
		return "product";
	}
	
	public String test(){
		return null;
	}

	@Autowired
	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

}