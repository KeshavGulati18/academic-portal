package org.example;
import java.util.Scanner;
import java.sql.*;
import java.sql.Connection;
import java.io.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
public class student {
    static Connection conn = connect_asap.ConnectDB();
    static Statement stmt = null;
    static Scanner input = new Scanner(System.in);
    public static String student_id="";
    public static boolean exist=false;
    public  String token="'logged in'";
    public static String batch_id="";
    int credits=0;
    public boolean login(String email,String password){


        String query="select * from student where email='"+email+"' and password='"+password+"';";
        try {
            stmt=conn.createStatement();
            ResultSet rs;

            rs=stmt.executeQuery(query);

            int f=0;
            while(rs.next()){
                f++;
                student_id=rs.getString("entry_number");
                batch_id=rs.getString("batch_id");
                String q="select course.c from course,grades where grades.course_id=course.id and grades.student_id='"+student_id+"' and grades.grade!='F';";
                rs = stmt.executeQuery(q);
                while (rs.next()) {
                    credits+= rs.getInt(1);
                }

            }

            if( f==0){
                return false;
            }
            else{
                exist=true;
                query="update student set token='logged in' where entry_number='"+student_id+"';";
                stmt.executeUpdate(query);
                System.out.println("logged in successfully");

                try {
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    String time= dtf.format(now);
                    BufferedWriter out = new BufferedWriter(
                            new FileWriter("logfile.txt", true));
                    query="student "+student_id+" logged in on "+ time +"\n";
                    out.write(query);
                    out.close();
                }

                // Catch block to handle the exceptions
                catch (IOException exc) {
                    // Display message when exception occurs
                    System.out.println("exception occurred" + exc);
                    return false;
                }

            }
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
        return true;
    }


//    ---------------------------------------------------------------------------------------------------------
    public boolean logout(){
        exist=false;
        String query="update student set token="+"'logged out'"+" where id='"+student_id+"';";
        try {

            stmt= conn.createStatement();
            stmt.executeUpdate(query);

            try {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                String time= dtf.format(now);
                BufferedWriter out = new BufferedWriter(
                        new FileWriter("logfile.txt", true));
                query="student "+student_id+" logged out on "+ time +"\n";
                out.write(query);
                out.close();
            }

            catch (IOException exc) {
                System.out.println("exception occurred" + exc);
                return false;
            }

        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
        System.out.println("logged out successfully");
        return true;
    }


//    ---------------------------------------------------------------------------------------------------------

    public  boolean profileView()
    {
        if(!exist){
            System.out.println("No user exists");
            return false;
        }

        String query="select * from student where entry_number='"+student_id+"';";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {

                String student_id = rs.getString("entry_number");
                String name = rs.getString("name");
                String batch_id = rs.getString("batch_id");
                String email = rs.getString("email");
                String phone_number = rs.getString(("phone_no"));
//                String credits = rs.getString("credits");
                System.out.print("student_id-->" + student_id + "    ");
                System.out.print("name-->" + name + "    ");
                System.out.print("batch_id-->" + batch_id + "    ");
                System.out.print("email-->" + email + "    ");
                System.out.print("phone_no-->" + phone_number + "    ");
                System.out.println("credits-->" + credits);
            }

        }catch (SQLException exc) {
            System.out.println(exc);
            System.out.println("Unable to view profile");
            return false;
        }
        return true;
    }



//    ------------------------------------------------------------------------------------------------------

    public boolean profileUpdate(String name, String password, String phone_number){
        if(!exist){
            System.out.println("No user exists");
            return false;
        }

        String query =" update student set name='"+name+"',phone_no='"+phone_number+"',password='"+password+"' where entry_number='"+student_id+"';";

        try {
            stmt=conn.createStatement();
            stmt.executeUpdate(query);
            System.out.println("Profile updated successfully!");
        } catch (SQLException exc) {
            System.out.println(exc);
            System.out.println("Sorry, profile cannot be updated");
            return false;
        }
        return true;
    }

//-------------------------------------------------------------------------------------------


    public boolean courseAdd(String course_id) {
        if (!exist) {
            System.out.println("No user exists");
            return false;
        }

        try {
            stmt = conn.createStatement();
            String query = "select * from offering_criteria where course_id='" + course_id + "';";
            double cgpa_criteria = 0.0;//initial cgpa
            String instructor_id = "";
            try {
                ResultSet rs;
                rs = stmt.executeQuery(query);
                int x = 0;
                while (rs.next()) {
                    cgpa_criteria = rs.getInt("cgpa_limit");//stores the cg_limit for this course
                    instructor_id = rs.getString("instructor_id");
                    x++;
                }

                if (x == 0) {
                    System.out.println("No such course is offered!");// so you cannot add this course
                    return false;
                }
            } catch (SQLException e) {
                System.out.println("No such course is offered!");
                return false;
            }
                if(gtt()) {
                    double cgpa = getCgpa();// get the current cgpa of student
                    if (cgpa > 0 && cgpa < cgpa_criteria) {
                        System.out.println("You are not eligible to take this course as your cg is below " + cgpa_criteria);
                        return false;
                    }
                }


            try {
                String q1 = "select * from prereq where course_id='" + course_id + "';";
                ResultSet rs = stmt.executeQuery(q1);
                int  temp = 1;
                while (rs.next()) {
                    String prereq = rs.getString("prereq_id");
                    try {
                        stmt = conn.createStatement();
                        String q2 = "select * from grades where course_id='" + prereq + "' and student_id='" + student_id + "';";
                        ResultSet rs2 = stmt.executeQuery(q2);
                        String grade = "";
                        while (rs2.next()) {
                            grade = rs2.getString("grade");
                        }
                        if (grade.equals("F") || grade.equals("")) {
                            System.out.println("First complete the course " + prereq + " to register for the course " + course_id + "");
                            temp= 0;
                        }
                    } catch (SQLException e) {
                        System.out.println("First complete the course " + prereq + " to register for the course " + course_id + "");
                        temp = 0;
                    }
                }

                if (temp == 0) {
                    System.out.println("Unable to add course");
                    return false;
                }
               query= "select course.c from course,registration_status where registration_status.student_id='"+student_id+"' and registration_status.course_id=course.id;";
                try {
                     rs = stmt.executeQuery(query);
                    int crd = 0;
                    while (rs.next()) {
                        crd+= rs.getInt("c");
                    }
                    //crd stores already having credits
                    query = "select * from course where id='" + course_id + "';";
                    rs = stmt.executeQuery(query);
                    int credit = 0;
                    while (rs.next()) {
                        credit= rs.getInt("c");
                    }
                    if (crd+credit> 24) {
                        System.out.println("your credit limit has exceeded for this semester");
                        return false;
                    }
                } catch (SQLException exc) {
                    System.out.println(exc);
                    System.out.println("Unable to add course");
                    return false;
                }

                String q3 = "insert into registration_status(course_id,student_id,instructor_id,status) values ('" + course_id + "','" + student_id + "','" + instructor_id + "'," + "'pending instructor approval');";
                stmt.executeUpdate(q3);
                System.out.println("Course added successfully!");

            } catch (SQLException e) {
                System.out.println(e);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return true;
    }



//        if(!exist){
//            System.out.println("No such user exists!");
//            return false ;
//        }
//
//            try {
//                stmt= conn.createStatement();
//                String query="select * from offering_criteria where course_id='"+course_id+"';";
//                try {
//                    ResultSet rs;
//                    rs=stmt.executeQuery(query);
//                }
//                catch (SQLException e){
//                    System.out.println("no such course is offered");
//                }
//
//                try {
//                    String instructor_id="";
//                    query="select * from offering_criteria where course_id='"+course_id+"';";
//                    ResultSet rs=stmt.executeQuery(query);
//                    double cg=getCgpa();
//                    int cgpa_limit=0;
//                    while (rs.next()){
//                        cgpa_limit=rs.getInt(2);
//                        instructor_id=rs.getString(3);
//                    }
//
//                    if(instructor_id.equals("")){
//                        System.out.println("no such course is offered");
//                        return false;
//                    }
//                    if(cg<cgpa_limit){
//                        System.out.println("Not eligible to register");
//                        return false;}
//

//    ------------------------------------------------------------------------------------------------------------




    public String coursesOffered()  {

        String query="\n" +
                "select offering_criteria.course_id,offering_criteria.cgpa_limit,record.course_type,offering_criteria.instructor_id\n" +
                "from record,offering_criteria\n" +
                "where '"+batch_id+"'=record.batch_id and record.course_id=offering_criteria.course_id;";
        try {
            stmt= conn.createStatement();
            ResultSet rs=stmt.executeQuery(query);
            String s="";
            while (rs.next()) {
                String course_id=rs.getString(1);
                int cgpa_limit=rs.getInt(2);
                String course_type=rs.getString(3);
                String instructor_id=rs.getString(4);
                s+="course_id-->" +course_id+ "    ";
                s+="cgpa_limit-->" +cgpa_limit+ "    ";
                s+="course_type-->" +course_type+ "    ";
                s+="instructor_id-->" +instructor_id+ "    ";
                s+="\n";
                }
            return s;
        }
        catch (SQLException e){
            System.out.println(e);
            return "no courses offered yet";
        }

    }

//---------------------------------------------------------------------------------







    public String myCourses(){

        String q="select * from registration_status where student_id='"+student_id+"';";

        try {
            stmt= conn.createStatement();
            ResultSet rs=stmt.executeQuery(q);
            int x=0;
            String s="";
            while (rs.next()) {
                x++;
                String course_id= rs.getString("course_id");
                String instructor_id= rs.getString("instructor_id");
                String status= rs.getString("status");
            s+="course_id-->" + course_id + "    ";
            s+="instructor_id-->" + instructor_id + "    ";
            s+="status-->" + status;
            s+="\n";
            }

            if(x==0){
                return "No courses!";
            }

             return s;
        } catch (SQLException exc) {
            System.out.println(exc);
            return "Error!!";
        }
    }


//    -----------------------------------------------------------------------------------




    public boolean courseDelete(String course_id)
    {
        if(!exist){
            System.out.println("No user exists");
            return false;
        }

            String query="delete from registration_status where course_id='"+course_id+"' and student_id='"+student_id+"';";
            try {
                stmt=conn.createStatement();
                stmt.executeUpdate(query);
            } catch (SQLException e) {
                System.out.println("This course is not offered by you!");
                return false;
            }
        return true;
    }

//    -----------------------------------------------------------------


    public String gradesShow()
    {
        String query="select * from grades where student_id='"+student_id+"';";
        try {
            stmt= conn.createStatement();
            ResultSet rs=stmt.executeQuery(query);
            int x=0;
            String s="";
            while (rs.next()) {
                x++;
                String student_id=rs.getString("student_id");
                String course_id=rs.getString("course_id");
                String grade=rs.getString("grade");
                String semester=rs.getString("semester");
                String academic_year=rs.getString("academic_year");

                s+="student_id-->" + student_id + "    ";
                s+="course_id-->" + course_id + "    ";
                s+="grade-->" + grade + "    ";
                s+="semester-->" + semester + "    ";
                s+="academic_year-->" + academic_year + "    ";
                s+="\n";
            }

            if(x==0){
                return "No grades to show yet" ;
            }
            return s;
        } catch (SQLException exc) {
            System.out.println(exc);
            return "Error!!";
        }

    }


//    -------------------------------------------------------------------------------------------

    public double getCgpa()
    {
        String query="select grades.grade,course.c from grades,course where student_id='"+student_id+"' and course.id=grades.course_id;";
        double cg=0.0;
        double credit=0.0;
        double score=0;
        int x=0;
        String grade="";
        try {
            stmt= conn.createStatement();
            ResultSet rs=stmt.executeQuery(query);
    int i=1;
    int j=2;
            while (rs.next()) {
                x++;
                grade = rs.getString(i);
                credit += rs.getInt(j);
                if (grade.equals("A")) score += 10;
                else if (grade.equals("A-")){ score += 9;}
                else if (grade.equals("B")) {score += 8;}
                else if (grade.equals("B-")) {score += 7;}
                else if (grade.equals("C")) {score += 6;}
                else if (grade.equals("C-")) {score += 5;}
                else if (grade.equals("D")) {score += 4;}
                else if (grade.equals("E")) {score += 2;}
                else if (grade.equals("F")) {score += 0;}
            }


            if(x==0){
                return 0.0;
            }
            else{
                cg=score/credit;
            }
        } catch (SQLException exc) {
    System.out.println(exc);
            if(credit!=0.0 && x!=0){
                cg=score/credit;
                return cg;}
            return 0.0;
        }
//        System.out.println(cg);
        return cg;
    }

//    --------------------------------------------------------------------------------------------

    public boolean checkGrad() {
//        String q = "\n" +
//                "select grades.course_id\n" +
//                "from grades,record\n" +
//                "where  grades.student_id='"+student_id+"' and record.batch_id='"+batch_id+"'\n" +
//                "except\n" +
//                "select grades.course_id\n" +
//                "from grades\n" +
//                "where grades.grade!='F' and grades.student_id='" + student_id + "';";// gives all the grades in which a student has got F grade and course_type is core
        String q="Select grades.course_id from grades,record where grades.student_id='"+student_id+"' and grades.grade='F' and record.course_type='core' and record.batch_id='"+batch_id+"';";
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(q);
            int x = 0;
            String s = "";
            while (rs.next()) {
                x++;
                s += rs.getString("course_id") + "    ";
            }
            System.out.println(s);

            if (x > 0) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException exc) {
            System.out.println(exc);
            System.out.println("cannot check error occurred");
            return false;
        }
    }

//    --------------------------------------------------------------------------------------------------------

    public  boolean gtt() {
        String query="select count(*) from grades where student_id='"+student_id+"' GROUP BY academic_year,semester;";
        int x=0;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                x++;
            }
            if (x > 2) {
                return true;
            }
            else {
                return false;
            }
        }catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }





}

