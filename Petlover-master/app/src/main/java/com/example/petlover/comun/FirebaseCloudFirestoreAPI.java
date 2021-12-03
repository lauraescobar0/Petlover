package com.example.petlover.comun;


import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class FirebaseCloudFirestoreAPI {

    public static final String PathUsers="Usuarios";
    public static final String PathClinicas="Clinicas";
    public static final String PathPublicaciones="Publicaciones";
    public static final String PathServicios="Servicios";
    public static final String PathEvaluaciones ="Evaluaciones";
    public static final String PathFavoritas="Favoritos";

    private static FirebaseCloudFirestoreAPI instance;

    private FirebaseFirestore firestoreReference;

    private FirebaseCloudFirestoreAPI(){
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();

        firestoreReference = FirebaseFirestore.getInstance();
        firestoreReference.setFirestoreSettings(settings);
    }

    public static FirebaseCloudFirestoreAPI getFirestoreInstance(){
        if (instance ==null){
            instance = new FirebaseCloudFirestoreAPI();
        }
        return instance;
    }

    public FirebaseFirestore getFirestoreReference() {
        return firestoreReference;
    }

}
