package com.jdbc.rowmappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.jdbc.pojo.Student;

//implement the row mapper interface, in order to use in retrieve query

public class StudentRowMapper implements RowMapper<Student> {

	//method that gives you a result set one by one
	//so for first record (rowNum= 0), the result set will contain 1 Bob Seattle
	
	//this method gets called multiple times based on how many records databse contains
	
	@Override
	public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		//fetch row data set it to a student object
		
		Student studentObject = new Student();
		int rollNo = rs.getInt("ROLL_NUM"); //get the roll number from result set
		String name = rs.getString("STUDENT_NAME");
		String address = rs.getString("STUDENT_ADDRESS");
		
		//set the student pojo
		studentObject.setRollNo(rollNo);
		studentObject.setName(name);
		studentObject.setAddress(address);
		
		//test
		System.out.println(studentObject.getRollNo());
		System.out.println(studentObject.getName());
		System.out.println(studentObject.getAddress());
		
		//return the student object
		return studentObject;
	}

}
