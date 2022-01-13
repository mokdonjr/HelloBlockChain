package com.seungchan.helloblockchain.part2;

import lombok.Getter;
import lombok.Setter;

/**
 * @author 백승찬
 * @date 2022-01-14
 */
@Getter
public class TransactionInput {

    private String transactionOutputId;
    @Setter
    private TransactionOutput UTXO;

    public TransactionInput(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
    }
}
