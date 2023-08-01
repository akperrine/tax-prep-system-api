package com.skillstorm.taxprepsystemapi.controllers;

import com.skillstorm.taxprepsystemapi.dtos.in.TaxDocumentDto;
import com.skillstorm.taxprepsystemapi.services.TaxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@CrossOrigin
@RequestMapping(value = "/tax")
public class TaxController {

    @Autowired
    private TaxService taxService;

    @GetMapping("/calculate/user/{userId}")
    public ResponseEntity getTaxCalculation(@PathVariable String userId) {
        try {
            return ResponseEntity.ok().body(taxService.calculateUserTaxes(new BigInteger(userId)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/documents/calculate")
    public ResponseEntity getTaxCalculationBeforeSubmit(@RequestBody TaxDocumentDto taxDocumentDto) {
        try {
            return ResponseEntity.ok().body(taxService.reviewTaxesBeforeSubmit(taxDocumentDto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
