package gr.teicrete.istlab.repros.model.db;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.List;

import gr.teicrete.istlab.repros.model.profiler.NonIntrusiveReadingSnapshot;
import gr.teicrete.istlab.repros.model.profiler.IntrusiveReadingSnapshot;

/**
 * Created by Antonis on 26-Dec-16.
 */

public class DBHandler {

    private DatabaseReference rootRef = null;
    private DatabaseReference roomRef = null;
    private DatabaseReference readingSnapshotsRef = null;
    private DatabaseReference recommendationsRef = null;

    private String roomId = null;
    private boolean intrusiveProfiling = false;


    public DBHandler(String roomId, boolean intrusiveProfiling) {
        this.roomId = roomId;
        this.intrusiveProfiling = intrusiveProfiling;

        rootRef = FirebaseDatabase.getInstance().getReference();

        if (intrusiveProfiling) {
            roomRef = rootRef.child("rooms").child(roomId);
        }
    }

    public void prepareForProfiling() {
        HashMap<String, Object> newRoomReadingMap = new HashMap<>();
        newRoomReadingMap.put("startTimestamp", System.currentTimeMillis());
        newRoomReadingMap.put("snapshots", null);
        newRoomReadingMap.put("recommendations", null);

        DatabaseReference readingsRoomRef;
        DatabaseReference newRoomReading;

        if (intrusiveProfiling) {
            readingsRoomRef = rootRef.child("readings").child(roomId);
        } else {
            readingsRoomRef = rootRef.child("readings").child("temp").child(roomId);
        }
        newRoomReading = readingsRoomRef.push();
        newRoomReading.setValue(newRoomReadingMap);

        String key = newRoomReading.getKey();

        createLastReadingRecordForRoom(roomId, key);

        readingSnapshotsRef = readingsRoomRef.child(key).child("snapshots");
        recommendationsRef = readingsRoomRef.child(key).child("recommendations");
    }

    private void createLastReadingRecordForRoom(String roomId, String lastReadingKey) {

        if (intrusiveProfiling) {
            rootRef.child("lastReadingsForRooms").child(roomId).setValue(lastReadingKey);
        } else {
            rootRef.child("lastReadingsForRooms").child("temp").child(roomId).setValue(lastReadingKey);
        }
    }

    public Query getLastReadingKeyRef() {

        Query lastReadingQuery;

        if (intrusiveProfiling) {
            lastReadingQuery = rootRef.child("lastReadingsForRooms").child(roomId);
        } else {
            lastReadingQuery = rootRef.child("lastReadingsForRooms").child("temp").child(roomId);
        }

        return lastReadingQuery;
    }

    public void removeTemporaryReadingsSnapshots() {
        if (!intrusiveProfiling) {
            DatabaseReference ref = rootRef.child("readings").child("temp").child(roomId);
            ref.removeValue();
            ref = rootRef.child("lastReadingsForRooms").child("temp").child(roomId);
            ref.removeValue();
        }
    }

    public DatabaseReference getRecommendationsRef(String lastReadingKey) {

        DatabaseReference ref;

        if (intrusiveProfiling) {
            ref = rootRef.child("readings").child(roomId).child(lastReadingKey).child("recommendations");
        } else {
            ref = rootRef.child("readings").child("temp").child(roomId).child(lastReadingKey).child("recommendations");
        }
        return ref;
    }

    public DatabaseReference getRoomRef() {
        return roomRef;
    }

    public Query getReadingSnapshotsRef(String lastReadingKey) {

        Query ref;

        if (intrusiveProfiling) {
            ref = rootRef.child("readings").child(roomId).child(lastReadingKey).child("snapshots").orderByKey();
        } else {
            ref = rootRef.child("readings").child("temp").child(roomId).child(lastReadingKey).child("snapshots").orderByKey();
        }

        return ref;
    }

    public void pushNewIntrusiveReadingSnapshot(IntrusiveReadingSnapshot intrusiveReadingSnapshot) {
        if (readingSnapshotsRef != null) {
            readingSnapshotsRef.push().setValue(intrusiveReadingSnapshot);
        }
    }

    public void pushNewNonIntrusiveReadingSnapshot(NonIntrusiveReadingSnapshot nonIntrusiveReadingSnapshot) {
        if (readingSnapshotsRef != null) {
            readingSnapshotsRef.push().setValue(nonIntrusiveReadingSnapshot);
        }
    }

    public void pushRecommendations(List<String> recommendations) {
        if (recommendationsRef != null) {
            recommendationsRef.setValue(recommendations);
        }
    }
}


