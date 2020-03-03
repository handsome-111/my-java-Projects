package com.whj.study.solr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.stereotype.Service;

import com.whj.study.solr.model.Book;
import com.whj.study.solr.repository.BookRepository;

@Service
public class BookServiceImpl implements BookService{
	
	@Autowired
	private BookRepository bookRepository;

	@Override
	public HighlightPage<Book> findByName(String name, Pageable pageable) {
		return bookRepository.findByName(name, pageable);
	}

	@Override
	public Page<Book> findByNameLike(String name, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteByNameLike(String name) {
		// TODO Auto-generated method stub
		
	}

}
