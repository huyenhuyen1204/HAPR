import java.text.DecimalFormat;

public class InvalidFundingAmountException extends BankException {
    /**
     * This is comment.
     */
    private static final long serialVersionUID = 1L;

    /**
     * This is comment.
     */
    public InvalidFundingAmountException(double balance) {
        super("Số tiền không hợp lệ: $" + (new DecimalFormat("0.00")).format(balance));
    }
}
