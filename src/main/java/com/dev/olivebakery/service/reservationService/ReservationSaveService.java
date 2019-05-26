package com.dev.olivebakery.service.reservationService;

import com.dev.olivebakery.domain.dto.ReservationDto;
import com.dev.olivebakery.domain.entity.Bread;
import com.dev.olivebakery.domain.entity.Reservation;
import com.dev.olivebakery.domain.entity.ReservationInfo;
import com.dev.olivebakery.exception.UserDefineException;
import com.dev.olivebakery.repository.BreadRepository;
import com.dev.olivebakery.repository.MemberRepository;
import com.dev.olivebakery.repository.ReservationInfoRepository;
import com.dev.olivebakery.utill.Explain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/**
 * Created by YoungMan on 2019-05-20.
 */

@Service
@RequiredArgsConstructor
public class ReservationSaveService {

	private final ReservationInfoRepository reservationInfoRepository;
	private final BreadRepository breadRepository;
	private final MemberRepository memberRepository;

	@Explain("예약 정보 저장")
	public void saveReservation(ReservationDto.ReservationSaveRequest saveDto) {
		timeValidationCheck(saveDto.getBringTime());
		Reservation reservation = getReservationBySaveDto(saveDto);
		List<ReservationInfo> reservationInfos = getReservationInfos(saveDto, reservation);
		reservationInfoRepository.saveAll(reservationInfos);
	}

	@Explain("수령시간은 매일 아침 8시 ~ 저녁 8시 사이// 예약시간보다 빠를 수 없다")
	public void timeValidationCheck(LocalDateTime bringTime) {
		Predicate<LocalDateTime> predicate = b -> b.isAfter(LocalDateTime.now()) && b.getHour() >= 8 && b.getHour() <= 19;
		if (!predicate.test(bringTime)) {
			throw new UserDefineException(bringTime.toString() + "  수령시간이 올바르지 않습니다.");
		}
	}

	private Reservation getReservationBySaveDto(ReservationDto.ReservationSaveRequest saveDto) {
		return Reservation.of(saveDto.getBringTime(),
				memberRepository.findByEmail(saveDto.getUserEmail()).orElseThrow(() -> new UserDefineException("해당 유저가 존재하지 않습니다.")),
				getTotalPrice(saveDto.getBreadNames(), saveDto.getBreadCounts())
		);
	}

	private int getTotalPrice(List<String> breadNames, List<Integer> counts) {
		List<Bread> breads = findsByNames(breadNames);

		return IntStream.range(0, breads.size())
				.map(index -> breads.get(index).getPrice() * counts.get(index))
				.sum()
		;
	}

	private List<Bread> findsByNames(List<String> breadNames) {
		return breadRepository.findByNameIn(breadNames);
	}

	private List<ReservationInfo> getReservationInfos(ReservationDto.ReservationSaveRequest saveDto, Reservation reservation) {
		List<ReservationInfo> reservationInfos = new ArrayList<>();
		List<Bread> breads = findsByNames(saveDto.getBreadNames());

		for (int i = 0; i < breads.size(); i++) {
			reservationInfos.add(ReservationInfo.of(saveDto.getBreadCounts().get(i),
					reservation,
					breads.get(i))
			);
		}
		return reservationInfos;
	}
}
