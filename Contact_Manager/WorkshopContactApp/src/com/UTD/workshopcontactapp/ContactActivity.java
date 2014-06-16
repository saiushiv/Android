package com.UTD.workshopcontactapp;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Vector;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.text.*;

public class ContactActivity extends Activity
	{
	EditText eFirstname;
	EditText eLastname;
	EditText ePhone;
	EditText eEmail;
	Button btnSave;
	Button btnDel;
	Validate watcher;
	String strFilename = "MyContacts.txt";
	Vector<String> contacts;
	ListView lv;
	int pos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
		{
		contacts = new Vector<String>();
		
		Scanner sFile = null;
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_contact);
		eFirstname = (EditText)this.findViewById(R.id.editFirstName);
		eLastname = (EditText)this.findViewById(R.id.editLastName);
		ePhone = (EditText)this.findViewById(R.id.editPhone);
		eEmail = (EditText)this.findViewById(R.id.editEmail);
		btnSave = (Button)this.findViewById(R.id.btnSave);
		btnDel = (Button)this.findViewById(R.id.btnDelete);
		watcher = new Validate();
		eFirstname.addTextChangedListener(watcher);
		eLastname.addTextChangedListener(watcher);
		ePhone.addTextChangedListener(watcher);
		eEmail.addTextChangedListener(watcher);
//???		File fInput = new File(getFilesDir(), "MyContacts.txt");
		readData();
		lv = (ListView) this.findViewById(R.id.listc);
		pos = -1;
		MyAdapter adapter = new MyAdapter(contacts);
		lv.setOnItemClickListener(new OnItemClickListener()
			{
				public void onItemClick(AdapterView<?> parent, View view,
			                int position, long id){
					String rawstring = contacts.elementAt(position);
					pos=position;
					String[] prosstr = rawstring.split("\t");
					eFirstname.setText(prosstr[0]);
					eLastname.setText(prosstr[1]);
					ePhone.setText(prosstr[2]);
					eEmail.setText(prosstr[3]);
					btnDel.setEnabled(true);
					btnSave.setEnabled(true);
				}
			});		
		btnSave.setEnabled(false);
		btnDel.setEnabled(false);
		lv.setAdapter(adapter);
		}

	private void readData()
	  {
	  Scanner sFile;
	  File newFolder = getFilesDir();
		File fInput = new File(newFolder.getAbsolutePath() + File.separator + "/" + strFilename);
		try
			{
		  sFile = new Scanner(fInput);
		  while (sFile.hasNextLine())
		  	{
		  	String str = sFile.nextLine();
		  	contacts.add(str);
		  	}
			}
		catch (Exception ex)
			{
			System.out.println(ex.getMessage());
			}
	  }

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
		{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contact, menu);
		return true;
		}
	
	public void onSaveClick(View view)
		{
		String str = eFirstname.getText() + "\t" +	
			  eLastname.getText() + "\t" +
				ePhone.getText() + "\t" +
				eEmail.getText();
		if (pos == -1)
			contacts.add(str);
		else
			contacts.setElementAt(str, pos);
		pos = -1;
		if (saveData())
			{
			eFirstname.setText("");
			eLastname.setText("");
			ePhone.setText("");
			eEmail.setText("");
			lv.invalidateViews();
			btnSave.setEnabled(false);
			btnDel.setEnabled(false);
			}
		}
	
	public void onDeleteClick(View view)
		{
		contacts.remove(pos);
		saveData();
		lv.invalidateViews();
		btnDel.setEnabled(false);
		
		}
	
	// Function to save data.
	private boolean saveData()
		{
		PrintWriter pw = null;
		File fileDir = getFilesDir();
		File file = null;
		
		boolean bExists = true;
		try
			{
			File newFolder = getFilesDir();
//???			File newFolder = new File(Environment.getExternalStorageDirectory(),"/Contacts");
			if (!newFolder.isDirectory())
				{
				bExists = newFolder.mkdir();
				}
			try
				{
  			file = new File(newFolder.getAbsolutePath() + File.separator + "/" + strFilename);
//???				file = new File(newFolder, "MyMsgs" + ".txt");
				file.createNewFile();
				}
			catch (Exception ex)
				{
				System.out.println("ex: " + ex);
				bExists = false;
				}
			}
		catch (Exception e)
			{
			System.out.println("e: " + e);
			bExists = false;
			}
		try
			{
			pw = new PrintWriter(file);
			}
		catch(Exception ex)
			{
			System.out.println("Error creating PW: " + ex.getMessage());
			return false;
			}

		if (bExists)
			{
			for (int ix=0; ix<contacts.size(); ix++)
				{
				pw.println(contacts.elementAt(ix));
				}
			pw.close();
		}
		return true;
		}

	
	private class ClickListener implements OnItemClickListener
	{
	public void onItemClick(AdapterView<?> parent, View view,
      int position, long id){
String rawstring = contacts.elementAt(position);
pos=position;
String[] prosstr = rawstring.split("\t");
eFirstname.setText(prosstr[0]);
eLastname.setText(prosstr[1]);
ePhone.setText(prosstr[2]);
eEmail.setText(prosstr[3]);
btnDel.setEnabled(true);
}
	}

	// Internal private class to validate the controls
	private class Validate implements TextWatcher
	{
	public void afterTextChanged(Editable s)
		{
		btnSave.setEnabled(eFirstname.getText().length() > 0 &&
				eLastname.getText().length() > 0 &&
				ePhone.getText().length() > 0 &&
				eEmail.getText().length() > 0);
		}

	@Override
  public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
	  {
	  return;
	  }

	@Override
  public void onTextChanged(CharSequence s, int start, int before, int count)
	  {
	  return;
	  }
	}
	
	private class MyAdapter implements ListAdapter
	{
	Vector v;
	
	public MyAdapter(Vector vp)
		{
		v = vp;
		}
		@Override
    public int getCount()
	    {
	    return v.size();
	    }

		@Override
    public Object getItem(int arg0)
	    {
	    return v.elementAt(arg0);
	    }

		@Override
    public long getItemId(int arg0)
	    {
	    // TODO Auto-generated method stub
	    return 0;
	    }

		@Override
    public int getItemViewType(int arg0)
	    {
	    // TODO Auto-generated method stub
	    return 0;
	    }

		@Override
    public View getView(int arg0, View arg1, ViewGroup arg2)
	    {
	    // TODO Auto-generated method stub
	    TextView tv = new TextView(arg2.getContext());
	    tv.setText((CharSequence) v.elementAt(arg0));
	    tv.setTextSize(20.0f);
	    return tv;
	    }

		@Override
    public int getViewTypeCount()
	    {
	    // TODO Auto-generated method stub
	    return 1;
	    }

		@Override
    public boolean hasStableIds()
	    {
	    // TODO Auto-generated method stub
	    return false;
	    }

		@Override
    public boolean isEmpty()
	    {
	    // TODO Auto-generated method stub
	    return false;
	    }

		@Override
    public void registerDataSetObserver(DataSetObserver arg0)
	    {
	    // TODO Auto-generated method stub
	    
	    }

		@Override
    public void unregisterDataSetObserver(DataSetObserver arg0)
	    {
	    // TODO Auto-generated method stub
	    
	    }
		@Override
    public boolean areAllItemsEnabled()
	    {
	    // TODO Auto-generated method stub
	    return true;
	    }
		@Override
    public boolean isEnabled(int arg0)
	    {
	    // TODO Auto-generated method stub
	    return true;
	    }
	}
	

	}
