package com.jaquantaylor.lil.learningspring.data.repository;

import com.jaquantaylor.lil.learningspring.data.entity.Guest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestRepository extends CrudRepository <Guest,Long> {
}
