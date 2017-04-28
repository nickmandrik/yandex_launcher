package com.yandex.mandrik.launcher.listappsactivity.appsrecycler.recycler.clicker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

import com.yandex.mandrik.launcher.listappsactivity.appsrecycler.recycler.ApplicationsRecycler;

import com.yandex.mandrik.launcher.R;

/**
 * Created by Home on 22.03.2017.
 */

public class DeleteConfirmOption {

    public static AlertDialog askOption(final Context context, final ApplicationsRecycler appRecyclerView,
                                        final int position) {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.delete))
                .setMessage(context.getString(R.string.confirm_delete) +
                        appRecyclerView.getAdapter().getAppInfoById(position).getLabel() + "?")
                .setIcon(R.drawable.yandex_icon)
                .setPositiveButton(context.getString(R.string.delete), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Uri uri = Uri.fromParts("package", appRecyclerView.getAdapter()
                                .getAppInfoById(position).getPackageName(), null);
                        Intent it = new Intent(Intent.ACTION_DELETE, uri);
                        context.startActivity(it);
                        appRecyclerView.getAdapter().remove(position);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                })
                .create();

        return alertDialog;
    }

    public static AlertDialog cannotDelete(final Context context, final ApplicationsRecycler appRecyclerView,
                                        final int position) {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.delete))
                .setMessage("Невозможно удалить приложение " +
                        appRecyclerView.getAdapter().getAppInfoById(position).getLabel() + ".")
                .setIcon(R.drawable.yandex_icon)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                })
                .create();

        return alertDialog;
    }

}