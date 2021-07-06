public class InvalidFundingAmountException extends BankException {
    /**
     * This is comment.
     */
    private static final long serialVersionUID = 1L;

    /**
     * This is comment.
     */
    public InvalidFundingAmountException(double balance) {
        super(String.format("Số tiền không hợp lệ: $%.2f", balance));
    }
}
