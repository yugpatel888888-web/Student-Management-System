import java.util.*;

// ==========================================
// 1. BASE ENTITY CLASS
// ==========================================
class Student {
    protected int studentId;
    protected String name;
    protected double gpa;

    public Student(int studentId, String name, double gpa) {
        this.studentId = studentId;
        this.name = name;
        this.gpa = gpa;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public double getGpa() {
        return gpa;
    }

    public String getStudentDetails() {
        return "ID: " + studentId + " | Name: " + name + " | GPA: " + gpa;
    }

    @Override
    public String toString() {
        return getStudentDetails();
    }
}

// ==========================================
// 2. INHERITANCE: SUB-CLASSES
// ==========================================
class RegularStudent extends Student {
    public RegularStudent(int studentId, String name, double gpa) {
        super(studentId, name, gpa);
    }

    @Override
    public String getStudentDetails() {
        return "[Regular] ID: " + studentId + " | Name: " + name + " | GPA: " + gpa;
    }
}

class ScholarshipStudent extends Student {
    public ScholarshipStudent(int studentId, String name, double gpa) {
        super(studentId, name, gpa);
    }

    @Override
    public String getStudentDetails() {
        return "[Scholarship] ID: " + studentId + " | Name: " + name + " | GPA: " + gpa;
    }
}

// ==========================================
// 3. COLLECTIONS SERVICE LAYER
// ==========================================
class StudentService {
    // Fast O(1) lookup by ID using HashMap
    private Map<Integer, Student> studentMap;
    // Duplicate prevention using HashSet
    private Set<Integer> idSet;
    // Dynamic resizing list using ArrayList
    private List<Student> studentList;

    public StudentService() {
        this.studentMap = new HashMap<>();
        this.idSet = new HashSet<>();
        this.studentList = new ArrayList<>();
    }

    public void addStudent(Student student) throws IllegalArgumentException {
        if (student == null) {
            throw new IllegalArgumentException("Student record cannot be null.");
        }
        if (idSet.contains(student.getStudentId())) {
            throw new IllegalArgumentException("Student ID already exists!");
        }

        idSet.add(student.getStudentId());
        studentMap.put(student.getStudentId(), student);
        studentList.add(student);
    }

    public Student searchStudent(int studentId) {
        return studentMap.get(studentId);
    }

    public boolean removeStudent(int studentId) {
        if (!studentMap.containsKey(studentId)) {
            return false;
        }
        Student removedStudent = studentMap.remove(studentId);
        idSet.remove(studentId);
        studentList.remove(removedStudent);
        return true;
    }

    public List<Student> getAllStudents() {
        return new ArrayList<>(studentList);
    }
}

// ==========================================
// 4. INPUT & VALIDATION HANDLER
// ==========================================
class StudentHandler {
    private Scanner scanner;
    private StudentService studentService;

    public StudentHandler(Scanner scanner, StudentService studentService) {
        this.scanner = scanner;
        this.studentService = studentService;
    }

    public boolean validateId(int id) {
        return id > 0;
    }

    public void addStudent() {
        try {
            System.out.print("Enter ID: ");
            String idStr = scanner.nextLine().trim();
            int id = Integer.parseInt(idStr);

            if (!validateId(id)) {
                throw new IllegalArgumentException("ID must be a positive integer.");
            }

            System.out.print("Enter Name: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                throw new IllegalArgumentException("Name cannot be empty.");
            }

            System.out.print("Enter GPA: ");
            String gpaStr = scanner.nextLine().trim();
            double gpa = Double.parseDouble(gpaStr);
            if (gpa < 0.0 || gpa > 10.0) {
                throw new IllegalArgumentException("GPA must be between 0.0 and 10.0.");
            }

            System.out.print("Enter Category (1. Regular, 2. Scholarship): ");
            String type = scanner.nextLine().trim();

            Student student;
            if (type.equals("2")) {
                student = new ScholarshipStudent(id, name, gpa);
            } else {
                student = new RegularStudent(id, name, gpa);
            }

            studentService.addStudent(student);
            System.out.println("Student Added Successfully!");

        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid number format entered.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void searchStudent() {
        try {
            System.out.print("Enter ID to search: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            Student student = studentService.searchStudent(id);

            if (student != null) {
                System.out.println("Found: " + student.getStudentDetails());
            } else {
                System.out.println("Error: Student ID not found.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid ID format.");
        }
    }

    public void removeStudent() {
        try {
            System.out.print("Enter ID to remove: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            boolean removed = studentService.removeStudent(id);

            if (removed) {
                System.out.println("Student ID " + id + " removed successfully.");
            } else {
                System.out.println("Error: Student ID does not exist.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid ID format.");
        }
    }
}

// ==========================================
// 5. MAIN ENTRY APPLICATION
// ==========================================
public class StudentManagementApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StudentService service = new StudentService();
        StudentHandler handler = new StudentHandler(scanner, service);

        boolean running = true;
        System.out.println("=== STUDENT MANAGEMENT SYSTEM ===");

        while (running) {
            System.out.println("\nSelect an option:");
            System.out.println("1. Add Student");
            System.out.println("2. Search Student by ID");
            System.out.println("3. Remove Student by ID");
            System.out.println("4. Exit");
            System.out.print("Choice: ");

            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1":
                        handler.addStudent();
                        break;
                    case "2":
                        handler.searchStudent();
                        break;
                    case "3":
                        handler.removeStudent();
                        break;
                    case "4":
                        running = false;
                        break;
                    default:
                        System.out.println("Error: Invalid choice. Select 1 to 4.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            } finally {
                System.out.println("Operation completed.");
            }
        }

        scanner.close();
        System.out.println("System terminated gracefully.");
    }
}
