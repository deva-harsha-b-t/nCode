package com.dtl.ncode.adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.dtl.ncode.fragCodes;
import com.dtl.ncode.fragImages;
import com.dtl.ncode.fragLinks;
import com.dtl.ncode.fragText;
import com.dtl.ncode.model.sharedViewModel;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class stateAdapter extends FragmentStateAdapter {
    TabLayout tabLayout;
    sharedViewModel shModel;



    public stateAdapter(@NonNull FragmentActivity fragmentActivity , ViewModelStoreOwner owner) {
        super(fragmentActivity);
        this.tabLayout = tabLayout;
        shModel = new ViewModelProvider(owner).get(sharedViewModel.class);

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position) {
            case 0:
                return fragText.newInstance();
            case 1:
                return fragImages.newInstance();
            case 2:
                return fragCodes.newInstance();
            default:
                return fragLinks.newInstance();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
