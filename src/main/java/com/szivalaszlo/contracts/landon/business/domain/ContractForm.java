package com.szivalaszlo.contracts.landon.business.domain;

import com.szivalaszlo.contracts.landon.business.exception.DateFormatConstraint;

import javax.validation.constraints.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ContractForm {

    public String id;

    @NotNull
    @Size(min = 2, max = 15)
    public String documentNumber;

    @DateFormatConstraint //custom annotation
    @NotNull
    public String startDate;

    @DateFormatConstraint //custom annotation
    @NotNull
    public String endDate;

    @NotNull
    @Size(min = 2, max = 10000)
    public String content;

    @NotNull
    private List<String> buyerIds;

    @NotNull
    private List<String> sellerIds;

    public ContractForm() {

    }

    public ContractForm(String id, List<String> sellerIds, List<String> buyerIds, String documentNumber, String startDate, String endDate, String content) {
        this.id = id;
        this.sellerIds = sellerIds;
        this.buyerIds = buyerIds;
        this.documentNumber = documentNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getBuyerIds() {
        return buyerIds;
    }

    public void setBuyerIds(List<String> buyerIds) {
        this.buyerIds = buyerIds;
    }

    public List<String> getSellerIds() {
        return sellerIds;
    }

    public void setSellerIds(List<String> sellerIds) {
        this.sellerIds = sellerIds;
    }

    public Map<String, Object> extractAllFields() {
        Map<String, Object> allFields = new HashMap<>();
        allFields.put("documentNumber", documentNumber);
        allFields.put("startDate", startDate);
        allFields.put("endDate", endDate);
        allFields.put("content", content);
        allFields.put("buyerIds", buyerIds);
        allFields.put("sellerIds", sellerIds);
        return allFields;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof ContractForm))
            return false;
        if (obj == this)
            return true;
        return Objects.equals(this.getId(), ((ContractForm) obj).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ContractForm{" +
                "id='" + id + '\'' +
                ", documentNumber='" + documentNumber + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", content='" + content + '\'' +
                ", buyerIds=" + buyerIds +
                ", sellerIds=" + sellerIds +
                '}';
    }
}
