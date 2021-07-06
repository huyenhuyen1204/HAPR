import java.text.DecimalFormat;

public class InsufficientFundsException extends BankException {
    public InsufficientFundsException(double amount) {
        super("Số dư tài khoản không đủ $"
                + (new DecimalFormat("0.00")).format(amount)
                + " để thực hiện giao dịch");
    }
}
