package com.dev.olivebakery.service.reservationService;

import com.dev.olivebakery.domain.dto.ReservationDto;
import com.dev.olivebakery.exception.UserDefineException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YoungMan on 2019-05-21.
 */

@Service
public class ReservationConverterService {

	/**
	 * ReservationResponseTemp -> ReservationResponse
	 */
	public static ReservationDto.ReservationResponse convertGetTmpDtoToGetDto(List<ReservationDto.ReservationResponseTemp> reservationResponseTemps) {

		if(ObjectUtils.isEmpty(reservationResponseTemps)) {
			throw new UserDefineException("예약 내역이 없습니다.");
		}
		List<ReservationDto.ReservationBread> reservationBreads = new ArrayList<>();

		for (ReservationDto.ReservationResponseTemp reservationResponseTemp : reservationResponseTemps) {
			reservationBreads.add(ReservationDto.ReservationBread.of(reservationResponseTemp));
		}
		return ReservationDto.ReservationResponse.of(reservationResponseTemps.get(0), reservationBreads);
	}

	/**
	 * List<GetTempDto> -> List<GetDto>
	 */
	public static List<ReservationDto.ReservationResponse> convertGetTempDtoListToGetDtoList(List<ReservationDto.ReservationResponseTemp> reservationResponseTemps) {

		List<ReservationDto.ReservationResponse> reservationResponses = new ArrayList<>();
		List<ReservationDto.ReservationBread> reservationBreads = new ArrayList<>();
		Long reservationId = reservationResponseTemps.get(0).getReservationId();

		for (ReservationDto.ReservationResponseTemp reservationResponseTemp : reservationResponseTemps) {
			if (reservationResponseTemp.getReservationId().equals(reservationId)) {
				reservationBreads.add(ReservationDto.ReservationBread.of(reservationResponseTemp));

				if (reservationResponseTemps.indexOf(reservationResponseTemp) == reservationResponseTemps.size() - 1) {
					reservationResponses.add(ReservationDto.ReservationResponse.of(reservationResponseTemps.get(reservationResponseTemps.indexOf(reservationResponseTemp)),
							reservationBreads)
					);
				}
				continue;
			}
			reservationResponses.add(ReservationDto.ReservationResponse.of(reservationResponseTemps.get(reservationResponseTemps.indexOf(reservationResponseTemp) - 1),
					reservationBreads));

			reservationId = reservationResponseTemp.getReservationId();
			reservationBreads = new ArrayList<>();
			reservationBreads.add(ReservationDto.ReservationBread.of(reservationResponseTemp));

			if (reservationResponseTemps.indexOf(reservationResponseTemp) == reservationResponseTemps.size() - 1) {
				reservationResponses.add(ReservationDto.ReservationResponse.of(reservationResponseTemps.get(reservationResponseTemps.indexOf(reservationResponseTemp)),
						reservationBreads)
				);
			}
		}
		return reservationResponses;
	}
}
