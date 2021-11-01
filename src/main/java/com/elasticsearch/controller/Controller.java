package com.elasticsearch.controller;

import com.elasticsearch.document.Person;
import com.elasticsearch.dto.PersonDto;
import com.elasticsearch.dto.SearchRequestDto;
import com.elasticsearch.service.PersonService;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/person")
public class Controller {

    private final PersonService personService;

    @Autowired
    public Controller(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody Person person) {
        personService.save(person);
        return ResponseEntity.ok().body("success");
    }

    @GetMapping("/{id}")
    public Person getOne(@PathVariable String id) {
        return personService.getOne(id);
    }

    @GetMapping
    public List<Person> list() {
        return personService.list();
    }

    @GetMapping("/q")
    public List<Person> search(@RequestBody SearchRequestDto dto) {
        return personService.search(dto);
    }

    @GetMapping("/q/{date}")
    public List<Person> getAllPersonCreatedSince(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        return personService.getAllPersonCreatedSince(date);
    }

    @GetMapping("/q/since/{date}")
    public List<Person> getAllPersonCreatedDateSince(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            @RequestBody SearchRequestDto dto) {
        return personService.getAllPersonCreatedSince(dto, date);
    }


}
