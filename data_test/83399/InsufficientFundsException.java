public class InsufficientFundsException extends BankException {
    /**
     * This is comment.
     */
    private static final long serialVersionUID = -9041970381046647823L;

    /**
     * This is comment.
     */
    public InsufficientFundsException(double balance) {
        super(String.format("Số dư tài khoản không đủ $%.2f để thực hiện giao dịch",
            balance));
    }
}
