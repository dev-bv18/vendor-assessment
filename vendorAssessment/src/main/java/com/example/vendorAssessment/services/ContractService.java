package com.example.vendorAssessment.services;

import com.example.vendorAssessment.DTOs.ContractDTO;
import com.example.vendorAssessment.DTOs.CreateContractDTO;
import com.example.vendorAssessment.entities.Bid;
import com.example.vendorAssessment.entities.BidStatus;
import com.example.vendorAssessment.entities.Contract;
import com.example.vendorAssessment.entities.ContractStatus;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.vendorAssessment.repositories.BidRepository;
import com.example.vendorAssessment.repositories.ContractRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ContractService {

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private BidRepository bidRepository;

    public List<ContractDTO> getAllContracts() {
        List<Contract> contracts = contractRepository.findAll();
        return contracts.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<ContractDTO> findContractById(Long id) {
        return contractRepository.findById(id).map(this::convertToDTO);
    }

    public ContractDTO createContract(CreateContractDTO createContractDTO) {
        Contract contract = new Contract();
        contract.setContractNumber(createContractDTO.getContractNumber());
        contract.setTitle(createContractDTO.getTitle());
        contract.setValue(createContractDTO.getValue());
        contract.setStartDate(createContractDTO.getStartDate());
        contract.setEndDate(createContractDTO.getEndDate());

        if (createContractDTO.getBidId() != null) {
            Optional<Bid> bid = bidRepository.findById(createContractDTO.getBidId());
            if (bid.isPresent()) {
                contract.setBid(bid.get());
                // Update bid status to contracted
                bid.get().setStatus(BidStatus.CONTRACTED);
                bidRepository.save(bid.get());
            }
        }

        contract = contractRepository.save(contract);
        return convertToDTO(contract);
    }

    public Optional<ContractDTO> updateContractStatus(Long id, ContractStatus status) {
        return contractRepository.findById(id).map(contract -> {
            contract.setStatus(status);
            contract = contractRepository.save(contract);
            return convertToDTO(contract);
        });
    }

    private ContractDTO convertToDTO(Contract contract) {
        ContractDTO dto = new ContractDTO();
        dto.setId(contract.getId());
        dto.setContractNumber(contract.getContractNumber());
        dto.setTitle(contract.getTitle());
        dto.setValue(contract.getValue());
        dto.setStartDate(contract.getStartDate());
        dto.setEndDate(contract.getEndDate());
        dto.setStatus(contract.getStatus());
        if (contract.getBid() != null) {
            dto.setBidId(contract.getBid().getId());
        }
        return dto;
    }
}
