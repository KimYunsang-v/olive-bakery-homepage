package com.dev.olivebakery.service.reservationService;

import com.dev.olivebakery.domain.dto.ReservationDto;
import com.dev.olivebakery.exception.UserDefineException;
import com.dev.olivebakery.utill.Explain;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YoungMan on 2019-05-21.
 */

@Service
public class ReservationConverterService {

	@Explain("ReservationResponseTemp 를 ReservationResponse 로 변환")
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

	@Explain("GetTempDto List 를 GetDto List 로 변환")
	public static List<ReservationDto.ReservationResponse> convertGetTempDtoListToGetDtoList(List<ReservationDto.ReservationResponseTemp> reservationResponseTemps) {

		List<ReservationDto.ReservationResponse> reservationRespons = new ArrayList<>();
		List<ReservationDto.ReservationBread> reservationBreads = new ArrayList<>();
		Long reservationId = reservationResponseTemps.get(0).getReservationId();

		for (ReservationDto.ReservationResponseTemp reservationResponseTemp : reservationResponseTemps) {
			if (reservationResponseTemp.getReservationId().equals(reservationId)) {
				reservationBreads.add(ReservationDto.ReservationBread.of(reservationResponseTemp));

				if (reservationResponseTemps.indexOf(reservationResponseTemp) == reservationResponseTemps.size() - 1) {
					reservationRespons.add(ReservationDto.ReservationResponse.of(reservationResponseTemps.get(reservationResponseTemps.indexOf(reservationResponseTemp)),
							reservationBreads)
					);
				}
				continue;
			}
			reservationRespons.add(ReservationDto.ReservationResponse.of(reservationResponseTemps.get(reservationResponseTemps.indexOf(reservationResponseTemp) - 1),
					reservationBreads));

			reservationId = reservationResponseTemp.getReservationId();
			reservationBreads = new ArrayList<>();
			reservationBreads.add(ReservationDto.ReservationBread.of(reservationResponseTemp));

			if (reservationResponseTemps.indexOf(reservationResponseTemp) == reservationResponseTemps.size() - 1) {
				reservationRespons.add(ReservationDto.ReservationResponse.of(reservationResponseTemps.get(reservationResponseTemps.indexOf(reservationResponseTemp)),
						reservationBreads)
				);
			}
		}
		return reservationRespons;
	}
}
