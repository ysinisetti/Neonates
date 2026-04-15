package com.neonates.service.implementation;

import com.neonates.entity.AppUser;
import com.neonates.entity.BalanceSnapshot;
import com.neonates.entity.DimDate;
import com.neonates.exception.BadRequestException;
import com.neonates.exception.DuplicateResourceException;
import com.neonates.exception.ResourceNotFoundException;
import com.neonates.mapper.BalanceSnapshotMapper;
import com.neonates.repository.AppUserRepository;
import com.neonates.repository.BalanceSnapshotRepository;
import com.neonates.repository.DimDateRepository;
import com.neonates.request.BalanceSnapshotRequestDTO;
import com.neonates.response.BalanceSnapshotResponseDTO;
import com.neonates.service.BalanceSnapshotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BalanceSnapshotServiceImpl implements BalanceSnapshotService {

    private final BalanceSnapshotRepository balanceSnapshotRepository;
    private final AppUserRepository appUserRepository;
    private final DimDateRepository dimDateRepository;
    private final BalanceSnapshotMapper balanceSnapshotMapper;

    @Override
    public BalanceSnapshotResponseDTO createSnapshot(BalanceSnapshotRequestDTO dto) {
        // Validate dateId format and existence
        validateDateId(dto.getDateId());

        // Check for duplicate snapshot for the same date
        List<BalanceSnapshot> existingSnapshots = balanceSnapshotRepository.findByDateId(dto.getDateId());
        if (!existingSnapshots.isEmpty()) {
            throw new DuplicateResourceException("Balance snapshot already exists for date: " + dto.getDateId());
        }

        // Validate capturedBy is provided
        if (dto.getCapturedBy() == null) {
            throw new BadRequestException("Captured by user is required");
        }

        // Validate user exists
        AppUser capturedByUser = appUserRepository.findById(dto.getCapturedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getCapturedBy()));

        // Validate amounts are not negative
        if (dto.getBankAmount() != null && dto.getBankAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Bank amount cannot be negative");
        }
        if (dto.getFdAmount() != null && dto.getFdAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("FD amount cannot be negative");
        }

        // Map DTO to entity
        BalanceSnapshot balanceSnapshot = balanceSnapshotMapper.toEntity(dto);
        balanceSnapshot.setCapturedBy(capturedByUser);

        // Default missing amounts to 0
        if (balanceSnapshot.getBankAmount() == null) {
            balanceSnapshot.setBankAmount(BigDecimal.ZERO);
        }
        if (balanceSnapshot.getFdAmount() == null) {
            balanceSnapshot.setFdAmount(BigDecimal.ZERO);
        }

        BalanceSnapshot savedSnapshot = balanceSnapshotRepository.save(balanceSnapshot);
        return balanceSnapshotMapper.toDTO(savedSnapshot);
    }

    @Override
    public List<BalanceSnapshotResponseDTO> getSnapshotsByDate(Integer dateId) {
        // Validate dateId format and existence
        validateDateId(dateId);

        List<BalanceSnapshot> snapshots = balanceSnapshotRepository.findByDateId(dateId);
        return snapshots.stream()
                .map(balanceSnapshotMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BalanceSnapshotResponseDTO getLatestSnapshot() {
        BalanceSnapshot latestSnapshot = balanceSnapshotRepository.findTopByOrderByCreatedAtDesc()
                .orElseThrow(() -> new ResourceNotFoundException("No balance snapshot found"));
        return balanceSnapshotMapper.toDTO(latestSnapshot);
    }

    @Override
    public List<BalanceSnapshotResponseDTO> getSnapshotsByUser(Long userId) {
        // Validate user exists
        appUserRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        List<BalanceSnapshot> snapshots = balanceSnapshotRepository.findByCapturedByUserId(userId);
        return snapshots.stream()
                .map(balanceSnapshotMapper::toDTO)
                .collect(Collectors.toList());
    }

    private void validateDateId(Integer dateId) {
        if (dateId == null) {
            throw new BadRequestException("Date ID cannot be null");
        }
        if (dateId < 10000000 || dateId > 99999999) {
            throw new BadRequestException("Date ID must be in YYYYMMDD format (e.g., 20240101)");
        }

        // Additional validation: check if it's a valid date
        int year = dateId / 10000;
        int month = (dateId % 10000) / 100;
        int day = dateId % 100;

        if (month < 1 || month > 12) {
            throw new BadRequestException("Invalid month in date ID: " + dateId);
        }
        if (day < 1 || day > 31) {
            throw new BadRequestException("Invalid day in date ID: " + dateId);
        }

        // Check existence in dim_date table, insert if not exists
        if (!dimDateRepository.existsById(dateId)) {
            // Create and save the dim_date entry
            DimDate dimDate = DimDate.builder()
                    .dateId(dateId)
                    .date(LocalDate.of(year, month, day))
                    .day((byte) day)
                    .month((byte) month)
                    .quarter((byte) ((month - 1) / 3 + 1))
                    .year((short) year)
                    .fiscalYear((short) year) // Assuming fiscal year = calendar year
                    .fiscalMonth((byte) month) // Assuming fiscal month = calendar month
                    .isMonthEnd(day == LocalDate.of(year, month, day).lengthOfMonth())
                    .build();
            dimDateRepository.save(dimDate);
        }
    }
}
