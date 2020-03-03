package com.whj.study.solr.conf;

import org.apache.solr.client.solrj.SolrClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

@Configuration
@EnableSolrRepositories("com.whj.study")		//激活Spring Data solr 存储库
public class ApplicationConfig {
	@Autowired
    SolrClient solrClient;
    
    @Bean
    public  SolrTemplate  getSolrTemplate(){
        return  new SolrTemplate(solrClient);
    }
}