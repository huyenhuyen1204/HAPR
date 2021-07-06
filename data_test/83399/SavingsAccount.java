public class SavingsAccount extends Account {
    /**
     * This is comment.
     */
    public SavingsAccount(long accountNumber, double balance) {
        super(accountNumber, balance);
    }

    /**
     * This is comment.
     */
    @Override
    public void withdraw(double out) {
        if (out > 1000 || balance < 5000) {
            return;
        }
        try {
            doWithdrawing(out);
            transactionList.add(new Transaction(3, out, balance + out, balance));
        } catch (BankException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * This is comment.
     */
    @Override
    public void deposit(double in) {
        try {
            doDepositing(in);
            transactionList.add(new Transaction(2, in, balance - in, balance));
        } catch (BankException e) {
            System.out.println(e.getMessage());
        }
    }
}
