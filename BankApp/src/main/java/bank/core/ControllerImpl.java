package bank.core;

import bank.common.ConstantMessages;
import bank.common.ExceptionMessages;
import bank.entities.bank.Bank;
import bank.entities.bank.BranchBank;
import bank.entities.bank.CentralBank;
import bank.entities.client.Adult;
import bank.entities.client.Client;
import bank.entities.client.Student;
import bank.entities.loan.Loan;
import bank.entities.loan.MortgageLoan;
import bank.entities.loan.StudentLoan;
import bank.repositories.LoanRepository;

import java.util.ArrayList;
import java.util.Collection;

public class ControllerImpl implements Controller {
    private static final String CENTRAL_BANK = "CentralBank";
    private static final String BRANCH_BANK = "BranchBank";
    private static final String STUDENT_LOAN = "StudentLoan";
    private static final String MORTGAGE_LOAN = "MortgageLoan";
    private static final String STUDENT_CLIENT = "Student";
    private static final String ADULT_CLIENT = "Adult";

    private LoanRepository loans;
    private Collection<Bank> banks;

    public ControllerImpl() {
        loans = new LoanRepository();
        banks = new ArrayList<>();
    }

    @Override
    public String addBank(String type, String name) {
        if (!CENTRAL_BANK.equals(type) && !BRANCH_BANK.equals(type)) {
            throw new NullPointerException(ExceptionMessages.INVALID_BANK_TYPE);
        }

        banks.add(CENTRAL_BANK.equals(type)
                ? new CentralBank(name)
                : new BranchBank(name));

        return String.format(ConstantMessages.SUCCESSFULLY_ADDED_BANK_OR_LOAN_TYPE, type);
    }

    @Override
    public String addLoan(String type) {
        if (!STUDENT_LOAN.equals(type) && !MORTGAGE_LOAN.equals(type)) {
            throw new IllegalArgumentException(ExceptionMessages.INVALID_LOAN_TYPE);
        }

        loans.addLoan(STUDENT_LOAN.equals(type)
                ? new StudentLoan()
                : new MortgageLoan());

        return String.format(ConstantMessages.SUCCESSFULLY_ADDED_BANK_OR_LOAN_TYPE, type);
    }

    @Override
    public String returnedLoan(String bankName, String loanType) {
        Loan loan = loans.findFirst(loanType);

        if (loan == null) {
            throw new IllegalArgumentException(String.format(ExceptionMessages.NO_LOAN_FOUND, loanType));
        }

        Bank bank = getBank(bankName);
        String result = null;

        if (bank != null) {
            bank.addLoan(loan);

            if (loans.removeLoan(loan)) {
                result = String.format(ConstantMessages.SUCCESSFULLY_ADDED_CLIENT_OR_LOAN_TO_BANK, loanType, bankName);
            }
        }

        return result;
    }

    @Override
    public String addClient(String bankName, String clientType, String clientName, String clientID, double income) {
        if (!STUDENT_CLIENT.equals(clientType) && !ADULT_CLIENT.equals(clientType)) {
            throw new IllegalArgumentException(ExceptionMessages.INVALID_CLIENT_TYPE);
        }

        Client client = clientType.equals(STUDENT_CLIENT)
                ? new Student(clientName, clientID, income)
                : new Adult(clientName, clientID, income);

        Bank bank = getBank(bankName);
        String result = null;

        if (bank != null) {
            String bankType = bank.getClass().getSimpleName();

            if ((CENTRAL_BANK.equals(bankType) && ADULT_CLIENT.equals(clientType))
                    || (BRANCH_BANK.equals(bankType) && STUDENT_CLIENT.equals(clientType))) {
                bank.addClient(client);
                result = String.format(ConstantMessages.SUCCESSFULLY_ADDED_CLIENT_OR_LOAN_TO_BANK, clientType, bankName);
            } else {
                result = ConstantMessages.UNSUITABLE_BANK;
            }
        }

        return result;
    }

    @Override
    public String finalCalculation(String bankName) {
        Bank bank = getBank(bankName);
        double funds;
        String result = null;

        if (bank != null) {
            funds = bank.getLoans().stream().mapToDouble(Loan::getAmount).sum() +
                    bank.getClients().stream().mapToDouble(Client::getIncome).sum();

            result = String.format(ConstantMessages.FUNDS_BANK, bankName, funds);
        }

        return result;
    }

    @Override
    public String getStatistics() {
        StringBuilder statistics = new StringBuilder();

        banks.forEach(bank -> statistics
                .append(bank.getStatistics())
                .append(System.lineSeparator()));

        return statistics.toString().trim();
    }

    private Bank getBank(String bankName) {
        return banks.stream()
                .filter(bank -> bankName.equals(bank.getName()))
                .findFirst()
                .orElse(null);
    }
}
