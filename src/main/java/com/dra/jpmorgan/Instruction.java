package com.dra.jpmorgan;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

/**
 * 
 * @author Dominika Rakoczy
 *
 */
public class Instruction implements Serializable {

	private static final long serialVersionUID = 1L;

	private String enitity;

	private TradeType tradeType;

	private BigDecimal agreedFx;

	private Currency currency;

	private LocalDate instructionDate;

	private LocalDate settlementDate;

	private Integer units;

	private BigDecimal pricePerUnit;

	public Instruction(String entity, TradeType tradeType, BigDecimal agreedFx, String currencyCode,
			LocalDate instructionDate, LocalDate settlementDate, Integer units, BigDecimal pricePerUnit) {
		this.enitity = entity;
		this.tradeType = tradeType;
		this.agreedFx = agreedFx;
		this.currency = Currency.getInstance(currencyCode);
		this.instructionDate = instructionDate;
		this.settlementDate = settlementDate;
		this.units = units;
		this.pricePerUnit = pricePerUnit;
	}

	public String getEnitity() {
		return enitity;
	}

	public void setEnitity(String enitity) {
		this.enitity = enitity;
	}

	public TradeType getTradeType() {
		return tradeType;
	}

	public void setTradeType(TradeType tradeType) {
		this.tradeType = tradeType;
	}

	public BigDecimal getAgreedFx() {
		return agreedFx;
	}

	public void setAgreedFx(BigDecimal agreedFx) {
		this.agreedFx = agreedFx;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public LocalDate getInstructionDate() {
		return instructionDate;
	}

	public void setInstructionDate(LocalDate instructionDate) {
		this.instructionDate = instructionDate;
	}

	public LocalDate getSettlementDate() {
		return settlementDate;
	}

	public void setSettlementDate(LocalDate settlementDate) {
		this.settlementDate = settlementDate;
	}

	public Integer getUnits() {
		return units;
	}

	public void setUnits(Integer units) {
		this.units = units;
	}

	public BigDecimal getPricePerUnit() {
		return pricePerUnit;
	}

	public void setPricePerUnit(BigDecimal pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}

	public BigDecimal getUSDAmountOfATrade() {
		return pricePerUnit.multiply(agreedFx).multiply(BigDecimal.valueOf(units));
	}

	@Override
	public String toString() {
		return "Instruction [enitity=" + enitity + ", tradeType=" + tradeType + ", agreedFx=" + agreedFx + ", currency="
				+ currency + ", instructionDate=" + instructionDate + ", settlementDate=" + settlementDate + ", units="
				+ units + ", pricePerUnit=" + pricePerUnit + ", USDAmountOfATrade=" + getUSDAmountOfATrade() + "]";
	}

}
