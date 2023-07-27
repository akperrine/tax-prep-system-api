package com.skillstorm.taxprepsystemapi.services;


import com.skillstorm.taxprepsystemapi.dtos.in.TaxDocumentDto;
import com.skillstorm.taxprepsystemapi.exceptions.TaxDocumentNotFoundException;
import com.skillstorm.taxprepsystemapi.exceptions.UserNotFoundException;
import com.skillstorm.taxprepsystemapi.models.AppUser;
import com.skillstorm.taxprepsystemapi.models.TaxDocument;
import com.skillstorm.taxprepsystemapi.repositories.AppUserRepository;
import com.skillstorm.taxprepsystemapi.repositories.TaxDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;

@Service
public class TaxService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private TaxDocumentRepository taxDocumentRepository;

    public List<TaxDocument> getTaxDocumentsByUserId(BigInteger userId) throws UserNotFoundException {
        Optional<AppUser> appUser = appUserRepository.findById(userId);
        if(!appUser.isPresent()) {
            throw new UserNotFoundException();
        }

        return appUser.get().getTaxDocuments();
    }

    public TaxDocument getTaxDocumentById(BigInteger taxDocumentId) throws TaxDocumentNotFoundException {
        Optional<TaxDocument> taxDocument = taxDocumentRepository.findById(taxDocumentId);
        if(!taxDocument.isPresent()) {
            throw new TaxDocumentNotFoundException();
        }

        return taxDocument.get();
    }

    public TaxDocument addTaxDocument(TaxDocumentDto taxDocumentDto) {
        return new TaxDocument();
    }
}
