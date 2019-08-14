package com.example.live;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class LiveUtils {
    public static Post DesrializeToPost(QueryDocumentSnapshot document) {
        Map<String, Object> objectData = document.getData();
        Timestamp ts = (Timestamp)objectData.get("date");

        return (new Post((String)(objectData.get("title")), (String)(objectData.get("text")), (String)(objectData.get("image")), (String)(objectData.get("user")), ts.toDate()));
    }
}
