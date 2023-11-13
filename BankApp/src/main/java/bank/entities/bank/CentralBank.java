package bank.entities.bank;

import bank.entities.client.Client;

public class CentralBank extends BaseBank {
    private static final int CAPACITY = 50;

    public CentralBank(String name) {
        super(name, CAPACITY);
    }
}
