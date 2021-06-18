package com.example.collgest;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.collgest.db.CollGestDBHelper;
import com.example.collgest.db.CollGestItem;
import com.example.collgest.ui.checkinout.CheckinoutFragment;
import com.example.collgest.ui.listing.ListingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private boolean isCheckinButtonChecked = false;
    private boolean isCheckoutButtonChecked = false;
    private boolean isPlayButtonChecked = false;
    private boolean isAutomaticUpdateChecked = false;
    private String itemGUID="";
    private String checkedOutTo = "";
    private ListView addItemListView;

    /***********************************************************************************************
     * Cration de l'activite
     * *********************************************************************************************
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_additem, R.id.navigation_listing, R.id.navigation_checkinout)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

    }

    /***********************************************************************************************
     * Gestion de l'evenement quand on détecte un tag NFC
     * *********************************************************************************************
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        CheckinoutFragment checkinoutFragment=null;
        setIntent(intent);
        System.out.println("--------------------------------- onNewIntent *************************************");
        StringBuilder itemData = new StringBuilder();
        int CheckinoutFragmentId = 0;

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
                            itemData.append(readText(messages[i].getRecords()[j])).append("\n");
                        } catch (UnsupportedEncodingException e) {
                            System.out.println(e.toString());
                        }
                    }
                }
            }
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ \n" + itemData + " +++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById((R.id.nav_host_fragment));
            assert navHostFragment != null : "navHostFragment is null";
            for (Fragment fragment : navHostFragment.getChildFragmentManager().getFragments()) {
                if (fragment.getClass().equals(CheckinoutFragment.class)) {
                    checkinoutFragment = (CheckinoutFragment) fragment;
                }
            }
            assert checkinoutFragment != null : "checkinoutFragment is null";
            checkinoutFragment.updateCheckinoutmText(itemData.toString());
            if (isAutomaticUpdateChecked) {
                if (isCheckinButtonChecked) {
                    System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  automatic Check In  +++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    checkinItem(itemGUID);
                } else if (isCheckoutButtonChecked) {
                    System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  automatic Check Out  +++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    checkoutItem(itemGUID);
                } else if (isPlayButtonChecked) {
                    System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  automatic lastPlay  +++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    updateLastPlayedField(itemGUID);
                } else {
                    System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  automatic nothing to do  +++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                }
            }
        }
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

    /***********************************************************************************************
     * Fragment : Checkin / Checkout
     * *********************************************************************************************
     * @param view
     */
    public void onCheckInButtonClicked(View view) {
        System.out.println((view.toString()));
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        if (checked) {
            isCheckinButtonChecked = true;
            isCheckoutButtonChecked = false;
            isPlayButtonChecked = false;
        }
    }

    public void onPlayButtonClicked(View view) {
        System.out.println((view.toString()));
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        if (checked) {
            isPlayButtonChecked = true;
            isCheckinButtonChecked = false;
            isCheckoutButtonChecked = false;
        }
    }

    public void onCheckOutButtonClicked(View view) {
        ViewGroup grandParentView = (ViewGroup) view.getParent().getParent();
        for(int i=0; i< grandParentView.getChildCount();i++){
            if (grandParentView.getChildAt(i).getId() == R.id.checkinout_checkedOutTo){
                checkedOutTo = Objects.requireNonNull(((AppCompatEditText) grandParentView.getChildAt(i)).getText()).toString();
            }
        }
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        if (checked) {
            isCheckinButtonChecked = false;
            isCheckoutButtonChecked = true;
            isPlayButtonChecked = false;
        }
    }

    public void onAutomaticUpdateClicked(View view) {
        // Is the button now checked?
        isAutomaticUpdateChecked = ((CheckBox) view).isChecked();
    }

    private void checkinItem(String itemGUID) {
        CollGestDBHelper collGestDBHelper = new CollGestDBHelper(this);
        CollGestItem collGestItem = collGestDBHelper.getCollGestItemFromDB(itemGUID);
        collGestDBHelper.updateGestItem(new CollGestItem(
                collGestItem.getItemGUID(),
                collGestItem.getItemName(),
                collGestItem.getItemMinJoueurs(),
                collGestItem.getItemMaxJoueurs(),
                collGestItem.getItemDuration(),
                collGestItem.getItemTypes(),
                collGestItem.getItemLastPlayed(),
                "")

        );
    }

    private void checkoutItem(String itemGUID) {
        CollGestDBHelper collGestDBHelper = new CollGestDBHelper(this);
        CollGestItem collGestItem = collGestDBHelper.getCollGestItemFromDB(itemGUID);
        collGestDBHelper.updateGestItem(new CollGestItem(
                collGestItem.getItemGUID(),
                collGestItem.getItemName(),
                collGestItem.getItemMinJoueurs(),
                collGestItem.getItemMaxJoueurs(),
                collGestItem.getItemDuration(),
                collGestItem.getItemTypes(),
                collGestItem.getItemLastPlayed(),
                checkedOutTo)

        );
    }

    private void updateLastPlayedField(String itemGUID) {
        CollGestDBHelper collGestDBHelper = new CollGestDBHelper(this);
        CollGestItem collGestItem = collGestDBHelper.getCollGestItemFromDB(itemGUID);
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
    }

    public void onManuallyValidate(View view){
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ onManuallyValidate  +++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
           if (isCheckinButtonChecked) {
                System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  Manual Check In  +++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                checkinItem(itemGUID);
            } else if (isCheckoutButtonChecked) {
                System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  Manual Check Out  +++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                checkoutItem(itemGUID);
            } else if (isPlayButtonChecked) {
                System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  Manual lastPlay  +++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                updateLastPlayedField(itemGUID);
            } else {
               System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  Manual Nothing to do  +++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
           }
    }


    /***********************************************************************************************
     * Gestion du fragment "Add Item
     * *********************************************************************************************
     * @param view
     */
    public void onAddItemButtonClicked(View view){
        String gameName=null;
        int nbMinPlayers=0;
        int nbMaxPLayer=0;
        int gameDuration=0;
        String gameTypes=null;
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  onAddItemButtonClicked  +++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        ViewGroup parentView = (ViewGroup) view.getParent();

        for(int i=0; i< parentView.getChildCount();i++){
            if(parentView.getChildAt(i) instanceof androidx.appcompat.widget.AppCompatEditText){
                switch (parentView.getChildAt(i).getId()){
                    case R.id.additem_gameName : gameName = Objects.requireNonNull(((AppCompatEditText) parentView.getChildAt(i)).getText()).toString();
                        break;
                    case R.id.additem_gameNbMinPlayer : nbMinPlayers = Integer.parseInt(Objects.requireNonNull(((AppCompatEditText) parentView.getChildAt(i)).getText()).toString());
                        break;
                    case R.id.additem_gameNbMaxPlayer : nbMaxPLayer = Integer.parseInt(Objects.requireNonNull(((AppCompatEditText) parentView.getChildAt(i)).getText()).toString());
                        break;
                    case R.id.additem_gameDuration : gameDuration = Integer.parseInt(Objects.requireNonNull(((AppCompatEditText) parentView.getChildAt(i)).getText()).toString());
                        break;
                    case R.id.additem_gameTypes : gameTypes = Objects.requireNonNull(((AppCompatEditText) parentView.getChildAt(i)).getText()).toString();
                        break;
                }
            }
        }

        if(gameName != null && nbMinPlayers != 0 && nbMaxPLayer != 0 && gameDuration != 0 && gameTypes != null) {
            CollGestDBHelper collGestDBHelper = new CollGestDBHelper(this);
            collGestDBHelper.updateGestItem(new CollGestItem(
                    itemGUID,
                    gameName,
                    nbMinPlayers,
                    nbMaxPLayer,
                    gameDuration,
                    gameTypes,
                    java.time.LocalDateTime.now().toString(),
                    "")

            );
        } else {
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  missing field +++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        }
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  onAddItemButtonClicked  end +++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
    }

    /***********************************************************************************************
     * Gestion du fragment "Listing"
     * *********************************************************************************************
     */

}