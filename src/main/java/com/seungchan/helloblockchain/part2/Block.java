package com.seungchan.helloblockchain.part2;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 백승찬
 * @date 2022-01-11
 */
@Getter
public class Block {
    private String hash; // 고유키
    private String prevHash; // 이전 블록 고유키
    private String data;
    private long timestamp;
    private int nonce;
    private String merkleRoot;

    private List<Transaction> transactionList = new ArrayList<>();

    public Block(String prevHash) {
        this.prevHash = prevHash;
        this.timestamp = new Date().getTime();
        this.hash = calculateHash(); // prevHash + data + nonce + timestamp
    }

    public String calculateHash() {
        return BlockUtil.applySha256(this.prevHash + this.data + Integer.valueOf(this.nonce).toString() + Long.valueOf(this.timestamp).toString());
    }

    public void mineBlock(int difficulty) {
        this.merkleRoot = BlockUtil.getMerkleRoot(this.transactionList);
        // difficulty 가 2 이면, 해시 앞자리에 00을 붙임
        String target = BlockUtil.getDifficultyString(difficulty);
        while (!this.hash.substring(0, difficulty).equals(target)) {
            this.nonce++;
            this.hash = calculateHash();
//            System.out.println("nonce:" + this.nonce + " target:" + target + " hash:" + this.hash);
        }
        System.out.println("===Block Mined!!! : " + this.hash);
    }

    public boolean addTransaction(Transaction tx) {
        if (tx == null)
            return false;
        if (!"0".equals(this.prevHash) && !tx.processTransaction()) {
            return false;
        }
        this.transactionList.add(tx);
        System.out.println("===Transaction added to Block");
        return true;
    }
}
