package com.anshansh.task3;

import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabsAccessAdapter extends FragmentPagerAdapter {


    /**public TabsAccessAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }*/

    public TabsAccessAdapter(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case 0:
                ChatsFragment chatsFragment = new ChatsFragment();
                return chatsFragment;

            case 1:
                GroupsFragment groupsFragment = new GroupsFragment();
                return groupsFragment;

            case 2:
                ContactsFragment contactsFragment = new ContactsFragment();
                return contactsFragment;

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position)
        {
            case 0:
                //ChatsFragment chatsFragment = new ChatsFragment();
                return "Chats";

            case 1:
                //GroupsFragment groupsFragment = new GroupsFragment();
                return "Groups";

            case 2:
                //ContactsFragment contactsFragment = new ContactsFragment();
                return "Contacts";

            default:
                return null;
        }


    }
}
