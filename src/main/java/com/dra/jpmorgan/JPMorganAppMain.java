package com.dra.jpmorgan;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 
 * @author Dominika Rakoczy
 *
 */
public class JPMorganAppMain {
	public static void main(String[] args) {
		InstructionsAggregationService instructionsAggregationService = new InstructionsAggregationService();

		instructionsAggregationService.createInstruction("foo", TradeType.SELL, BigDecimal.valueOf(0.1), "SAR",
				LocalDate.now(), LocalDate.now().plusDays(4), 700, BigDecimal.valueOf(27));
		instructionsAggregationService.createInstruction("foo", TradeType.SELL, BigDecimal.valueOf(0.1), "SAR",
				LocalDate.now(), LocalDate.now(), 100, BigDecimal.valueOf(27));
		instructionsAggregationService.createInstruction("bar", TradeType.SELL, BigDecimal.valueOf(0.1), "SAR",
				LocalDate.now(), LocalDate.now(), 100, BigDecimal.valueOf(27));

		instructionsAggregationService.createInstruction("foo", TradeType.BUY, BigDecimal.valueOf(0.1), "PLN",
				LocalDate.now(), LocalDate.now(), 50, BigDecimal.valueOf(27));
		instructionsAggregationService.createInstruction("foo", TradeType.BUY, BigDecimal.valueOf(0.1), "SAR",
				LocalDate.now(), LocalDate.now(), 100, BigDecimal.valueOf(27));

		instructionsAggregationService.amountInUSDSettledIncomingEveryday();
		instructionsAggregationService.amountInUSDSettledOutgoingEveryday();

		instructionsAggregationService.rankingOfEntitiesOutgoing();
		instructionsAggregationService.rankingOfEntitiesIncoming();

	}
}
