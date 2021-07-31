package com.example.demo.dao;

import com.example.demo.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("postgres")
public class PersonDataAccessService implements PersonDao{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insertPerson(UUID id, Person person) {
        String sql="insert into person(id,name) values (?,?);";
        String idd=id.toString();
        if (jdbcTemplate.update(sql,id, person.getName())==1) {
            return 1;
        }
        return 0;
    }

    @Override
    public List<Person> selectAllPeople() {
        String sql="select id,name from person ";

        List<Person> people = jdbcTemplate.query(sql, (resultSet,i )-> {
            UUID id = UUID.fromString(resultSet.getString("id"));
            String name = resultSet.getString("name");
            return new Person(id,name);
        });
        return people;
    }

    @Override
    public Optional<Person> selectPersonById(UUID id) {
//        return DB.stream().filter(person->person.getId().equals(id)).findFirst();
        String sql="select id,name from person  where id=?";

        Person person = jdbcTemplate.queryForObject(sql,
                new Object[]{id},
                (resultSet,i )-> {
                    UUID personId = UUID.fromString(resultSet.getString("id"));
                    String name = resultSet.getString("name");
                    return new Person(personId,name);
                });

        return Optional.ofNullable(person);
    }

    @Override
    public int deletePersonById(UUID id) {

        String sql="delete from person where id=?";
        Object[] args = new Object[] {id};
        if(jdbcTemplate.update(sql, args) == 1){
            return 1;
        }
        return 0;
    }

    @Override
    public int updatePersonById(UUID id, Person person) {
        String sql="update person set name=? where id=?";

        if(jdbcTemplate.update(sql, person.getName(), id) == 1){
            return 1;
        }
        return 0;

    }


}
