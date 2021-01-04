package com.jdbc.main;

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
		
		//set pojo details
		Student studentOne = new Student();
		//studentOne.setRollNo(123);
		studentOne.setName("Rod");
		studentOne.setAddress("1234 162nd lane");
		
		//retrieve dao implementation and execute updates
		StudentDaoImpl executor = context.getBean("dao", StudentDaoImpl.class);
		executor.insert(studentOne);
		executor.deleteByNameOrAddress("Bob", "Seattle");
		executor.deleteAll();
		//executor.batchInsert(BatchGenerator.createBatch());
		//another way to call the batch insert is through the auto setup method we created
		BatchGenerator batch = context.getBean(BatchGenerator.class);
		batch.setupStudentTable();
		
		
		executor.deleteRecord(3);
		
	
	}

}
