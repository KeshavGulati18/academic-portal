package org.example;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

class adminTest {
admin x=new admin();//creating new object of admin type
    @BeforeAll
    @Test
    void login() {
        assertTrue(x.login("admin","iitropar"));
    }

    @AfterAll
    @Test
    void logout() {
        x.logout();
        assertFalse(x.exist);
    }

    @Test
    void batchAdd() {
        assertTrue(x.batchAdd("2019eeb", "2019", "EE"));
        x.batchDelete("2019eeb");
    }

    @Test
    void batchDelete() {
        x.batchAdd("2021csb", "2021","CS" );
        assertTrue(x.batchDelete("2021csb"));
    }

    @Test
    void addcourse() {

        List<String> prereq=new ArrayList<String>();
        boolean f1=x.courseAdd("CS308","core",3,3,3,3,"CS",prereq);
        boolean f2=x.courseAdd("CS309","core",3,3,3,4,"CS",prereq);
        assertTrue(f1);
        assertTrue(f2);
        x.courseDelete("CS308");
        x.courseDelete("CS309");
    }

    @Test
    void courseDelete() {
        List<String> preq= new ArrayList<String>();
        preq.add("CS301");
        x.courseAdd("CS306", "softe", 3, 3, 3, 4, "CS",preq );
        assertTrue(x.courseDelete("CS305"));
    }

    @Test
    void userAdd() {
        List<String> val1=new ArrayList<String>();
        val1.add("def");
        val1.add("CS");
        val1.add("9812498128");

        List<String> val2=new ArrayList<String>();
        val2.add("abc");
        val2.add("2020csb");
        val2.add("8607067771");
        assertNotEquals(x.userAdd("1",val1), "failed!!");
        assertNotEquals(x.userAdd("2",val2), "failed!!");
        x.userDelete("1",x.userAdd("1",val1));
        x.userDelete("2",x.userAdd("2",val2));
    }

    @Test
    void recordAdd() {
        assertTrue(x.recordAdd("CS304", "core", "2020csb"));
        x.recordDelete("CS304", "2020csb");
    }

    @Test
    void recordDelete() {
        x.recordAdd("EE111", "elective", "2020csb");
        assertTrue(x.recordDelete("EE111", "2020csb"));
    }

    @Test
    void gradesShow() {
        assertTrue(x.gradesShow("2020csb1110"));
    }

    @Test
    void userDelete() {
        List<String> a=new ArrayList<String>();
        List<String> b=new ArrayList<String>();
        a.add("abc");
        a.add("2020csb");
        a.add("8607067771");
        b.add("def");
        b.add("CS");
        b.add("9812498128");
        String ins_id=x.userAdd("1", b);
        String stu_id=x.userAdd("2",a);

        assertTrue(x.userDelete("1",ins_id));
        assertTrue(x.userDelete("2",stu_id));
    }

    @Test
    void catalogueAdd() {
        if(x.semView().equals("Currently no semester is running!!")){
            x.semStart("2024","monsoon");
            assertTrue(x.catalogueAdd("CS306"));
            assertTrue(x.catalogueDelete("CS306"));
            x.semEnd();
        }
        else{
            assertTrue(x.catalogueAdd("CS306"));
            assertTrue(x.catalogueDelete("CS306"));
        }
    }

    @Test
    void semStart() {
        String str=x.semView();
        String s=x.semStart("2023","winter");
        if(str.equals("Currently no semester is running!!")){
            assertNotEquals(s,"Semester is currently running!!");
            boolean b=x.semEnd();
            assertEquals(b,true);
        }
        else{
            assertEquals(s,"Semester is currently running!!");
        }
    }

    @Test
    void semEnd() {
        String str=x.semView();;
        if(str.equals("Currently no semester is running!!")){
            assertEquals(x.semEnd(),false);
        }
        else{
            assertEquals(x.semEnd(),true);

        }
    }

    @Test
    void catalogueDelete() {
        x.semStart("2023", "monsoon");
        x.catalogueAdd("CS301");
        boolean a=x.catalogueDelete("CS301");
        assertEquals(a,true);
        x.semEnd();
    }

    @Test
    void usersView() {
        assertEquals(x.usersView("1"),true);
        assertEquals(x.usersView("2"),true);
    }

    @Test
    void coursesView() {
        assertEquals(x.coursesView(),true);
    }

    @Test
    void semesterView() {
        String s=x.semStart("2023", "monsoon");
        String str=x.semView();
        if(s.equals("Semester is currently running")){
            assertNotEquals(str,"Currently no semester is running!!");
        }
    }

    @Test
    void transcriptSubmit() {
        boolean a= x.transcriptSubmit("2020csb0");
        assertEquals(a,true);
        boolean b=x.transcriptDelete("2020csb0");
        assertEquals(b,true);
    }

    @Test
    void transcriptView() {
        boolean a=x.transcriptView("2020csb0");
        assertEquals(a,true);
    }

    @Test
    void transcriptDelete() {
        x.transcriptSubmit("2020csb0");
        boolean a=x.transcriptDelete("2020csb0");
        assertEquals(a,true);
    }
}