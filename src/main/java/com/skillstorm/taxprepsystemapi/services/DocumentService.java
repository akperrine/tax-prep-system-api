package com.skillstorm.taxprepsystemapi.services;

import com.skillstorm.taxprepsystemapi.exceptions.TaxDocumentNotFoundException;
import com.skillstorm.taxprepsystemapi.exceptions.UserNotFoundException;
import com.skillstorm.taxprepsystemapi.models.AppUser;
import com.skillstorm.taxprepsystemapi.models.TaxDocument;
import com.skillstorm.taxprepsystemapi.repositories.AppUserRepository;
import com.skillstorm.taxprepsystemapi.repositories.TaxDocumentRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DocumentService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private TaxDocumentRepository taxDocumentRepository;

    public TaxDocument getTaxDocumentById(String userId, String docId) throws UserNotFoundException, TaxDocumentNotFoundException {
        Optional<AppUser> userCheck = appUserRepository.findById(userId);

        if(!userCheck.isPresent()) {
            throw new UserNotFoundException();
        }

        // just double checking that the doc id actually belongs to the user.
        List<TaxDocument> taxDocuments = userCheck.get().getTaxDocuments().stream().filter(t -> t.getId().equals(docId)).collect(Collectors.toList());
        if(taxDocuments.isEmpty()) {
            throw new TaxDocumentNotFoundException();
        }

        // there should only be one since TaxDocument-Id is unique
        return taxDocuments.get(0);
    }

    /*public TaxDocument postTaxDocument(String userId, TaxDocument taxDocument) throws UserNotFoundException {
        Optional<AppUser> userCheck = appUserRepository.findById(userId);

        if(!userCheck.isPresent()) {
            throw new UserNotFoundException();
        }

    }*/


}
