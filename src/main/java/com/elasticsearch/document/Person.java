package com.elasticsearch.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Document(indexName = "person")
public class Person {

    @Field(type = FieldType.Keyword)
    private String id;
    @Field(type = FieldType.Text)
    private String name;
    @Field(type = FieldType.Date)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdDate;

    private String _class;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String get_class() {
        return _class;
    }

    public void set_class(String _class) {
        this._class = _class;
    }
}
