package com.skillstorm.taxprepsystemapi.services;


import com.github.javafaker.App;
import com.skillstorm.taxprepsystemapi.dtos.in.TaxDocumentDto;
import com.skillstorm.taxprepsystemapi.enums.FilingStatus;
import com.skillstorm.taxprepsystemapi.exceptions.NegativeIncomeException;
import com.skillstorm.taxprepsystemapi.exceptions.TaxDocumentNotFoundException;
import com.skillstorm.taxprepsystemapi.exceptions.UserNotFoundException;
import com.skillstorm.taxprepsystemapi.models.AppUser;
import com.skillstorm.taxprepsystemapi.models.Form1099;
import com.skillstorm.taxprepsystemapi.models.FormW2;
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

    public TaxDocument addTaxDocument(TaxDocumentDto taxDocumentDto) throws UserNotFoundException, NegativeIncomeException {
        Optional<AppUser> userCheck = appUserRepository.findById(taxDocumentDto.getUserId());
        if(!userCheck.isPresent()) {
            throw new UserNotFoundException();
        }

        // checking forms for neg amounts
        for(FormW2 w2: taxDocumentDto.getFormW2s()) {
            if(w2.getIncome() < 0) {
                throw new NegativeIncomeException();
            }
        }

        for(Form1099 form1099: taxDocumentDto.getForm1099s()) {
            if(form1099.getIncome() < 0) {
                throw new NegativeIncomeException();
            }
        }

        TaxDocument newTaxDocument = TaxDocument.builder()
                .filingStatus(FilingStatus.FILED)
                .filed(new Date())
                .maritalStatus(taxDocumentDto.getMaritalStatus())
                .build();

        TaxDocument savedTaxDocument = taxDocumentRepository.save(newTaxDocument);

        AppUser appUser = userCheck.get();
        List<TaxDocument> taxDocuments = appUser.getTaxDocuments();
        taxDocuments.add(savedTaxDocument);
        appUser.setTaxDocuments(taxDocuments);
        appUserRepository.save(appUser);
        return savedTaxDocument;

    }

    public Double reviewTaxesBeforeSubmit(TaxDocumentDto taxDocumentDto) {
        Double totalIncome = 0d;

        for(Form1099 f: taxDocumentDto.getForm1099s()) {
            totalIncome += f.getIncome();
        }

        for(FormW2 f: taxDocumentDto.getFormW2s()) {
            totalIncome += f.getIncome();
        }

        Double taxedTotalIncome = 0d;

        switch (taxDocumentDto.getMaritalStatus()) {
            case SINGLE:
                taxedTotalIncome = calcSingleTax(totalIncome);
                break;

            case MARRIED_FILING_SEPARATELY:
                taxedTotalIncome = calcMarriedFilingSeparately(totalIncome);
                break;

            case MARRIED_FILING_JOINTLY:
                taxedTotalIncome = calcMarriedFilingJointly(totalIncome);
                break;

            case HEAD_OF_HOUSEHOLD:
                taxedTotalIncome = calcHeadOfHousehold(totalIncome);
                break;

            case QUALIFYING_SURVIVING_SPOUSE:
                taxedTotalIncome = calcMarriedFilingJointly(totalIncome);
                break;

            default:
                break;
        }

        return taxedTotalIncome;
    }

    public Double calculateUserTaxes(BigInteger userId) throws UserNotFoundException {
        Optional<AppUser> userCheck = appUserRepository.findById(userId);

        if(!userCheck.isPresent()) {
            throw new UserNotFoundException();
        }

        // assumption - last tax document is the most current one
        TaxDocument taxDoc = userCheck.get().getTaxDocuments().get(userCheck.get().getTaxDocuments().size()-1);

        Double totalIncome = 0d;

        for(Form1099 f: taxDoc.getForm1099s()) {
            totalIncome += f.getIncome();
        }

        for(FormW2 f: taxDoc.getFormW2s()) {
            totalIncome += f.getIncome();
        }

        Double taxedTotalIncome = 0d;

        switch (taxDoc.getMaritalStatus()) {
            case SINGLE:
                taxedTotalIncome = calcSingleTax(totalIncome);
                break;

            case MARRIED_FILING_SEPARATELY:
                taxedTotalIncome = calcMarriedFilingSeparately(totalIncome);
                break;

            case MARRIED_FILING_JOINTLY:
                taxedTotalIncome = calcMarriedFilingJointly(totalIncome);
                break;

            case HEAD_OF_HOUSEHOLD:
                taxedTotalIncome = calcHeadOfHousehold(totalIncome);
                break;

            case QUALIFYING_SURVIVING_SPOUSE:
                taxedTotalIncome = calcMarriedFilingJointly(totalIncome);
                break;

            default:
                break;
        }

        return taxedTotalIncome;
    }

    /*Helper functions*/
    private Double calcSingleTax(Double totalIncome) {
        Double taxedTotalIncome = 0d;
        Double excess = 0d;
        if(totalIncome < 11000d) {
            taxedTotalIncome += .1d * totalIncome;
        } else if(totalIncome >= 11000d && totalIncome < 44725d) {
            excess = totalIncome - 11000d;
            taxedTotalIncome += 1100d + (.12d * excess);
        } else if(totalIncome >= 44725d && totalIncome < 95375d) {
            excess = totalIncome - 44725d;
            taxedTotalIncome += 5147d + (.22d * excess);
        } else if(totalIncome >= 95375d && totalIncome < 182100d) {
            excess = totalIncome - 95375d;
            taxedTotalIncome += 16290d + (.24d * excess);
        } else if(totalIncome >= 182100d && totalIncome < 231250d) {
            excess = totalIncome - 182100d;
            taxedTotalIncome += 37104d + (.32d * excess);
        } else if (totalIncome >= 231250d && totalIncome < 578125d) {
            excess = totalIncome - 231250d;
            taxedTotalIncome += 52832d + (.35d * excess);
        } else if (totalIncome >= 578125d) {
            excess = totalIncome - 578125d;
            taxedTotalIncome += 174238.25d + (.37 * excess);
        }

        return taxedTotalIncome;
    }

    public Double calcMarriedFilingSeparately(Double totalIncome) {
        Double taxedTotalIncome = 0d;
        Double excess = 0d;
        if(totalIncome < 11000d) {
            taxedTotalIncome += .1d * totalIncome;
        } else if(totalIncome >= 11000d && totalIncome < 44725d) {
            excess = totalIncome - 11000d;
            taxedTotalIncome += 1100d + (.12d * excess);
        } else if(totalIncome >= 44725d && totalIncome < 95375d) {
            excess = totalIncome - 44725d;
            taxedTotalIncome += 5147d + (.22d * excess);
        } else if(totalIncome >= 95375d && totalIncome < 182100d) {
            excess = totalIncome - 95375d;
            taxedTotalIncome += 16290d + (.24d * excess);
        } else if(totalIncome >= 182100d && totalIncome < 231250d) {
            excess = totalIncome - 182100d;
            taxedTotalIncome += 37104d + (.32d * excess);
        } else if (totalIncome >= 231250d && totalIncome < 346875d) {
            excess = totalIncome - 231250d;
            taxedTotalIncome += 52832d + (.35d * excess);
        } else if (totalIncome >= 346875d) {
            excess = totalIncome - 346875d;
            taxedTotalIncome += 93300.75d + (.37 * excess);
        }

        return taxedTotalIncome;
    }

    public Double calcHeadOfHousehold(Double totalIncome) {
        Double taxedTotalIncome = 0d;
        Double excess = 0d;
        if(totalIncome < 15700d) {
            taxedTotalIncome += .1d * totalIncome;
        } else if(totalIncome >= 15700d && totalIncome < 59850d) {
            excess = totalIncome - 15700d;
            taxedTotalIncome += 1570d + (.12d * excess);
        } else if(totalIncome >= 59850d && totalIncome < 95350d) {
            excess = totalIncome - 59850d;
            taxedTotalIncome += 6868d + (.22d * excess);
        } else if(totalIncome >= 95350d && totalIncome < 182100d) {
            excess = totalIncome - 95350d;
            taxedTotalIncome += 14678d + (.24d * excess);
        } else if(totalIncome >= 182100d && totalIncome < 231250d) {
            excess = totalIncome - 182100d;
            taxedTotalIncome += 35498d + (.32d * excess);
        } else if (totalIncome >= 231250d && totalIncome < 578100d) {
            excess = totalIncome - 231250d;
            taxedTotalIncome += 51226d + (.35d * excess);
        } else if (totalIncome >= 578100d) {
            excess = totalIncome - 578100d;
            taxedTotalIncome += 172623.50d + (.37 * excess);
        }

        return taxedTotalIncome;
    }

    public Double calcMarriedFilingJointly(Double totalIncome) {
        Double taxedTotalIncome = 0d;
        Double excess = 0d;
        if(totalIncome < 22000d) {
            taxedTotalIncome += .1d * totalIncome;
        } else if(totalIncome >= 22000d && totalIncome < 89450d) {
            excess = totalIncome - 22000d;
            taxedTotalIncome += 2200d + (.12d * excess);
        } else if(totalIncome >= 89450d && totalIncome < 190750d) {
            excess = totalIncome - 89450d;
            taxedTotalIncome += 10294d + (.22d * excess);
        } else if(totalIncome >= 190750d && totalIncome < 364200d) {
            excess = totalIncome - 190750d;
            taxedTotalIncome += 32580d + (.24d * excess);
        } else if(totalIncome >= 364200d && totalIncome < 462500d) {
            excess = totalIncome - 364200d;
            taxedTotalIncome += 74208d + (.32d * excess);
        } else if (totalIncome >= 462500d && totalIncome < 693750d) {
            excess = totalIncome - 462500d;
            taxedTotalIncome += 105664d + (.35d * excess);
        } else if (totalIncome >= 693750d) {
            excess = totalIncome - 693750d;
            taxedTotalIncome += 186601.50d + (.37 * excess);
        }

        return taxedTotalIncome;
    }
}
