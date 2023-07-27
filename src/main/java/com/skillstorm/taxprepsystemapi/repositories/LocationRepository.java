package com.skillstorm.taxprepsystemapi.repositories;

import com.skillstorm.taxprepsystemapi.models.Location;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface LocationRepository extends MongoRepository<Location, BigInteger> {
}
