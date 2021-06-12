import java.util.ArrayList;
import java.util.List;

/**
 * Created by CCNE on 03/12/2020.
 */
public class Customer {

    private long idNumber;
    private String fullName;
    private List<Account> accountList;

    /**
     * ok.
     */
    public Customer() {
        accountList = new ArrayList<>();
    }

    /**
     * ok.
     */
    public Customer(long idNumber, String fullName) {
        this.idNumber = idNumber;
        this.fullName = fullName;
        accountList = new ArrayList<>();
    }

    /**
     * ok.
     */
    public void addAccount(Account account) {
        for(Account account1: accountList){
            if(account1.equals(account)) {
                return;
            }
        }
        accountList.add(account);
    }

    /**
     * ok.
     */
    public void removeAccount(Account account) {
        for (Account ac : accountList) {
            if (ac.equals(account)) {
                accountList.remove(account);
                return;
            }
        }
    }


    public long getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(long idNumber) {
        this.idNumber = idNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
    }

    /**
     * ok.
     */
    public String getCustomerInfo() {
        String res = "Số CMND: " + this.idNumber;
        res += ". Họ tên: " + this.fullName + ".";
        return res;
    }
}
 