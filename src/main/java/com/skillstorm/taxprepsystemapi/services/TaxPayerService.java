//package com.skillstorm.taxprepsystemapi.services;
//
//import com.skillstorm.taxprepsystemapi.enums.SignInType;
//import com.skillstorm.taxprepsystemapi.exceptions.TaxPayerNotFoundException;
//import com.skillstorm.taxprepsystemapi.models.TaxPayer;
//import com.skillstorm.taxprepsystemapi.repositories.TaxPayerRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//public class TaxPayerService {
//
//    @Autowired
//    private TaxPayerRepository taxPayerRepository;
//
//    public TaxPayer getTaxPayerByUsername(String username) throws TaxPayerNotFoundException {
//        Optional<TaxPayer> taxPayerOptional = taxPayerRepository.findByUsername(username);
//        if (taxPayerOptional.isPresent()) {
//            return taxPayerOptional.get();
//        } else {
//            throw new TaxPayerNotFoundException();
//        }
//    }
//}
