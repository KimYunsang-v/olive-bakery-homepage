package com.dev.olivebakery.service.reservationService;

import com.dev.olivebakery.domain.dto.ReservationDto;
import com.dev.olivebakery.domain.entity.Reservation;
import com.dev.olivebakery.exception.UserDefineException;
import com.dev.olivebakery.repository.ReservationRepository;
import com.dev.olivebakery.utill.Explain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Created by YoungMan on 2019-05-21.
 */

@Service
@RequiredArgsConstructor
public class ReservationUpdateService {

	private final ReservationRepository reservationRepository;
	private final ReservationDeleteService reservationDeleteService;
	private final ReservationSaveService reservationSaveService;

	@Explain("예약 정보 수정 // 삭제 후 재저장")
	public void updateReservation(ReservationDto.ReservationUpdateRequest reservationUpdateRequest) {
		reservationDeleteService.deleteReservation(reservationUpdateRequest.getReservationId());
		reservationSaveService.saveReservation(reservationUpdateRequest.getReservationSaveRequest());
	}

	@Explain("예약 상태 수정")
	public void updateReservationType(Long reservationId) {
		Reservation reservation = reservationRepository.findById(reservationId)
				.orElseThrow((() -> new UserDefineException("해당 예약이 존재하지 않습니다.")));

		reservation.updateReservationType();
		reservationRepository.save(reservation);
	}
}
