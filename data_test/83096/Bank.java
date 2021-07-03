import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Bank {
    private List<Customer> customerList = new ArrayList<>();

    public List<Customer> getCustomerList() {
        return customerList;
    }

    /**
     * javadoc.
     */
    public void readCustomerList(InputStream inputStream) {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        StringBuilder data = new StringBuilder();
        int x;
        char c;
        try {
            while ((x = inputStreamReader.read()) != -1) {
                // converts integer to character
                c = (char) x;
                data.append(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //System.out.println(data);
        String[] lines = data.toString().split("\n");

        Customer currentCustomer = new Customer();
        for (String line : lines) {
            String[] parseLine = line.split(" ");

            if (!('0' <= parseLine[0].charAt(0) && parseLine[0].charAt(0) <= '9')) {
                StringBuilder name = new StringBuilder();
                for (int i = 0; i < parseLine.length - 1; ++i) {
                    name.append(parseLine[i]);
                    if (i < parseLine.length - 2) {
                        name.append(" ");
                    }
                }

                String id = parseLine[parseLine.length - 1].substring(0, 9);
                long idNumber = Long.parseLong(id);
                currentCustomer = new Customer(idNumber, name.toString());
                customerList.add(currentCustomer);
                //System.out.println(name.toString() + "hihi");
                //System.out.println(id);
            } else {
                if (parseLine[1].equals(Account.CHECKING)) {
                    currentCustomer.addAccount(new CheckingAccount(Long.parseLong(parseLine[0]),
                            Double.parseDouble(parseLine[2])));
                } else {
                    currentCustomer.addAccount(new SavingsAccount(Long.parseLong(parseLine[0]),
                            Double.parseDouble(parseLine[2])));
                }
            }
        }
    }

    /**
     * javadoc.
     */
    public String getCustomersInfoByIdOrder() {
        for (int i = 0; i < customerList.size(); ++i) {
            for (int j = i + 1; j < customerList.size(); ++j) {
                Customer a = customerList.get(i);
                Customer b = customerList.get(j);
                if (a.getIdNumber() > b.getIdNumber()) {
                    customerList.set(j, a);
                    customerList.set(i, b);
                }
            }
        }


        StringBuilder res = new StringBuilder();
        for (Customer i : customerList) {
            res.append(i.getCustomerInfo()).append('\n');
        }
        res.deleteCharAt(res.lastIndexOf("\n"));
        return res.toString();
    }

    /**
     * javadoc.
     */
    public String getCustomersInfoByNameOrder() {
        for (int i = 0; i < customerList.size(); ++i) {
            for (int j = i + 1; j < customerList.size(); ++j) {
                Customer a = customerList.get(i);
                Customer b = customerList.get(j);
                if (a.getFullName().compareTo(b.getFullName()) > 0) {
                    customerList.set(j, a);
                    customerList.set(i, b);
                }
            }
        }

        StringBuilder res = new StringBuilder();
        for (Customer i : customerList) {
            res.append(i.getCustomerInfo()).append('\n');
        }
        res.deleteCharAt(res.lastIndexOf("\n"));
        return res.toString();
    }

    /**
     * javadoc.
     */
    public static void main(String[] args) throws FileNotFoundException {
        Bank bank = new Bank();
        bank.readCustomerList(new FileInputStream("src/test.txt"));
        System.out.println(bank.getCustomersInfoByNameOrder());
        List<Customer> customers = bank.getCustomerList();
        customers.get(2).getAccountList().get(0).withdraw(-1000.00);
        for (Customer customer : customers) {
            for (Account account : customer.getAccountList()) {
                System.out.println(account.getBalance() + " "
                        + account.getAccountNumber() + " " + (account instanceof CheckingAccount));
            }
        }
    }
}
 