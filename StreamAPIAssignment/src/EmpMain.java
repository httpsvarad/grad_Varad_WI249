import java.util.*;
import java.util.function.Predicate;
import java.util.stream.*;

class Employee {
    String name;
    int age;
    int salary;
    String designation;
    String gender;
    String dept;

    public Employee(String name, int age, int salary, String designation, String gender, String dept) {
        this.name = name;
        this.age = age;
        this.salary = salary;
        this.designation = designation;
        this.gender = gender;
        this.dept = dept;
    }

    public String toString() {
        return name + " | " + age + " | " + gender + " | " + salary + " | " + designation + " | " + dept;
    }
}

class InsertHandler {
    public static void addEmployees(List<Employee> employees) {
        employees.add(new Employee("Rahul", 21, 50000, "Programmer", "Male", "IT"));
        employees.add(new Employee("Anita", 24, 55000, "Software Engineer", "Female", "IT"));
        employees.add(new Employee("Vikram", 30, 75000, "Senior Developer", "Male", "IT"));
        employees.add(new Employee("Priya", 27, 62000, "UI Designer", "Female", "Design"));
        employees.add(new Employee("Amit", 35, 90000, "Manager", "Male", "Management"));
        employees.add(new Employee("Sneha", 29, 68000, "Business Analyst", "Female", "Business"));
        employees.add(new Employee("Rohit", 26, 58000, "QA Engineer", "Male", "Testing"));
        employees.add(new Employee("Neha", 23, 48000, "Trainee Engineer", "Female", "IT"));
        employees.add(new Employee("Suresh", 40, 95000, "Tech Lead", "Male", "IT"));
        employees.add(new Employee("Pooja", 32, 72000, "HR Executive", "Female", "HR"));
        employees.add(new Employee("Karan", 28, 65000, "DevOps Engineer", "Male", "IT"));
        employees.add(new Employee("Meena", 34, 70000, "Accountant", "Female", "Finance"));
        employees.add(new Employee("Arjun", 31, 80000, "Data Analyst", "Male", "Analytics"));
        employees.add(new Employee("Kavita", 29, 67000, "Content Strategist", "Female", "Marketing"));
        employees.add(new Employee("Manoj", 45, 100000, "Manager", "Male", "Operations"));
        employees.add(new Employee("Ritu", 26, 56000, "Recruiter", "Female", "HR"));
        employees.add(new Employee("Deepak", 38, 88000, "System Architect", "Male", "IT"));
        employees.add(new Employee("Nisha", 33, 74000, "Product Owner", "Female", "Product"));
        employees.add(new Employee("Alok", 41, 92000, "Security Engineer", "Male", "IT"));
        employees.add(new Employee("Swati", 25, 52000, "Digital Marketer", "Female", "Marketing"));
        employees.add(new Employee("Harsh", 28, 63000, "Mobile App Developer", "Male", "IT"));
        employees.add(new Employee("Isha", 27, 60000, "UX Researcher", "Female", "Design"));
        employees.add(new Employee("Rajesh", 50, 110000, "Director", "Male", "Management"));
        employees.add(new Employee("Tanvi", 22, 45000, "Intern", "Female", "IT"));
        employees.add(new Employee("Nitin", 36, 82000, "Database Administrator", "Male", "IT"));
    }
}

public class EmpMain {

    public static void main(String[] args) {

        Predicate<Employee> p = employee -> employee.designation.equals("Manager");

        List<Employee> employees = new ArrayList<>();
        InsertHandler.addEmployees(employees);

        // 1. Highest salary paid employee
        Employee highestPaid = employees.stream().max(Comparator.comparingInt(e -> e.salary));
        System.out.println("Highest Paid Employee:");
        System.out.println(highestPaid);

        // 2. Count based on gender
        System.out.println("\nGender Count:");
        Map<Boolean, Long> genderCount = employees.stream().collect(Collectors.partitioningBy(e -> e.gender.equalsIgnoreCase("Male"), Collectors.counting()));

//        System.out.println(genderCount);
        System.out.println("Male: "+genderCount.get(true));
        System.out.println("Female: "+genderCount.get(false));


        // 3. Total expense department wise
        Map<String, Integer> deptExpense = employees.stream().collect(Collectors.groupingBy(e -> e.dept, Collectors.summingInt(e -> e.salary)));
        System.out.println("\nDepartment Wise Expense:");
        System.out.println(deptExpense);

        // 4. Top 5 senior employees
        System.out.println("\nTop 5 Senior Employees:");
        employees.stream().sorted(Comparator.comparingInt((Employee e) -> e.age).reversed()).limit(5).forEach(System.out::println);

        // 5. Names of all managers
        System.out.println("\nManagers:");
        employees.stream().filter(p).map(e -> e.name).forEach(System.out::println);

        // 6. Hike salary by 20% except managers
        System.out.println("\nSalary After Hike:");
        employees.stream().filter(p.negate()).forEach(e -> {
                    e.salary = e.salary + (e.salary * 20 / 100);
                    System.out.println(e);
                });

        // 7. Total number of employees
        System.out.println("\nTotal Employees: " + employees.size());
    }
}
