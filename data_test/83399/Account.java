import java.util.ArrayList;
import java.util.List;

public abstract class Account {
    public static final String CHECKING = "CHECKING";
    public static final String SAVINGS = "SAVINGS";

    protected long accountNumber;
    protected double balance;
    protected List<Transaction> transactionList = new ArrayList<>();

    public Account() {}

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

    /**
     * This is comment.
     */
    public void doWithdrawing(double out) throws BankException {
        if (out < 0.0) {
            throw new InvalidFundingAmountException(out);
        } else {
            if (out > balance) {
                throw new InsufficientFundsException(out);
            } else {
                balance -= out;
            }
        }
    }

    /**
     * This is comment.
     */
    public void doDepositing(double in) throws BankException {
        if (in < 0.0) {
            throw new InvalidFundingAmountException(in);
        } else {
            balance += in;
        }
    }

    public abstract void withdraw(double out);

    public abstract void deposit(double in);

    /**
     * This is comment.
     */
    public String getTransactionHistory() {
        String transactionHistory = String.format("Lịch sử giao dịch của tài khoản %d:%n", 
            accountNumber);
        for (int i = 0; i < transactionList.size(); i++) {
            transactionHistory += transactionList.get(i).getTransactionSummary();
            if (i < transactionList.size() - 1) {
                transactionHistory += '\n';
            }
        }
        return transactionHistory;
    }

    /**
     * This is comment.
     */
    public void addTransaction(Transaction transaction) {
        transactionList.add(transaction);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Account) {
            Account oth = (Account) obj;
            return (oth.accountNumber == accountNumber);
        } else {
            return false;
        }
    }
}
