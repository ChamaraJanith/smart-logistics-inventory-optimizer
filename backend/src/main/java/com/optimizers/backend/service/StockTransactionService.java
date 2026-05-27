// service/StockTransactionService.java
package com.optimizers.backend.service;

import java.util.List;
import com.optimizers.backend.dto.response.StockTransactionResponseDTO;

public interface StockTransactionService {

    // Get all transactions
    List<StockTransactionResponseDTO> getAllTransactions();



    // Get transactions by item
    List<StockTransactionResponseDTO> getTransactionsByItem(
            Integer itemId);



    // Get transactions by warehouse
    List<StockTransactionResponseDTO> getTransactionsByWarehouse(
            Integer warehouseId);


    // Get transactions by type (RESTOCK, DISPATCH, etc.)
    List<StockTransactionResponseDTO> getTransactionsByType(
            String transactionType);

            
}