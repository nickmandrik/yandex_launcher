package com.yandex.mandrik.launcher.listappsactivity.appsfavorities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.yandex.mandrik.launcher.R;
import com.yandex.mandrik.launcher.listappsactivity.appsfavorities.recycler.adapter.CustomFavoritiesTouchListener;
import com.yandex.mandrik.launcher.listappsactivity.appsfavorities.recycler.adapter.FavoritesListAdapter;
import com.yandex.mandrik.launcher.settingsactivity.SettingsActivity;
import com.yandex.mandrik.launcher.util.clicker.RecyclerViewItemClickListener;
import com.yandex.mandrik.launcher.util.eventbus.ClearFavoritesEvent;
import com.yandex.mandrik.launcher.util.eventbus.FavoritesRecyclerChangedEvent;
import com.yandex.mandrik.launcher.util.layout.RecyclerSpanSizeLookup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import static com.yandex.mandrik.launcher.util.constants.LauncherConstants.*;

/**
 * Created by Home on 27.04.2017.
 */

public class AppsFavoritiesFragment extends Fragment {
    private Context context;

    private RecyclerView favoritesRecycler;
    private FavoritesListAdapter listAdapter;

    private EditText editUri;

    private EventBus bus = EventBus.getDefault();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bus.register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);

        context = rootView.getContext();

        editUri = (EditText) rootView.findViewById(R.id.editUri);

        setSpinner(rootView);

        editUri.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideSoftKeyboard(v);
                    editUri.setCursorVisible(false);
                } else {
                    editUri.setCursorVisible(true);
                }
            }
        });

        editUri.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(editUri.getText())));


                    if(intent.resolveActivity(context.getPackageManager()) != null) {
                        // save uri in the file
                        SharedPreferences appSettings =
                                context.getSharedPreferences(APP_PREFERENCE_MEMORABLE_URI, Context.MODE_PRIVATE);

                        int countUri = appSettings.getInt(COUNT_URI_IN_SETTING, 0);

                        SharedPreferences.Editor e = appSettings.edit();
                        e.putString(URI_NUMBER + new Integer(countUri + 1).toString(), String.valueOf(editUri.getText()));
                        e.putInt(COUNT_URI_IN_SETTING, countUri+1);
                        e.apply();

                        setSpinner(rootView);

                        // start activity
                        startActivity(intent);
                    } else {
                        Toast toast = Toast.makeText(context, R.string.incorrect_uri + " " + editUri.getText(), Toast.LENGTH_LONG);
                        TextView view = (TextView) toast.getView().findViewById(android.R.id.message);
                        view.setBackgroundColor(Color.DKGRAY);
                        view.setTextColor(Color.WHITE);
                        toast.show();
                    }

                    editUri.setText("");
                    editUri.clearFocus();
                    hideSoftKeyboard(editUri);
                    return true;
                }
                return false;
            }
        });

        favoritesRecycler = (RecyclerView) rootView.findViewById(R.id.favoritesRecyclerView);

        listAdapter = new FavoritesListAdapter();
        listAdapter.setHeader(getString(R.string.favorites));
        favoritesRecycler.setAdapter(listAdapter);
        favoritesRecycler.setHasFixedSize(true);


        /*ImageButton imageButton = (ImageButton) rootView.findViewById(R.id.open_settings_but);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSettings(v);
            }
        });*/


        /*favoritesRecycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editUri.setCursorVisible(false);
                hideSoftKeyboard(v);
            }
        });*/

        favoritesRecycler.addOnItemTouchListener(new CustomFavoritiesTouchListener(context, favoritesRecycler,
                new RecyclerViewItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (listAdapter.getAppInfoById(position) != null) {
                            /*remCountClicks(position);*/
                    Intent intent = context.getPackageManager().getLaunchIntentForPackage(
                            listAdapter.getAppInfoById(position).
                                    getPackageName().toString());
                    listAdapter.getAppInfoById(position).setCountClicks(
                            listAdapter.getAppInfoById(position)
                                    .getCountClicks() + 1
                    );
                    context.startActivity(intent);
                }
            }

            @Override
            public void onLongClick(View view, final int position) {
                new AlertDialog.Builder(context)
                        .setTitle(context.getString(R.string.delete_favorite))
                        .setMessage(context.getString(R.string.confirm_delete_favorite) +
                                listAdapter.getAppInfoById(position).getLabel() + "?")
                        .setIcon(listAdapter.getAppInfoById(position).getIcon())
                        .setPositiveButton(context.getString(R.string.delete), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                listAdapter.favoriteAppsList.remove(position-1);
                                listAdapter.notifyItemRemoved(position);
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
            }
        }));

        setLayoutManagerOnRecycler();

        return rootView;
    }

    public void setSpinner(View rootView) {
        SharedPreferences appSettings =
                context.getSharedPreferences(APP_PREFERENCE_RECYCLER_APPS_SETTINGS, Context.MODE_PRIVATE);

        Integer countShownUri = appSettings.getInt(COUNT_MEMORABLE_URI, 5);

        SharedPreferences uriSettings =
                context.getSharedPreferences(APP_PREFERENCE_MEMORABLE_URI, Context.MODE_PRIVATE);

        Integer countUri = uriSettings.getInt(COUNT_URI_IN_SETTING, 0);

        final Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner);

        int min = countShownUri;
        if(min > countUri) {
            min = countUri;
        }

        if(min > 0) {
            spinner.setVisibility(View.VISIBLE);

            String[] data = new String[min];

            for(int i = 0; i < min; i++) {
                data[i] = uriSettings.getString(URI_NUMBER + new Integer(countUri - i).toString(), "none");
            }

            ArrayAdapter<String> adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, data);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner.setAdapter(adapter);
            spinner.setPrompt(context.getString(R.string.last_uris));

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    editUri.setText(spinner.getSelectedItem().toString());
                }
                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
        } else if(spinner != null){
            spinner.setVisibility(View.INVISIBLE);
        }
    }





    public void hideSoftKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setLayoutManagerOnRecycler();
    }

    private void setLayoutManagerOnRecycler() {

        SharedPreferences appSettings = context.getSharedPreferences
                (APP_PREFERENCE_RECYCLER_APPS_SETTINGS, Context.MODE_PRIVATE);

        int countInRow = appSettings.getInt(COUNT_ELEMENTS_IN_ROW_LANDSCAPE, 6);
        if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            countInRow = appSettings.getInt(COUNT_ELEMENTS_IN_ROW_PORTRAIT, 4);
        }

        GridLayoutManager layoutManager = new GridLayoutManager(context, countInRow, GridLayoutManager.VERTICAL, false);
        layoutManager.setSpanSizeLookup(new RecyclerSpanSizeLookup(countInRow, favoritesRecycler.getAdapter()));

        favoritesRecycler.setLayoutManager(layoutManager);
    }

    public void startSettings(View view) {
        Intent intentMainApps = new Intent (context, SettingsActivity.class);
        this.startActivity(intentMainApps);
    }

    @Subscribe
    public void onEvent(FavoritesRecyclerChangedEvent event) {
        if(event.isInstall) {
            if(!listAdapter.favoriteAppsList.contains(event.appInfo)) {
                listAdapter.favoriteAppsList.add(event.appInfo);
                favoritesRecycler.getAdapter().notifyItemInserted(listAdapter.favoriteAppsList.size());
            }
        } else {
            if(listAdapter.favoriteAppsList.contains(event.appInfo)) {
                int pos = listAdapter.favoriteAppsList.indexOf(event.appInfo);
                listAdapter.favoriteAppsList.remove(event.appInfo);
                favoritesRecycler.getAdapter().notifyItemRemoved(pos + 1);
            }
        }
    }

    @Subscribe
    public void onEvent(ClearFavoritesEvent event) {
        listAdapter.favoriteAppsList = new ArrayList();
        favoritesRecycler.getAdapter().notifyAll();
    }
}
