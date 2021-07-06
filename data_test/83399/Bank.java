import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Bank {
    private List<Customer> customerList = new ArrayList<>();

    /**
     * This is comment.
     */
    public void readCustomerList(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String line = reader.readLine();
            Customer customer = null;
            while (line != null) {
                if (line.contains(Account.SAVINGS) || line.contains(Account.CHECKING)) {
                    String[] infors = line.split(" ", -1);
                    long accountNumber = Long.valueOf(infors[0]);
                    String type = infors[1];
                    double balance = Double.valueOf(infors[2]);
                    if (type.equals(Account.SAVINGS)) {
                        customer.addAccount(new SavingsAccount(accountNumber, balance));
                    } else {
                        customer.addAccount(new CheckingAccount(accountNumber, balance));
                    }
                } else {
                    if (customer != null) {
                        customerList.add(customer);
                        customer = new Customer();
                    } else {
                        customer = new Customer();
                    }
                    int index = line.lastIndexOf(" ");
                    String name = line.substring(0, index);
                    long idNumber = Long.valueOf(line.substring(index + 1));
                    customer.setFullName(name);
                    customer.setIdNumber(idNumber);
                }
                line = reader.readLine();
            }
            if (customer != null) {
                customerList.add(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This is comment.
     */
    public String getCustomersInfoByNameOrder() {
        Collections.sort(customerList, new Comparator<Customer>() {
            public int compare(Customer obj1, Customer obj2) {
                return obj1.getFullName().compareTo(obj2.getFullName());
            }
        });
        String customerInfoList = "";
        for (int i = 0; i < customerList.size(); i++) {
            customerInfoList += customerList.get(i).getCustomerInfo();
            if (i < customerList.size() - 1) {
                customerInfoList += '\n';
            }
        }
        return customerInfoList;
    }

    /**
     * This is comment.
     */
    public String getCustomersInfoByIdOrder() {
        Collections.sort(customerList, new Comparator<Customer>() {
            public int compare(Customer obj1, Customer obj2) {
                return obj1.getIdNumber() < obj2.getIdNumber() ? -1 : 1;
            }
        });
        String customerInfoList = "";
        for (int i = 0; i < customerList.size(); i++) {
            customerInfoList += customerList.get(i).getCustomerInfo();
            if (i < customerList.size() - 1) {
                customerInfoList += '\n';
            }
        }
        return customerInfoList;
    }

    /**
     * This is comment.
     */
    public List<Customer> getCustomerList() {
        return customerList;
    }
}
