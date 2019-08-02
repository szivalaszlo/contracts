package com.szivalaszlo.contracts.landon.business.domain;

import com.szivalaszlo.contracts.landon.data.entity.Contract;
import com.szivalaszlo.contracts.landon.data.entity.Person;

import java.util.ArrayList;
import java.util.List;

public class ContractFormConverter {
    private Contract contract;
    private ContractForm contractForm;
    private List<Contract> contracts;
    private List<ContractForm> contractForms;


    public ContractFormConverter(Contract contract) {
        this.contract = contract;
        contractForm = new ContractForm();

        setAllAttributesOnContractForm();
    }

    public ContractFormConverter(List<Contract> contracts) {
        this.contracts = contracts;
        contractForms = new ArrayList<>();
        setAllAttributesOnEveryContractForms();
    }


    private void setAllAttributesOnContractForm() {
        contractForm.setId(Integer.toString(contract.getId()));
        contractForm.setDocumentNumber(contract.getDocumentNumber());
        contractForm.setStartDate(contract.getStartDate().toString());
        contractForm.setEndDate(contract.getEndDate().toString());
        contractForm.setContent(contract.getContent());
        contractForm.setBuyerIds(getListOfIdsOfPersons(contract.getBuyers()));
        contractForm.setSellerIds(getListOfIdsOfPersons(contract.getSellers()));
    }

    private void setAllAttributesOnEveryContractForms() {
        for (Contract currentContract : contracts) {
            contractForms.add(new ContractFormConverter(currentContract).getContractFormInstance());
        }
    }

    private List<String> getListOfIdsOfPersons(List<Person> persons) {
        List<String> listOfIds = new ArrayList<>();
        for (Person currentPerson : persons) {
            listOfIds.add(Integer.toString(currentPerson.getId()));
        }
        return listOfIds;
    }

    public ContractForm getContractFormInstance() {
        return contractForm;
    }

    public List<ContractForm> getContractForms() {
        return contractForms;
    }
}
