package com.dev.olivebakery.service.reservationService;

import com.dev.olivebakery.repository.ReservationRepository;
import com.dev.olivebakery.utill.Explain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Created by YoungMan on 2019-05-21.
 */

@Service
@RequiredArgsConstructor
public class ReservationDeleteService {

	private final ReservationRepository reservationRepository;

	@Explain("예약 정보 삭제")
	public void deleteReservation(Long reservationId) {
		reservationRepository.deleteById(reservationId);
	}
}
