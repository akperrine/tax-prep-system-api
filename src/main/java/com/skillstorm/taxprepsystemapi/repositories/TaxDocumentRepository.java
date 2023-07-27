package com.skillstorm.taxprepsystemapi.repositories;

import com.skillstorm.taxprepsystemapi.models.TaxDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface TaxDocumentRepository extends MongoRepository<TaxDocument, BigInteger> {
}
