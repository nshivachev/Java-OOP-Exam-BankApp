package bank.entities.client;

import bank.common.ExceptionMessages;

public abstract class BaseClient implements Client {
    private String name;
    private String id;
    private int interest;
    private double income;

    public BaseClient(String name, String id, int interest, double income) {
        setName(name);
        setId(id);
        setInterest(interest);
        setIncome(income);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(ExceptionMessages.CLIENT_NAME_CANNOT_BE_NULL_OR_EMPTY);
        }
        this.name = name;
    }

    private void setId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException(ExceptionMessages.CLIENT_ID_CANNOT_BE_NULL_OR_EMPTY);
        }
        this.id = id;
    }

    @Override
    public int getInterest() {
        return interest;
    }

    private void setInterest(int interest) {
        this.interest = interest;
    }

    @Override
    public double getIncome() {
        return income;
    }

    private void setIncome(double income) {
        if (income <= 0) {
            throw new IllegalArgumentException(ExceptionMessages.CLIENT_INCOME_CANNOT_BE_BELOW_OR_EQUAL_TO_ZERO);
        }
        this.income = income;
    }

    @Override
    public void increase() {
        interest = "Adult".equals(this.getClass().getSimpleName())
                ? interest + 2
                : interest + 1;
        }
    }
