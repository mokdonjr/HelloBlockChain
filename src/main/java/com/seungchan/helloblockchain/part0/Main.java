package com.seungchan.helloblockchain.part0;

/**
 * @author 백승찬
 * @date 2022-01-11
 */
public class Main {

    public static void main(String[] args) {
        Block block = new Block(1); // 1번블록
        block.next = new Block(2); // 2번블록
        block.next.next = new Block(3);
    }
}

class Block {
    int val;
    Block next;

    public Block(int val) {
        this.val = val;
    }
}
