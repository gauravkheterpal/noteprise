package com.metacube.noteprise.salesforce;

import android.app.Activity;

import com.metacube.noteprise.core.NotepriseActivity;
import com.salesforce.androidsdk.app.ForceApp;
import com.salesforce.androidsdk.security.Encryptor;
import com.salesforce.androidsdk.ui.SalesforceR;

public class NotepriseApplication extends ForceApp {

private SalesforceR salesforceR = new SalesforceRImpl();
	
	@Override
	public Class<? extends Activity> getMainActivityClass() {
		return NotepriseActivity.class;
	}
	
	@Override
	protected String getKey(String name) {
		return Encryptor.hash(name + "x;lksalk1jsadihh23lia;lsdhasd2", name + "112;kaslkxs0-12;skcxn1203ph");
	}

	@Override
	public SalesforceR getSalesforceR() {
		return salesforceR;
	}

}
