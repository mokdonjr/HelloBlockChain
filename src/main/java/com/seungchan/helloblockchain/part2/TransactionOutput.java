package com.seungchan.helloblockchain.part2;

import lombok.Getter;

import java.security.PublicKey;

/**
 * @author 백승찬
 * @date 2022-01-14
 */
@Getter
public class TransactionOutput {

    private String id;
    private PublicKey recipient;
    private float value;
    private String parentTransactionId;

    public TransactionOutput(PublicKey recipient, float value, String parentTransactionId) {
        this.recipient = recipient;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
    }

    public boolean isMine(PublicKey publicKey) {
        return this.recipient.equals(publicKey);
    }
}
