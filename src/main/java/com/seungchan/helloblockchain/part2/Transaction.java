package com.seungchan.helloblockchain.part2;

import lombok.Getter;
import lombok.Setter;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 백승찬
 * @date 2022-01-14
 */
@Getter
public class Transaction {

    @Setter
    private String transactionId;
    private PublicKey sender;
    private PublicKey recipient;
    private float value;
    private byte[] signature;

    // 수신자가 보낼 돈을 가지고 있다는것을 증명할 이전 트랜잭션 참조값
    private List<TransactionInput> inputs = new ArrayList<>();

    // 거래에서 받은 관련 주소 금액 -> 다음에 일어날 새 트랜잭션의 input 으로 사용됨
    private List<TransactionOutput> outputs = new ArrayList<>();

//    private static int sequence;
    private int sequence;

    public Transaction(PublicKey sender, PublicKey recipient, float value, List<TransactionInput> inputs) {
        this.sender = sender;
        this.recipient = recipient;
        this.value = value;
        this.inputs = inputs; // 이전 트랜잭션의 output 임 (wallet 에서 sendFunds 할때 만듬)
    }

    /**
     * 트랜잭션 처리 (검증중 에러가 나면 롤백
     * @return
     */
    public boolean processTransaction() {

        // 1. 서명 검증
        if (!verifySignature())
            return false;

        // 2. 메인함수에 임시 저장된 transactionOutputId 으로 UTXO 검증. 사용되지 않은것이 맞는지 체크
        for (TransactionInput input : this.inputs) {
            input.setUTXO(Main.UTXOs.get(input.getTransactionOutputId()));
        }

        // 3. 최소단위 0.1f 를 넘는지 체크
        float inputValue = getInputValue();
        if (inputValue < Main.minimumTransaction) {
            return false;
        }

        // 4. transactionOutput 생성하기 100 => value 40,60
        this.transactionId = calculateHash();
        this.outputs.add(new TransactionOutput(this.recipient, this.value, this.transactionId)); // 보낸돈
        this.outputs.add(new TransactionOutput(this.sender, inputValue - this.value, this.transactionId)); // 남은돈

        // 5. output to unspent list
        for (TransactionOutput output : this.outputs) {
            Main.UTXOs.put(output.getId(), output);
        }

        // 6. remove transactionInput
        for (TransactionInput input : this.inputs) {
            Main.UTXOs.remove(input.getUTXO().getId());
        }

        return true;
    }

    public String calculateHash() {
        this.sequence++;
        return BlockUtil.applySha256(BlockUtil.getStringFromKey(this.sender) + BlockUtil.getStringFromKey(this.recipient) + Float.valueOf(this.value).toString() + this.sequence);
    }

    public float getInputValue() {
        float total = 0f;
        for (TransactionInput input : this.inputs) {
            if (input.getUTXO() != null) {
                total += input.getUTXO().getValue();
            }
        }
        return total;
    }

    public boolean verifySignature() {
        String data = BlockUtil.getStringFromKey(this.sender) + BlockUtil.getStringFromKey(this.recipient) + Float.toString(this.value);
        return BlockUtil.verifyECDSASig(this.sender, data, this.signature);
    }

    public void generateSignature(PrivateKey privateKey) {
        String data = BlockUtil.getStringFromKey(this.sender) + BlockUtil.getStringFromKey(this.recipient) + Float.toString(this.value);
        this.signature = BlockUtil.applyECDSASig(privateKey, data);

    }
}
