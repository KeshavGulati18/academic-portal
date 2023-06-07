package org.example;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class instructorTest {
    instructor x=new instructor();
    @BeforeAll
    @Test
    void login() {
        boolean f=x.login("k@g","1234");
        assertTrue(f);
    }



    @Test
    void profileView() {
        String s=x.profileView();
        assertNotEquals(s,"Error!");
    }

    @Test
    void profileUpdate() {
        instructor i=new instructor();
        i.login("s@s", "1234");
        boolean a=i.profileUpdate("xyz", "8607067772", "1234");
        assertTrue(a);
        i.logout();
    }

    @Test
    void courseAdd() {
        admin a=new admin();
        if(a.semView().equals("Currently no semester is running!!")){
            boolean f=x.courseAdd("CS301",4);
            assertFalse(f);
        }
        else{
            a.semEnd();
        }
        a.semStart("2023","monsoon");
        a.catalogueAdd("CS304");
        boolean f=x.courseAdd("CS304",4);
        assertTrue(f);
        a.semEnd();
    }
    @Test
    void coursesOffered() {
        admin a=new admin();
        if(a.semView().equals("Currently no semester is running!!")){
            boolean f=x.coursesOffered();
            assertFalse(f);
        }
        else{
            a.semEnd();
        }
        a.semStart("2024","winter");
        a.catalogueAdd("CS304");
        boolean f=x.coursesOffered();
        assertTrue(f);
        a.catalogueDelete("CS304");
        a.semEnd();
    }

    @Test
    void myCourses() {
        admin a=new admin();
        if(admin.semView().equals("Currently no semester is  running!!")){
            assertEquals(x.myCourses(),"Error!!");
        }
        else{
            a.semEnd();
        }
        a.semStart("2025","monsoon");
        a.catalogueAdd("CS304");
        assertEquals(x.myCourses(),"You have not offered any course!");
        boolean f=x.courseAdd("CS304",3);
        assertTrue(f);
        assertNotEquals(x.myCourses(),"You have not offered any course!");
        a.catalogueDelete("CS304");
        a.semEnd();
    }

    @Test
    void courseDelete() {
        admin a=new admin();
        if(a.semView().equals("Currently no semester is running!!")){
            boolean f=x.courseDelete("CS304");
            assertFalse(f);
        }
        else{
            a.semEnd();
        }
        a.semStart("2024","monsoon");
        a.catalogueAdd("CS304");
        boolean f=x.courseAdd("CS304", 3);
        assertTrue(f);
        boolean t=x.courseDelete("CS304");
        assertTrue(t);
        a.catalogueDelete("CS304");
        a.semEnd();
    }

    @Test
    void gradesShow() {
        boolean f=x.gradesShow();
        assertTrue(f);
    }

    @Test
    void enrollmentRequests() {
        admin a=new admin();

        if(a.semView().equals("Currently no semester is running!!")){
            assertEquals(x.enrollmentRequests(),"Error!!");
        }
        else{
            a.semEnd();
        }
        a.semStart("2024","monsoon");
        a.catalogueAdd("CS304");
        x.courseAdd("CS304",1);
//        assertEquals(x.enrollmentRequests(),"No enrollment requests!");
        student s=new student();
        assertTrue(s.login("a@b","1212"));
//        s.courseAdd("CS304");
//        assertTrue(s.logout());
        assertEquals(x.enrollmentRequests(),"No enrollment requests!");
        assertTrue(x.courseDelete("CS304"));
        a.catalogueDelete("CS304");
        a.semEnd();
    }

    @Test
    void AorD() {
        admin a=new admin();

        if(admin.semView().equals("Currently no semester is running!!")){
            boolean x1=x.AorD("CS304", "11111111111", "1");
            boolean x2=x.AorD("CS304", "11111111111", "2");
            assertFalse(x1);
            assertFalse(x2);
        }
        else{
            a.semEnd();
        }
        a.semStart("2025","monsoon");
        a.catalogueAdd("CS304");
        x.courseAdd("CS304",0);
        student s=new student();
        assertTrue(s.login("a@b","1212"));
        s.courseAdd("CS304");
        boolean x1=x.AorD("CS304", "11111111111", "1");
        boolean x2=x.AorD("CS301", "11111111111", "2");
        assertTrue(x1);
        assertTrue(x2);
        s.logout();
        assertTrue(x.courseDelete("CS304"));
        a.catalogueDelete("CS304");
        a.semEnd(); }

    @Test
    void gradesSubmit() {
        admin a=new admin();
        if(admin.semView().equals("Currently no semester is running!!")){
            boolean f=x.gradesSubmit();
            assertFalse(f);}
        else{
            a.semEnd();
        }
        a.semStart("2020","winter");
        boolean f=x.gradesSubmit();
        assertTrue(f);
        a.semEnd();  }

    @AfterAll
    @Test
    void logout() {
        instructor i=new instructor();
        x.logout();
        assertFalse(x.exist);
    }
}