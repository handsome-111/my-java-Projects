package com.whj.study.solr.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.HighlightPage;

import com.whj.study.solr.model.Book;

public interface BookService {

	HighlightPage<Book> findByName(String name, Pageable pageable);
		
	Page<Book> findByNameLike(String name, Pageable pageable);

	void deleteByNameLike(String name);
	


}
