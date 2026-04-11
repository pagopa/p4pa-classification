package it.gov.pagopa.pu.classification.service;

import it.gov.pagopa.pu.classification.dto.generated.CalculateAmountBalanceRequest;
import it.gov.pagopa.pu.classification.enums.BalanceDefaultAmountType;
import it.gov.pagopa.pu.classification.exception.custom.InvalidValueException;
import it.gov.pagopa.pu.classification.util.ErrorCodeConstants;
import it.gov.pagopa.pu.classification.util.Utilities;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtAccertamentoDefault;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtBilancio;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtBilancioDefault;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtCapitoloDefault;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.math.BigDecimal;

@Service
@Slf4j
public class BalanceTemplateResolverService {

  private final BalanceService balanceService;
  private final BalanceDefaultMarshallingService balanceDefaultMarshallingService;
  private final ScriptEngineManager factory = new ScriptEngineManager();
  private final ScriptEngine engine = factory.getEngineByName("rhino");

  public BalanceTemplateResolverService(BalanceService balanceService, BalanceDefaultMarshallingService balanceDefaultMarshallingService) {
    this.balanceService = balanceService;
    this.balanceDefaultMarshallingService = balanceDefaultMarshallingService;
  }

  public String calculateAmountBalance(CalculateAmountBalanceRequest calculateAmountBalanceRequest) {
    String balance = calculateAmountBalanceRequest.getBalance();
    BigDecimal amountInstallment = Utilities.longCentsToBigDecimalEuro(calculateAmountBalanceRequest.getAmountCents());

    Object balanceXML = balanceService.unmarshalBalance(balance, null);

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
            calculatedAmount = new BigDecimal(result);
          } else if (ctAccertamentoDefault.getImporto().contains(BalanceDefaultAmountType.CALCULATE_AMOUNT.getType())) {
            engine.eval(ctAccertamentoDefault.getImporto());
            Invocable invocable = (Invocable) engine;
            String result = String.valueOf(
              invocable.invokeFunction(BalanceDefaultAmountType.CALCULATE_AMOUNT.getType(), amountInstallment));
            calculatedAmount = new BigDecimal(result);
          } else {
            throw new InvalidValueException(ErrorCodeConstants.ERROR_CODE_BALANCE_CALCULATION_ERROR, ctAccertamentoDefault.getImporto() + " as function type to calculate amount balance not supported");
          }
          String amountString = Utilities.amountToString(calculatedAmount);

          ctAccertamentoDefault.setImporto(amountString);
        }
      }
      return balanceDefaultMarshallingService.marshal(ctBilancioDefault);
    } catch (Exception e) {
      throw new InvalidValueException(ErrorCodeConstants.ERROR_CODE_BALANCE_CALCULATION_ERROR, "Error calculating amount of balance: " + e.getMessage());
    }
  }

}
