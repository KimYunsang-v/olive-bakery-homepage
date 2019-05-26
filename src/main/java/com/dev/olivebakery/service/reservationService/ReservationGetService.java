package com.dev.olivebakery.service.reservationService;

import com.dev.olivebakery.domain.dto.ReservationDto;
import com.dev.olivebakery.domain.entity.Reservation;
import com.dev.olivebakery.domain.enums.ReservationType;
import com.dev.olivebakery.exception.UserDefineException;
import com.dev.olivebakery.repository.ReservationRepository;
import com.dev.olivebakery.utill.DateUtils;
import com.dev.olivebakery.utill.Explain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by YoungMan on 2019-05-21.
 */

@Service
@RequiredArgsConstructor
public class ReservationGetService {

	private final ReservationRepository reservationRepository;

	public Reservation findById(Long reservationId) {
		return reservationRepository.findById(reservationId).orElseThrow(() -> new UserDefineException("해당 예약내역이 없습니다."));
	}

	@Explain("유저의 모든 예약내역을 예약타입별로 가져옴 ")
	public List<ReservationDto.ReservationResponse> getReservationInfos(String email, ReservationType reservationType) {
		List<ReservationDto.ReservationResponseTemp> reservationResponseTemps = reservationRepository.getReservationInfos(email,
				reservationType)
		;

		return ReservationConverterService.convertGetTempDtoListToGetDtoList(reservationResponseTemps);
	}

	@Explain("유저의 가장 최근 예약내역을 예약타입에 무관하게 조회")
	public ReservationDto.ReservationResponse getReservationInfoByRecently(String email) {
		List<ReservationDto.ReservationResponseTemp> reservationResponseTemps = reservationRepository.getReservationInfoByRecently(email);
		return ReservationConverterService.convertGetTmpDtoToGetDto(reservationResponseTemps);
	}

	@Explain("날짜별 예약 조회, Admin 에서 사용")
	public List<ReservationDto.ReservationResponse> getReservationInfosByDate(ReservationDto.ReservationDateRequest reservationDateRequest) {
		List<ReservationDto.ReservationResponseTemp> reservationResponseTemps = reservationRepository.getReservationInfosByDate(
				reservationDateRequest.getReservationType(),
				DateUtils.getStartOfDay(reservationDateRequest.getSelectDate()),
				DateUtils.getEndOfDay(reservationDateRequest.getSelectDate())
		);

		return ReservationConverterService.convertGetTempDtoListToGetDtoList(reservationResponseTemps);
	}

	@Explain("날짜구간별 예약 조회, Admin 에서 사용")
	public List<ReservationDto.ReservationResponse> getReservationInfosByDateRange(ReservationDto.ReservationDateRangeRequest reservationDateRangeRequest) {
		List<ReservationDto.ReservationResponseTemp> reservationResponseTemps = reservationRepository.getReservationInfosByDate(
				reservationDateRangeRequest.getReservationType(),
				DateUtils.getStartOfDay(reservationDateRangeRequest.getStartDate()),
				DateUtils.getEndOfDay(reservationDateRangeRequest.getEndDate())
		);

		return ReservationConverterService.convertGetTempDtoListToGetDtoList(reservationResponseTemps);
	}
}
