public class CheckingAccount extends Account {
    public CheckingAccount(long accountNumber, double balance) {
        super(accountNumber, balance);
    }

    @Override
    public void deposit(double amount) {
        try {
            doDepositing(amount);
            addTransaction(new Transaction(1, amount, getBalance() - amount, getBalance()));
        } catch (InvalidFundingAmountException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void withdraw(double amount) {
        try {
            doWithdrawing(amount);
            addTransaction(new Transaction(2, amount, getBalance() + amount, getBalance()));
        } catch (InvalidFundingAmountException | InsufficientFundsException e) {
            System.out.println(e.getMessage());
        }
    }
}
