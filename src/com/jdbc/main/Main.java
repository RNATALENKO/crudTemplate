package com.jdbc.main;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.jdbc.batch.BatchGenerator;
import com.jdbc.dao.StudentDao;
import com.jdbc.dao.StudentDaoImpl;
import com.jdbc.pojo.Student;


/*
 * 
 * this configuration converts beans into annotations
 */

public class Main {

	public static void main(String[] args) {
		
		
		
		
		//retrieve beans xml and store in context 
		ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
		System.out.println("number of beans defined: " + context.getBeanDefinitionCount()); //get number of beans defined
		
		
		/*
		 * delete and insert records
		 */
		
		//set pojo details
		Student studentOne = new Student();
		//studentOne.setRollNo(123);
		studentOne.setName("Rod");
		studentOne.setAddress("1234 162nd lane");
		
		//retrieve dao implementation and execute updates
		StudentDaoImpl studentDaoImpl = context.getBean("dao", StudentDaoImpl.class);
		//studentDaoImpl.insert(studentOne);
		//studentDaoImpl.deleteByNameOrAddress("Bob", "Seattle");
		studentDaoImpl.deleteAll();
		//executor.batchInsert(BatchGenerator.createBatch());
		//another way to call the batch insert is through the auto setup method we created
		BatchGenerator batch = context.getBean(BatchGenerator.class);
		batch.setupStudentTable();
		//studentDaoImpl.deleteRecord(3);
		
		
		
		/*
		 * Retrieve records
		 */
		List<Student> studentList = studentDaoImpl.retrieveAllRecords();
		batch.printStudentList(studentList);
		
		Student student = studentDaoImpl.retrieveRecord(1);
		batch.printStudentObject(student);
		
		//retrieve with results set extractor
		System.out.println("break");
		List<Student> studentList2 = studentDaoImpl.findStudentsByName("Bob");
		batch.printStudentList(studentList2);
		
		
		System.out.println("");
		System.out.println("test between row mapper and result set extractor:");
		List<Student> studentListExtractor = studentDaoImpl.findStudentsByName("Bob"); //extracts full dataset
		batch.printStudentList(studentListExtractor);
		System.out.println("row mapper:");
		List<Student> studentListRowMapper = studentDaoImpl.retrieveAllRecords(); //calls mapRow() every record
		batch.printStudentList(studentListRowMapper);
		
		
		
		/*
		 * 
		 *  update records
		 * 
		 */
		
		//set the details of the student' you'd like to update
		Student updateStudent = new Student(); 
		updateStudent.setRollNo(4); //we want to update the student whose roll number is 4
		updateStudent.setAddress("India");//changing address to india
		
		//int updateNum = studentDaoImpl.updateStudent(updateStudent);
		//System.out.println("Number of records updated: " + updateNum);
		
		
		/*
		 * batch update
		 */
		
		//we want to update three records, we need this info, the roll numbers and their address, and set them as a pojo
		
		Student student1 = new Student();
		List<Student> updateList = new ArrayList<Student>();
		student1.setRollNo(1);
		student1.setAddress("Denver TransHandling");
		
		Student student2= new Student();
		student2.setRollNo(4);
		student2.setAddress("U-district TransHandling saddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
		
		Student student3 = new Student();
		student3.setRollNo(6);
		student3.setAddress("Hawaii TransHandling");
		
		updateList.add(student1);
		updateList.add(student2);
		updateList.add(student3);
		
	
	
		int batchNumUpdate = studentDaoImpl.updateBatch(updateList);
		System.out.println("number of records updated in batch: " + batchNumUpdate);
	
		
		
		
	}

}
