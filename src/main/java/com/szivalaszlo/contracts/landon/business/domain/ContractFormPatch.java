package com.szivalaszlo.contracts.landon.business.domain;

import com.szivalaszlo.contracts.landon.business.exception.DateFormatConstraint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.validation.constraints.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ContractFormPatch {

    private static Logger logger = LogManager.getLogger();

    @NotNull
    public String id;

    @Size(min = 2, max = 15)
    public String documentNumber;

    @DateFormatConstraint //custom annotation
    public String startDate;

    @DateFormatConstraint //custom annotation
    public String endDate;

    @Size(min = 2, max = 10000)
    public String content;

    private List<String> buyerIds;

    private List<String> sellerIds;

    private Map<String, Object> notNullFields;

    public ContractFormPatch() {
        notNullFields = new HashMap<>();
        id = null;
        documentNumber = null;
        startDate = null;
        endDate = null;
        content = null;
        buyerIds = null;
        sellerIds = null;
    }

    public Map<String, Object> getFieldsToPatch() {
        if (documentNumber != null) {
            notNullFields.put("documentNumber", documentNumber);
        }
        if (startDate != null) {
            notNullFields.put("startDate", startDate);
        }
        if (endDate != null) {
            notNullFields.put("endDate", endDate);
        }
        if (content != null) {
            notNullFields.put("content", content);
        }
        if (buyerIds != null) {
            notNullFields.put("buyerIds", buyerIds);
        }
        if (sellerIds != null) {
            notNullFields.put("sellerIds", sellerIds);
        }
        return notNullFields;
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
