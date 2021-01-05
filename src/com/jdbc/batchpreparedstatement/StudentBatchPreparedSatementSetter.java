package com.jdbc.batchpreparedstatement;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import com.jdbc.pojo.Student;



//this class will be used and passed into jdbce.updateBatch(sql, bpss);

public class StudentBatchPreparedSatementSetter implements BatchPreparedStatementSetter {
	
	
	List<Student> studentList; 
	
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
	}

	//defines how many times query will execute, or how many times setValues() method will execute
	@Override
	public int getBatchSize() {
		// TODO Auto-generated method stub
		return 0;
	}

}
