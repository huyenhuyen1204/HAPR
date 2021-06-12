import java.text.DecimalFormat;

/**
 * Created by CCNE on 03/12/2020.
 */
public class Transaction {

    public static final int TYPE_DEPOSIT_CHECKING = 0;
    public static final int TYPE_WITHDRAW_CHECKING = 1;
    public static final int TYPE_DEPOSIT_SAVINGS = 2;
    public static final int TYPE_WITHDRAW_SAVINGS = 3;

    private int type;
    private double amount;
    private double initialBalance;
    private double finalBalance;

    /**
     * ok.
     */
    public Transaction(int type, double amount, double initialBalance, double finalBalance) {
        this.type = type;
        this.amount = amount;
        this.initialBalance = initialBalance;
        this.finalBalance = finalBalance;
    }

    /**
     * ok.
     */
    private String getTransactionTypeString(int type) {
        String res = "";
        if (type == 0) {
            res = "Nạp tiền vãng lai";
        } else if (type == 1) {
            res = "Rút tiền vãng lai";
        } else if (type == 2) {
            res = "Nạp tiền tiết kiệm";
        } else if (type == 3) {
            res = "Rút tiền tiết kiệm";
        }
        return res;
    }

    /**
     * ok.
     */
    public String getTransactionSummary() {
        DecimalFormat df = new DecimalFormat("0.00");
        return "- Kiểu giao dịch: " + getTransactionTypeString(type) + ". Số dư ban đầu: $"
                + df.format(initialBalance) + ". Số tiền: $"
                + df.format(amount) + ". Số dư cuối: $"
                + df.format(finalBalance) + ".";
    }

} 