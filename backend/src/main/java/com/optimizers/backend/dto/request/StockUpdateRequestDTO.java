// dto/request/StockUpdateRequestDTO.java
package com.optimizers.backend.dto.request;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;

public class StockUpdateRequestDTO {

    // RESTOCK, DISPATCH, ADJUSTMENT, RESERVE, RELEASE_RESERVE
    @NotNull(message = "Transaction type is required")
    private String transactionType;

    @NotNull(message = "Quantity is required")
    private BigDecimal quantity;

    private String notes;
    private String performedBy;

    public StockUpdateRequestDTO() {}

    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getPerformedBy() { return performedBy; }
    public void setPerformedBy(String performedBy) { this.performedBy = performedBy; }
}