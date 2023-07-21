package com.skillstorm.taxprepsystemapi.services;


import com.skillstorm.taxprepsystemapi.exceptions.TaxDocumentNotFoundException;
import com.skillstorm.taxprepsystemapi.exceptions.UserNotFoundException;
import com.skillstorm.taxprepsystemapi.models.AppUser;
import com.skillstorm.taxprepsystemapi.models.TaxDocument;
import com.skillstorm.taxprepsystemapi.repositories.AppUserRepository;
import com.skillstorm.taxprepsystemapi.repositories.TaxDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TaxService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private TaxDocumentRepository taxDocumentRepository;

    public List<TaxDocument> getTaxDocumentsByUserId(Long userId) throws UserNotFoundException {
        Optional<AppUser> appUser = appUserRepository.findById(userId);
        if(!appUser.isPresent()) {
            throw new UserNotFoundException();
        }

        return appUser.get().getAppUserInformation().getTaxDocuments();
    }

    public TaxDocument getTaxDocumentById(Long taxDocumentId) throws TaxDocumentNotFoundException {
        Optional<TaxDocument> taxDocument = taxDocumentRepository.findById(taxDocumentId);
        if(!taxDocument.isPresent()) {
            throw new TaxDocumentNotFoundException();
        }

        return taxDocument.get();
    }

}
