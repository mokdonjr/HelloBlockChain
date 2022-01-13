package com.seungchan.helloblockchain.part2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 백승찬
 * @date 2022-01-11
 */
public class Main {

    public static List<Block> blockChain = new ArrayList<>();
    public static int difficulty = 2; // 앞 두자리가 00
    public static float minimumTransaction = 0.1f;

    private static Wallet walletA; // 거래를 발생시키는것
    private static Wallet walletB;

    private static Transaction genesisTransaction;
    public static Map<String, TransactionOutput> UTXOs = new HashMap<>(); // Unspent TransactionOutput

    public static void main(String[] args) {

        // 1. 지갑 생성
        walletA = new Wallet(); // 각 클라이언트들에 설치되어있음. 단말기의 지갑 프로그램을 통해 로그인을 하고 거래를할수있음. 개인키(비번)/공개키(계좌번호)로 이루어져있다.
        // 송신자는 개인키로 데이터를 암호화하고, 수신자는 송신자의 공개키로 데이터를 복호화한다.
        walletB = new Wallet();
        Wallet coinBase = new Wallet(); // 채굴이 일어났을때 코인을 주는 지갑

        // 2. 제네시스 트랜잭션 생성
        // 최초에 있는돈이 없으므로 genesisTransaction 의 transactionInput 은 항상 null 이다
        // 송신자 (coinBase) 에서 수신자 (walletA) 에게 100 을 보낸다.
        genesisTransaction = new Transaction(coinBase.getPublicKey(), walletA.getPublicKey(), 100f, null);
        // 2-1. 제네시스 트랜잭션 서명 생성
        genesisTransaction.generateSignature(coinBase.getPrivateKey());
        // 2-2. 제네시스 트랜잭션 transactionId=0
        genesisTransaction.setTransactionId("0");
        // 2-3. 제네시스 트랜잭션 transactionOutput 리스트 생성
        genesisTransaction.getOutputs().add(new TransactionOutput(genesisTransaction.getRecipient(), genesisTransaction.getValue(), genesisTransaction.getTransactionId()));

        // 3. 메인함수 내 UTXO 에 저장
        UTXOs.put(genesisTransaction.getOutputs().get(0).getId(), genesisTransaction.getOutputs().get(0));

        // 4. 제네시스 블록 생성
        Block genesisBlock = new Block("0");
        // 4-1. 제네시스 블록에 트랜잭션 등록
        genesisBlock.addTransaction(genesisTransaction);
        // 4-2. 블록체인에 제네시스 블록 등록
        addBlock(genesisBlock);

        // 5. 다음 블록 생성
        Block block1 = new Block(genesisBlock.getHash()); // 이전블록의 hash 를 prevHash 로
        System.out.println("block1 블록체인 추가 전 walletA.balance : " + walletA.getBalance());
        System.out.println("block1 블록체인 추가 전 walletB.balance : " + walletB.getBalance());
        // 5-1. 다음 블록에 트랜잭션 등록
        Transaction walletATransaction = walletA.sendFunds(walletB.getPublicKey(), 40f);
        block1.addTransaction(walletATransaction);
        // 5-2. 블록체인에 다음 블록 등록
        addBlock(block1);
        System.out.println("block1 블록체인 추가 후 walletA.balance : " + walletA.getBalance());
        System.out.println("block1 블록체인 추가 후 walletB.balance : " + walletB.getBalance());
    }

    public static void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        blockChain.add(newBlock);
    }
}
