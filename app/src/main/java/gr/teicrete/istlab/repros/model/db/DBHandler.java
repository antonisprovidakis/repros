package gr.teicrete.istlab.repros.model.db;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import gr.teicrete.istlab.repros.model.profiler.ReadingsSnapshot;

/**
 * Created by Antonis on 26-Dec-16.
 */

public class DBHandler {

    private DatabaseReference rootRef = null;
    private DatabaseReference roomProfileRef = null;
    private DatabaseReference roomReadingSnapshotsRef = null;

    private String roomId;

    public DBHandler(String roomId) {
        this.roomId = roomId;

        rootRef = FirebaseDatabase.getInstance().getReference();
        roomProfileRef = rootRef.child("rooms").child(roomId);
    }

    public void prepareForProfiling(){
        HashMap<String, Object> newRoomReadingMap = new HashMap<>();
        newRoomReadingMap.put("startTimestamp", System.currentTimeMillis());
        newRoomReadingMap.put("snapshots", null);
        DatabaseReference newRoomReading = rootRef.child("readings").child(roomId).push();
        newRoomReading.setValue(newRoomReadingMap);

        String key = newRoomReading.getKey();
        roomReadingSnapshotsRef = rootRef.child("readings").child(roomId).child(key).child("snapshots");
    }

    public DatabaseReference getRoomProfileRef() {
        return roomProfileRef;
    }

    public DatabaseReference getRoomReadingSnapshotsRef() {
        return roomReadingSnapshotsRef;
    }

    public void pushNewReadingsSnapshot(ReadingsSnapshot readingsSnapshot) {
        roomReadingSnapshotsRef.push().setValue(readingsSnapshot);
    }
}
