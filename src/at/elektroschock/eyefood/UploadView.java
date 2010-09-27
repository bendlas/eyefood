package at.elektroschock.eyefood;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class UploadView extends Activity {

	String[] places = new String[] {
    		"Flex", "Fluc", "Centimeter", "Halusa", "KFC", "Sur la table"
    };
    String[] meals = new String[] {
    		"Schnitzl", "Burger", "Nudln", "Baguette", "Cordon Bleu", "Suppe", "aaa", "aab", "aac", "aad",
    		"aaq",
    		"aaw",
    		"aae",
    		"aar",
    		"aat",
    		"aay",
    		"aau",
    		"aai",
    		"aaod",
    		"aaf",
    };
	
	private boolean readyToSend() {
		TextView ft = (TextView) findViewById(R.id.food_title);
		TextView pt = (TextView) findViewById(R.id.place_title);
		if (ft.getText().length() > 0 && pt.getText().length() > 0) {
			return true;
		}
		return false;
	}
	
	protected void onSaveInstanceState(Bundle out) {
		ImageButton ib = (ImageButton) findViewById(R.id.photoResultButton);
		out.putParcelable("pic", ib.getDrawingCache());
		super.onSaveInstanceState(out);
	}
	
	private void showPlacesDialog() {
		final CharSequence[] items = places;

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Pick a color");
		builder.setItems(items, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		        Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
		        setPlace(items[item]);
		    }

		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void setPlace(CharSequence place) {
		TextView tv = (TextView) findViewById(R.id.place_title);
		tv.setText(place);
	}

	public final static int CAMERA_PIC_REQUEST = 1337;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload);
        
        Bitmap savedPic = null;
        if (savedInstanceState != null) {
        	savedPic = savedInstanceState.getParcelable("pic");
        }
        
        final ImageButton ib = (ImageButton) findViewById(R.id.photoResultButton);
        ib.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);  
        		startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
        	}
        });
        
        if (savedPic != null) {
        	ib.setImageBitmap(savedPic);
        } else {
        	ib.setImageResource(R.drawable.takeapic);
        }
        
        // show places dialog, when user clicks on place label
        final TextView placeTitle = (TextView) findViewById(R.id.place_title);
        placeTitle.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		// show dialog with places to select
        		showPlacesDialog();
        	}

        });

        // auto completion for food title
        AutoCompleteTextView atv = (AutoCompleteTextView) findViewById(R.id.food_title);
        int placeID = 0;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, getFoodList(placeID));
        atv.setAdapter(adapter);
        
        // button to send food to server
        final Button uploadButton = (Button) findViewById(R.id.upload_button);
        uploadButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (readyToSend()) {
					// start to send as background task
					// show dialog with loading animation
				}
			}
		});
    }
    
    private String[] getFoodList(int placeID) {
    	// DUMMY
    	// contact server, get list of already postet meals for place with placeID
		return this.meals;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (requestCode == CAMERA_PIC_REQUEST) {  
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            ImageButton ib = (ImageButton) findViewById(R.id.photoResultButton);
            ib.setImageBitmap(thumbnail);
        }  
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.upload_menu, menu);
    	return true;
    }
}