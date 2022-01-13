package com.seungchan.helloblockchain.part1;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 백승찬
 * @date 2022-01-11
 */
public class Main {

    public static List<Block> blockChain = new ArrayList<>();
    public static int difficulty = 2; // 앞 두자리가 00

    /**
     * block 에 add 하면서 hash 값을 구하는게 목표!
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("=========================================");
        addBlock(new Block("first", "0"));
        System.out.println("=========================================");
        addBlock(new Block("second", blockChain.get(blockChain.size() - 1).getHash()));
        System.out.println("=========================================");
        addBlock(new Block("third", blockChain.get(blockChain.size() - 1).getHash()));
        System.out.println("=========================================");
        addBlock(new Block("fourth", blockChain.get(blockChain.size() - 1).getHash()));

        System.out.println("=========================================");
        System.out.println(BlockUtil.getJson(blockChain));
    }

    public static void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        blockChain.add(newBlock);
    }
}
