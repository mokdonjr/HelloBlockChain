package com.seungchan.helloblockchain.part2;

import lombok.Getter;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author 백승찬
 * @date 2022-01-12
 */
@Getter
public class Wallet {

    private PrivateKey privateKey;
    private PublicKey publicKey;
    private Map<String, TransactionOutput> UTXOWallet = new HashMap<>();

    public Wallet() {
        generateKeyPair();
    }

    /**
     * 잔고 확인
     * @return
     */
    public float getBalance() {
        float total = 0;

        // main
        for (Entry<String, TransactionOutput> e : Main.UTXOs.entrySet()) {
            TransactionOutput UTXO = e.getValue();
            // UTXO 의 수신자가 내 지갑의 publicKey 인가
            if (UTXO.isMine(this.publicKey)) {
                total += UTXO.getValue();
                this.UTXOWallet.put(UTXO.getId(), UTXO);
            }
        }

        return total;
    }

    /**
     * 송금
     * @return
     */
    public Transaction sendFunds(PublicKey recipient, float value) {
        if (getBalance() < value) {
            System.out.println("Not enough money");
            return null;
        }

        List<TransactionInput> transactionInputs = new ArrayList<>();
        float total = 0;
        for (Entry<String, TransactionOutput> e : this.UTXOWallet.entrySet()) {
            TransactionOutput UTXO = e.getValue();
            total += UTXO.getValue();
            // transactionInput 은 전단계 트랜잭션의 transactionOutput 의 id 이다! 시스템적으로 동일한것!
            transactionInputs.add(new TransactionInput(UTXO.getId()));
            if (total > value) {
                break;
            }
        }

        Transaction newTransaction = new Transaction(this.publicKey, recipient, value, transactionInputs);
        newTransaction.generateSignature(this.privateKey);

        // 송신한것은 제거되어야함
        for (TransactionInput transactionInput : transactionInputs) {
            UTXOWallet.remove(transactionInput.getTransactionOutputId());
        }

        return newTransaction;
    }

    public void generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");

            keyGen.initialize(ecSpec, random);
            KeyPair keyPair = keyGen.generateKeyPair();

            this.privateKey = keyPair.getPrivate();
            this.publicKey = keyPair.getPublic();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
