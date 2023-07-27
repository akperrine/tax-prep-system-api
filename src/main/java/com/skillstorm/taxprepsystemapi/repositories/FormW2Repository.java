package com.skillstorm.taxprepsystemapi.repositories;

import com.skillstorm.taxprepsystemapi.models.FormW2;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface FormW2Repository extends MongoRepository<FormW2, BigInteger> {
}
