import java.text.DecimalFormat;

/**
 * Created by CCNE on 03/12/2020.
 */
public class Transaction {
    public static final int TYPE_DEPOSIT_CHECKING = 1;
    public static final int TYPE_WITHDRAW_CHECKING = 2;
    public static final int TYPE_DEPOSIT_SAVINGS = 3;
    public static final int TYPE_WITHDRAW_SAVINGS = 4;

    private int type;
    private double amount;
    private double initialBalance;
    private double finalBalance;

    /**
     * javadoc.
     */
    public Transaction(int type, double amount, double initialBalance, double finalBalance) {
        this.type = type;
        this.amount = amount;
        this.initialBalance = initialBalance;
        this.finalBalance = finalBalance;
    }

    private String getTransactionTypeString(int type) {
        //"Nạp tiền vãng lai", "Rút tiền vãng lai", "Nạp tiền tiết kiệm", "Rút tiền tiết kiệm"
        if (type == 1) {
            return "Nạp tiền vãng lai";
        } else if (type == 2) {
            return "Rút tiền vãng lai";
        } else if (type == 3) {
            return "Nạp tiền tiết kiệm";
        } else {
            return "Rút tiền tiết kiệm";
        }
    }

    /**
     * javadoc.
     */
    public String getTransactionSummary() {
        return "- Kiểu giao dịch: " + getTransactionTypeString(type)
                + ". Số dư ban đầu: " + initialBalance
                + ". Số tiền: $" + new DecimalFormat("0.00").format(amount)
                + ". Số dư cuối: $" + finalBalance + ".";
    }
}
 