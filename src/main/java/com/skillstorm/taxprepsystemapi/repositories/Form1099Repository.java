package com.skillstorm.taxprepsystemapi.repositories;

import com.skillstorm.taxprepsystemapi.models.Form1099;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface Form1099Repository extends MongoRepository<Form1099, BigInteger> {
}
