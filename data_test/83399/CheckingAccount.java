public class CheckingAccount extends Account {
    /**
     * This is comment.
     */
    public CheckingAccount(long accountNumber, double balance) {
        super(accountNumber, balance);
    }

    /**
     * This is comment.
     */
    public void withdraw(double out) {
        try {
            doWithdrawing(out);
            transactionList.add(new Transaction(1, out, balance + out, balance));
        } catch (BankException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * This is comment.
     */
    public void deposit(double in) {
        try {
            doDepositing(in);
            transactionList.add(new Transaction(0, in, balance - in, balance));
        } catch (BankException e) {
            System.out.println(e.getMessage());
        }
    }
}
