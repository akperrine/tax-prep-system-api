//package com.skillstorm.taxprepsystemapi.controllers;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/taxpayers")
//public class TaxPayerController {
//
//    @Autowired
//    private TaxPayerService taxPayerService;
//
//    @GetMapping(value = "/taxpayer/username/{username}")
//    public ResponseEntity getTaxPayerByUsername(@PathVariable String username) {
//        try {
//            return ResponseEntity.ok().body(taxPayerService.getTaxPayerByUsername(username));
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//}
