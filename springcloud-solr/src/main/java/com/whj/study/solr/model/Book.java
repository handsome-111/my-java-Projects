package com.whj.study.solr.model;

import java.util.List;

import org.springframework.data.solr.core.mapping.SolrDocument;

@SolrDocument(collection = "product")		//标记核心名称
public class Book {

    private String id;
    private List<String> cat;
    private String name;
    private String author;
    private int sequence_i;
    private String genre_s;
    private boolean inStock;
    private double price;
    private int pages_i;
    public void setId(String id) {
         this.id = id;
     }
     public String getId() {
         return id;
     }

    public void setCat(List<String> cat) {
         this.cat = cat;
     }
     public List<String> getCat() {
         return cat;
     }

    public void setName(String name) {
         this.name = name;
     }
     public String getName() {
         return name;
     }

    public void setAuthor(String author) {
         this.author = author;
     }
     public String getAuthor() {
         return author;
     }

    public void setSequence_i(int sequence_i) {
         this.sequence_i = sequence_i;
     }
     public int getSequence_i() {
         return sequence_i;
     }

    public void setGenre_s(String genre_s) {
         this.genre_s = genre_s;
     }
     public String getGenre_s() {
         return genre_s;
     }

    public void setInStock(boolean inStock) {
         this.inStock = inStock;
     }
     public boolean getInStock() {
         return inStock;
     }

    public void setPrice(double price) {
         this.price = price;
     }
     public double getPrice() {
         return price;
     }

    public void setPages_i(int pages_i) {
         this.pages_i = pages_i;
     }
     public int getPages_i() {
         return pages_i;
     }
	@Override
	public String toString() {
		return "Book [id=" + id + ", cat=" + cat + ", name=" + name + ", author=" + author + ", sequence_i="
				+ sequence_i + ", genre_s=" + genre_s + ", inStock=" + inStock + ", price=" + price + ", pages_i="
				+ pages_i + "]";
	}

}