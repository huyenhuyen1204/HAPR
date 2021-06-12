/**
 * Created by CCNE on 03/12/2020.
 */
public class SavingsAccount extends Account {

    /**
     * ok.
     */
    public SavingsAccount(long accountNumber, double balance) {
        super(accountNumber, balance);
    }

    @Override
    public void withdraw(double t) {
        try {
            doWithdrawing(t);
        } catch (BankException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deposit(double t) {
        try {
            doDepositing(t);
        } catch (BankException e) {
            e.printStackTrace();
        }
    }

}
 