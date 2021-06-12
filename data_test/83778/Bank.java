import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * Created by CCNE on 03/12/2020.
 */
public class Bank {

    private List<Customer> customerList = new ArrayList<>();

    /**
     * ok.
     */
    public void readCustomerList(InputStream is) {
        Scanner scanner = new Scanner(is);
        Customer customer = new Customer();
        int cnt = 0;
        while (scanner.hasNext()) {
            String line = scanner.nextLine();

            if (line.charAt(0) < '0' || line.charAt(0) > '9') {
                int length = line.length();
                long cmnd = Long.parseLong(line.substring(length - 9, length));
                String name = line.substring(0, length - 10);
                cnt++;
                if (cnt != 1) {
                    customerList.add(customer);
                }
                customer = new Customer();
                customer.setFullName(name);
                customer.setIdNumber(cmnd);

            } else {
                String tokens[] = line.split(" ");
                long STK = Long.parseLong(tokens[0]);
                String type = tokens[1];
                double sodu = Double.parseDouble(tokens[2]);
                Account account;
                if (type.equals("CHECKING")) {
                    account = new CheckingAccount(STK, sodu);
                } else {
                    account = new SavingsAccount(STK, sodu);
                }
                customer.addAccount(account);
            }

        }
        customerList.add(customer);
    }

    /**
     * ok.
     */
    public String getCustomersInfoByNameOrder() {
        customerList.sort(new Comparator<Customer>() {
            @Override
            public int compare(Customer o1, Customer o2) {
                return o1.getFullName().compareTo(o2.getFullName());
            }
        });
        String res = "";
        for (Customer customer : customerList) {
            res += customer.getCustomerInfo() + '\n';
        }
        res = res.substring(0, res.length() - 1);
        return res;
    }

    /**
     * ok.
     */
    public String getCustomersInfoByIdOrder() {
        customerList.sort(new Comparator<Customer>() {
            @Override
            public int compare(Customer o1, Customer o2) {
                if (o1.getIdNumber() < o2.getIdNumber()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        String res = "";
        for (Customer customer : customerList) {
            res += customer.getCustomerInfo() + '\n';
        }
        res = res.substring(0, res.length() - 1);
        return res;
    }

    /**
     * ok.
     */
    public List<Customer> getCustomerList() {
        return customerList;
    }

}
 