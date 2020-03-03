package com.whj.study.solr.service.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.GroupPage;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.whj.study.solr.model.Book;
import com.whj.study.solr.service.BookService;

@RestController
@RequestMapping("/test")
public class TestController {
	@Autowired
	private SolrTemplate solrTemplate;
	
	@Autowired
	private BookService bookService;
	
	@RequestMapping("/t1")
	public String test(){
		System.out.println("哈哈" + solrTemplate);
		Query query = new SimpleQuery("*:*");
		GroupPage<Book> books  = solrTemplate.query("book", query, Book.class);
		List list = books.getContent();
		
		/*GroupResult<Book> groupResult = groupPage.getGroupResult("item_category");
		
		Page<GroupEntry<Book>> groupEntries = groupResult.getGroupEntries();
		
		List list = new ArrayList();
		for (GroupEntry<Book> entry : groupEntries) {
			String groupValue = entry.getGroupValue();
			if (groupValue != null && !groupValue.equals("")) {
				list.add(groupValue);
			}
		}
		System.out.println(list);*/
		return list.toString();
	}
	
	@RequestMapping("/t2")
	public String test2(){
		HighlightPage<Book> h = bookService.findByName("Sea", PageRequest.of(0, 10));
		if(h.hasContent()) {
			System.out.println(h.getContent());
		}
		return null;
	}
}
