package com.dev.olivebakery.service;

import com.dev.olivebakery.domain.dto.ReservationDto;
import com.dev.olivebakery.domain.entity.Member;
import com.dev.olivebakery.domain.enums.ReservationType;
import com.dev.olivebakery.exception.UserDefineException;
import com.dev.olivebakery.repository.BreadRepository;
import com.dev.olivebakery.repository.MemberRepository;
import com.dev.olivebakery.service.reservationService.ReservationGetService;
import com.dev.olivebakery.service.reservationService.ReservationSaveService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ReservationSaveServiceTest {

	@Autowired
	ReservationSaveService reservationSaveService;


	@Test
	public void timeValidationCheck_올바른_예약시간() throws Exception {

		//given
		LocalDateTime bringTime = LocalDate.now()
				.plusDays(1)
				.atTime(12, 12, 12)
		;

		//then
		reservationSaveService.timeValidationCheck(bringTime);
	}


	@Test(expected = UserDefineException.class)
	public void timeValidationCheck_잘못된_예약시간_20시이후() throws Exception {

		//given
		LocalDateTime bringTime = LocalDate.now()
				.plusDays(1)
				.atTime(20, 12, 12)
		;

		//then
		reservationSaveService.timeValidationCheck(bringTime);
	}

}
