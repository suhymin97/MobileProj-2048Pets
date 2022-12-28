package dsa.hcmiu.a2048pets.profile_shop;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import dsa.hcmiu.a2048pets.R;

public class DialogNoti extends DialogFragment {

    TextView tvMessage;
    Button btnClose;

    public static DialogNoti newInstance(){
        DialogNoti dialogNoti = new DialogNoti();
        return dialogNoti;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.noti_dialog,container);
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {

        btnClose = (Button) view.findViewById(R.id.btn_close);
        tvMessage = (TextView) view.findViewById(R.id.tvMessage);

        super.onViewCreated(view, savedInstanceState);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    getDialog().dismiss();
            }
        });
    }
}
