package com.example.studenttrack;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ResourcesActivity extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST = 1;

    private Button uploadButton;
    private ListView resourceListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> resourceList;
    private DatabaseReference resourcesRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);

        uploadButton = findViewById(R.id.uploadButton);
        resourceListView = findViewById(R.id.resourceListView);

        resourcesRef = FirebaseDatabase.getInstance().getReference().child("resources");

        resourceList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, resourceList);
        resourceListView.setAdapter(adapter);

        loadResourcesFromDatabase();

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri fileUri = data.getData();
            if (fileUri != null) {
                String fileName = fileUri.getLastPathSegment();

                String downloadUrl = fileUri.toString();
                saveResourceToDatabase(fileName, downloadUrl);
            }
        }
    }

    private void saveResourceToDatabase(String fileName, String downloadUrl) {
        String key = resourcesRef.push().getKey();
        resourcesRef.child(key).child("fileName").setValue(fileName);
        resourcesRef.child(key).child("downloadUrl").setValue(downloadUrl)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ResourcesActivity.this, "Resource uploaded successfully.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ResourcesActivity.this, "Failed to upload resource.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadResourcesFromDatabase() {
        resourcesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                resourceList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String fileName = snapshot.child("fileName").getValue(String.class);
                    resourceList.add(fileName);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors
                Toast.makeText(ResourcesActivity.this, "Failed to load resources.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
