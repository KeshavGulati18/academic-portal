
package org.example;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

class studentTest {
    student x = new student();
    static Connection conn = connect_asap.ConnectDB();
    static Statement stmt = null;

    @BeforeAll
    @Test
    void login() {
        assertTrue(x.login("a@b", "1212"));
    }

    @AfterAll
    @Test
    void logout() {
        x.logout();
        assertFalse(x.exist);
    }

    @Test
    void profileView() {
        boolean f = x.profileView();
        assertTrue(f);
    }

    @Test
    void profileUpdate() {
        boolean f=x.profileUpdate("me", "1212", "8607067771");
        assertTrue(f);
    }

    @Test
    void canAddcourse() {// wrt cg limit

        instructor i = new instructor();
        admin a = new admin();
        if (a.semView().equals("Currently no semester is running!!")) {

        } else {
            a.semEnd();
        }
        a.semStart("2020", "winter");
        List<String> val = new ArrayList<String>();
        a.catalogueAdd("CS304");

        i.login("k@g", "1234");
        i.courseAdd("CS304", 1);
        i.logout();
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate("insert into grades(student_id,course_id,grade,semester,academic_year) values ('11111111111','CS306','A','monsoon','2019')");
            stmt.executeUpdate("insert into grades(student_id,course_id,grade,semester,academic_year) values ('11111111111','CS307','A','winter','2019')");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        boolean f = x.courseAdd("CS304");
        assertTrue(f);
        a.catalogueDelete("CS304");
        a.semEnd();
        try {
            stmt.executeUpdate("delete from grades where student_id='11111111111'");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    void myCourses() {
        admin a = new admin();
        if (a.semView().equals("Currently no semester is running!!")) {
            assertEquals(x.myCourses(), "Error!!");
        } else {
            assertNotEquals(x.myCourses(), "Error!!");
        }
    }


    @Test
    void courseDelete() {
        if (admin.semView().equals("Currently no semester is running!!")) {
            boolean f = x.courseDelete("CS304");
            assertFalse(f);
        } else {
            instructor i = new instructor();
            i.login("k@g", "1234");
            i.courseAdd("CS304", 1);
            assertTrue(x.courseAdd("CS304"));
            boolean f = x.courseDelete("CS304");
            assertTrue(f);
        }
    }

    @Test
    void gradesShow() {
        assertEquals(x.gradesShow(), "No grades to show yet");
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate("insert into grades(student_id,course_id,grade,semester,academic_year) values ('11111111111','CS306','B','winter','2020')");
            stmt.executeUpdate("insert into grades(student_id,course_id,grade,semester,academic_year) values ('11111111111','CS307','A','monsoon','2018')");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        assertNotEquals(x.gradesShow(), "No grades to show yet");
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate("delete from grades where student_id='11111111111'");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getCgpa() {
        assertEquals(x.getCgpa(), 0.0);
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate("insert into grades(student_id,course_id,grade,semester,academic_year) values ('11111111111','CS306','A','winter','2020')");
            stmt.executeUpdate("insert into grades(student_id,course_id,grade,semester,academic_year) values ('11111111111','CS307','A','monsoon','2018')");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        assertEquals(x.getCgpa(), 2.5);
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate("delete from grades where student_id='11111111111'");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void checkGrad() {
        assertTrue(x.checkGrad());// initially
        admin a = new admin();
        a.semStart("2020", "monsoon");
        a.recordAdd("CS304", "core", "2020csb");
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate("insert into grades(student_id,course_id,grade,semester,academic_year) values ('11111111111','CS304','F','winter','2020')");

        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("Error!!");
        }

        assertFalse(x.checkGrad());
        a.logout();
        try {
            stmt.executeUpdate("delete from grades where student_id='11111111111'");
            stmt.executeUpdate("delete from record where course_id='CS304'");
            a.semEnd();
        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("Error!!!");
        }

    }

    @Test
    void gtt() {
        assertFalse(x.gtt());
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate("insert into grades(student_id,course_id,grade,semester,academic_year) values ('11111111111','CS306','A','winter','2019')");
            stmt.executeUpdate("insert into grades(student_id,course_id,grade,semester,academic_year) values ('11111111111','CS307','A','winter','2018')");
            stmt.executeUpdate("insert into grades(student_id,course_id,grade,semester,academic_year) values ('11111111111','CS304','B','winter','2017')");
        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("Error!!");
        }
        assertTrue(x.gtt());

        try {
            stmt.executeUpdate("delete from grades where student_id='11111111111'");

        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("Error!!!");
        }

    }

}