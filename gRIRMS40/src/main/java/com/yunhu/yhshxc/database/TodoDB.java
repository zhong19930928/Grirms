package com.yunhu.yhshxc.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.yunhu.yhshxc.activity.todo.Todo;

/**
 * 
 * @author jishen 数据库操作
 */
public class TodoDB {
	private DatabaseHelper openHelper;

	public TodoDB(Context context) {
		openHelper = DatabaseHelper.getInstance(context);
	}

	/*
	 * 查询所有Push内容
	 */
	public List<Todo> findAllTodo() {
		List<Todo> list = new ArrayList<Todo>();
		StringBuffer sql=new StringBuffer();
		sql.append("select * from ").append(openHelper.TO_DO).append(" order by id");
		Cursor cursor = openHelper.query(sql.toString(), null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(putToDo(cursor));
			}
		}
		cursor.close();
		return list;
	}
	

	/*
	 * 插入ToDo
	 */
	public void insertToDo(Todo todo) {
		
		ContentValues cv = putContentValues(todo);
		openHelper.insert(openHelper.TO_DO, cv);
	}

	public void deleteToDoById(int id){
		openHelper.delete(openHelper.TO_DO, "id=?", new String[]{String.valueOf(id)});
	}
	
	public void deleteAllToDo(){
		StringBuffer sql = new StringBuffer();
		sql.append(" delete from ").append(openHelper.TO_DO);
		openHelper.execSQL(sql.toString());
	}
	
	private Todo putToDo(Cursor cursor) {
		int i = 0;
		Todo todo = new Todo();
		todo.setId(cursor.getInt(i++));
		todo.setMenuName(cursor.getString(i++));
		todo.setStateName(cursor.getString(i++));
		todo.setTodoNum(cursor.getInt(i++));
		return todo;
	}

	private ContentValues putContentValues(Todo todo) {
		ContentValues cv = new ContentValues();
		cv.put("m_name", todo.getMenuName());
		cv.put("s_name", todo.getStateName());
		cv.put("todo_num", todo.getTodoNum());
		return cv;
	}

}
