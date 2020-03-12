package me.cqp.zxlhao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

public class CSVUtils
{
	private String fileName = null;
	private BufferedReader bufferedReader = null;
	private Vector<String> v = new Vector<String>();

	public void CsvUtil(String filename) throws IOException
	{
		this.fileName = filename;
		bufferedReader = new BufferedReader(new FileReader(fileName));
		String stemp;
		while ((stemp = bufferedReader.readLine()) != null)
			if (!stemp.startsWith("#"))// 以#开头表示注释
				v.add(stemp);

	}

	public Vector<String> getVector()
	{
		return v;
	}

	// 得到CSV的行数
	public int getRowCount()
	{
		return v.size();
	}

	// 取得指定行
	public String getRow(int index)
	{
		int rownum = this.getRowCount();
		if (index <= 0 || rownum == 0 || rownum < index)
			return null;
		return v.get(index - 1);
	}

	// 取得指定列
	public String getColumn(int index)
	{
		int column = this.getColumnCount();
		if (index <= 0 || column == 0 || column < index)
			return null;
		StringBuffer scol = new StringBuffer();
		// 获取列以 a,b,格式
		if (column >= 1)
			for (Iterator<String> it = v.iterator(); it.hasNext();)
				scol = scol.append(it.next().split(",")[index - 1] + ",");
		String str = scol.toString();
		return str.substring(0, str.length() - 1);
	}

	// 得到列数
	public int getColumnCount()
	{
		if (!v.toString().equals("[]"))
			if (v.get(0).contains(","))
				return v.get(0).split(",").length;
			else if (v.get(0).trim().length() != 0)
				return 1;
		return 0;
	}

	// 取得指定行，指定列的值
	public String getValueAt(int row, int col)
	{
		int column = this.getColumnCount();
		int rownum = this.getRowCount();
		if (row <= 0 || col <= 0 || column == 0 || rownum == 0 || column < col || rownum < row)
			return null;
		return v.get(row - 1).toString().split(",")[col - 1];
	}

	public void insertRow(Vector<String> v) throws IOException
	{
		// 当前的vector增加
		// this.v.add错误
		BufferedWriter bw = new BufferedWriter(new FileWriter(this.fileName, true));// 一定要接true，表示追加
		StringBuffer temp = new StringBuffer();
		Iterator<String> it = v.iterator();
		temp.append(it.next());
		while (it.hasNext())
			temp.append("," + it.next());
		bw.write(temp.toString());
		bw.newLine();
		bw.flush();
		bw.close();

	}

	public void deleteRow(int index) throws IOException
	{
		v.remove(index);
		BufferedWriter bw = new BufferedWriter(new FileWriter(this.fileName));
		for (Iterator<String> it = v.iterator(); it.hasNext();)
		{
			bw.write(it.next());
			bw.newLine();// 一定要有
		}
		bw.flush();
		bw.close();
	}

	public void printAll()
	{
		Iterator<String> it = v.iterator();
		while (it.hasNext())
			System.out.println(it.next());
	}
	public void CsvClose() throws IOException
	{
		this.bufferedReader.close();
	}
}
