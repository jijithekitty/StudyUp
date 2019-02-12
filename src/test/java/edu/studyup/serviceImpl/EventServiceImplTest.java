package edu.studyup.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;

//ECS 161 HW 2
//Partners:
//Alex Hong 
//Jenny Nguyen


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

		
		//create event2
		//future data
		Event eventFuture = new Event();
		eventFuture.setEventID(2);
		eventFuture.setDate(new Date(9999999999999L)); //creates a date in the future
		eventFuture.setName("Event 2");
		Location location2 = new Location(122, 40);
		eventFuture.setLocation(location2);
		DataStorage.eventData.put(eventFuture.getEventID(), eventFuture);		
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
	void testUpdateEvent_WrongEventID_badCase() throws StudyUpException{
		int eventID = 3;
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.updateEventName(eventID, "Renamed Event 3");
		  });
	}
	
	//Our written test cases
	//assert that string length is less than or equal to 20 characters
	@Test
	void testEventNameLengthIsLessThanMaximum()throws StudyUpException {
		Event event = new Event();
		event.setEventID(1);
		event.setDate(new Date());
		event.setName("Event 1");
		DataStorage.eventData.put(event.getEventID(), event);
		
		//update event name, should be less than 20 characters
		eventServiceImpl.updateEventName(1, "Testing");
	}
	
	//check that an exception was thrown for a string length exceeding 20 characters
	@Test
	void testUpdateEventNameExceedsMaximum() throws StudyUpException {
		//test to see if exception is thrown
		int eventID = 1;
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.updateEventName(eventID, "Very Long Event Name That Should Throw An Error For Being Too Long");
		});
	}
	
	//testing event name to be maximum length
	//bug found, exception should be thrown for trying to set empty string
	@Test
	void testUpdateEventNameLengthIsTwenty() throws StudyUpException{
		//test to see if exception is thrown
		int eventID = 1;
		
		//check if an exception is thrown for empty name for event
		//BUG FOUND, maximum event name is 20 characters, but exception is thrown for length at 20
		eventServiceImpl.updateEventName(eventID, "abcdefghijklmnopqrst");
	}
	
	//get an events location and assert it is not null
	@Test
	void getLocationOfAnEvent_GoodCase() throws StudyUpException{
		int eventID = 1;
		assertNotNull(DataStorage.eventData.get(eventID).getLocation());
	}
	
	//get an events date and assert it is not null
	//if it is not null, the date for the event exists
	@Test
	void getDateOfEvent_GoodCase() throws StudyUpException{
		int eventID = 1;
		assertNotNull(DataStorage.eventData.get(eventID).getDate());
	}
	
	//get a list of students for an event, assert it is not null
	@Test
	void getListOfStudents_GoodCase() throws StudyUpException{
		int eventID = 1;
		
		//create student, check student exists in student list
		Student student = new Student();
		student.setFirstName("John");
		student.setLastName("Doe");
		student.setEmail("JohnDoe@email.com");
		student.setId(1);
		
		//add student to list
		eventServiceImpl.addStudentToEvent(student, eventID);
		
		//get list of current students
		List<Student> currentStudents = DataStorage.eventData.get(eventID).getStudents();
		assertNotNull(currentStudents);
		
		//check that a student exists in the list
		assertTrue(currentStudents.contains(student));
	}
	
	//get list of active events, assert it is not null
	@Test
	void getActiveEvents_GoodCase() throws StudyUpException{
			
		//create event
		Event event = new Event();
		event.setEventID(1);
		event.setDate(new Date());
		event.setName("Event 1");
	
		//add event to active events
		//get list of active events
		DataStorage.eventData.put(event.getEventID(), event);
		List<Event> activeEvents = eventServiceImpl.getActiveEvents();
		assertNotNull(activeEvents);
		
		//assert event is contained in active events
		assertTrue(activeEvents.contains(event));
	}
	
	//check to see future date is in active event
	@Test 
	void checkActiveEventsHasFutureDate() throws StudyUpException{
		int eventID = 2;
		Event futureEvent = DataStorage.eventData.get(eventID);
		System.out.println(futureEvent);
		List <Event> activeEvents = eventServiceImpl.getActiveEvents();
		assertTrue(activeEvents.contains(futureEvent));
	}
	
	//check if active event has past date
	//bug found, active event contains past date
	@Test
	void checkActiveEventsForPastDate() throws StudyUpException {
		Event pastEvent = new Event();
		pastEvent.setEventID(1);
		pastEvent.setDate(new Date(100));
		pastEvent.setName("Past Event");
		System.out.println(pastEvent.getDate());
		
		DataStorage.eventData.put(pastEvent.getEventID(), pastEvent);
		
		//get list of active events
		//check to see if past date is present in active events list 
		List<Event> activeEvents = eventServiceImpl.getActiveEvents();
		
		//check that there are no past events in active events list
		for(int i = 0; i < activeEvents.size(); i++) {
			System.out.println(activeEvents.get(i).getDate());
		}
		
		//bug found, past event was found in active event
		assertFalse(activeEvents.contains(pastEvent));
	}
	
	
	
	//get list of past events assert it is not null
	@Test
	void getPastEvents_GoodCase() throws StudyUpException{
		//get current list of past events
		List<Event> pastEvents = eventServiceImpl.getPastEvents();	
		assertNotNull(pastEvents);
		
		//create past event
		//Create Event with date in the future
		int eventID = 2;
	
		//create event in the past
		int newEventID = 3;
		Event eventPast = new Event();
		eventPast.setEventID(newEventID);
		eventPast.setDate(new Date(100)); //creates a date in the past, 1960s
		eventPast.setName("Event 3");
		Location location2 = new Location(122, 40);
		eventPast.setLocation(location2);
		DataStorage.eventData.put(eventPast.getEventID(), eventPast);
		
		//get past events, assert not empty
		pastEvents = eventServiceImpl.getPastEvents();
		assertNotNull(pastEvents);
		
		//make sure event in the past exists in pastEvents
		assertTrue(pastEvents.contains(eventPast));
		
		//make sure the event in the future is not in the pastEvents list
		assertFalse(pastEvents.contains(DataStorage.eventData.get(eventID)));
	}
		
	//add student to an event list, assert student was inserted by comparing lengths
	//new length of student list from getStudents() should be larger
	@Test
	void addStudentToEvent_GoodCase() throws StudyUpException{
		int eventID = 1;
		Event event = DataStorage.eventData.get(eventID);
		
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
		
		//make sure student added exists in student list
		assertTrue(newStudents.contains(student));
	}
	
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
	
	//add same student to same event multiple times
	//bug found, student shouldn't able to be added again to an event if already added
	@Test
	void addSameStudentToEvent_BadCase() throws StudyUpException{
		int eventID = 1;
		Event event = DataStorage.eventData.get(eventID);
		
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
		
		//add student to event
		event = eventServiceImpl.addStudentToEvent(student, eventID);
		
		//add student again
		//assertion should be thrown for adding same student to event
		//bug found, doesn't check if student already exists in list
		//able to add same student to an event multiple times
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.addStudentToEvent(student,eventID);
		});
	}
	
	//delete student based on ID, assert the student deleted is null
	@Test
	void deleteEvent_GoodCase() throws StudyUpException{
		int eventID = 1;
		Event event = eventServiceImpl.deleteEvent(eventID);
		assertNull(DataStorage.eventData.get(eventID));
		//ensure event deleted is not present, false
		assertFalse(eventServiceImpl.getActiveEvents().contains(event));
	}
	
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
		assertTrue(event.getStudents().contains(student));
	}
	
}