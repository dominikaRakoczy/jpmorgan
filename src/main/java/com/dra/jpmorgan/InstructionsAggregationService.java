package com.dra.jpmorgan;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Dominika Rakoczy
 *
 */
public class InstructionsAggregationService {
	protected final Logger log = LoggerFactory.getLogger(InstructionsAggregationService.class);

	private List<Instruction> instructions;

	public InstructionsAggregationService() {
		instructions = new LinkedList<Instruction>();
	}

	public void createInstruction(String entity, TradeType tradeType, BigDecimal agreedFx, String currencyCode,
			LocalDate instructionDate, LocalDate settlementDate, Integer units, BigDecimal pricePerUnit) {
		instructions.add(new Instruction(entity, tradeType, agreedFx, currencyCode, instructionDate,
				calculateSettlementDate(instructionDate, settlementDate, currencyCode), units, pricePerUnit));
		log.info("New instruction to {} {} is saved.", tradeType.name(), entity);
	}

	public List<Instruction> getInstructions() {
		return instructions;
	}

	public void amountInUSDSettledIncomingEveryday() {
		System.out.println("Amount in USD settled incoming everyday: ");
		amountInUSDSettledEverydayByTradeType(TradeType.SELL).entrySet().stream().forEach(System.out::println);
	}

	public void amountInUSDSettledOutgoingEveryday() {
		System.out.println("Amount in USD settled outgoing everyday");
		amountInUSDSettledEverydayByTradeType(TradeType.BUY).entrySet().stream().forEach(System.out::println);
	}

	public void rankingOfEntitiesOutgoing() {
		System.out.println("Ranking of entities based on incoming amount:");
		rankingOfEntitiesByTradeType(TradeType.SELL);
	}

	public void rankingOfEntitiesIncoming() {
		System.out.println("Ranking of entities based on outgoing amount:");
		rankingOfEntitiesByTradeType(TradeType.BUY);
	}

	private void rankingOfEntitiesByTradeType(TradeType tradeType) {
		AtomicInteger index = new AtomicInteger();
		instructions.stream()
				.filter(instruction -> instruction.getTradeType().equals(tradeType))
				.collect(Collectors.groupingBy(Instruction::getEnitity,
						Collectors.mapping(Instruction::getUSDAmountOfATrade,
								Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))))
				.entrySet().stream()
				.sorted((instruction1, instruction2) -> instruction2.getValue().compareTo(instruction1.getValue()))
				.forEach(instruction -> System.out.println(index.incrementAndGet() + ". " + instruction.toString()));
	}

	private Map<LocalDate, BigDecimal> amountInUSDSettledEverydayByTradeType(TradeType tradeType) {
		return instructions.stream()
				.filter(instruction -> instruction.getTradeType().equals(tradeType))
				.collect(Collectors.groupingBy(Instruction::getSettlementDate, Collectors.mapping(
						Instruction::getUSDAmountOfATrade, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));
	}

	private LocalDate calculateSettlementDate(LocalDate instructionDate, LocalDate settlementDate,
			String currencyCode) {
		if (settlementDate.isBefore(instructionDate)) {
			log.error(
					"Settelment date ({}) must be after instruction date ({}). System set settelment date based on instruction date.",
					settlementDate, instructionDate);
			settlementDate = instructionDate;
		}
		if (currencyCode.equals("AED") || currencyCode.equals("SAR")) {
			return setAEDAndSARWorkingDay(settlementDate);
		} else {
			return setWorkingDay(settlementDate);
		}
	}

	private LocalDate setAEDAndSARWorkingDay(LocalDate settlementDate) {
		if (settlementDate.getDayOfWeek().equals(DayOfWeek.FRIDAY)) {
			return settlementDate.plus(2, ChronoUnit.DAYS);
		} else if (settlementDate.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
			return settlementDate.plus(1, ChronoUnit.DAYS);
		} else {
			return settlementDate;
		}
	}

	private LocalDate setWorkingDay(LocalDate settlementDate) {
		if (settlementDate.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
			return settlementDate.plus(2, ChronoUnit.DAYS);
		} else if (settlementDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
			return settlementDate.plus(1, ChronoUnit.DAYS);
		} else {
			return settlementDate;
		}
	}

}
