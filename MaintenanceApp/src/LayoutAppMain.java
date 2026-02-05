import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

enum Role{
    ADMIN,
    OWNER
}

enum UpdateStatus {
    PENDING, APPROVED, REJECTED
}

enum SiteType {
    VILLA, APARTMENT, INDEPENDENT_HOUSE, OPEN_SITE
}

class InputHelper {
    public static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
}

class DBConnection {
    private static Connection connection;
    private DBConnection() {}

    public static Connection connectToDB() {
        if (connection == null) {
            try {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/my_db",
                        "postgres",
                        "password"
                );
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException("Error connecting to DB: ", e);
            }
        }
        return connection;
    }
}

class AuthService {
    public static User login(String email, String password){
        try{
            PreparedStatement ps = DBConnection.connectToDB().prepareStatement("SELECT name, user_id, role FROM users WHERE email = (?) AND password = (?)");
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()){
                User user = new User(rs.getInt("user_id"), rs.getString("name"), Role.valueOf(rs.getString("role")));
                return user;
            }

        } catch (Exception e){
            System.out.println(e);
        }
        return null;
    }
}

class SiteUpdateRequest {
    int requestId;
    int siteId;
    int requestedBy;
    String updateType;
    String oldValue;
    String newValue;
    UpdateStatus status;

    public SiteUpdateRequest(int requestId, int siteId, int requestedBy, String updateType,
                             String oldValue, String newValue, UpdateStatus status) {
        this.requestId = requestId;
        this.siteId = siteId;
        this.requestedBy = requestedBy;
        this.updateType = updateType;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.status = status;
    }
}

class Site {
    int siteId;
    int length;
    int width;
    SiteType type;
    boolean occupied;
    int ownerId;
    int area;

    public Site(int siteId, int length, int width, SiteType type, int ownerId, boolean occupied, int area) {
        this.siteId = siteId;
        this.length = length;
        this.width = width;
        this.type = type;
        this.ownerId = ownerId;
        this.occupied = occupied;
        this.area=area;
    }

    public int getSqft() {
        return length * width;
    }
    public boolean isOccupied() {
        return occupied;
    }
    public int getOwnerId() {
        return ownerId;
    }
    public int getSiteId() {
        return siteId;
    }

    public String toString() {
        return String.format("Site ID: %d | Type: %s | Dimensions: %dx%d ft | Area: %d sqft | Occupied: %s | Owner ID: %d",
                siteId, type, length, width, area, (occupied ? "Yes" : "No"), ownerId);
    }
}

class User{
    int userId;
    String name;
    String email;
    Role role;

    User(int userId, String name, Role role){
        this.userId=userId;
        this.name=name;
        this.role=role;
    }

    public Role getRole() {
        return role;
    }
}

interface AdminDAO{
    Boolean addSite(Site site);
    void generateMaintenance();
    void viewPaymentRecords();
    void approveRejectRequest();
}

interface OwnerDAO{
    List<Site> getOwnerSites(User user);
    void payMaintenance(User user);
    void requestSiteUpdate(User user);
}

class OwnerOperations implements OwnerDAO {

    public void requestSiteUpdate(User user) {
        try {
            List<Site> ownedSites = getOwnerSites(user);

            if (ownedSites.isEmpty()) {
                System.out.println("You don't own any sites.");
                return;
            }

            System.out.println("\nYour Sites:");
            for (Site site : ownedSites) {
                System.out.println(site.siteId + " | " + site.type + " | Occupied: " + site.occupied);
            }

            System.out.print("\nEnter Site ID: ");
            int siteId = Integer.parseInt(InputHelper.br.readLine());

            Site selectedSite = null;
            for (Site s : ownedSites) {
                if (s.siteId == siteId) {
                    selectedSite = s;
                    break;
                }
            }

            if (selectedSite == null) {
                System.out.println("Invalid site ID!");
                return;
            }

            System.out.println("\n1. Update Site Type");
            System.out.println("2. Update Occupation Status");
            System.out.print("Choice: ");
            int choice = Integer.parseInt(InputHelper.br.readLine());

            String updateType, oldValue, newValue;

            if (choice == 1) {
                updateType = "site_type";
                oldValue = selectedSite.type.toString();
                System.out.println("Current: " + oldValue);
                for (int i = 0; i < SiteType.values().length; i++) {
                    System.out.println((i + 1) + ". " + SiteType.values()[i]);
                }
                System.out.print("New Type: ");
                int typeChoice = Integer.parseInt(InputHelper.br.readLine());
                newValue = SiteType.values()[typeChoice - 1].toString();
            } else {
                updateType = "is_occupied";
                oldValue = String.valueOf(selectedSite.occupied);
                System.out.print("Set occupied (true/false): ");
                newValue = InputHelper.br.readLine();
            }

            PreparedStatement ps = DBConnection.connectToDB().prepareStatement(
                    "INSERT INTO site_update_requests (site_id, requested_by, update_type, old_value, new_value, status) VALUES (?,?,?,?,?,?)"
            );
            ps.setInt(1, siteId);
            ps.setInt(2, user.userId);
            ps.setString(3, updateType);
            ps.setString(4, oldValue);
            ps.setString(5, newValue);
            ps.setString(6, "PENDING");
            ps.executeUpdate();

            System.out.println("\nRequest submitted!");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public List<Site> getOwnerSites(User user) {
        List<Site> Ownersites = new ArrayList<>();
        String query = "SELECT s.*, u.name as owner_name FROM sites s " +
                "LEFT JOIN users u ON s.owner_id = u.user_id WHERE s.owner_id = ? ORDER BY s.site_id";

        try {
            PreparedStatement ps = DBConnection.connectToDB().prepareStatement(query);

            ps.setInt(1, user.userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Ownersites.add(new Site(
                        rs.getInt("site_id"),
                        rs.getInt("length_ft"),
                        rs.getInt("width_ft"),
                        SiteType.valueOf(rs.getString("site_type")),
                        rs.getInt("owner_id"),
                        rs.getBoolean("is_occupied"),
                        rs.getInt("sqft")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching sites: " + e.getMessage());
        }
        return Ownersites;
    }

    public void payMaintenance(User user) {
        try {
            List<Site> ownedSites = getOwnerSites(user);

            if (ownedSites.isEmpty()) {
                System.out.println("You don't own any sites.");
                return;
            }

            System.out.println("\nYour Sites:");
            for (Site site : ownedSites) {
                System.out.println(site.siteId + " | " + site.type + " | " + site.area + " sqft");
            }

            System.out.print("\nSelect Site ID to Pay Maintenance: ");
            int siteId = Integer.parseInt(InputHelper.br.readLine());

            boolean owns = false;
            for (Site s : ownedSites) {
                if (s.siteId == siteId) {
                    owns = true;
                    break;
                }
            }

            if (!owns) {
                System.out.println("You don't own this site!");
                return;
            }

            String query = "SELECT m.*, COALESCE(SUM(p.paid_amt), 0) as total_paid " +
                    "FROM maintenance m " +
                    "LEFT JOIN payments p ON m.maintenance_id = p.payment_id " +
                    "WHERE m.site_id = ? " +
                    "GROUP BY m.maintenance_id " +
                    "ORDER BY m.generated_date DESC LIMIT 1";

            try (PreparedStatement ps = DBConnection.connectToDB().prepareStatement(query)) {
                ps.setInt(1, siteId);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    int maintenanceId = rs.getInt("maintenance_id");
                    double totalAmount = rs.getDouble("amount");
                    double totalPaid = rs.getDouble("total_paid");
                    double pending = totalAmount - totalPaid;

                    System.out.println("\n--- Maintenance Details ---");
                    System.out.println("Total Amount: Rs. " + totalAmount);
                    System.out.println("Already Paid: Rs. " + totalPaid);
                    System.out.println("Pending Amount: Rs. " + pending);

                    if (pending <= 0) {
                        System.out.println("No pending maintenance for this site.");
                        return;
                    }

                    System.out.print("\nEnter amount to pay: ");
                    double amount = Double.parseDouble(InputHelper.br.readLine());

                    if (amount <= 0 || amount > pending) {
                        System.out.println("Invalid amount!");
                        return;
                    }

                    String insertQuery = "INSERT INTO payments (payment_id, paid_by, paid_amt, pending_amt, status) VALUES (?,?,?,?,?)";
                    try (PreparedStatement pst = DBConnection.connectToDB().prepareStatement(insertQuery)) {
                        pst.setInt(1, maintenanceId);
                        pst.setInt(2, user.userId);
                        pst.setDouble(3, amount);
                        pst.setDouble(4, pending - amount);
                        pst.setString(5, (pending - amount) <= 0 ? "PAID" : "PENDING");
                        pst.executeUpdate();

                        System.out.println("\nPayment successful!");
                        System.out.println("Remaining balance: Rs. " + (pending - amount));
                    }
                } else {
                    System.out.println("No maintenance record found for this site.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

class AdminDBOperations implements AdminDAO{

    public void approveRejectRequest() {
        try {

            String query = "SELECT * FROM site_update_requests WHERE status = 'PENDING'";
            Statement st = DBConnection.connectToDB().createStatement();
            ResultSet rs = st.executeQuery(query);

            System.out.println("\n=== PENDING REQUESTS ===");
            int count = 0;
            while (rs.next()) {
                count++;
                System.out.println("[" + rs.getInt("request_id") + "] Site " + rs.getInt("site_id") +
                        ": " + rs.getString("update_type") +
                        " (" + rs.getString("old_value") + " → " + rs.getString("new_value") + ")");
            }

            if (count == 0) {
                System.out.println("No pending requests.");
                return;
            }

            System.out.println("========================");
            System.out.print("\nEnter Request ID: ");
            int requestId = Integer.parseInt(InputHelper.br.readLine());


            PreparedStatement ps1 = DBConnection.connectToDB().prepareStatement(
                    "SELECT * FROM site_update_requests WHERE request_id = ?"
            );
            ps1.setInt(1, requestId);
            ResultSet rs1 = ps1.executeQuery();

            if (!rs1.next()) {
                System.out.println("Invalid Request ID!");
                return;
            }

            int siteId = rs1.getInt("site_id");
            String updateType = rs1.getString("update_type");
            String newValue = rs1.getString("new_value");

            System.out.print("\nApprove? (Y/N): ");
            String decision = InputHelper.br.readLine();

            if (decision.equalsIgnoreCase("Y")) {

                PreparedStatement ps2 = DBConnection.connectToDB().prepareStatement(
                        "UPDATE sites SET " + updateType + " = ? WHERE site_id = ?"
                );

                if (updateType.equals("is_occupied")) {
                    ps2.setBoolean(1, Boolean.parseBoolean(newValue));
                } else {
                    ps2.setString(1, newValue);
                }
                ps2.setInt(2, siteId);
                ps2.executeUpdate();


                PreparedStatement ps3 = DBConnection.connectToDB().prepareStatement(
                        "UPDATE site_update_requests SET status = 'APPROVED' WHERE request_id = ?"
                );
                ps3.setInt(1, requestId);
                ps3.executeUpdate();

                System.out.println("APPROVED!");

            } else {

                PreparedStatement ps3 = DBConnection.connectToDB().prepareStatement(
                        "UPDATE site_update_requests SET status = 'REJECTED' WHERE request_id = ?"
                );
                ps3.setInt(1, requestId);
                ps3.executeUpdate();

                System.out.println("REJECTED!");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void getOwners() {
        try {
            Connection conn = DBConnection.connectToDB();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT user_id, name FROM users WHERE role='OWNER'");
            while (rs.next()) {
                System.out.println(rs.getInt("user_id") + " : " + rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching owners: " + e.getMessage());
        }
    }

    public void viewPaymentRecords() {
        String query = "SELECT p.payment_id, m.site_id, u.name as owner_name, " +
                "p.paid_amt, p.pending_amt, m.amount, p.status " +
                "FROM payments p " +
                "LEFT JOIN users u ON p.paid_by = u.user_id " +
                "JOIN maintenance m ON p.payment_id = m.maintenance_id";

        try {
            Connection conn = DBConnection.connectToDB();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            System.out.println("\n=== PAYMENT RECORDS ===");

            while (rs.next()) {
                String ownerName = rs.getString("owner_name");
                if (ownerName == null) ownerName = "Admin";

                System.out.println("Payment ID: " + rs.getInt("payment_id"));
                System.out.println("Site ID: " + rs.getInt("site_id"));
                System.out.println("Owner: " + ownerName);
                System.out.println("Paid: Rs. " + rs.getDouble("paid_amt"));
                System.out.println("Pending: Rs. " + rs.getDouble("pending_amt"));
                System.out.println("Total: Rs. " + rs.getDouble("amount"));
                System.out.println("Status: " + rs.getString("status"));
                System.out.println("------------------------");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public List<Site> getAllSites() {
        List<Site> sites = new ArrayList<>();
        try {
            Connection conn = DBConnection.connectToDB();
            Statement st = conn.createStatement();
            SiteType type;
            ResultSet rs = st.executeQuery("SELECT * FROM Sites");
            while (rs.next()) {
                sites.add(new Site(
                        rs.getInt("site_id"),
                        rs.getInt("length_ft"),
                        rs.getInt("width_ft"),
                        SiteType.valueOf(rs.getString("site_type")),
                        rs.getInt("owner_id"),
                        rs.getBoolean("is_occupied"),
                        rs.getInt("sqft")
                ));
            }
            return sites;
        } catch (SQLException e) {
            System.out.println("Error fetching owners: " + e.getMessage());
            return null;
        }
    }

    public int calculateRate(boolean is_occ, int area){
        if(is_occ){
            return area*9;
        } else
            return area*6;
    }

    public Boolean addSite(Site site){
        try {
            PreparedStatement ps = DBConnection.connectToDB().prepareStatement("INSERT INTO sites (length_ft, width_ft, sqft, site_type, is_occupied, owner_id) VALUES (?,?,?,?,?,?)");
            ps.setInt(1, site.length);
            ps.setInt(2, site.width);
            ps.setInt(3, site.area);
            ps.setString(4, site.type.toString());
            ps.setBoolean(5, site.isOccupied());
            if (site.getOwnerId() > 0)
                ps.setInt(6, site.getOwnerId());
            else
                ps.setNull(6, Types.INTEGER);
            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void generateMaintenance(){
        List<Site> allSites = getAllSites();

        try {
            for(Site i : allSites){
                try {
                    PreparedStatement ps = DBConnection.connectToDB().prepareStatement("INSERT INTO maintenance (site_id, amount, generated_date) VALUES (?,?,?)");
                    ps.setInt(1, i.siteId);
                    ps.setInt(2, calculateRate(i.occupied, i.area));
                    ps.setDate(3, Date.valueOf(LocalDate.now()));
                    ps.execute();
                } catch (SQLException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
            System.out.println("MAINTENANCE GENERATED");
        } catch (Exception e) {
            System.out.println(e);
        }

    }


}

class MenuController {

    public static void showMenu(User user) throws Exception {
        AdminDBOperations adminOps = new AdminDBOperations();
        OwnerOperations ownOps = new OwnerOperations();

        if (user.getRole() == Role.ADMIN) {
            while (true) {
                System.out.println("\n--- ADMIN MENU ---");
                System.out.println("1. Add Site");
                System.out.println("2. View Payment Records");
                System.out.println("3. Generate Maintenance");
                System.out.println("4. Approve/Reject Requests");
                System.out.println("5. Logout");

                System.out.print("Enter choice: ");
                int choice = Integer.parseInt(InputHelper.br.readLine());

                switch (choice) {
                    case 1:
                        addSiteMenu(adminOps);
                        break;
                    case 2:
                        adminOps.viewPaymentRecords();
                        break;
                    case 3:
                        adminOps.generateMaintenance();
                        break;
                    case 4:
                        adminOps.approveRejectRequest();
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Invalid choice! Try again.");
                }
            }
        } else {
            while (true) {
                System.out.println("\n--- OWNER MENU ---");
                System.out.println("1. View Sites");
                System.out.println("2. Pay Maintenance");
                System.out.println("3. Request Site Update");
                System.out.println("4. Logout");

                System.out.print("Enter choice: ");
                int choice = Integer.parseInt(InputHelper.br.readLine());

                switch (choice) {
                    case 1:
                        List<Site> sites = ownOps.getOwnerSites(user);
                        if (sites.isEmpty()) {
                            System.out.println("You don't own any sites.");
                        } else {
                            System.out.println("\n--- Your Sites ---");
                            for (Site site : sites) {
                                System.out.println(site);
                            }
                        }
                        break;
                    case 2:
                        ownOps.payMaintenance(user);
                        break;
                    case 3:
                        ownOps.requestSiteUpdate(user);
                        break;
                    case 4:
                        return;
                    default:
                        System.out.println("Invalid choice! Try again.");
                }
            }
        }
    }

    private static void addSiteMenu(AdminDBOperations adminOps) throws Exception {
        BufferedReader br = InputHelper.br;

        System.out.println("\n--- ADD SITE ---");

        System.out.println("Existing Owners:");
        adminOps.getOwners();

        System.out.print("Enter Length (ft): ");
        int length = Integer.parseInt(br.readLine());

        System.out.print("Enter Width (ft): ");
        int width = Integer.parseInt(br.readLine());

        System.out.println("Select Site Type:");
        for (int i = 0; i < SiteType.values().length; i++) {
            System.out.print(i+1+". "+SiteType.values()[i]+" ");
        }
        System.out.print("\nEnter choice: ");
        int typeChoice = Integer.parseInt(br.readLine());
        SiteType type = SiteType.values()[typeChoice - 1];

        System.out.print("Enter Owner ID OR 0 for no owner): ");
        int ownerId = Integer.parseInt(br.readLine());

        int area = length*width;
        boolean occ = type != SiteType.OPEN_SITE;

        Site newSite = new Site(0, length, width, type, ownerId, occ, area);

        if (adminOps.addSite(newSite)) {
            System.out.println("Site added successfully!");
        } else {
            System.out.println("Failed to add site.");
        }
    }
}


public class LayoutAppMain {
    public static void main(String[] args) {
        String email;
        String password;

        try {
            System.out.println("=====Layout Maintenance Portal=====");
            System.out.print("Enter Email : ");
            email = InputHelper.br.readLine();
            System.out.print("Enter Password : ");
            password=InputHelper.br.readLine();

            User user = AuthService.login(email, password);

            if(user!=null){
                System.out.println("Login Successful! Hi, " + user.name);
                MenuController.showMenu(user);
            } else System.out.println("INVALID CREDENTIALS");

        } catch (Exception e){
            System.out.println("Something went wrong: "+e.getMessage());
        }

    }
}


