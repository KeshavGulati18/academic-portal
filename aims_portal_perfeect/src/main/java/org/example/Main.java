package org.example;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    static Connection conn = connect_asap.ConnectDB();
    static Statement stmt = null;

    public static void main(String[] args) {
        while (true) {
            System.out.println("Welcome to aims portal of IIT ROPAR");
            Scanner input = new Scanner(System.in);
            String str = "";
            int x = 0;
            int y = 0;
            admin a = new admin();
            student s = new student();
            instructor i = new instructor();
            String query = "select * from instructor where token='Logged in!!';";

            try {
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    i.exist = true;
                    i.instructor_id = rs.getString("id");
                    y++;
                    str = "1";
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if (y == 0) {
                query = "select * from student where token='Logged in!!';";
                try {
                    stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()) {
                        s.exist = true;
                        s.student_id = rs.getString("student_id");
                        s.batch_id = rs.getString("batch_id");
                        s.credits = rs.getInt("credits");
                        x++;
                        str = "2";
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }


            if (x == 0 && y == 0) {
                System.out.println("Enter your role");
                System.out.println("0. to exit ");
                System.out.println("1. instructor");
                System.out.println("2. student");
                System.out.println("3. admin");
                str = input.nextLine();
                if (str.equals("0")) {
                    break;
                }
            }

//--------------------------------------------------------------------------------------------------------------------


            switch (str) {

                case "1":{
                    if (y == 0) {
                        while (true) {
                            String email = "", password = "";
                            System.out.println("Enter your email");
                            email = input.nextLine();
                            System.out.println("Enter your password");
                            password = input.nextLine();
                            if (i.login(email, password)) break;
                            else {
                                System.out.println("Wrong username or password!!");
                                System.out.println("press any key to continue");
                                input.nextLine();
                            }

                        }

                    }
                    while (i.exist) {
                        System.out.println("Press \n0. to logout \n1. to view profile\n2. to update profile\n3. to add course\n4. to view offered courses\n5. to view your courses\n6. to delete Course\n7. to view grades\n8. to view enrollment requests\n9. to approve or disapprove enrollment requests\n10. to submit grades  ");
                        String t = "";
                        t = input.nextLine();
                        switch (t) {
                            case "0":
                                i.logout();
                                break;
                            case "1": {
                                String temp = i.profileView();
                                System.out.println(temp);
                                System.out.println("press any key to continue");
                                input.nextLine();
                                break;
                            }
                            case "2": {
                                String name = "", password = "", phone_no = "";
                                System.out.println("Enter name to update");
                                name = input.nextLine();
                                System.out.println("Enter phone number to update");
                                phone_no = input.nextLine();
                                System.out.println("Enter password to update");
                                password = input.nextLine();
                                i.profileUpdate(name, password, phone_no);
//                                    System.out.println("Successfully updated the profile!!");
//                                else System.out.println("Error!!");
                                System.out.println("press any key to continue");
                                input.nextLine();
                                break;
                            }
                            case "3": {
                                while (true) {
                                    String course_id;
                                    System.out.println("Enter the course_id or 0 to exit");
                                    course_id = input.nextLine();
                                    if (course_id.equals("0")) {
                                        break;
                                    }
                                    int cgpa_limit;
                                    System.out.println("set the cgpa limit for this course");
                                    cgpa_limit = Integer.parseInt(input.nextLine());
                                    i.courseAdd(course_id, cgpa_limit);
                                }
                                break;
                            }
                            case "4":
                                i.coursesOffered();
                                System.out.println("press any key to continue");
                                input.nextLine();
                                break;
                            case "5": {
                                String rec = i.myCourses();
                                System.out.println(rec);
                                System.out.println("press any key to continue");
                                input.nextLine();
                                break;
                            }
                            case "6":
                                while (true) {
                                    String course_id;
                                    System.out.println("Enter course_id to delete or 0 to exit");
                                    course_id = input.nextLine();
                                    if (course_id.equals("0")) {
                                        break;
                                    }
                                    i.courseDelete(course_id);
                                }
                                break;
                            case "7": {
                                i.gradesShow();
                                System.out.println("press any key to continue");
                                input.nextLine();
                                break;
                            }
                            case "8": {
                                String temp = i.enrollmentRequests();
                                System.out.println(temp);
                                System.out.println("press any key to continue");
                                input.nextLine();
                                break;
                            }
                            case "9": {
                                while (true) {
                                    String course_id, student_id;
                                    System.out.println("Enter course_id or 0 to exit");
                                    course_id = input.nextLine();
                                    if (course_id.equals("0")) {
                                        break;
                                    }
                                    System.out.println("Enter Student_id ");
                                    student_id = input.nextLine();
                                    String temp;
                                    System.out.println("press 1 to approve the registration request and 2 to disapprove the registration request");
                                    temp = input.nextLine();
                                    i.AorD(course_id, student_id, temp);
                                }
                                break;
                            }
                            case "10": {
                                i.gradesSubmit();
                                System.out.println("press any key to continue");
                                input.nextLine();
                            }
                            default:
                                System.out.println("Follow the given instructions!!");
                                break;
                        }
                    }

                    break;

            }


//            -------------------------------------------------------------------------------------------------------------
                case "2":{

                    if (x == 0) {
                        String email = "", password = "";
                        while (true) {
                            System.out.println("Enter your email");
                            email = input.nextLine();
                            System.out.println("Enter your password");
                            password = input.nextLine();
                            if (s.login(email, password)) break;
                            else {
                                System.out.println("Wrong username or password!!");
                                System.out.println("press any key to continue");
                                input.nextLine();
                            }

                        }
                    }
                    while (s.exist) {
                        System.out.println("Press \n0. to logout \n1. to view profile\n2. to update profile\n3. to add courses\n4. to view the offered courses\n5. to view your courses\n6. to delete Course\n7. to view grades\n8. to view your cgpa\n9. to check graduation  ");
                        String t = "";

                        t = input.nextLine();
                        switch (t) {
                            case "0":
                                s.logout();
                                break;
                            case "1": {
                                s.profileView();
                                System.out.println("press any key to continue");
                                input.nextLine();
                                break;
                            }
                            case "2": {
                                String name = "", password = "", phone_no;
                                System.out.println("Enter name to update");
                                name = input.nextLine();
                                System.out.println("Enter phone number to update");
                                phone_no = input.nextLine();
                                System.out.println("Enter password to update");
                                password = input.nextLine();
                                s.profileUpdate(name, password, phone_no);
                                System.out.println("press any key to continue");
                                input.nextLine();
                                break;
                            }
                            case "3": {

                                while (true) {
                                    String course_id;
                                    System.out.println("enter the course_id or 0 to exit");
                                    course_id = input.nextLine();
                                    if (course_id.equals("0")) {
                                        break;
                                    }
                                    s.courseAdd(course_id);
                                    System.out.println("press any key to continue");
                                    input.nextLine();

                                }
                                break;
                            }
                            case "4":
                                while (true) {
                                    String temp = s.coursesOffered();
                                    System.out.println(temp);
                                    System.out.println("press any key to continue");
                                    input.nextLine();
                                    break;
                                }
                            case "5": {
                                String temp=s.myCourses();
                                System.out.println(temp);
                                System.out.println("press any key to continue");
                                input.nextLine();
                                break;
                            }
                            case "6":{
                                while (true) {
                                    String course_id;
                                    System.out.println("Enter course_id to delete or 0 to exit");
                                    course_id = input.nextLine();
                                    if (course_id.equals("0")) {
                                        break;
                                    }
                                    s.courseDelete(course_id);
                                }
                                break;}
                            case "7": {
                                String temp=s.gradesShow();
                                System.out.println(temp);
                                System.out.println("press any key to continue");
                                input.nextLine();
                                break;
                            }
                            case "8": {
                                double temp = s.getCgpa();
                                System.out.println("Your cgpa is " +temp);
                                System.out.println("press any key to continue");
                                input.nextLine();
                                break;
                            }
                            case "9": {
                                if (s.checkGrad())
                                    System.out.println("Congratulations,you don't have any backlogs!!");
                                else
                                    System.out.println("Sorry, you cannot graduate you have backlogs in core courses!!");
                                System.out.println("press any key to continue");
                                input.nextLine();
                                break;
                            }
                            default:
                                System.out.println("Follow the given instructions");
                                break;
                        }
                    }

                    break;
            }



//            --------------------------------------------------------------------------------------------------

                case "3":
                    while (true) {
                        String username = "";
                        String password = "";
                        System.out.println("Enter the username");
                        username = input.nextLine();
                        System.out.println("Enter the password");
                        password = input.nextLine();
                        boolean f = a.login(username, password);
                        if (f)
                            break;
                        else
                            System.out.println("Wrong username or password!!!");
                    }

                    int flag = 1;//
                    while (a.exist) {
                        System.out.println("Press \n0. to logout \n1. to add a new batch \n2. delete a batch\n3. to add a new course \n4. delete the course \n5. to add record\n6. to delete record \n7. to view grades\n8. to add users\n9. to delete user\n10. to add course to catalogue\n11. to start sem\n12. to end sem\n13. to delete from catalogue\n14. to view users\n15. to view courses\n16. to view semester\n17. to submit transcript\n18. to view transcript\n19. to delete transcript ");
                        String t = "";

                        t = input.nextLine();
                        switch (t) {
                            case "0":
                                a.exist = false;
                                a.logout();
                                break;
                            case "1": {
                                while (true) {
                                    String batch_id = "", year = "", dept_id = "";
                                    System.out.println("Enter the batch id");
                                    batch_id = input.nextLine();
                                    System.out.println("Enter the  year");
                                    year = input.nextLine();
                                    System.out.println("Enter the  dept_id");
                                    dept_id = input.nextLine();
                                    a.batchAdd(batch_id, year, dept_id);
                                    System.out.println("press 0 for exit and 1 to continue");
                                    if (input.nextLine().equals("0")) {
                                        break;
                                    }
                                }

                                break;
                            }

                            case "2": {
                                while (true) {
                                    String batch_id = "";
                                    System.out.println("Enter batch_id");
                                    batch_id = input.nextLine();
                                    a.batchDelete(batch_id);
                                    System.out.println("press 0 for exit and 1 to continue");
                                    if (input.nextLine().equals("0")) {
                                        break;
                                    }
                                }

                                break;

                            }


                            case "3": {
                                while (true) {
                                    String course_id = "", course_name = "", dept_id = "";
                                    int l, tut, p, c;
                                    System.out.println("Enter the course id");
                                    course_id = input.nextLine();
                                    System.out.println("Enter the course name");
                                    course_name = input.nextLine();
                                    System.out.println("Enter lectures per week");
                                    l = Integer.parseInt(input.nextLine());
                                    System.out.println("Enter tutorials per week");
                                    tut = Integer.parseInt(input.nextLine());
                                    System.out.println("Enter course practicals per week");
                                    p = Integer.parseInt(input.nextLine());
                                    System.out.println("Enter course credits");
                                    c = Integer.parseInt(input.nextLine());
                                    System.out.println("Enter dept_id");
                                    dept_id = input.nextLine();
                                    List<String> prereq = new ArrayList<String>();
                                    while (true) {
                                        String pr;
                                        System.out.println("Enter the prerequisite course code  of the course " + course_id + " or 0 to exit");
                                        pr = input.nextLine();
                                        if (pr.equals("0")) break;
                                        prereq.add(pr);
                                    }
                                    a.courseAdd(course_id, course_name, l, tut, p, c, dept_id, prereq);
                                    System.out.println("press 0 for exit and 1 to continue");
                                    if (input.nextLine().equals("0")) {
                                        break;
                                    }
                                }
                                break;

                            }
                            case "4": {
                                while (true) {
                                    String course_id = "";
                                    System.out.println("Enter course_id");
                                    course_id = input.nextLine();
                                    a.courseDelete(course_id);
                                    System.out.println("press 0 for exit and 1 to continue");
                                    if (input.nextLine().equals("0")) {
                                        break;
                                    }
                                }
                                break;
                            }

                            case "5": {
                                while (true) {
                                    String course_id = "", course_type, batch_id;
                                    System.out.println("Enter the course id or enter 0 to quit");
                                    course_id = input.nextLine();
                                    if (course_id.equals("0")) {
                                        break;
                                    }
                                    System.out.println("Enter the batch_id ");
                                    batch_id = input.nextLine();
                                    System.out.println("Enter the course type");
                                    course_type = input.nextLine();
                                    a.recordAdd(course_id, course_type, batch_id);
                                }
                                break;

                            }
                            case "6": {
                                while (true) {
                                    String course_id = "";
                                    String course_type = "";
                                    String batch_id = "";
                                    System.out.println("Enter course_id");
                                    course_id = input.nextLine();
                                    System.out.println("Enter batch_id");
                                    batch_id = input.nextLine();
                                    a.recordDelete(course_id, batch_id);
                                    System.out.println("press 0 for exit and 1 to continue");
                                    if (input.nextLine().equals("0")) {
                                        break;
                                    }
                                }
                                break;
                            }
                            case "7": {
                                while (true) {
                                    String student_id;
                                    System.out.println("enter student_id or 0 to exit");
                                    student_id = input.nextLine();
                                    if (student_id.equals("0")) break;
                                    a.gradesShow(student_id);
                                    System.out.println("press any key to continue");
                                    input.nextLine();
                                }
                                break;
                            }

                            case "8":
                                int j = 0;
                                while (j == 0) {
                                    System.out.println("press \n0 to exit\n1 to add instructor\n2 to add student");
                                    String role = input.nextLine();
                                    List<String> val = new ArrayList<String>();
                                    switch (role) {
                                        case "0":{
                                            j = 1;
                                            break;}
                                        case "1": {
                                            String instructor_name = "", dept_id = "", phone_number = "";
                                            System.out.println("Enter name of the instructor");
                                            instructor_name = input.nextLine();
                                            System.out.println("Enter dept_id of the instructor");
                                            dept_id = input.nextLine();
                                            System.out.println("Enter phone number of the instructor");
                                            phone_number = input.nextLine();
                                            val.add(instructor_name);
                                            val.add(dept_id);
                                            val.add(phone_number);
                                            a.userAdd("1", val);
                                       break;
                                        }

                                        case "2": {
                                            String student_name = "", batch_id = "", phone_number = "";
                                            System.out.println("Enter name of the student");
                                            student_name = input.nextLine();
                                            System.out.println("Enter batch_id of the student");
                                            batch_id = input.nextLine();
                                            System.out.println("Enter phone number of the student");
                                            phone_number = input.nextLine();
                                            val.add(student_name);
                                            val.add(batch_id);
                                            val.add(phone_number);
                                            a.userAdd("2", val);
                                            break;
                                        }
                                        default:
                                            System.out.println("Enter the valid role");
                                    }
                                }
                                break;

                            case "9": {
                                while (true) {
                                    String role = "";
                                    String id = "";
                                    System.out.println("Enter 1 for instructor and 2 for student to delete");
                                    role = input.nextLine();
                                    System.out.println("Enter id");
                                    id = input.nextLine();
                                    a.userDelete(role,id);
                                    System.out.println("press 0 for exit and 1 to continue");
                                    if (input.nextLine().equals("0")) {
                                        break;
                                    }
                                }
                                break;
                            }
                            case "10": {
                                while (true) {
                                    String course_id;
                                    System.out.println("Enter course code or enter 0 to exit");
                                    course_id = input.nextLine();
                                    if (course_id.equals("0")) {
                                        break;
                                    }
                                    a.catalogueAdd(course_id);
                                }
                                break;
                            }
                            case "11": {
                                String academic_year, semester;
                                System.out.println("Enter the academic year");
                                academic_year = input.nextLine();
                                System.out.println("Enter the semester monsoon/winter");
                                semester = input.nextLine();
                                String temp = a.semStart(academic_year, semester);
                                System.out.println(temp);
                                if (!temp.equals("a sem is already running")) {
                                    System.out.println("Add course to catalogue for this semester");
                                }
                                System.out.println("press any key to continue");
                                input.nextLine();
                                break;

                            }

                            case "12": {
                                a.semEnd();
                                System.out.println("press any key to continue");
                                input.nextLine();
                                break;
                            }

                            case "13": {
                                while (true) {
                                    String course_id = "";
                                    System.out.println("Enter course_id");
                                    course_id = input.nextLine();
                                    a.catalogueDelete(course_id);
                                    System.out.println("press 0 for exit and 1 to continue");
                                    if (input.nextLine().equals("0")) {
                                        break;
                                    }
                                }
                                break;
                            }
                            case "14": {
                                String role = "0";
                                System.out.println("enter 1 for instructor and 2 for student");
                                role = input.nextLine();
                                a.usersView(role);
                                System.out.println("press any key to continue");
                                input.nextLine();
                                break;
                            }
                            case "15": {
                                a.coursesView();
                                System.out.println("press any key to continue");
                                input.nextLine();
                                break;
                            }
                            case "16": {
                                String rec=a.semView();
                                System.out.println(rec);
                                System.out.println("press any key to continue");
                                input.nextLine();
                                break;
                            }

                            case "17": {
                                String student_id = "";
                                System.out.println("Enter the student_id to submit its transcript");
                                student_id = input.nextLine();
                                a.transcriptSubmit(student_id);
                            }
                            break;
                            case "18":{
                                String student_id = "";
                                System.out.println("Enter student id");
                                student_id = input.nextLine();
                                a.transcriptView(student_id);
                                break;
                        }
                            case "20": {
                                while (true) {
                                    String student_id="";
                                    System.out.println("Enter course_id");
                                    student_id=input.nextLine();
                                    a.transcriptDelete(student_id);
                                    System.out.println("press 0 for exit and 1 to continue");
                                    if (input.nextLine().equals("0")) {
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                    }

                    break;
                default:
                    System.out.println("Oops, you entered invalid role!!");
                    break;
            }

        }

    }
}


