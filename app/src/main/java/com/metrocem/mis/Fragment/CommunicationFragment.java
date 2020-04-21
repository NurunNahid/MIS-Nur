package com.metrocem.mis.Fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.metrocem.mis.Activity.PushNotificationActivity;
import com.metrocem.mis.Activity.SMSActivity;
import com.metrocem.mis.R;

public class CommunicationFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_communication, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(R.string.communication);

        LinearLayout pushNotificationBtn = view.findViewById(R.id.pushNotificationBtn);
        pushNotificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PushNotificationActivity.class);
                getActivity().startActivity(intent);
            }
        });

        LinearLayout smsBtn = view.findViewById(R.id.smsBtn);
        smsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SMSActivity.class);
                getActivity().startActivity(intent);
            }
        });

    }
}
