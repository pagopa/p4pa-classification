package it.gov.pagopa.pu.classification.service;

import it.gov.pagopa.pu.classification.dto.generated.CalculateAmountBalanceRequest;
import it.gov.pagopa.pu.classification.enums.BalanceDefaultAmountType;
import it.gov.pagopa.pu.classification.exception.custom.InvalidValueException;
import it.gov.pagopa.pu.classification.util.Utilities;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtBilancio;
import it.veneto.regione.schemas._2012.pagamenti.ente.bilanciodefault.CtAccertamentoDefault;
import it.veneto.regione.schemas._2012.pagamenti.ente.bilanciodefault.CtBilancioDefault;
import it.veneto.regione.schemas._2012.pagamenti.ente.bilanciodefault.CtCapitoloDefault;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.math.BigDecimal;

@Service
@Slf4j
public class BalanceAmountService {

  private final BalanceService balanceService;
  private final BalanceDefaultMarshallingService balanceDefaultMarshallingService;
  private final ScriptEngineManager factory = new ScriptEngineManager();
  private final ScriptEngine engine = factory.getEngineByName("rhino");

  public BalanceAmountService(BalanceService balanceService, BalanceDefaultMarshallingService balanceDefaultMarshallingService) {
    this.balanceService = balanceService;
    this.balanceDefaultMarshallingService = balanceDefaultMarshallingService;
  }

  public String calculateAmountBalance(CalculateAmountBalanceRequest calculateAmountBalanceRequest) {
    String balance = calculateAmountBalanceRequest.getBalance();
    BigDecimal amountInstallment = Utilities.longCentsToBigDecimalEuro(calculateAmountBalanceRequest.getAmountCents());

    Object balanceXML = balanceService.unmarshalBalance(balance);

    if (balanceXML instanceof CtBilancio) {
      log.info("The balance amount is already calculated");
      return balance;
    }

    try {
      CtBilancioDefault ctBilancioDefault = (CtBilancioDefault) balanceXML;
      log.info("Calculating balance amount resolving default type");
      for (CtCapitoloDefault capitolo : ctBilancioDefault.getCapitolo()) {
        for (CtAccertamentoDefault ctAccertamentoDefault : capitolo.getAccertamento()) {
          BigDecimal calculatedAmount;
          if (ctAccertamentoDefault.getImporto().equals(BalanceDefaultAmountType.TOTAL.getType())) {
            calculatedAmount = amountInstallment;
          } else if (ctAccertamentoDefault.getImporto().contains(BalanceDefaultAmountType.EXTRACT_AMOUNT.getType())) {
            engine.eval(ctAccertamentoDefault.getImporto());
            Invocable invocable = (Invocable) engine;
            String result = String.valueOf(invocable.invokeFunction(BalanceDefaultAmountType.EXTRACT_AMOUNT.getType(),
              calculateAmountBalanceRequest.getRemittanceInformation()));
            double resultDouble = Double.parseDouble(result);
            calculatedAmount = BigDecimal.valueOf(resultDouble);
          } else if (ctAccertamentoDefault.getImporto().contains(BalanceDefaultAmountType.CALCULATE_AMOUNT.getType())) {
            engine.eval(ctAccertamentoDefault.getImporto());
            Invocable invocable = (Invocable) engine;
            String result = String.valueOf(
              invocable.invokeFunction(BalanceDefaultAmountType.CALCULATE_AMOUNT.getType(), amountInstallment));
            double resultDouble = Double.parseDouble(result);
            calculatedAmount = BigDecimal.valueOf(resultDouble);
          } else {
            throw new InvalidValueException(ctAccertamentoDefault.getImporto() + " as function type to calculate amount balance not supported");
          }
          String amountString = Utilities.amountToString(calculatedAmount);
          String amountWithoutSeparator = amountString.replace("\\.", "");
          String amountWithSeparatorAndDecimal = amountWithoutSeparator.replace(",", ".");

          ctAccertamentoDefault.setImporto(amountWithSeparatorAndDecimal);
        }
      }
      return balanceDefaultMarshallingService.marshal(ctBilancioDefault);
    } catch (Exception e) {
      throw new InvalidValueException("Error calculating amount of balance: " + e.getMessage());
    }
  }

}
