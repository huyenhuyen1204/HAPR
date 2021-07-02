public class SavingsAccount extends Account {
    public SavingsAccount(long accountNumber, double balance) {
        super(accountNumber, balance);
    }

    @Override
    public void deposit(double amount) {
        try {
            doDepositing(amount);
            addTransaction(new Transaction(3, amount, getBalance() - amount, getBalance()));
        } catch (InvalidFundingAmountException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void withdraw(double amount) {
        if (amount > 1000 || getBalance() < 5000) {
            return;
        }

        try {
            doWithdrawing(amount);
            addTransaction(new Transaction(4, amount, getBalance() + amount, getBalance()));
        } catch (InvalidFundingAmountException | InsufficientFundsException e) {
            System.out.println(e.getMessage());
        }
    }
}
