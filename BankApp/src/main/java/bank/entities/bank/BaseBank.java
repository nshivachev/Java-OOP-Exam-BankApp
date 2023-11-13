package bank.entities.bank;

import bank.common.ExceptionMessages;
import bank.entities.client.Client;
import bank.entities.loan.Loan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public abstract class BaseBank implements Bank {
    private String name;
    private int capacity;
    private Collection<Loan> loans;
    private Collection<Client> clients;

    public BaseBank(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
        loans = new ArrayList<>();
        clients = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(ExceptionMessages.BANK_NAME_CANNOT_BE_NULL_OR_EMPTY);
        }
        this.name = name;
    }

    @Override
        public Collection<Client> getClients() {
            return clients;
        }

    @Override
    public Collection<Loan> getLoans() {
        return loans;
    }

    @Override
    public void addClient(Client client) {
        if (clients.size() == capacity) {
            throw new IllegalStateException(ExceptionMessages.NOT_ENOUGH_CAPACITY_FOR_CLIENT);
        }

        clients.add(client);
    }

    @Override
    public void removeClient(Client client) {
        clients.remove(client);
    }

    @Override
    public void addLoan(Loan loan) {
        loans.add(loan);
    }

    @Override
    public int sumOfInterestRates() {
        return loans.stream().mapToInt(Loan::getInterestRate).sum();
    }

    @Override
    public String getStatistics() {
        StringBuilder statistics = new StringBuilder(String
                .format("Name: %s, Type: %s%nClients: ",
                        name, getClass().getSimpleName()));

        String clientNames = clients.isEmpty()
                ? "none"
                : String.join(", ", clients.stream()
                .map(Client::getName)
                .collect(Collectors.toList()));

        statistics
                .append(clientNames)
                .append(System.lineSeparator())
                .append(String
                        .format("Loans: %d, Sum of interest rates: %d", loans.size(),
                                sumOfInterestRates()));

        return statistics.toString().trim();
    }
}
