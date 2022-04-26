package com.mi.coderchat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatListFragment extends Fragment {
    private ListView listView;
    private ArrayList<String> namesList;
    private ArrayAdapter mArrayAdapter;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    public  ArrayList<String> ids;

    FloatingActionMenu fabMenu;
    FloatingActionButton fabSettings;
    FloatingActionButton fabExit;
    FloatingActionButton fabShare;


    public ChatListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        listView = view.findViewById(R.id.ListviewChatList);
        namesList = new ArrayList<>();
        ids = new ArrayList<>();
        mArrayAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,namesList);
        listView.setAdapter(mArrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), MessageActivity.class);
                intent.putExtra("ReceiverId",ids.get(position));
                intent.putExtra("ReceiverName",namesList.get(position));
                startActivity(intent);
            }
        });
        mAuth= FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");



        userRef.child(mAuth.getCurrentUser().getUid()).child("ChatLists")
                .orderByValue().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists())
                {
                    String usernameid = snapshot.getKey();

                    userRef.child(usernameid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists() && snapshot.hasChild("Name"))
                            {
                                ids.add(0,snapshot.getKey());
                                namesList.add(0,snapshot.child("Name").getValue().toString());
                                mArrayAdapter.notifyDataSetChanged();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                //FAB
                fabMenu=view.findViewById(R.id.fabMenu);
                fabSettings=view.findViewById(R.id.fabSettings);
                fabExit=view.findViewById(R.id.fabExit);
                fabShare=view.findViewById(R.id.fabShare);

                fabShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                            String shareMessage= "\nLet me recommend you this application\n\n";
                            shareMessage = shareMessage + "https://play.google.com/store/apps/developer?id=MI_CODER"+"\n\n";
                            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                            startActivity(Intent.createChooser(shareIntent, "choose one"));
                        } catch(Exception e) {
                            //e.toString();
                        }
                        fabMenu.close(true);
                    }
                });
                fabSettings.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(), "Settings", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity(),SettingsActivity.class));
                        fabMenu.close(true);
                    }
                });
                fabExit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AlertDialog.Builder(getActivity())
                                .setMessage("Are you sure want to exit???")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        getActivity().finish();
                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();
                        fabMenu.close(true);
                    }
                });

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists())
                {
                    final String usernameid = snapshot.getKey();
                    userRef.child(usernameid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists() && snapshot.hasChild("Name"))
                            {
                                int index =ids.indexOf(usernameid);
                                namesList.remove(index);
                                ids.remove(index);
                                ids.add(0,usernameid);
                                namesList.add(0,snapshot.child("Name").getValue().toString());

                                mArrayAdapter.notifyDataSetChanged();


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        return  view;
    }
}