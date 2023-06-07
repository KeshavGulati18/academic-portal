package org.example;

import java.util.Scanner;
import java.sql.SQLException;
import java.io.*;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
public class instructor {
    static Connection conn = connect_asap.ConnectDB();
    static Scanner input = new Scanner(System.in);
    static Statement stmt = null;
    public static String instructor_id="";
    public static String token="'logged in'";
    public static boolean exist=false;
    public boolean login(String email,String password){


        String query="select * from instructor where email='"+email+"' and password='"+password+"';";
        try {
            stmt=conn.createStatement();
            ResultSet rs;

            rs=stmt.executeQuery(query);
            int f=0;
            while(rs.next()){
                f++;
                instructor_id=rs.getString("id");
            }
            if(f==0){
                System.out.println("Sorry,cannot login");
                return false;
            }
            else{
                exist=true;
                query="update instructor set token='logged in' where id='"+instructor_id+"';";
                stmt.executeUpdate(query);
                System.out.println("logged in successfully");
                try {
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    String time= dtf.format(now);
                    BufferedWriter out = new BufferedWriter(
                            new FileWriter("logfile.txt", true));
                    query="instructor "+instructor_id+" logged in on "+ time+"\n";
                    out.write(query);
                    out.close();
                }
                catch (IOException exc) {
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
    public boolean logout(){
        if(!exist){
            System.out.println("no user is logged in");
            return false;
        }
        exist=false;
        String query="update instructor set token='logged out' where id='"+instructor_id+"';";
        try {

            stmt= conn.createStatement();
            stmt.executeUpdate(query);
            try {

                // Open given file in append mode by creating an
                // object of BufferedWriter class
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                String time= dtf.format(now);
                BufferedWriter out = new BufferedWriter(
                        new FileWriter("logfile.txt", true));
                query="instructor "+instructor_id+" logged out on "+ time +"\n";
                out.write(query);
                out.close();
            }
            catch (IOException exc) {

                // Display message when exception occurs
                System.out.println("exception occurred" + exc);
                return false;
            }

        } catch (SQLException exc) {
            System.out.println("exception occurred" + exc);
            return false;
        }
        return true;
    }


//    ---------------------------------------------------------------------------------------------


    public String profileView()
    {
        if(!exist){
            return "No such user exists!";
        }

        String query="select * from instructor where id='"+instructor_id+"';";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            String s="";
            while (rs.next()) {

                String id = rs.getString("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String dept_id = rs.getString("dept_id");
                String phone_number = rs.getString(("phone_no"));
                s+="id-->" + id+ "    ";
                s+="name-->" + name+ "    ";
                s+="email-->" + email+ "    ";
                s+="dept_id-->" + dept_id+ "    ";
                s+="phone_number-->" + phone_number;
                s+="\n";
            }
            return s;
        }catch (SQLException exc) {
            System.out.println(exc);
            return "Error!";
        }
    }

//-------------------------------------------------------------------------------------------------------
    public boolean profileUpdate(String name, String phone_number, String password){
        if(!exist){
            System.out.println("NO such user exists!");
            return false;
        }
        String query =" update instructor set name='"+name+"',phone_no='"+phone_number+"',password='"+password+"' where id='"+instructor_id+"';";

        try {
            stmt=conn.createStatement();
            stmt.executeUpdate(query);
            System.out.println("profile updated successfully!");
            return true;
        } catch (SQLException exc) {
            System.out.println(exc);
            System.out.println("Sorry, profile cannot be updated!");
            return false;
        }
    }


//    -----------------------------------------------------------------------------------------------------------

    public boolean courseAdd(String course_id,int cg_criteria){
        if(!exist){
            System.out.println(("No such user exists!"));
            return false;
        }

            try {
                stmt= conn.createStatement();
                String q1="select * from catalogue where course_id='"+course_id+"';";// selecting the course from catalogue

                try {
                    ResultSet rs;
                    rs=stmt.executeQuery(q1);
                }
                catch (SQLException e){
                    System.out.println("Course not available");
                    return false;
                }

                String q2="select * from offering_criteria where course_id='"+course_id+"';";
                try {
                    int i=1;
                    ResultSet rs;
                    rs=stmt.executeQuery(q2);
                    String x="";
                    while(rs.next()) {
                        x = rs.getString(i);
                    }
                    if(x.equals(""))// no such course_id exists
                    {
                        String query="insert into offering_criteria(course_id,cgpa_limit,instructor_id) values ('"+course_id+"',"+cg_criteria+",'"+instructor_id+"');";
                        stmt.executeUpdate(query);
                        System.out.println("Course added successfully!");
                    }
                    else{
                        System.out.println("Sorry, Course already offered!!");
                        return false;
                    }
                }
                catch (SQLException e){
                    System.out.println(e);
                    return false;
                }

            } catch (SQLException e) {
                System.out.println(e);
                return false;
            }
        return true;
    }

//    -----------------------------------------------------------------------------------------------------------------

    public boolean coursesOffered(){
        if(!exist){
            System.out.println("No user exist");
            return false;
        }
        String query="select * from catalogue;";

        try {
            stmt= conn.createStatement();
            try {
                ResultSet rs=stmt.executeQuery(query);
                System.out.println("Course_id");

                while(rs.next()){
                    String course_id= rs.getString("course_id");
                    System.out.println(course_id);
                }
            }
            catch (SQLException ee){
                System.out.println("Semester not started!");
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Error occurred");
            throw new RuntimeException(e);
        }
        return true;
    }

//    ---------------------------------------------------------------------------------------------------------------
    public String myCourses(){

        String query="select * from offering_criteria where instructor_id='"+instructor_id+"';";//selecting all the courses
        String s="";
        try {
            stmt= conn.createStatement();
            ResultSet rs=stmt.executeQuery(query);
            int x=0;
            while (rs.next()) {
                x++;
                String course_id = rs.getString("course_id");
                String cgpa_limit= rs.getString("cgpa_limit");
                String instructor_id = rs.getString("instructor_id");
                s+="course_id-->" + course_id + "    ";
                s+="cgpa_limit-->" + cgpa_limit + "    ";
                s+="instructor_id-->" + instructor_id;
                s+="\n";
            }
            if(x==0){
                return "You have not offered any course!";
            }
            return s;
        } catch (SQLException e) {
            return "Error!!";
        }
    }


//    ------------------------------------------------------------------------------------------------------


    public boolean courseDelete(String course_id)
    {
        if(!exist){
            System.out.println("No user exist");
            return false;
        }
            String query="delete from offering_criteria where course_id='"+course_id+"' and instructor_id='"+instructor_id+"';";
            try {
                stmt=conn.createStatement();
                String q="delete from registration_status where course_id='"+course_id+"';";
                stmt.executeUpdate(q);
                stmt.executeUpdate(query);
                System.out.println("Course deleted successfully");
            } catch (SQLException e) {
                System.out.println("No such course is offered by you!");
                    return false;
            }
            return true;
    }

//-----------------------------------------------------------------------------------------------------------------
    public boolean gradesShow()
    {
        if(!exist){
            System.out.print("No user exist");
            return false;
        }

        try {
            stmt=conn.createStatement();
            String query = "select * from grades;";
            try {
                ResultSet rs;
                rs= stmt.executeQuery(query);
                while (rs.next()) {

                    String student_id= rs.getString("student_id");
                    String course_id= rs.getString("course_id");
                    String grade= rs.getString("grade");
                    String semester= rs.getString("semester");
                    String academic_year= rs.getString("academic_year");
                    System.out.print("student_id-->" + student_id + "    ");
                    System.out.print("course_id-->" + course_id + "    ");
                    System.out.print("grade-->" + grade + "    ");
                    System.out.print("semester-->" + semester + "    ");
                    System.out.println("academic_year-->" + academic_year);
                }
            } catch (SQLException e) {
                System.out.println(e);
                System.out.println("Unable to show grades");
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("Unable to show grades");
            return false;
        }
        return true;
    }

//    ----------------------------------------------------------------------------------------------------------

    public String enrollmentRequests()
    {
        if(!exist){
            return "No such user exists";
        }
        String query="select * from registration_status where instructor_id='"+instructor_id+"' and status='pending instructor approval';";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            int x = 0;
            String s = "";
            while (rs.next()) {
                x++;
                String course_id = rs.getString("course_id");
                String student_id = rs.getString("student_id");
                s += "course_id-->" + course_id + "    ";
                s += "student_id-->" + student_id;
                s += "\n";
            }
            if (x == 0) {
                return "No enrollment requests!";
            }
            return s;
        }catch (SQLException exc) {
            System.out.println(exc);
            return "Error!!";
        }
    }

//    --------------------------------------------------------------------------



    public boolean AorD(String course_id,String student_id, String a){
               String q;
               try{
                   stmt=conn.createStatement();
                   switch (a) {
                       case "1": {
                           q = "update registration_status set status='approved by the instructor' where course_id='" + course_id + "' and student_id='" + student_id + "';";
                           stmt.executeUpdate(q);
                           System.out.println("Updated successfully");
                           break;
                       }
                       case "2": {
                           q = "update registration_status set status='rejected by the instructor' where course_id='" + course_id + "' and student_id='" + student_id + "';";
                           stmt.executeUpdate(q);
                           System.out.println("Updated successfully");
                           break;
                       }
                   }
            } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
               return true;
    }


//    --------------------------------------------------------------------------------------------------------

    public boolean gradesSubmit(){
        String csvFilePath="src/main/resources/grades.csv";
        String c_id="";
        String q = "INSERT INTO grades (student_id,course_id, grade, semester, academic_year) VALUES (?,?, ?, ?, ?)";
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(q);
        } catch (SQLException exc) {
            System.out.println(exc);
//            System.out.println("Unable to submit grades");
            return false;
        }

        BufferedReader lineReader = null;
        try {
            lineReader = new BufferedReader(new FileReader(csvFilePath));
        } catch (FileNotFoundException e) {
            System.out.println(e);
//            System.out.println("Unable to submit grades");
            return false;
        }
        String lineText = null;

        int count = 0;

        try {
            lineReader.readLine(); // skip header line
        } catch (IOException e) {
            System.out.println(e);
//            System.out.println("Unable to submit grades");
            return false;
        }
        while (true) {
            try {
                if (!((lineText = lineReader.readLine()) != null))
                    break;
            } catch (IOException e) {

                System.out.println(e);
//                System.out.println("Unable to submit grades");
                return false;
            }
            String[] data = lineText.split(",");
            if(data.length!=5){
                System.out.println("Some lines were buggy");
                continue;
            }
            String student_id = data[0];
            String course_id = data[1];

            c_id=course_id;
            String grade = data[2];
            String semester = data[3];
            String academic_year = data[4] ;

            try{

                statement.setString(1, student_id);
                statement.setString(2, course_id);

                statement.setString(3, grade);

                statement.setString(4, semester);

                statement.setString(5, academic_year);
            }
            catch (Exception e){
                System.out.println(e);
//                System.out.println("Unable to submit grades");
                return false;
            }

            try {
                statement.execute();
                System.out.println("grades submitted successfully");
            } catch (SQLException e) {
                System.out.println(e);
//                System.out.println("Unable to submit grades");
                return false;
            }
            count++;
        }
        if(count==0){
            System.out.println("Enter some data into the file");
            return false;
        }

        try {
            lineReader.close();
        } catch (IOException e) {
//            System.out.println("Unable to submit grades");
            return false;
        }

        String query="select * from registration_status where course_id='"+c_id+"';";
        try {
            stmt= conn.createStatement();
            ResultSet rs=stmt.executeQuery(query);
            while(rs.next()){
                String s_id=rs.getString(2);
                query="select * from grades where student_id='"+s_id+"' and course_id='"+c_id+"';";
                ResultSet rs1=stmt.executeQuery(query);
                int f=0;
                while(rs1.next())f++;
                if(f==0){
                    System.out.println("no grade has been submitted for student with id "+s_id);
                    query="delete from grades where course_id='"+c_id+"';";
                    stmt.executeUpdate(query);
//                    System.out.println("Unable to submit grades");
                    return false;
                } else if (f>1) {
                    System.out.println("more than 1  grade has been submitted for student with id "+s_id);
                    query="delete from grades where course_id='"+c_id+"';";
                    stmt.executeUpdate(query);
                    return false;
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
//            System.out.println("Unable to submit grades");
            return false;
        }


        return true;
    }

}




















