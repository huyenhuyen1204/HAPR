import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by CCNE on 03/12/2020.
 */
public abstract class Account {

    public long accountNumber;
    public double balance;

    protected List<Transaction> transactionList = new ArrayList<>();
    public static final String CHECKING = "CHECKING";
    public static final String SAVINGS = "SAVINGS";

    /**
     * ok.
     */
    public Account() {

    }

    /**
     * ok.
     */
    public Account(long accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    /**
     * ok.
     */
    public long getAccountNumber() {
        return accountNumber;
    }

    /**
     * ok.
     */
    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * ok.
     */
    public double getBalance() {
        return balance;
    }

    /**
     * ok.
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * ok.
     */
    public void doWithdrawing(double t) throws BankException {

        if (t < 0) {
            throw new InvalidFundingAmountException(t);
        } else if (t > balance) {
            throw new InsufficientFundsException(t);
        }
        if (this instanceof SavingsAccount) {

            if (t > 1000) {
                throw new InvalidFundingAmountException(t);
            } else if (balance - t < 5000) {
                throw new InsufficientFundsException(t);
            }
        }

        Transaction transaction;
        if (this instanceof SavingsAccount) {
            transaction = new Transaction(Transaction.TYPE_WITHDRAW_SAVINGS, t, balance, balance - t);
        } else {
            transaction = new Transaction(Transaction.TYPE_WITHDRAW_CHECKING, t, balance, balance - t);
        }
        transactionList.add(transaction);
        balance -= t;
    }

    /**
     * ok.
     */
    public void doDepositing(double t) throws BankException {
        if (t < 0) {
            throw new InvalidFundingAmountException(t);
        }
        Transaction transaction;
        if (this instanceof SavingsAccount) {
            transaction = new Transaction(Transaction.TYPE_DEPOSIT_SAVINGS, t, balance, balance + t);
        } else {
            transaction = new Transaction(Transaction.TYPE_DEPOSIT_CHECKING, t, balance, balance + t);
        }
        transactionList.add(transaction);
        balance += t;

    }

    /**
     * ok.
     */
    public abstract void withdraw(double t);

    /**
     * ok.
     */
    public abstract void deposit(double t);

    /**
     * ok.
     */
    public String getTransactionHistory() {
        String res = "Lịch sử giao dịch của tài khoản " + this.accountNumber + ":";
        for (Transaction transaction : transactionList) {
            res += '\n' + transaction.getTransactionSummary();
        }
        return res;
    }

    /**
     * ok.
     */
    public void addTransaction(Transaction transaction) {
        transactionList.add(transaction);
    }

    /**
     * ok.
     */
    @Override
    public boolean equals(Object o) {
        if (this.accountNumber == ((Account) o).getAccountNumber()) {
            return true;
        }
        return false;
    }
}