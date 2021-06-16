import java.text.DecimalFormat;

public class InsufficientFundsException extends BankException {
    /**
     * This is comment.
     */
    private static final long serialVersionUID = -9041970381046647823L;

    /**
     * This is comment.
     */
    public InsufficientFundsException(double balance) {
        super("Số dư tài khoản không đủ $"
                + (new DecimalFormat("0.00")).format(balance)
                + " để thực hiện giao dịch");
    }
}
