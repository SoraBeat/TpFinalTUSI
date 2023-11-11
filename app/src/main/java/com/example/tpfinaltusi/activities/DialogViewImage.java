package com.example.tpfinaltusi.activities;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.example.tpfinaltusi.R;

public class DialogViewImage extends AppCompatDialogFragment {

    public Bitmap PICTURE_SELECTED;
    ImageView ivClose;
    public static DialogViewImage newInstance(Bundle arguments) {
        Bundle args = arguments;
        DialogViewImage fragment = new DialogViewImage();
        fragment.setArguments(args);
        return fragment;
    }

    public DialogViewImage() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Esta linea de código hace que tu DialogFragment sea Full screen
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_NoTitleBar);
        Bundle arguments = getArguments();
        PICTURE_SELECTED = arguments.getParcelable("PICTURE_SELECTED");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_view_image, container, false);

        ImageView ivImage = (ImageView)view.findViewById(R.id.ivImage);
        ImageView ivClose = (ImageView) view.findViewById(R.id.ivClose);
        if(PICTURE_SELECTED != null)
            ivImage.setImageBitmap(PICTURE_SELECTED);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return view;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), getTheme()){
            @Override
            public void onBackPressed() {
                // Aqui puedes capturar el OnBackPressed
                dismiss();
            }
        };
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

}