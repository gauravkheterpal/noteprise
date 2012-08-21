package com.metacube.noteprise.util.richtexteditor;

import com.metacube.noteprise.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
//import android.text.Html;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView.BufferType;
import android.widget.ToggleButton;

public class RichTextEditor extends LinearLayout implements ColorPickerDialog.OnColorChangedListener, TextSizeDialog.OnSizeChangedListener
{
	EditText content;
	LinearLayout toolbar;
	
	boolean showHtml = false;
	
	int styleStart = -1, cursorLoc = 0;
	
	public RichTextEditor(Context context) 
	{
		super(context);
		
		init(context);
	}
	
	public RichTextEditor(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		
		init(context);
	}
	
	private void init(Context context)
	{
		//Inflate the view from the layout resource
		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li;
		li = (LayoutInflater)getContext().getSystemService(infService);
		li.inflate(R.layout.richtexteditor, this, true);
		
		//Get references to the child controls
		content = (EditText)findViewById(R.id.content);
		toolbar = (LinearLayout)findViewById(R.id.toolbar);
		
		addListeners();
	}
	
	public EditText getEditor()
	{
		return content;
	}
	
	public LinearLayout getToolbar()
	{
		return toolbar;
	}
	
	private void addListeners()
	{
		final ToggleButton boldButton = (ToggleButton)findViewById(R.id.bold);
		
		boldButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	            	
            	int selectionStart = content.getSelectionStart();
            	
            	styleStart = selectionStart;
            	
            	int selectionEnd = content.getSelectionEnd();
            	
            	if (selectionStart > selectionEnd){
            		int temp = selectionEnd;
            		selectionEnd = selectionStart;
            		selectionStart = temp;
            	}
            	
            	if (selectionEnd > selectionStart)
            	{
            		Spannable str = content.getText();
            		StyleSpan[] ss = str.getSpans(selectionStart, selectionEnd, StyleSpan.class);
            		
            		boolean exists = false;
            		for (int i = 0; i < ss.length; i++) {
            			if (ss[i].getStyle() == android.graphics.Typeface.BOLD){
            				str.removeSpan(ss[i]);
            				exists = true;
            			}
                    }
            		
            		if (!exists){
            			str.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            		}
            		
            		boldButton.setChecked(false);
            	}
            }
		});
		
		final ToggleButton italicButton = (ToggleButton)findViewById(R.id.italic);
		
		italicButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	            	
            	int selectionStart = content.getSelectionStart();
            	
            	styleStart = selectionStart;
            	
            	int selectionEnd = content.getSelectionEnd();
            	
            	if (selectionStart > selectionEnd){
            		int temp = selectionEnd;
            		selectionEnd = selectionStart;
            		selectionStart = temp;
            	}
            	
            	if (selectionEnd > selectionStart)
            	{
            		Spannable str = content.getText();
            		StyleSpan[] ss = str.getSpans(selectionStart, selectionEnd, StyleSpan.class);
            		
            		boolean exists = false;
            		for (int i = 0; i < ss.length; i++) {
            			if (ss[i].getStyle() == android.graphics.Typeface.ITALIC){
            				str.removeSpan(ss[i]);
            				exists = true;
            			}
                    }
            		
            		if (!exists){
            			str.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            		}
            		
            		italicButton.setChecked(false);
            	}
            }
		});
		
		final ToggleButton underlineButton = (ToggleButton) findViewById(R.id.underline);   
        
        underlineButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	 
            	EditText contentText = (EditText) findViewById(R.id.content);

            	int selectionStart = contentText.getSelectionStart();
            	
            	styleStart = selectionStart;
            	
            	int selectionEnd = contentText.getSelectionEnd();
            	
            	if (selectionStart > selectionEnd){
            		int temp = selectionEnd;
            		selectionEnd = selectionStart;
            		selectionStart = temp;
            	}
            	
            	if (selectionEnd > selectionStart)
            	{
            		Spannable str = contentText.getText();
            		UnderlineSpan[] ss = str.getSpans(selectionStart, selectionEnd, UnderlineSpan.class);
            		
            		boolean exists = false;
            		for (int i = 0; i < ss.length; i++) {
            				str.removeSpan(ss[i]);
            				exists = true;
                    }
            		
            		if (!exists){
            			str.setSpan(new UnderlineSpan(), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            		}
            		
            		underlineButton.setChecked(false);
            	}
            }
        });
        
        final EditText contentEdit = (EditText) findViewById(R.id.content);
        
        contentEdit.addTextChangedListener(new TextWatcher() { 
            public void afterTextChanged(Editable s) { 
            	
            	//add style as the user types if a toggle button is enabled
            	ToggleButton boldButton = (ToggleButton) findViewById(R.id.bold);
            	ToggleButton emButton = (ToggleButton) findViewById(R.id.italic);
            	ToggleButton underlineButton = (ToggleButton) findViewById(R.id.underline);
            	
            	int position = Selection.getSelectionStart(contentEdit.getText());
            	
        		if (position < 0){
        			position = 0;
        		}
            	
        		if (position > 0){
        			
        			if (styleStart > position || position > (cursorLoc + 1)){
						//user changed cursor location, reset
						if (position - cursorLoc > 1){
							//user pasted text
							styleStart = cursorLoc;
						}
						else{
							styleStart = position - 1;
						}
					}
        			
                	if (boldButton.isChecked()){  
                		StyleSpan[] ss = s.getSpans(styleStart, position, StyleSpan.class);

                		for (int i = 0; i < ss.length; i++) {
                			if (ss[i].getStyle() == android.graphics.Typeface.BOLD){
                				s.removeSpan(ss[i]);
                			}
                        }
                		s.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), styleStart, position, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                	}
                	if (emButton.isChecked()){
                		StyleSpan[] ss = s.getSpans(styleStart, position, StyleSpan.class);
                		
                		for (int i = 0; i < ss.length; i++) {
                			if (ss[i].getStyle() == android.graphics.Typeface.ITALIC){
                				s.removeSpan(ss[i]);
                			}
                        }
                		s.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), styleStart, position, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                	}
                	if (underlineButton.isChecked()){
                		UnderlineSpan[] ss = s.getSpans(styleStart, position, UnderlineSpan.class);

                		for (int i = 0; i < ss.length; i++) {
                				s.removeSpan(ss[i]);
                        }
                		s.setSpan(new UnderlineSpan(), styleStart, position, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                	}
        		}
        		
        		cursorLoc = Selection.getSelectionStart(contentEdit.getText());
            } 
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { 
                    //unused
            } 
            public void onTextChanged(CharSequence s, int start, int before, int count) { 
                    //unused
            } 
        });
        
		final ToggleButton htmlButton = (ToggleButton)findViewById(R.id.html);
		
		htmlButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	
            	if(showHtml)
            	{
            		content.setText(Html.fromHtml(content.getText().toString()),BufferType.SPANNABLE);
            	}
            	else
            	{
            		content.setText(Html.toHtml(content.getText()));
            	}
            	
            	showHtml = !showHtml;
            	
            }
		});
		
		final Button linkButton = (Button)findViewById(R.id.link);
		
		linkButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	
            	AlertDialog.Builder alert = new AlertDialog.Builder(RichTextEditor.this.getContext());

            	alert.setTitle("Link");
            	alert.setMessage("Enter URL:");

            	final LinearLayout layout = new LinearLayout(RichTextEditor.this.getContext());
            	layout.setPadding(10, 0, 10, 0);
            	
            	final EditText input = new EditText(RichTextEditor.this.getContext());
            	input.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
            	input.setText("http://");
            	
            	layout.addView(input);
            	
            	alert.setView(layout);

            	alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            	public void onClick(DialogInterface dialog, int whichButton) {
            		String linkUrl = input.getText().toString();
            		
            		if(!linkUrl.equals("") && !linkUrl.equals("http://"))
            		{
	            		int selectionStart = content.getSelectionStart();
	                	int selectionEnd = content.getSelectionEnd();
	                	
	                	if(selectionStart != selectionEnd)
	                	{
		                	if (selectionStart > selectionEnd){
		                		int temp = selectionEnd;
		                		selectionEnd = selectionStart;
		                		selectionStart = temp;
		                	}
		                	
		            		Spannable str = content.getText();
		        			str.setSpan(new URLSpan(linkUrl),  selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	                	}
            		}
            	  }
            	});

            	alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            	  public void onClick(DialogInterface dialog, int whichButton) {
            	    // Canceled.
            	  }
            	});

            	alert.show();
            	
            }
		});
		
		final Button sizeButton = (Button)findViewById(R.id.size);
		
		sizeButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	
	            TextSizeDialog colorDlg = new TextSizeDialog(RichTextEditor.this.getContext(), (TextSizeDialog.OnSizeChangedListener)RichTextEditor.this);
            	colorDlg.show();
            }
		});
		
		final Button colorButton = (Button)findViewById(R.id.color);
		
		colorButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	
	            ColorPickerDialog colorDlg = new ColorPickerDialog(RichTextEditor.this.getContext(), (ColorPickerDialog.OnColorChangedListener)RichTextEditor.this, 0);
            	colorDlg.show();
            }
		});
	}
	
	public void colorChanged(int color) 
	{
        //((Button)findViewById(R.id.color)).setTextColor(color);
        
        int selectionStart = content.getSelectionStart();
    	int selectionEnd = content.getSelectionEnd();
    	
    	if(selectionStart != selectionEnd)
    	{
        	if (selectionStart > selectionEnd){
        		int temp = selectionEnd;
        		selectionEnd = selectionStart;
        		selectionStart = temp;
        	}
        	
        	Spannable str = content.getText();
        	
        	ForegroundColorSpan[] ss = str.getSpans(selectionStart, selectionEnd, ForegroundColorSpan.class);
    		
    		for (int i = 0; i < ss.length; i++) {
    				str.removeSpan(ss[i]);
            }
        	
			str.setSpan(new ForegroundColorSpan(color), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    	}
    }
	
	public void sizeChanged(int size) 
	{
        //((Button)findViewById(R.id.color)).setTextColor(color);
        
        int selectionStart = content.getSelectionStart();
    	int selectionEnd = content.getSelectionEnd();
    	
    	if(selectionStart != selectionEnd)
    	{
        	if (selectionStart > selectionEnd){
        		int temp = selectionEnd;
        		selectionEnd = selectionStart;
        		selectionStart = temp;
        	}
        	
        	Spannable str = content.getText();
        	
        	AbsoluteSizeSpan[] ss = str.getSpans(selectionStart, selectionEnd, AbsoluteSizeSpan.class);
    		
    		for (int i = 0; i < ss.length; i++) {
    				str.removeSpan(ss[i]);
            }
        	
			str.setSpan(new AbsoluteSizeSpan(size), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    	}
    }
}
