package fr.maxime_agusti.weightkeeper.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import fr.maxime_agusti.weightkeeper.R;
import fr.maxime_agusti.weightkeeper.entity.Record;

public class AddRecordDialogFragment extends DialogFragment {

    private AddRecordDialogListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mListener = (AddRecordDialogListener) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_record, null);

        final int orientation = getActivity().getRequestedOrientation();

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(view);
        builder.setTitle(R.string.new_record);
//        builder.setMessage(R.string.new_name_collection);
        builder.setPositiveButton(R.string.ok, null);
        builder.setNegativeButton(R.string.cancel, null);

        final EditText weightView = (EditText) view.findViewById(R.id.edit_weight);
//        final EditText instantView = (EditText) view.findViewById(R.id.edit_instant);


        final AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button ok = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button cancel = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Double weight = Double.parseDouble(weightView.getText().toString());
//                        String instant = instantView.getText().toString();

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
                        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                        String instant = simpleDateFormat.format(new Date());

                        Record record = new Record();
                        record.setWeight(weight);
                        record.setInstant(instant);

                        mListener.onAddRecord(record);

                        getActivity().setRequestedOrientation(orientation);
                        dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getActivity().setRequestedOrientation(orientation);
                        dismiss();
                    }
                });
            }
        });

        return dialog;

    }

    public interface AddRecordDialogListener {

        void onAddRecord(Record record);
    }
}
