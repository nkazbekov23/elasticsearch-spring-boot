package com.elasticsearch.config;

import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.elasticsearch.repos")
@ComponentScan(basePackages = "com.elasticsearch")
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {

    @Value("${es.url}")
    public String elasticSarchUrl;


    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {
        ClientConfiguration configuration = ClientConfiguration.builder()
                .connectedTo(elasticSarchUrl).build();
        return RestClients.create(configuration).rest();
    }
}
