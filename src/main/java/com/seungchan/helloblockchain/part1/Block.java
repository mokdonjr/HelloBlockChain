package com.seungchan.helloblockchain.part1;

import lombok.Getter;

import java.util.Date;

/**
 * @author 백승찬
 * @date 2022-01-11
 */
@Getter
public class Block {
    private String data;
    private String prevHash;
    private String hash;
    private long timestamp;
    private int nonce;

    public Block(String data, String prevHash) {
        this.data = data;
        this.prevHash = prevHash;
        this.timestamp = new Date().getTime();
        this.hash = calculateHash(); // prevHash + data + nonce + timestamp
    }

    public String calculateHash() {
        return BlockUtil.applySha256(this.prevHash + this.data + Integer.valueOf(this.nonce).toString() + Long.valueOf(this.timestamp).toString());
    }

    public void mineBlock(int difficulty) {
        // difficulty 가 2 이면, 해시 앞자리에 00을 붙임
        String target = BlockUtil.getDifficultyString(difficulty);
        while (!this.hash.substring(0, difficulty).equals(target)) {
            this.nonce++;
            this.hash = calculateHash();
            System.out.println("nonce:" + this.nonce + " target:" + target + " hash:" + this.hash);
        }
    }
}
