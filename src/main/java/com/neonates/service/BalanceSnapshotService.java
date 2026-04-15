package com.neonates.service;

import com.neonates.request.BalanceSnapshotRequestDTO;
import com.neonates.response.BalanceSnapshotResponseDTO;

import java.util.List;

public interface BalanceSnapshotService {

    BalanceSnapshotResponseDTO createSnapshot(BalanceSnapshotRequestDTO dto);

    List<BalanceSnapshotResponseDTO> getSnapshotsByDate(Integer dateId);

    BalanceSnapshotResponseDTO getLatestSnapshot();

    List<BalanceSnapshotResponseDTO> getSnapshotsByUser(Long userId);
}
