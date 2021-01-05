/**
 * 
 */
package com.jdbc.resultsetextractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.jdbc.pojo.Student;

/*
 * row mapper maps one record at a time to a pojo object, and extracts one record into the result set at a time
 * result set extractor extracts the full data set into the result set, then creates and stores it into the pojo at the end
 * import one at a time (row mapper) vs. import everything at a time to result set (extractor). 
 * 
 */

//https://www.youtube.com/watch?v=b1hABx2Yoms
//in the extractor we want to return a list of students, not just one student
//all we need to do in the extractor is extract all data from the resultset, and store it as an object, inside a list
public class StudentResultSetExtractor implements ResultSetExtractor<List<Student>> {

	//this method fetches all data and stores it into the rs, unlike row mapper, which fetches one record at a time
	@Override
	public List<Student> extractData(ResultSet rs) throws SQLException, DataAccessException {
		
		List<Student> studentList = new ArrayList<Student>();
		int rollNo; 
		String name;
		String address;
		
		//loop through result set, extracting all the data
		while(rs.next()) {
			
			Student student = new Student(); 
			
			//retrieve data from result set and store
			rollNo = rs.getInt("ROLL_NUM");
			name=rs.getString("STUDENT_NAME");
			address = rs.getString("STUDENT_ADDRESS");
			
			//set data from result to object
			student.setRollNo(rollNo);
			student.setName(name);
			student.setAddress(address);
			
			//add student Object into list
			studentList.add(student);
			
		}
		
		System.out.println("inside extractData() of resultsetextractor");
		
		return studentList;
	}

}
