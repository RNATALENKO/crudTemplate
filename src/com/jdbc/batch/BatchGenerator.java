package com.jdbc.batch;

import com.jdbc.dao.StudentDaoImpl;
import com.jdbc.pojo.Student;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("Batch") //put it into the context containers
public class BatchGenerator {
	
	@Autowired
	StudentDaoImpl studentDaoImpl; 
	
	public static List<Student> createBatch() {
		
		
		ArrayList<Student> batchList = new ArrayList<Student>(); 
		
		//create a batch of student objects
		Student studentOne = new Student();
		Student studentTwo = new Student();
		Student studentThree = new Student();
	
		
		//set the data
		studentOne.setRollNo(1);
		studentOne.setName("Bob");
		studentOne.setAddress("Seattle");
		studentTwo.setRollNo(2);
		studentTwo.setName("Rod");
		studentTwo.setAddress("Seattle");
		studentThree.setRollNo(3);
		studentThree.setName("Susan");
		studentThree.setAddress("Renton");
		
		
		//add them to a list
		batchList.add(studentOne);
		batchList.add(studentTwo);
		batchList.add(studentThree);
		
		//return a list
		return batchList;
		
		
	}
	
	public void setupStudentTable() {
		
		studentDaoImpl.batchInsert(createBatch());
	}
	
	public void printStudentList(List<Student> studentList) {
		
		for(Student tempStudent : studentList) {
			System.out.println(tempStudent);
		}
		
	}

}
