package ua.ck.android.geekhub.mclaut.ui;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;

import ua.ck.android.geekhub.mclaut.R;
import ua.ck.android.geekhub.mclaut.app.McLautApplication;
import ua.ck.android.geekhub.mclaut.data.Repository;
import ua.ck.android.geekhub.mclaut.data.model.UserCharacteristic;
import ua.ck.android.geekhub.mclaut.ui.authorization.LoginActivity;

/**
 * Created by Sergo on 12/17/17.
 */

public class RemoveUserDialogFragment extends DialogFragment {
    NavigationView mNavigationView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle paramsBundle = getArguments();
        String userId = paramsBundle.getString(getResources()
                .getString(R.string.btn_tag_key_id));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_text)
                .setPositiveButton(R.string.dialog_button_delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(Repository.getInstance().getMapUsersCharacteristic()
                                .getValue().size() == Repository.ONE_USER){

                            Intent intent = new Intent(McLautApplication.getContext(),
                                    LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            getActivity().finish();
                            McLautApplication.selectUser("NULL");

                            Repository.getInstance().deleteUserFromDatabase(userId);
                        } else {
                            Object[] tempArray = Repository.getInstance()
                                    .getMapUsersCharacteristic().getValue().values().toArray();

                            UserCharacteristic nextUser = (UserCharacteristic) tempArray[0];
                            if(!nextUser.getInfo().getId().equals(userId)) {
                                McLautApplication.selectUser(nextUser.getInfo().getId());
                            } else {
                                nextUser = (UserCharacteristic) tempArray[1];
                                McLautApplication.selectUser(nextUser.getInfo().getId());
                            }

                            Repository.getInstance().deleteUserFromDatabase(userId);

                            mNavigationView = getActivity().findViewById(R.id.drawer_navigation_view);

                            Menu drawerMenu = mNavigationView.getMenu();
                            drawerMenu.removeItem(R.id.users_list +
                                    paramsBundle.getInt(getResources()
                                            .getString(R.string.btn_tag_key_item_index)));
                        }
                    }
                })
                .setNegativeButton(R.string.dialog_button_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {}
                });

        return builder.create();
    }
}
