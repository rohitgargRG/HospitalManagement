package com.example.HospitalManagement.Event;

import com.example.HospitalManagement.Entity.Block;
import com.example.HospitalManagement.Entity.BlockId;
import com.example.HospitalManagement.Entity.Room;
import com.example.HospitalManagement.Repository.BlockRepository;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler(Room.class)
public class RoomEventHandler {

    private final BlockRepository blockRepository;

    public RoomEventHandler(BlockRepository blockRepository) {
        this.blockRepository = blockRepository;
    }

    @HandleBeforeCreate
    @HandleBeforeSave
    public void ensureBlockExists(Room room) {
        if (room.getBlockFloor() == null || room.getBlockCode() == null) {
            return;
        }
        BlockId id = new BlockId(room.getBlockFloor(), room.getBlockCode());
        if (!blockRepository.existsById(id)) {
            blockRepository.save(new Block(room.getBlockFloor(), room.getBlockCode()));
        }
    }
}
