import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Main {

    /**
     * ok.
     */
    public static void main(String[] args) throws FileNotFoundException {
        Bank bank = new Bank();
        InputStream is = new FileInputStream("src/test");
        bank.readCustomerList(is);
        System.out.println(bank.getCustomersInfoByIdOrder());

        Customer chieu = new Customer(987654321, "A Quang Chieu");

        Account ac1 = new SavingsAccount(1234567890, 10000);
        ac1.withdraw(60000);
        System.out.println(ac1.getTransactionHistory());
        chieu.addAccount(ac1);

    }

}
