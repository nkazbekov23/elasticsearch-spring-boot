package com.elasticsearch.service;

import com.elasticsearch.document.Person;
import com.elasticsearch.dto.PersonDto;
import com.elasticsearch.dto.SearchRequestDto;
import com.elasticsearch.repos.PersonRepository;
import com.elasticsearch.util.SearchUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    private final RestHighLevelClient client;

    private ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);

    private static final Logger log = LoggerFactory.getLogger(PersonService.class);

    @Autowired
    public PersonService(PersonRepository personRepository, @Qualifier(value = "elasticsearchClient") RestHighLevelClient client) {
        this.personRepository = personRepository;
        this.client = client;

    }

    public void save(Person person) {
        personRepository.save(person);
    }

    public Person getOne(String id) {
        return personRepository.findById(id).get();
    }

    public List<Person> list() {
        List<Person> personList = new ArrayList<>();
        personRepository.findAll().forEach(person -> {
            personList.add(person);
        });
        return personList;
    }


    /**
     * use search for person based on data provided the {@link SearchRequestDto}
     * @param  dto containing info about what to search.
     * @return list of found person
     */
    public List<Person> search(final SearchRequestDto dto) {

        final SearchRequest request =
                SearchUtil.searchRequest("person", dto);

        return searchInternal(request);
    }


    /**
     * used get all person that have created since forwarded date
     * @param date
     * @return return all person since forwarded date
     */
    public List<Person> getAllPersonCreatedSince(final Date date) {

        final SearchRequest request =
                SearchUtil.buildSearchRequest(
                        "person", "createdDate", date

                );
        return searchInternal(request);
    }

    /**
     *
     * @param dto
     * @param date
     * @return
     */
    public List<Person> getAllPersonCreatedSince(final SearchRequestDto dto, final Date date) {

        final SearchRequest request =
                SearchUtil.buildSearchRequest("person", dto, date);
        if (request == null) {
            return Collections.emptyList();
        }

        return searchInternal(request);
    }




    private List<Person> searchInternal(SearchRequest request) {
        if (request == null) {
            log.error("field to build for search request");
            return Collections.emptyList();
        }

        try {
            final SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            final SearchHit[] hits = response.getHits().getHits();
            final List<Person> personList = new ArrayList<>();

            for (SearchHit hit : hits) {
                personList.add(
                        mapper.readValue(hit.getSourceAsString(), Person.class)
                );
            }
            return personList;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}
