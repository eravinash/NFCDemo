package com.tutsplus.nfcdemo;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private NfcAdapter nfcAdapter;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		PackageManager pm = this.getPackageManager();
		// Check whether NFC is available on device		
        if (!pm.hasSystemFeature(PackageManager.FEATURE_NFC)) {
        	// NFC is not available on the device.
            Toast.makeText(this, "The device does not has NFC hardware.", 
            				Toast.LENGTH_SHORT).show();           
        } 
        // Check whether device is running Android 4.1 or higher 
        else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            // Android Beam feature is not supported.            
        	Toast.makeText(this, "Android Beam is not supported.", 
        					Toast.LENGTH_SHORT).show();                     
        }         
        else {
        	// NFC and Android Beam file transfer is supported.        	
        	Toast.makeText(this, "Android Beam is supported on your device.", 
        					Toast.LENGTH_SHORT).show();
        }
	}			
	
	public void sendFile(View view) {
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);  
		
		// Check whether NFC is enabled on device
        if(!nfcAdapter.isEnabled()){
        	// NFC is disabled, show the settings UI
        	// to enable NFC
        	Toast.makeText(this, "Please enable NFC.", Toast.LENGTH_SHORT).show(); 
        	startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
        }        
        // Check whether Android Beam feature is enabled on device
        else if(!nfcAdapter.isNdefPushEnabled()) {
        	// Android Beam is disabled, show the settings UI
        	// to enable Android Beam 
        	Toast.makeText(this, "Please enable Android Beam.", 
        					Toast.LENGTH_SHORT).show();
        	startActivity(new Intent(Settings.ACTION_NFCSHARING_SETTINGS));
        }
        else {
        	// NFC and Android Beam both are enabled 
        
        	// File to be transferred    
        	// For the sake of this tutorial I've placed an image named 'wallpaper.png'
        	// in the 'Pictures' directory
        	String fileName = "wallpaper.png";
        
        	// Retrieve the path to the user's public pictures directory 
        	File fileDirectory = Environment
        							.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        
        	// Create a new file using the specified directory and name
        	File fileToTransfer = new File(fileDirectory, fileName);
        	fileToTransfer.setReadable(true, false);
        
        	nfcAdapter.setBeamPushUris(new Uri[]{Uri.fromFile(fileToTransfer)}, this);		        
        }               	
	}
}
