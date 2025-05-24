package com.example.vendorAssessment.controllers;

import com.example.vendorAssessment.DTOs.ContractDTO;
import com.example.vendorAssessment.DTOs.ContractStatusUpdateDTO;
import com.example.vendorAssessment.DTOs.CreateContractDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.vendorAssessment.services.ContractService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/contracts")
@CrossOrigin(origins = "http://localhost:3001")
public class ContractController {

    @Autowired
    private ContractService contractService;

    @GetMapping
    public ResponseEntity<List<ContractDTO>> getAllContracts() {
        List<ContractDTO> contracts = contractService.getAllContracts();
        return ResponseEntity.ok(contracts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContractDTO> getContractById(@PathVariable Long id) {
        Optional<ContractDTO> contract = contractService.findContractById(id);
        return contract.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ContractDTO> createContract(@RequestBody CreateContractDTO createContractDTO) {
        ContractDTO contract = contractService.createContract(createContractDTO);
        return ResponseEntity.ok(contract);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ContractDTO> updateContractStatus(
            @PathVariable Long id,
            @RequestBody ContractStatusUpdateDTO statusUpdate) {

        Optional<ContractDTO> updatedContract = contractService.updateContractStatus(id, statusUpdate.getStatus());
        return updatedContract.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
