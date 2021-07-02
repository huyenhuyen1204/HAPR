import java.util.ArrayList;
import java.util.List;

public abstract class Account {
    public static final String CHECKING = "CHECKING";
    public static final String SAVINGS = "SAVINGS";

    private long accountNumber;
    private double balance;
    private List<Transaction> transactionList = new ArrayList<>();

    public Account() {
    }

    public Account(long accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public abstract void deposit(double amount);

    public abstract void withdraw(double amount);

    public void doDepositing(double amount) throws InvalidFundingAmountException {
        if (amount < 0) {
            throw new InvalidFundingAmountException(amount);
        } else {
            balance += amount;
        }
    }

    public void doWithdrawing(double amount) throws InvalidFundingAmountException, InsufficientFundsException {
        if (amount < 0) {
            throw new InvalidFundingAmountException(amount);
        } else if (amount > balance) {
            throw new InsufficientFundsException(amount);
        } else {
            balance -= amount;
        }
    }

    public void addTransaction(Transaction transaction) {
        transactionList.add(transaction);
    }

    public String getTransactionHistory() {
        StringBuilder res = new StringBuilder();
        res.append("Lịch sử giao dịch của tài khoản ");
        res.append(accountNumber);
        res.append(":" + "\n");

        for (Transaction transaction : transactionList) {
            res.append(transaction.getTransactionSummary()).append("\n");
        }
        return res.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        return accountNumber == account.accountNumber;

    }

    @Override
    public int hashCode() {
        return (int) (accountNumber ^ (accountNumber >>> 32));
    }
}
 