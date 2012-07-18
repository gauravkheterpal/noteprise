package com.metacube.noteprise.salesforce;

import android.webkit.CookieSyncManager;

import com.metacube.noteprise.R;
import com.metacube.noteprise.common.BaseActivity;
import com.salesforce.androidsdk.app.ForceApp;
import com.salesforce.androidsdk.rest.ClientManager;
import com.salesforce.androidsdk.rest.ClientManager.LoginOptions;
import com.salesforce.androidsdk.rest.ClientManager.RestClientCallback;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.security.PasscodeManager;

public class SalesforceLoginUtility implements RestClientCallback
{
	private PasscodeManager passcodeManager;
	BaseActivity baseActivity;
	ClientManager salesforceClientManager;
	public RestClient salesforceRestClient;
	
	public SalesforceLoginUtility(BaseActivity baseActivity) 
	{
		this.baseActivity = baseActivity;
		CookieSyncManager.createInstance(baseActivity);
		passcodeManager = ForceApp.APP.getPasscodeManager();
	}
	
	public void onAppResume()
	{
		if (passcodeManager.onResume(baseActivity)) 
		{
			String accountType = ForceApp.APP.getAccountType();
	    	LoginOptions loginOptions = new LoginOptions(null, ForceApp.APP.getPasscodeHash(), baseActivity.getString(R.string.oauth_callback_url),
	    			baseActivity.getString(R.string.oauth_client_id), new String[] {"api"});
	    	salesforceClientManager = new ClientManager(baseActivity, accountType, loginOptions);
	    	salesforceClientManager.getRestClient(baseActivity, this);
		}
	}

	@Override
	public void authenticatedRestClient(RestClient client) 
	{
		if (client == null) 
		{
			ForceApp.APP.logout(baseActivity);
			return;
		}	
		this.salesforceRestClient = client;
		baseActivity.loggedInSalesforce = Boolean.TRUE;
		baseActivity.handleSalesforceLoginComplete();
		// Show everything
		//findViewById(R.id.root).setVisibility(View.VISIBLE);

		// Show welcome
		//((TextView) findViewById(R.id.welcome_text)).setText(getString(R.string.welcome, client.getClientInfo().username));
	}
	
	public void onAppUserInteraction() 
	{
		passcodeManager.recordUserInteraction();
	}
	
	public void onAppPause() 
	{
    	passcodeManager.onPause(baseActivity);
    }
	
	public void onSalesforceLogout() 
	{
		ForceApp.APP.logout(baseActivity);
	}
}
