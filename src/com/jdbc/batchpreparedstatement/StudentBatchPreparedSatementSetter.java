package com.jdbc.batchpreparedstatement;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import com.jdbc.pojo.Student;



//this class will be used and passed into jdbce.updateBatch(sql, bpss);

public class StudentBatchPreparedSatementSetter implements BatchPreparedStatementSetter {
	
	//will be contstructor injected
	List<Student> studentList; 
	
	//constructor that receives studentlist 
	public StudentBatchPreparedSatementSetter(List<Student> studentList) {
		this.studentList = studentList; 
	}

	//sets the values for address = ? parameter and rollnum = ?
	//ps is the prepared statement, and i is index of the list you will pass in
	//i will automatically loop through the list
	@Override
	public void setValues(PreparedStatement ps, int i) throws SQLException {
		
		//setting ? parameters in sql string
		//first question mark is parameter index 1 which is address
		ps.setString(1, studentList.get(i).getAddress()); //sets first parameter as the index of the List<Student> that you pass in, so if you pass in 0, that will be student1 object
		ps.setInt(2, studentList.get(i).getRollNo());
		
		System.out.println("setting prepared statement values...in batch prepared statement setter");
	}

	//defines how many times setValues() method will execute i.e. how many times the query will execute
	@Override
	public int getBatchSize() {
		
		System.out.println("getting batch size...in batch prepared statement setter");
		
		//run it as many records there are in the list
		return studentList.size();
	}

}



