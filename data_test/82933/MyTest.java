import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class MyTest {
    private Bank bank;
    private Customer customer;

    @Before
    public void init() {
        bank = new Bank();
        String dbStr = "Nguyễn Thị Quỳnh 400246802\n" +
                "0290822547 CHECKING 5000.0\n" +
                "Lê Hoàng Quân 823804031\n" +
                "Hoàng Văn Phượng 832443592\n" +
                "2448690203 SAVINGS 8000.0\n" +
                "1006190735 CHECKING 3000.0\n" +
                "Nguyễn Ngọc Sơn 237225996\n" +
                "4450210822 CHECKING 2000.0\n" +
                "Phạm Duy Quốc 443914214\n" +
                "7562459315 CHECKING 10000.5\n" +
                "7046865999 SAVINGS 30000.0";
        InputStream db = new ByteArrayInputStream(dbStr.getBytes(StandardCharsets.UTF_8));
        bank.readCustomerList(db);

        customer = new Customer();
        customer.setIdNumber(443914214L);
        customer.setFullName("Phạm Duy Quốc");
        customer.addAccount(new CheckingAccount(7562459315L, 10000.5));
        customer.addAccount(new SavingsAccount(7046865999L, 30000.0));
    }

    @Test
    public void test1_readCustomerList() {
        List<Customer> customerList = bank.getCustomerList();

        Assert.assertEquals(5, customerList.size());
        Assert.assertEquals(400246802L, customerList.get(0).getIdNumber());
        Assert.assertEquals(823804031L, customerList.get(1).getIdNumber());
        Assert.assertEquals(832443592L, customerList.get(2).getIdNumber());
        Assert.assertEquals(237225996L, customerList.get(3).getIdNumber());
        Assert.assertEquals(443914214L, customerList.get(4).getIdNumber());

        Assert.assertEquals(2, customerList.get(4).getAccountList().size());
        Assert.assertEquals(7562459315L, customerList.get(4).getAccountList().get(0).getAccountNumber());
        Assert.assertEquals(7046865999L, customerList.get(4).getAccountList().get(1).getAccountNumber());
        Assert.assertEquals(10000.5, customerList.get(4).getAccountList().get(0).getBalance(), 0.01);
        Assert.assertEquals(30000.0, customerList.get(4).getAccountList().get(1).getBalance(), 0.01);
        Assert.assertTrue(customerList.get(4).getAccountList().get(0) instanceof CheckingAccount);
        Assert.assertTrue(customerList.get(4).getAccountList().get(1) instanceof SavingsAccount);
    }

    @Test
    public void test2_add_remove_account() {
        Account newAccount = new SavingsAccount(1669429043L, 3000.25);
        customer.addAccount(newAccount);

        Assert.assertEquals(3, customer.getAccountList().size());

        customer.removeAccount(new SavingsAccount(7046865999L, 0.0));
        Assert.assertEquals(2, customer.getAccountList().size());
        Assert.assertEquals(1669429043L, customer.getAccountList().get(1).getAccountNumber());
    }

    @Test
    public void test3_deposit_account() {
        Account checkingAccount = customer.getAccountList().get(0);
        checkingAccount.deposit(2000.0);

        Assert.assertEquals(12000.5, checkingAccount.getBalance(), 0.01);

        checkingAccount.deposit(-2000.0);
        Assert.assertEquals(12000.5, checkingAccount.getBalance(), 0.01);
    }

    @Test
    public void test4_withdraw_account() {
        Account checkingAccount = customer.getAccountList().get(0);
        checkingAccount.withdraw(2000.0);

        Assert.assertEquals(8000.5, checkingAccount.getBalance(), 0.01);

        checkingAccount.withdraw(-2000.0);
        Assert.assertEquals(8000.5, checkingAccount.getBalance(), 0.01);

        checkingAccount.withdraw(9000.0);
        Assert.assertEquals(8000.5, checkingAccount.getBalance(), 0.01);
    }

    @Test
    public void test5_withdraw_savings_account() {
        Account checkingAccount = customer.getAccountList().get(1);

        checkingAccount.withdraw(900.0);
        Assert.assertEquals(29100.0, checkingAccount.getBalance(), 0.01);

        checkingAccount.withdraw(2000.0);
        Assert.assertEquals(29100.0, checkingAccount.getBalance(), 0.01);

        checkingAccount.withdraw(100.0);
        Assert.assertEquals(29000.0, checkingAccount.getBalance(), 0.01);

        checkingAccount.withdraw(-100.0);
        Assert.assertEquals(29000.0, checkingAccount.getBalance(), 0.01);

        Account tempAccount = new SavingsAccount(123L, 1000.0);
        Assert.assertEquals(1000.0, tempAccount.getBalance(), 0.01);
        tempAccount.withdraw(900.0);
        Assert.assertEquals(1000.0, tempAccount.getBalance(), 0.01);
    }

    @Test
    public void test6_doDepositing() {
        Account tempAccount = new SavingsAccount(123L, 1000.0);
        try {
            tempAccount.doDepositing(-100.0);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals("Số tiền không hợp lệ: $-100.00", e.getMessage());

        }
    }

    @Test
    public void test7_doWithdrawing() {
        Account tempAccount = new SavingsAccount(123L, 1000.0);
        try {
            tempAccount.doWithdrawing(-100.0);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals("Số tiền không hợp lệ: $-100.00", e.getMessage());

            try {
                tempAccount.doWithdrawing(2000.0);
                Assert.fail();
            } catch (Exception e1) {
                Assert.assertEquals("Số dư tài khoản không đủ $2000.00 để thực hiện giao dịch", e1.getMessage());
            }
        }
    }

    @Test
    public void test8_getTransactionHistory_1() {
        Account checkingAccount = customer.getAccountList().get(0);

        checkingAccount.deposit(3000.0);
        checkingAccount.deposit(-4000.0);
        checkingAccount.deposit(6000.0);
        checkingAccount.withdraw(8000.0);
        checkingAccount.withdraw(100000.0);
        Assert.assertEquals("Lịch sử giao dịch của tài khoản 7562459315:\n" +
                "- Kiểu giao dịch: Nạp tiền vãng lai. Số dư ban đầu: $10000.50. Số tiền: $3000.00. Số dư cuối: $13000.50.\n" +
                "- Kiểu giao dịch: Nạp tiền vãng lai. Số dư ban đầu: $13000.50. Số tiền: $6000.00. Số dư cuối: $19000.50.\n" +
                "- Kiểu giao dịch: Rút tiền vãng lai. Số dư ban đầu: $19000.50. Số tiền: $8000.00. Số dư cuối: $11000.50.",
                checkingAccount.getTransactionHistory().trim());
    }

    @Test
    public void test9_getTransactionHistory_2() {
        Account savingsAccount = customer.getAccountList().get(1);

        savingsAccount.deposit(50000.25);
        savingsAccount.deposit(-4000.0);
        savingsAccount.deposit(2000.25);
        savingsAccount.withdraw(1000.25);
        savingsAccount.withdraw(900.27);
        savingsAccount.deposit(8000.77);
        Assert.assertEquals("Lịch sử giao dịch của tài khoản 7046865999:\n" +
                "- Kiểu giao dịch: Nạp tiền tiết kiệm. Số dư ban đầu: $30000.00. Số tiền: $50000.25. Số dư cuối: $80000.25.\n" +
                "- Kiểu giao dịch: Nạp tiền tiết kiệm. Số dư ban đầu: $80000.25. Số tiền: $2000.25. Số dư cuối: $82000.50.\n" +
                "- Kiểu giao dịch: Rút tiền tiết kiệm. Số dư ban đầu: $82000.50. Số tiền: $900.27. Số dư cuối: $81100.23.\n" +
                "- Kiểu giao dịch: Nạp tiền tiết kiệm. Số dư ban đầu: $81100.23. Số tiền: $8000.77. Số dư cuối: $89101.00.",
                savingsAccount.getTransactionHistory().trim());
    }

    @Test
    public void test10_getCustomersInfoByIdOrder() {
        Assert.assertEquals("Số CMND: 237225996. Họ tên: Nguyễn Ngọc Sơn.\n" +
                "Số CMND: 400246802. Họ tên: Nguyễn Thị Quỳnh.\n" +
                "Số CMND: 443914214. Họ tên: Phạm Duy Quốc.\n" +
                "Số CMND: 823804031. Họ tên: Lê Hoàng Quân.\n" +
                "Số CMND: 832443592. Họ tên: Hoàng Văn Phượng.",
                bank.getCustomersInfoByIdOrder());
    }

    @Test
    public void test11_getCustomersInfoByNameOrder() {
        Assert.assertEquals("Số CMND: 832443592. Họ tên: Hoàng Văn Phượng.\n" +
                "Số CMND: 823804031. Họ tên: Lê Hoàng Quân.\n" +
                "Số CMND: 237225996. Họ tên: Nguyễn Ngọc Sơn.\n" +
                "Số CMND: 400246802. Họ tên: Nguyễn Thị Quỳnh.\n" +
                "Số CMND: 443914214. Họ tên: Phạm Duy Quốc.",
                bank.getCustomersInfoByNameOrder());
    }
}
