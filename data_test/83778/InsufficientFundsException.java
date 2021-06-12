import java.text.DecimalFormat;

public class InsufficientFundsException extends BankException {

    public InsufficientFundsException(double t) {
        super("Số dư tài khoản không đủ $" + (new DecimalFormat("0.00")).format(t)
                + " để thực hiện giao dịch");
    }

}
