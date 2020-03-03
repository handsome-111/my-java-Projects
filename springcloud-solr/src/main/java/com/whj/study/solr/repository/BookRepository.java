package com.whj.study.solr.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.repository.Highlight;

import com.whj.study.solr.model.Book;


public interface BookRepository extends PagingAndSortingRepository<Book,String>{
	  /**
     * 根据指定属性查询，高亮显示
     * @param stuName
     * @param pageable
     * @return
     */
    @Highlight(prefix = "<b>", postfix = "</b>")
    HighlightPage<Book> findByName(String name, Pageable pageable);

    /**
     * 分页模糊查询
     * @param stuName
     * @param pageable
     * @return
     */
    Page<Book> findByNameLike(String name, Pageable pageable);


    /**
     * 删除数据（模糊匹配）
     * @param stuName
     */
    void deleteByNameLike(String name);
}
