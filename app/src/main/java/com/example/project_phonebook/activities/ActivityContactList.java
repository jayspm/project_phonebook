package com.example.project_phonebook.activities;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_phonebook.ContactListAdapter;
import com.example.project_phonebook.R;
import com.example.project_phonebook.controllers.SwipeController;
import com.example.project_phonebook.controllers.SwipeControllerActions;
import com.example.project_phonebook.database.PhonebookDatabase;
import com.example.project_phonebook.models.Contact;
import com.example.project_phonebook.models.MyHash;
import com.example.project_phonebook.models.MyHashModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.project_phonebook.database.PhonebookDatabase.getDbInstance;

public class ActivityContactList extends AppCompatActivity {

    PhonebookDatabase _db;
    private String TAG = this.getClass().getSimpleName();
    private MyHashModel hash;
    private RecyclerView _recyclerView;
    private Button btnSortAToZ;
    private Button btnSortZToA;
    private FloatingActionButton btnAddNewContact;
    private ContactListAdapter adapter;
    SwipeController swipeController = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        setContentView(R.layout.activity_listdisplayed);

        // set title bar
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Contact");
        }

        // connect to database
        _db = getDbInstance(this);
        //initializeDb();

        // get data from db, and hash it.
        final ArrayList<Contact> allContacts = (ArrayList<Contact>) _db.contactDao().getAllContactsForModel();
        hash = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MyHashModel.class);
        if(hash.myHash == null) {
            Log.d(TAG, "ViewModel has not been created yet.");
            hash.myHash = new MyHash();
            hash.myHash.buildHashTable(allContacts);
        } else {
            Log.d(TAG, "ViewModel has been created.");
        }

        // create hash list to adapter then set adapter to recyclerview
        _recyclerView = findViewById(R.id.recycler_view_main_list);
        adapter = new ContactListAdapter(hash.myHash.toList(false), ActivityContactList.this);
        _recyclerView.setAdapter(adapter);
        _recyclerView.setLayoutManager(new LinearLayoutManager(this));
        _recyclerView.setItemAnimator(new DefaultItemAnimator());

        // set on item click listener to adapter, so when click on item, it will bring users to detail page
        adapter.setOnItemClickListener(new ContactListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                int getItemId = adapter.getId(position);
                Intent detailContactIntent = new Intent(getApplicationContext(), ActivityContactDetail.class);
                detailContactIntent.putExtra("id", getItemId);
                startActivity(detailContactIntent);
                Log.d(TAG, "For sending to show detail page, here is get item id => " + getItemId);
            }
        });

        // swipe for action go to edit page
        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                //adapter.dataSet.remove(position);
                //adapter.notifyItemRemoved(position);
                //adapter.notifyItemRangeChanged(position, adapter.getItemCount());

                int getItemId = adapter.getId(position);
                Intent updateContactIntent = new Intent(ActivityContactList.this, ActivityUpdateContact.class);
                updateContactIntent.putExtra("id", getItemId);
                startActivity(updateContactIntent);
                Log.d(TAG, "For sending to update detail page, here is get item id => " + getItemId);
            }
        });

        // drag and drop
        ItemTouchHelper dragNDropHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
           @Override
           public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder dragged, @NonNull RecyclerView.ViewHolder target) {
               int positionDrag = dragged.getAdapterPosition();
               int positionTarget = target.getAdapterPosition();

               Collections.swap(adapter.dataset, positionDrag, positionTarget);

               adapter.notifyItemMoved(positionDrag, positionTarget);

               return false;
           }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

            }
        });

        // attach swipe and drag and drop to recyclerview
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(_recyclerView);
        dragNDropHelper.attachToRecyclerView(_recyclerView);

        _recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });

        // search item
        SearchView searchView = (SearchView) findViewById(R.id.list_display_search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String wantedStr) {
                if(wantedStr != null && !wantedStr.isEmpty()){
                    ActivityContactList.this.adapter.reloadContactList(hash.myHash.shortList(wantedStr));
                } else {
                    Toast.makeText(ActivityContactList.this, "No Match found",Toast.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String wantedStr) {
                ActivityContactList.this.adapter.reloadContactList(hash.myHash.shortList(wantedStr));
                return false;
            }
        });

        // register buttons
        btnAllNavClickListener();
        btnSortAToZClick();
        btnSortZToAClick();
        btnAddNewContactClick();
    }

    private void btnAddNewContactClick() {
        btnAddNewContact = findViewById(R.id.list_display_btn_add);
        btnAddNewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addNewContactIntent = new Intent(ActivityContactList.this, ActivityAddContact.class);
                startActivity(addNewContactIntent);
            }
        });
    }

    private void btnSortAToZClick() {
        btnSortAToZ = findViewById(R.id.list_display_btn_a_to_z);
        btnSortAToZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityContactList.this.adapter.reloadContactList(hash.myHash.toList(false));
            }
        });
    }

    private void btnSortZToAClick() {
        btnSortZToA = findViewById(R.id.list_display_btn_z_to_a);
        btnSortZToA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityContactList.this.adapter.reloadContactList(hash.myHash.toList(true));
            }
        });
    }

    private void navBtnClick(int key) {
        if (key < 0 || key > 26) {
            return;
        }
        // calculate the offset based on key
        int offset = hash.myHash.calcOffsetByKey(key);
        Log.d(TAG, "offset( " + key + ") = " + offset);

        // scroll the view
        ((LinearLayoutManager)_recyclerView.getLayoutManager()).scrollToPositionWithOffset(offset, 0);
    }

    private void btnAllNavClickListener(){
        findViewById(R.id.btn_main_nav_ee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityContactList.this.navBtnClick(0);
            }
        });
        findViewById(R.id.btn_main_nav_a).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityContactList.this.navBtnClick(1);
            }
        });
        findViewById(R.id.btn_main_nav_b).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityContactList.this.navBtnClick(2);
            }
        });
        findViewById(R.id.btn_main_nav_c).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityContactList.this.navBtnClick(3);
            }
        });
        findViewById(R.id.btn_main_nav_d).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityContactList.this.navBtnClick(4);
            }
        });
        findViewById(R.id.btn_main_nav_e).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityContactList.this.navBtnClick(5);
            }
        });
        findViewById(R.id.btn_main_nav_f).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityContactList.this.navBtnClick(6);
            }
        });
        findViewById(R.id.btn_main_nav_g).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityContactList.this.navBtnClick(7);
            }
        });
        findViewById(R.id.btn_main_nav_h).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityContactList.this.navBtnClick(8);
            }
        });
        findViewById(R.id.btn_main_nav_i).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityContactList.this.navBtnClick(9);
            }
        });
        findViewById(R.id.btn_main_nav_j).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityContactList.this.navBtnClick(10);
            }
        });
        findViewById(R.id.btn_main_nav_k).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityContactList.this.navBtnClick(11);
            }
        });
        findViewById(R.id.btn_main_nav_l).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityContactList.this.navBtnClick(12);
            }
        });
        findViewById(R.id.btn_main_nav_m).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityContactList.this.navBtnClick(13);
            }
        });
        findViewById(R.id.btn_main_nav_n).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityContactList.this.navBtnClick(14);
            }
        });
        findViewById(R.id.btn_main_nav_o).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityContactList.this.navBtnClick(15);
            }
        });
        findViewById(R.id.btn_main_nav_p).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityContactList.this.navBtnClick(16);
            }
        });
        findViewById(R.id.btn_main_nav_q).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityContactList.this.navBtnClick(17);
            }
        });
        findViewById(R.id.btn_main_nav_r).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityContactList.this.navBtnClick(18);
            }
        });
        findViewById(R.id.btn_main_nav_s).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityContactList.this.navBtnClick(19);
            }
        });
        findViewById(R.id.btn_main_nav_t).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityContactList.this.navBtnClick(20);
            }
        });
        findViewById(R.id.btn_main_nav_u).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityContactList.this.navBtnClick(21);
            }
        });
        findViewById(R.id.btn_main_nav_v).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityContactList.this.navBtnClick(22);
            }
        });
        findViewById(R.id.btn_main_nav_w).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityContactList.this.navBtnClick(23);
            }
        });
        findViewById(R.id.btn_main_nav_x).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityContactList.this.navBtnClick(24);
            }
        });
        findViewById(R.id.btn_main_nav_y).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityContactList.this.navBtnClick(25);
            }
        });
        findViewById(R.id.btn_main_nav_z).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityContactList.this.navBtnClick(26);
            }
        });
    }

    private void initializeDb() {
        // empty all tables
        _db.contactDao().clearTable();

        //contactArrayList = new ArrayList<>();
        //contactArrayList.add()

        // add data to table students
        _db.contactDao().insertContacts(
                new Contact("Ricky Jay", "0478032000", "rj@gmail.com", "12/12/1998"),
                new Contact("Keissku Higa", "0491570156", "khiga@gmail.com", "1/1/1987"),
                new Contact("Nack Nuttawut", "0475102315", "nnuttawut@gmail.com", "2/2/1897"),
                new Contact("Stefany Salazar", "0495412365", "ssalazar@gmail.com", "3/3/1956"),
                new Contact("Nicky Micky", "0432154789", "nmicky@gmail.com", "4/4/1899")
        );
    }
}