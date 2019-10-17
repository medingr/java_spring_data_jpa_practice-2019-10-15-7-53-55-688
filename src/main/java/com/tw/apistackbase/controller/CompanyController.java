package com.tw.apistackbase.controller;

import com.tw.apistackbase.core.Company;
import com.tw.apistackbase.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sun.nio.cs.Surrogate;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/companies")
public class CompanyController {
    @Autowired
    private CompanyRepository companyRepository;


    @GetMapping(value= "/all" , produces = {"application/json"})
    @ResponseStatus(code = HttpStatus.OK)
    public Iterable<Company> list(
            @RequestParam(required = false , defaultValue = "1")  Integer page ,
            @RequestParam(required = false , defaultValue = "5")Integer size ) {
        return companyRepository.findAll(PageRequest.of(page,size, Sort.by("name").ascending()));
    }

    @GetMapping( produces = {"application/json"})
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Optional<Company>> getCompanyByCompanyName
            (@RequestParam(required = false , defaultValue = "") String name) {
        Optional<Company> fetchedCompany = companyRepository.findByNameContaining(name);
        if(fetchedCompany.isPresent()){
            return new ResponseEntity<>(fetchedCompany, HttpStatus.OK);
        }
        return  new ResponseEntity<>( HttpStatus.NOT_FOUND);
    }

    @PostMapping(produces = {"application/json"})
    @ResponseStatus(code = HttpStatus.CREATED)
    public Company add(@RequestBody Company company) {
        return companyRepository.save(company)  ;
    }

    @DeleteMapping(value = "/{id}", produces = {"application/json"})
    public ResponseEntity<Optional<Company>> deleteCompanyByCompanyID (@PathVariable Long id) {
        Optional<Company> fetchedCompany = companyRepository.findById(id);
        if(fetchedCompany.isPresent()){
            companyRepository.deleteById(id);
            return new ResponseEntity<>(fetchedCompany, HttpStatus.OK);
        }
        return  new ResponseEntity<>( HttpStatus.NOT_FOUND);
    }

    @PatchMapping(value = "/{id}", produces = {"application/json"})
    public ResponseEntity<Company> update(@PathVariable Long id, @RequestBody Company company) {
        Optional<Company> companyToUpdate = companyRepository.findById(id);
        if (companyToUpdate.isPresent()) {
            Company modifyCompany = companyToUpdate.get();
            modifyCompany.setName(company.getName());
            Company savedCompany = companyRepository.save(modifyCompany);
            return new ResponseEntity<>(savedCompany, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
