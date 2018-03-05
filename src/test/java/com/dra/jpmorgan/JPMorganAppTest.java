package com.dra.jpmorgan;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.Test;

import junit.framework.Assert;
/**
 * 
 * @author Dominika Rakoczy
 *
 */
public class JPMorganAppTest {

	@Test
	public void shouldCorrectlyCreateInstruction() {
		InstructionsAggregationService instructionsAggregationService = new InstructionsAggregationService();

		// Working day in PLN
		LocalDate settelmentDate = LocalDate.of(2018, 02, 05);
		instructionsAggregationService.createInstruction("mis", TradeType.SELL, BigDecimal.valueOf(0.1), "PLN",
				settelmentDate, settelmentDate, 700, BigDecimal.valueOf(27));

		List<Instruction> instructions = instructionsAggregationService.getInstructions();
		Assert.assertEquals(1, instructions.size());
		Assert.assertEquals(settelmentDate, instructions.get(0).getSettlementDate());
		// USD amount of a trade = Price per unit * Units * Agreed Fx
		// 27*700*0,1=1890
		Assert.assertEquals(BigDecimal.valueOf(1890.0), instructions.get(0).getUSDAmountOfATrade());

	}

	@Test
	public void shouldCorrectlyCreateInstructionAndChangeDateToWrokingDay() {
		InstructionsAggregationService instructionsAggregationService = new InstructionsAggregationService();

		// Weekend in PLN (Sunday)
		LocalDate settelmentDate = LocalDate.of(2018, 02, 04);
		instructionsAggregationService.createInstruction("mis", TradeType.SELL, BigDecimal.valueOf(0.1), "PLN",
				settelmentDate, settelmentDate, 700, BigDecimal.valueOf(27));

		List<Instruction> instructions = instructionsAggregationService.getInstructions();
		Assert.assertEquals(1, instructions.size());
		// Monday
		Assert.assertEquals(settelmentDate.plusDays(1), instructions.get(0).getSettlementDate());
		// USD amount of a trade = Price per unit * Units * Agreed Fx
		// 27*700*0,1=1890
		Assert.assertEquals(BigDecimal.valueOf(1890.0), instructions.get(0).getUSDAmountOfATrade());

		// Weekend in PLN (Saturday)
		LocalDate settelmentDate1 = LocalDate.of(2018, 02, 03);
		instructionsAggregationService.createInstruction("mis", TradeType.SELL, BigDecimal.valueOf(0.1), "PLN",
				settelmentDate1, settelmentDate1, 700, BigDecimal.valueOf(27));

		instructions = instructionsAggregationService.getInstructions();
		Assert.assertEquals(2, instructions.size());
		// Monday
		Assert.assertEquals(settelmentDate1.plusDays(2), instructions.get(1).getSettlementDate());
		// USD amount of a trade = Price per unit * Units * Agreed Fx
		// 27*700*0,1=1890
		Assert.assertEquals(BigDecimal.valueOf(1890.0), instructions.get(1).getUSDAmountOfATrade());

	}
	
	@Test
	public void shouldCorrectlyCreateInstructionAndChangeDateToWorkingDayForSAR() {
		InstructionsAggregationService instructionsAggregationService = new InstructionsAggregationService();

		// Weekend in SAR (Friday)
		LocalDate settelmentDate = LocalDate.of(2018, 02, 02);
		instructionsAggregationService.createInstruction("mis", TradeType.SELL, BigDecimal.valueOf(0.1), "SAR",
				settelmentDate, settelmentDate, 700, BigDecimal.valueOf(27));

		List<Instruction> instructions = instructionsAggregationService.getInstructions();
		Assert.assertEquals(1, instructions.size());
		// Sunday
		Assert.assertEquals(settelmentDate.plusDays(2), instructions.get(0).getSettlementDate());
		// USD amount of a trade = Price per unit * Units * Agreed Fx
		// 27*700*0,1=1890
		Assert.assertEquals(BigDecimal.valueOf(1890.0), instructions.get(0).getUSDAmountOfATrade());

		// Weekend in PLN (Saturday)
		LocalDate settelmentDate1 = LocalDate.of(2018, 02, 03);
		instructionsAggregationService.createInstruction("mis", TradeType.SELL, BigDecimal.valueOf(0.1), "SAR",
				settelmentDate1, settelmentDate1, 700, BigDecimal.valueOf(27));

		instructions = instructionsAggregationService.getInstructions();
		Assert.assertEquals(2, instructions.size());
		// Sunday
		Assert.assertEquals(settelmentDate1.plusDays(1), instructions.get(1).getSettlementDate());
		// USD amount of a trade = Price per unit * Units * Agreed Fx
		// 27*700*0,1=1890
		Assert.assertEquals(BigDecimal.valueOf(1890.0), instructions.get(1).getUSDAmountOfATrade());

	}
	
	@Test
	public void shouldCorrectlyCreateInstructionAndSetInstructonDateAsSettelmentDate() {
		InstructionsAggregationService instructionsAggregationService = new InstructionsAggregationService();

		//settelemnt date is before instruction date, unable to create past transaction
		LocalDate instructionDate = LocalDate.of(2018, 02, 06);
		LocalDate settelmentDate = LocalDate.of(2018, 02, 05);
		instructionsAggregationService.createInstruction("mis", TradeType.SELL, BigDecimal.valueOf(0.1), "PLN",
				instructionDate, settelmentDate, 700, BigDecimal.valueOf(27));

		List<Instruction> instructions = instructionsAggregationService.getInstructions();
		Assert.assertEquals(1, instructions.size());
		//Both dates are the same, settelment date is set to instruction date
		Assert.assertEquals(instructionDate, instructions.get(0).getInstructionDate());
		Assert.assertEquals(instructionDate, instructions.get(0).getSettlementDate());

		// USD amount of a trade = Price per unit * Units * Agreed Fx
		// 27*700*0,1=1890
		Assert.assertEquals(BigDecimal.valueOf(1890.0), instructions.get(0).getUSDAmountOfATrade());

	}

}
