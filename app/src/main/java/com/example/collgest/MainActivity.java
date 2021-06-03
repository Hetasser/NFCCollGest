package com.example.collgest;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

import com.example.collgest.db.CollGestDBHelper;
import com.example.collgest.db.CollGestItem;
import com.example.collgest.ui.checkinout.CheckinoutFragment;
import com.example.collgest.ui.checkinout.CheckinoutViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {

    private boolean isCheckinButtonChecked = false;
    private boolean isCheckoutButtonChecked = false;
    private boolean isPlayButtonChecked = false;
    private boolean isAutomaticUpdateChecked = false;
    private String checkoutToTextValue = "";

    private CheckinoutViewModel checkinoutViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("******************************** onCreate ---------------------------------------");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkinoutViewModel = new ViewModelProvider(this).get(CheckinoutViewModel.class);
        checkinoutViewModel.getSelectedItem().observe(this,item -> {System.out.println((item.toString()));});

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_play, R.id.navigation_additem, R.id.navigation_listing, R.id.navigation_checkinout)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


    }

    @Override
    protected void onNewIntent(Intent intent) {
        System.out.println("--------------------------------- onNewIntent *************************************");
        String itemData="";
        String itemGUID = "";
        int CheckinoutFragmentId = 0;

       // CollGestDBHelper collGestDBHelper = new CollGestDBHelper();

        System.out.println("--------------------------------- ---------------------------------------------------");
        System.out.println(intent.getData());
        System.out.println("--------------------------------- ---------------------------------------------------");

        super.onNewIntent(intent);
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMessages != null) {
                NdefMessage[] messages = new NdefMessage[rawMessages.length];
                for (int i = 0; i < rawMessages.length; i++) {
                    messages[i] = (NdefMessage) rawMessages[i];
                    for (int j = 0; j < messages[i].getRecords().length; j++) {
                        try {
                            itemGUID = readText(messages[i].getRecords()[0]);
                            itemData += readText(messages[i].getRecords()[j]) + "\n";
                        } catch (UnsupportedEncodingException e) {
                            System.out.println(e.toString());
                        }
                    }
                }
            }
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ \n" + itemData + " +++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById((R.id.nav_host_fragment));
            //CheckinoutFragment checkinoutFragment = (CheckinoutFragment) navHostFragment.getChildFragmentManager().findFragmentById((R.id.navigation_checkinout));
            for (Fragment fragment: navHostFragment.getChildFragmentManager().getFragments()) {

                if (fragment.getClass().equals(CheckinoutFragment.class)){
                    CheckinoutFragmentId = fragment.getId();;
                }
            }
            CheckinoutFragment checkinoutFragment = (CheckinoutFragment) navHostFragment.getChildFragmentManager().findFragmentById(CheckinoutFragmentId);
            checkinoutFragment.updateCheckinoutmText(itemData);

            if(isAutomaticUpdateChecked) {
                if(isCheckinButtonChecked) {}
                else if (isCheckoutButtonChecked) {}
                else if (isPlayButtonChecked) {updateLastPlayedField(itemGUID);}
}
        }
    }

    public void afterEmailTextChanged(CharSequence s) {
        checkoutToTextValue = s.toString();
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ \n" + checkoutToTextValue + " +++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

    }

    public void onCheckInButtonClicked(View view) {
        System.out.println((view.toString()));
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        if(checked) {
            isCheckinButtonChecked = true;
            isCheckoutButtonChecked = false;
            isPlayButtonChecked = false;
        }}

    public void onPlayButtonClicked(View view) {
        System.out.println((view.toString()));
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        if(checked) {
            isPlayButtonChecked = true;
            isCheckinButtonChecked = false;
            isCheckoutButtonChecked = false;
        }}

    public void onCheckOutButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        if(checked) {
            isCheckinButtonChecked = false;
            isCheckoutButtonChecked = true;
            isPlayButtonChecked = false;
        }}

    public void onAutomaticUpdateClicked(View view) {
        // Is the button now checked?
         isAutomaticUpdateChecked = ((CheckBox) view).isChecked();
    }

    private void updateLastPlayedField(String itemGUID){
        CollGestDBHelper collGestDBHelper = new CollGestDBHelper(this);
        CollGestItem collGestItem = collGestDBHelper.getCollGestItemFromDB(itemGUID);
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ \n" + collGestItem.toString() + " +++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        collGestDBHelper.updateGestItem(new CollGestItem(
                        collGestItem.getItemGUID(),
                        collGestItem.getItemName(),
                        collGestItem.getItemMinJoueurs(),
                        collGestItem.getItemMaxJoueurs(),
                        collGestItem.getItemDuration(),
                        collGestItem.getItemTypes(),
                        java.time.LocalDateTime.now().toString(),
                        collGestItem.getItemCheckedOut()
                )
        );
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ \n" + collGestItem.toString() + " +++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

    }


    private String readText(NdefRecord record) throws UnsupportedEncodingException {
        /*
         * See NFC forum specification for "Text Record Type Definition" at 3.2.1
         *
         * http://www.nfc-forum.org/specs/
         *
         * bit_7 defines encoding
         * bit_6 reserved for future use, must be 0
         * bit_5..0 length of IANA language code
         */

        byte[] payload = record.getPayload();

        // Get the Text Encoding
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

        // Get the Language Code
        int languageCodeLength = payload[0] & 0063;

        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
        // e.g. "en"

        // Get the Text
        return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
    }

}