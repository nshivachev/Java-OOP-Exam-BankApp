package bank.entities.bank;

import bank.common.ExceptionMessages;
import bank.entities.client.Client;

public class BranchBank extends BaseBank {
    private static final int CAPACITY = 25;

    public BranchBank(String name) {
        super(name, CAPACITY);
    }
}
