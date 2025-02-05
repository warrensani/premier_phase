package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

public class Detail extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        CardView btnAjouterCommande = findViewById(R.id.btnAjouterCommande);


        String client = getIntent().getStringExtra("client");
        String produit = getIntent().getStringExtra("produit");
        int quantite = getIntent().getIntExtra("quantite", 0);
        double prixTotal = getIntent().getDoubleExtra("prixTotal", 0.0);

        // Création d'un objet Commande avec les données reçues
        String idCommande = UUID.randomUUID().toString();
        Commande commande = new Commande(idCommande, client, produit, quantite, prixTotal);

        btnAjouterCommande.setOnClickListener(v -> ajouterCommande(commande));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.page_1) {
                    startActivity(new Intent(Detail.this, home.class));
                    return true;
                } else if (id == R.id.page_2) {
                    startActivity(new Intent(Detail.this, liste.class));
                    return true;
                }

                return false;
            }



        });





    }


    private void ajouterCommande(Commande commande) {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("ajout de la commande en cours...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        db.collection("commandes").document(commande.getId())
                .set(commande)
                .addOnSuccessListener(aVoid ->
                        {
                            progressDialog.dismiss();
                            Toast.makeText(this, "Commande ajoutée !", Toast.LENGTH_SHORT).show();
                        }
                )
                .addOnFailureListener(e ->

                        {
                            progressDialog.dismiss();
                            Toast.makeText(this, "Erreur d'ajout", Toast.LENGTH_SHORT).show();
                        }
                );
    }

}