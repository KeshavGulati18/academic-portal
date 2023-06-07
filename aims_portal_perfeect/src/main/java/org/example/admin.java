
package org.example;
import java.io.*;
import java.sql.*;
import javax.swing.*;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.sql.Statement;
import java.util.Scanner;

public class admin {
    static Connection conn = connect_asap.ConnectDB();
    static Statement stmt = null;
    public String username = "";
    static Scanner input = new Scanner(System.in);
    public String password = "";
    public boolean exist = false;

//    to login by the admin with default username as admin and password as iitropar
    public boolean login(String username,String password) {
            if (username.equals("admin") && password.equals("iitropar")) {
                try {
                    FileWriter fileWriter = new FileWriter("logfile.txt", true);
                    BufferedWriter out = new BufferedWriter(fileWriter);
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    String time = dtf.format(now);
                    String q = "Admin logged in at " + time + "\n";
                    out.write(q);
                    System.out.println("Successfully logged in!!");
                    out.close();
                } catch (IOException exc) {
                    System.out.println("Exception " + exc);
                    return false;
                }
            } else {
                System.out.println("Wrong username or password");
                return false;
            }

        exist = true;
        return true;
    }



    //-----------------------------------------------------------------------------------------------------------------
    public void logout() {
        try {


            FileWriter fileWriter = new FileWriter("logfile.txt", true);
            BufferedWriter out = new BufferedWriter(fileWriter);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String time = dtf.format(now);
            String q = "Admin  logged out on " + time + "\n";
            out.write(q);
            System.out.println("Successfully logged out!!");
            exist = false;
            out.close();
        } catch (IOException exc) {

            System.out.println("Exception" + exc);
        }
    }

//   -----------------------------------------------------------------------------------------------------------------


    // to add the complete batch by  admin
    public boolean batchAdd(String batch_id, String year, String dept_id) {

        try {
            stmt = conn.createStatement();// to connect to database
            String q1 = "INSERT INTO batch(id,year,dept_id) VALUES('" + batch_id + "'," + "'" + year + "'," + "'" + dept_id + "');";
            int x = stmt.executeUpdate(q1);//execute the query
            if (x > 0) {
                System.out.println("Successfully Inserted");
                return true;
            }
            else {
                System.out.println("Insert Failed");
                return false;
            }
        } catch (SQLException exc) {
            System.out.println(exc);
            return false;
        }

    }
//-------------------------------------------------------------------------------


    public boolean batchDelete(String batch_id) {
        String q = "delete from batch where id='" + batch_id + "';";
        try {
            stmt = conn.createStatement();//create statement
            stmt.executeUpdate(q);
            System.out.println("Batch deleted successfully");
            return true;
        } catch (SQLException exc) {
            System.out.println(exc);
            System.out.println("Unable to delete this batch");
            return false;
        }
    }

//    -------------------------------------------------------------------------------


    public boolean courseAdd(String course_id, String course_name, int l, int t, int p, int c, String dept_id, List<String> prereq) {
        try {
            stmt = conn.createStatement();
            String q1 = "INSERT INTO course(id,name,l,t,p,c,dept_id) VALUES('"+course_id+"'," + " '"+course_name+"',"+l+","+t+","+p+","+c+",'"+dept_id+"');";
            try {
                stmt.executeUpdate(q1);
                Iterator<String> str=prereq.iterator();
                while(str.hasNext()){
                    q1 = "insert into prereq(course_id,prereq_id) values('" + course_id + "','" + str + "');";
                    stmt.executeUpdate(q1);
                }
            } catch (SQLException exc) {
                System.out.println(exc);
                System.out.println("Unable to add course");
                return false;
            }
        } catch (SQLException exc) {
            System.out.println(exc);
            System.out.println("Unable to add course");
            return false;
        }
        System.out.println("Course added successfully");
        return true;

    }


//-----------------------------------------------------------------------------------

    public boolean courseDelete(String course_id) {
        String q = "delete from course where id='" + course_id + "';";
        try {
            stmt = conn.createStatement();//create statement
//            String query = "delete from preqreq where prereq_id='" + course_id + "' or course_id='" + course_id + "';";
            stmt.executeUpdate(q);
//            stmt.executeUpdate(query);// execute it
            System.out.println("Course deleted successfully!!");
            return true;
        } catch (SQLException exc) {
            System.out.println(exc);
            System.out.println("Sorry, unable to delete course");
            return false;
        }
    }
    //    --------------------------------------------------------------------------------------------------
    public String userAdd(String role, List<String> val) {
        String id="";
            switch (role) {
                case "1": {
                    String name="",dept_id="",phone_number="",password;
                    int x=0;
                    for (String str:val) {
                        if(x==0)name=str;
                        if(x==1)dept_id=str;
                        if(x==2)phone_number=str;
                        x++;
                    }
                    if(dept_id.equals("")) {
                        System.out.println("Enter valid data");
                        return "failed!!";
                    }

                    try {
                        stmt = conn.createStatement();
                        try {
                            ResultSet rs;
                            ResultSetMetaData rsmd;
                            String q = "select count(*) from instructor where dept_id='" + dept_id + "';";
                            rs = stmt.executeQuery(q);
                            rsmd = rs.getMetaData();
                            int columnsNumber = rsmd.getColumnCount();
                            String responseQuery = "";
                            while (rs.next()) {
                                for (int i = 1; i <= columnsNumber; i++) {
                                    String columnValue = rs.getString(i);
                                    responseQuery += columnValue;
                                }

                            }
                            id = dept_id + responseQuery;
                            q = "insert into instructor(id,name,dept_id,email,password,phone_no,token) values('" + id + "','" + name + "','" + dept_id + "','" + id + "@iitrpr.ac.in','" + "not set currently','" + phone_number + "','');";
                            stmt.executeUpdate(q);
                            System.out.println("User added!");
                        } catch (SQLException e) {
                            System.out.println(e);
                            return "failed!!";
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }

                case "2": {
                    String entry_number="", name="", batch_id="", phone_number="",password;
                    int x=0;
                    for (String str:val) {
                        if(x==0)name=str;
                        if(x==1)batch_id=str;
                        if(x==2)phone_number=str;
                        x++;
                    }
                    if(batch_id.equals("")) {
                        System.out.println("Enter the valid data");
                        return "failed!!";
                    }

                    try {
                        stmt = conn.createStatement();
                        try {
                            ResultSet rs;
                            ResultSetMetaData rsmd;
                            String q = "select count(*) from student where batch_id='" + batch_id + "';";// all the students corresponding to same batch
                            rs = stmt.executeQuery(q);
                            rsmd = rs.getMetaData();// getting the data from the executed query
                            String responseQuery = "";
                            int columnsNumber = rsmd.getColumnCount();
                            while (rs.next()) {
                                for (int i = 1; i <= columnsNumber; i++) {
                                    String columnValue = rs.getString(i);
                                    responseQuery += columnValue;
                                }

                            }
                            entry_number = batch_id + responseQuery;
                            q = "insert into student(entry_number,name,batch_id,email,password,phone_no,credits,token) values('" + entry_number + "','" + name + "','" + batch_id + "','" + entry_number + "@iitrpr.ac.in','" + "not set currently','" + phone_number + "',0,'');";
                            stmt.executeUpdate(q);
                            System.out.println("User added");

                        } catch (SQLException exc) {
                            System.out.println(exc);
                            return "failed!!";
                        }
                    } catch (SQLException exc) {
                        System.out.println(exc);
                        return "failed!!";
                    }
                    break;
                }

                default:
                    System.out.println("Enter the valid role please!");
                    return "failed!!";

            }
            return id;
        }



//    -------------------------------------------------------------------------------------------

    public boolean recordAdd(String course_id, String course_type, String batch_id) {
        try {
            stmt = conn.createStatement();
            String q1 = "INSERT INTO record(course_id,batch_id,course_type) VALUES('" + course_id + "'," + "'" + batch_id + "'," + "'" + course_type + "');";
            int x = stmt.executeUpdate(q1);
            if (x > 0) {
                System.out.println("Record inserted successfully!");
                return true;
            }else{
                System.out.println("Record insertion failed");
                return false;}
        } catch (SQLException exc) {
            System.out.println(exc);
            System.out.println("Record insertion failed");
            return false;
        }
    }


//    ----------------------------------------------------------------------------------------------------------

    public boolean recordDelete(String course_id, String batch_id) {
        String q = "delete from record where course_id='" + course_id + "' and batch_id='" + batch_id + "';";
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(q);
            System.out.println("Record deleted successfully");
            return true;
        } catch (SQLException exc) {
            System.out.println(exc);
            System.out.println("Record deletion failed");
            return false;
        }
    }

//   ---------------------------------------------------------------------------------------------------------


    public boolean gradesShow(String student_id) {
        try {
            stmt = conn.createStatement();
            String q = "select * from grades where student_id='" + student_id + "';";//selecting the grades of student
            try {
                ResultSet rs;
                rs = stmt.executeQuery(q);
                while (rs.next()) {
                    String course_id = rs.getString("course_id");
                    String grade = rs.getString("grade");
                    String semester = rs.getString("semester");
                    String academic_year = rs.getString("academic_year");
                    System.out.print("student_id-->" + student_id + "    ");
                    System.out.print("course_id-->" + course_id + "    ");
                    System.out.print("grade-->" + grade + "    ");
                    System.out.print("semester-->" + student_id + "    ");
                    System.out.println("academic_year-->" + academic_year);

                }
            } catch (SQLException exc) {
                System.out.println(exc);
                System.out.println("Error occurred!");
                return false;
            }
        } catch (SQLException exc) {
            System.out.println(exc);
            System.out.println("Error occurred!");
            return false;
        }
        return true;
    }

//    -----------------------------------------------------------------------------------------------------


    public boolean userDelete(String role, String id) {
        String q = "";
        if (role.equals("1"))
            q = "delete from instructor where id='" + id + "';";
        else if (role.equals("2")) {
            q = "delete from student where entry_number='" + id + "';";
        }
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(q);
            System.out.println("deleted successfully");
            return true;
        } catch (SQLException exc) {
            System.out.println(exc);
            System.out.println("Unable to delete user");
            return false;
        }
    }

    //    ------------------------------------------------------------------------------------------
    public boolean catalogueAdd(String course_id) {
        try {
            ResultSet rs;
            stmt = conn.createStatement();

            rs = stmt.executeQuery("select *from semester");


        } catch (SQLException e) {
            System.out.println("semester is not yet started");// if nothing is found is semester table
            return false;
        }
        if (course_id.equals("0")) {
            return false;
        }
        try {
            stmt = conn.createStatement();
            String query = "INSERT INTO catalogue(course_id) VALUES('" + course_id + "');";
            try {
                stmt.executeUpdate(query);
                System.out.println("inserted successfully");
            } catch (SQLException exc) {
                System.out.println(exc);
                System.out.println("unable to insert");
                return false;
            }
        } catch (SQLException exc) {
            System.out.println(exc);
            System.out.println("unable to insert");
            return false;
        }
        return true;
    }

//    -----------------------------------------------------------------------------------------

    public String semStart(String academic_year, String semester) {
        try {
            ResultSet rs;
            stmt = conn.createStatement();

            rs = stmt.executeQuery("select *from semester;");
           // if rs!=NULL then semester is already running has to be taken care?
            return "Semester is currently running!!";
        } catch (SQLException e) {

        }

        String s1 = "CREATE TABLE semester(\n" +
                "academic_year VARCHAR(10),\n" +
                "semester VARCHAR(10)\n" +
                ");";
        String s2 = "CREATE TABLE catalogue(\n" +
                "course_id VARCHAR(5),\n" +
                "PRIMARY KEY(course_id),\n" +
                "FOREIGN KEY (course_id) references course (id)\n" +
                ");";
        String s3 = "CREATE TABLE offering_criteria(\n" +
                "course_id VARCHAR(5),\n" +
                "cgpa_limit INTEGER,\n" +
                "instructor_id CHAR(11),\n" +
                "PRIMARY KEY(course_id),\n" +
                "FOREIGN KEY (course_id) references catalogue (course_id),\n" +
                "FOREIGN KEY (instructor_id) references instructor (id)\n" +
                "\n" +
                ");";
        String s="update student set credits=0";
        String s4 = "CREATE TABLE registration_status(\n" +
                "course_id VARCHAR(5),\n" +
                "student_id CHAR(11),\n" +
                "instructor_id CHAR(11),\n" +
                "status VARCHAR(100),\n" +
                "FOREIGN KEY (course_id) references offering_criteria (course_id),\n" +
                "FOREIGN KEY (student_id) references student (entry_number),\n" +
                "FOREIGN KEY (instructor_id) references instructor (id)\n" +
                ");";

        try {
            stmt = conn.createStatement();
            try {
                stmt.execute(s1);
                stmt.execute(s2);
                stmt.execute(s3);
                stmt.execute(s4);
                String q = "insert into semester(academic_year,semester) values('" + academic_year + "','" + semester + "');";
                stmt.executeUpdate(q);
                return academic_year + "--" + semester + " started successfully!!";
            } catch (SQLException exc) {
                System.out.println(exc);
                return "unable to start semester";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


//    -----------------------------------------------------------------------------------------------

    public boolean semEnd() {
        String academic_year = "";
        String semester = "";
        String q = "select *from semester;";
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(q);
            while (rs.next()) {
                academic_year = rs.getString("academic_year");
                semester = rs.getString("semester");
            }
        } catch (SQLException e) {
            System.out.println("No semester is currently running");
            return false;
        }
        try {
            q = "drop table semester;";
            stmt.execute(q);
            String s5 = "drop table catalogue;";
            String s6 = "drop table offering_criteria;";
            String s7 = "drop table registration_status;";
            stmt.execute(s7);
            stmt.execute(s6);
            stmt.execute(s5);
        } catch (SQLException e) {
            System.out.println("unable to end sem");
            throw new RuntimeException(e);
        }
        System.out.println(academic_year + "-" + semester + " ended successfully");
        return true;
    }

    //-----------------------------------------------------------------------------------------
    public boolean catalogueDelete(String course_id) {
        String q = "delete from catalogue where course_id='" + course_id + "';";
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(q);
            System.out.println("deleted from catalogue");
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("unable to delete");
            return false;
        }
    }

//---------------------------------------------------------------------------------------------------------------


    public boolean usersView(String x) {

        String q = "select * from instructor;";
        switch (x) {
            case "1": {
                try {
                    stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(q);
                    while (rs.next()) {
                        String id = rs.getString("id");
                        String name = rs.getString("name");
                        String email = rs.getString("email");
                        String dept_id = rs.getString("dept_id");
                        String phone_number = rs.getString(("phone_no"));
                        System.out.print("id-->" + id + "    ");
                        System.out.print("name-->" + name + "    ");
                        System.out.print("email-->" + email + "    ");
                        System.out.print("dept_id-->" + dept_id + "    ");
                        System.out.println("phone_number-->" + phone_number);
                    }

                } catch (SQLException e) {
                    System.out.println(e);
                    System.out.println("unable  to view");
                    return false;
                }
                break;
            }
            case "2": {
                try {
                    q = "select * from student;";
                    stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(q);
                    while (rs.next()) {
                        String student_id = rs.getString("entry_number");
                        String name = rs.getString("name");
                        String batch_id = rs.getString("batch_id");
                        String email = rs.getString("email");
                        String phone_number = rs.getString(("phone_no"));
                        System.out.print("student_id-->" + student_id + "    ");
                        System.out.print("name-->" + name + "    ");
                        System.out.print("batch_id-->" + batch_id + "    ");
                        System.out.print("email-->" + email + "    ");
                        System.out.println("phone_no-->" + phone_number);
                    }
                } catch (SQLException e) {
                    System.out.println(e);
                    System.out.println("unable to view");
                    return false;
                }
                break;
            }
            default: {
                System.out.println("Incorrect role entered!!");
                break;
            }
        }
        return true;
    }
//-----------------------------------------------------------------------


    public boolean coursesView() {
        String q = "select * from course;";
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(q);
            while (rs.next()) {
                String course_id = rs.getString("id");
                String name = rs.getString("name");
                int l = rs.getInt("l");
                int t = rs.getInt("t");
                int p = rs.getInt(("p"));
                int c = rs.getInt("c");
                String dept_id = rs.getString("dept_id");
                System.out.print("course_id-->" + course_id + "    ");
                System.out.print("name-->" + name + "    ");
                System.out.print("l-->" + l + "    ");
                System.out.print("t-->" + t + "    ");
                System.out.print("p-->" + p + "    ");
                System.out.println("dept_id-->" + dept_id);
            }
        } catch (SQLException e) {
            System.out.println("No course to view!!");
            return false;
        }
        return true;
    }

//--------------------------------------------------------------------------------------------

    public static String semView() {
        String q = "select *from semester;";
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(q);
            String academic_year = null;
            String semester = null;
            while (rs.next()) {
                academic_year = rs.getString("academic_year");
                semester = rs.getString("semester");
            }
            String s = academic_year + "-" + semester + " sem";
            return s;
        } catch (SQLException e) {
            return "Currently no semester is running!!";
        }
    }

    //--------------------------------------------------------------------------------------------------
    public boolean transcriptSubmit(String student_id) {
        File file = new File("src/main/resources/transcript.txt");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException exc) {
            System.out.println(exc);
            return false;
        }
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("INSERT INTO transcript VALUES (?, ?)");
        } catch (SQLException exc) {
            System.out.println(exc);
            return false;
        }

        try {
            ps.setString(1, student_id);
            ps.setBinaryStream(2, fis, file.length());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException exc) {
            System.out.println(exc);
            return false;
        }

        try {
            fis.close();
        } catch (IOException exc) {
            System.out.println(exc);
            return false;
        }
        return true;
    }


//    ----------------------------------------------------------------------------------------------------------

    public boolean transcriptView(String student_id) {
        String transcript = "";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("SELECT transcript FROM transcript WHERE student_id = ?");
        } catch (SQLException exc) {
            System.out.println(exc);
            return false;
        }
        try {
            ps.setString(1, student_id);
        } catch (SQLException exc) {
            System.out.println(exc);
            return false;
        }
        ResultSet rs = null;
        try {
            rs = ps.executeQuery();
        } catch (SQLException exc) {
            System.out.println(exc);
            return false;
        }
        if (rs != null) {
            while (true) {
                try {
                    if (!rs.next()) break;
                } catch (SQLException exc) {
                    System.out.println(exc);
                    return false;
                }
                try {
                    byte[] imgBytes = rs.getBytes(1);
                    transcript = new String(imgBytes);
                } catch (SQLException exc) {
                    System.out.println(exc);
                    return false;
                }
                // use the data in some way here
            }
            try {
                rs.close();
            } catch (SQLException exc) {
                System.out.println(exc);
                return false;
            }
        } else {
            System.out.println("There is no transcript for this student");
        }
        try {
            ps.close();
        } catch (SQLException exc) {
            System.out.println(exc);
            return false;
        }
        System.out.println(transcript);
        return true;
    }

    public boolean transcriptDelete(String student_id){
        String q="delete from transcript where student_id='"+student_id+"';";
        try {
            stmt= conn.createStatement();
            stmt.executeUpdate(q);
            System.out.println("transcript deleted successfully");
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("unable to delete transcript");
            return false;
        }
    }
}


