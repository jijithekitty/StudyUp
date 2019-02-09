package edu.studyup.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import edu.studyup.entity.Event;
import edu.studyup.entity.Location;
import edu.studyup.entity.Student;
import edu.studyup.util.DataStorage;
import edu.studyup.util.StudyUpException;

class EventServiceImplTest {

	EventServiceImpl eventServiceImpl;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		eventServiceImpl = new EventServiceImpl();
		//Create Student
		Student student = new Student();
		student.setFirstName("John");
		student.setLastName("Doe");
		student.setEmail("JohnDoe@email.com");
		student.setId(1);
		
		//Create Event1
		Event event = new Event();
		event.setEventID(1);
		event.setDate(new Date());
		event.setName("Event 1");
		Location location = new Location(-122, 37);
		event.setLocation(location);
		List<Student> eventStudents = new ArrayList<>();
		eventStudents.add(student);
		event.setStudents(eventStudents);
		
		DataStorage.eventData.put(event.getEventID(), event);
	}

	@AfterEach
	void tearDown() throws Exception {
		DataStorage.eventData.clear();
	}

	@Test
	void testUpdateEventName_GoodCase() throws StudyUpException {
		int eventID = 1;
		eventServiceImpl.updateEventName(eventID, "Renamed Event 1");
		assertEquals("Renamed Event 1", DataStorage.eventData.get(eventID).getName());
	}
	
	
	@Test
	void testUpdateEvent_WrongEventID_badCase() {
		int eventID = 3;
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.updateEventName(eventID, "Renamed Event 3");
		  });
	}
	
	//Our written test cases
	//test 1
	//assert that string length is less than or equal to 20 characters
	@Test
	void testEventNameLengthIsLessThanMaximum()throws StudyUpException {
		int eventID = 1;
		//update event name, should be less than 20 characters
		eventServiceImpl.updateEventName(eventID, "Testing");
		String test = "";
		test = DataStorage.eventData.get(eventID).getName();
		assertTrue(test.length() <= 20);
	}
	
	//test2 
	//check that an exception was thrown for a string length exceeding 20 characters
	@Test
	void testUpdateEventNameExceedsMaximum() throws StudyUpException {
		//test to see if exception is thrown
		int eventID = 1;
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.updateEventName(eventID, "Very Long Event Name That Should Throw An Error For Being Too Long");
		});
	}
	
	//test3
	//get an events location and assert it is not null
	@Test
	void getLocationOfAnEvent_GoodCase() throws StudyUpException{
		int eventID = 1;
		assertNotNull(DataStorage.eventData.get(eventID).getLocation());
	}
	
	//test4
	//get an events date and assert it is not null
	@Test
	void getDateOfEvent_GoodCase() throws StudyUpException{
		int eventID = 1;
		assertNotNull(DataStorage.eventData.get(eventID).getDate());
	}
	
	//test5
	//get a list of students for an event, assert it is not null
	@Test
	void getListOfStudents_GoodCase() throws StudyUpException{
		int eventID = 1;
		List<Student> currentStudents = DataStorage.eventData.get(eventID).getStudents();
		assertNotNull(currentStudents);
	}
	
	//test6
	//get list of active events, assert it is not null
	@Test
	void getActiveEvents_GoodCase() throws StudyUpException{
		List<Event> activeEvents = eventServiceImpl.getActiveEvents();
		assertNotNull(activeEvents);
	}
	
	//test7
	//get list of past events assert it is not null
	@Test
	void getPastEvents_GoodCase() throws StudyUpException{
		//get current list of past events
		List<Event> pastEvents = eventServiceImpl.getPastEvents();	
		assertNotNull(pastEvents);
		
		//create past event
		//Create Event with date in the past
		int eventID = 2;
		Event event = new Event();
		event.setEventID(eventID);
		event.setDate(new Date(100));
		event.setName("Event 2");
		Location location = new Location(122, 40);
		event.setLocation(location);
		DataStorage.eventData.put(event.getEventID(), event);
		
		//get past events, assert not empty
		pastEvents = eventServiceImpl.getPastEvents();	
		assertNotNull(pastEvents);
		assertNotNull(DataStorage.eventData.get(eventID).getDate());
		
	}
	
	//test8
	//add student to an event list, assert student was inserted by comparing lengths
	//new length of student list from getStudents() should be larger
	@Test
	void addStudentToEvent_GoodCase() throws StudyUpException{
		int eventID = 1;
		Event event = DataStorage.eventData.get(eventID);
		List<Student> currentStudents = event.getStudents();
		int currentStudentList = currentStudents.size();
		
		//add new student to list
		Student student = new Student();
		student.setFirstName("John");
		student.setLastName("Smith");
		student.setEmail("johnsmith123@gmail.com");
		student.setId(2);
		
		//test creating student as well
		assertNotNull(student.getFirstName());
		assertNotNull(student.getLastName());
		assertNotNull(student.getEmail());
		assertNotNull(student.getId());
		
		//add student to event, ensure student list is larger than previous student list
		event = eventServiceImpl.addStudentToEvent(student, eventID);
		List<Student> newStudents = event.getStudents();
		int newStudentListSize = newStudents.size();
		assertTrue(currentStudentList < newStudentListSize);
	}
	
	//test9
	//assert an exception was thrown by adding a student to a non existent event
	@Test
	void addingStudentToNonExistentEvent() throws StudyUpException{
		//non existing event id
		int eventID = 101;
		//add new student to list
		Student student = new Student();
		student.setFirstName("John");
		student.setLastName("Smith");
		student.setEmail("johnsmith123@gmail.com");
		student.setId(2);
		
		//assert an exception is thrown
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.addStudentToEvent(student, eventID);
		 });
	}
	
	//test 10
	//delete student based on ID, assert the student deleted is null
	@Test
	void deleteEvent_GoodCase() throws StudyUpException{
		int eventID = 1;
		Event event = eventServiceImpl.deleteEvent(eventID);
		assertNull(DataStorage.eventData.get(eventID));
	}
	
	//test 11
	//add student to an event with no students currently added
	@Test
	void addStudentWhenStudentListIsEmpty() throws StudyUpException{
		//create new event with no student list
		//Create Event
		int eventID = 2;
		Event event = new Event();
		event.setEventID(eventID);
		event.setDate(new Date());
		event.setName("Event 2");
		Location location = new Location(122, 40);
		event.setLocation(location);
		DataStorage.eventData.put(event.getEventID(), event);
		
		//assert no students are present
		System.out.println("is students null");
		System.out.println(event.getStudents());
		assertNull(event.getStudents());
		
		//Create New Student
		//add new student to list
		Student student = new Student();
		student.setFirstName("John");
		student.setLastName("Smith");
		student.setEmail("johnsmith@gmail.com");
		student.setId(2);
		
		//add student, assert student list is not null after adding
		event = eventServiceImpl.addStudentToEvent(student, eventID);
		assertNotNull(event.getStudents());
	}
	

}
